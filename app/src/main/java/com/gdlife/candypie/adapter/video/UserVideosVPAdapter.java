package com.gdlife.candypie.adapter.video;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ActivityPlayer2Binding;
import com.gdlife.candypie.databinding.LayoutDiscoverVideoBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.im.ImChatStatusBean;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.entity.User;
import com.heboot.rxbus.RxBus;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.heboot.event.DiscoverEvent.DISCOVER_PAUSE_PLAY_EVENT;
import static com.heboot.event.DiscoverEvent.DISCOVER_PAUSE_PLAY_EVENT_BY_USER_VIDEOS;
import static com.heboot.event.DiscoverEvent.DISCOVER_TO_USERINFO_EVENT;

public class UserVideosVPAdapter extends PagerAdapter {
    private List<HomepageVideoBean> userList;

    protected QMUITipDialog tipDialog;

    private Context context;

    public UserVideosVPAdapter(Context context, List<HomepageVideoBean> s) {
        this.userList = s;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        ActivityPlayer2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.activity_player2, null, false);

        ImageUtils.showImage(binding.ivCover, userList.get(position).getCover_img());

        binding.tvNext.setVisibility(View.GONE);

        binding.ivBack.setVisibility(View.GONE);
        binding.ovBack.setVisibility(View.GONE);

        binding.clytChildContainer.setOnClickListener((v) -> {
            RxBus.getInstance().post(DISCOVER_PAUSE_PLAY_EVENT_BY_USER_VIDEOS);
        });

        View view = binding.getRoot();
        view.setId(position);
        container.addView(view);
        return view;
    }

    private void toRight() {
        RxBus.getInstance().post(DISCOVER_TO_USERINFO_EVENT);
    }

    private void doFav(int fav, User user, LayoutDiscoverVideoBinding binding) {
        Map<String, Object> params = new HashMap<>();
        params = SignUtils.getNormalParams();
        params.put(MKey.TO_UID, user.getId());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        if (fav == 0) {

            HttpClient.Builder.getGuodongServer().favs(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                @Override
                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                    user.setIs_favs(1);
                    binding.tvFav.setTextColor(ContextCompat.getColor(context, R.color.color_FF5252));
                    binding.tvFav.setText(context.getString(R.string.fav_status_on));
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                    tipDialog = DialogUtils.getFailDialog(context, baseBean.getMessage(), true);
                    tipDialog.show();
                }
            });


        } else {

            HttpClient.Builder.getGuodongServer().unfavs(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                @Override
                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                    binding.tvFav.setText(context.getString(R.string.fav_status_un));
                    user.setIs_favs(0);
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {
                    tipDialog = DialogUtils.getFailDialog(context, baseBean.getMessage(), true);
                    tipDialog.show();
                }
            });
        }
    }


    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(container.findViewById(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
