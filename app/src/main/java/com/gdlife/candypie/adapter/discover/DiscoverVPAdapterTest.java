package com.gdlife.candypie.adapter.discover;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
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
import com.heboot.entity.User;
import com.heboot.event.IMEvent;
import com.heboot.rxbus.RxBus;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.heboot.event.DiscoverEvent.DISCOVER_PAUSE_PLAY_EVENT;
import static com.heboot.event.DiscoverEvent.DISCOVER_TO_USERINFO_EVENT;

public class DiscoverVPAdapterTest extends PagerAdapter {


    private List<User> userList;

    protected QMUITipDialog tipDialog;

    private Context context;

    public DiscoverVPAdapterTest(Context context, List<User> s) {
        this.userList = s;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        User user = userList.get(position);

        LayoutDiscoverVideoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.layout_discover_video, null, false);

        ImageUtils.showImage(binding.ivCover, user.getMain_video_list().get(0).getCover_img());
        binding.tvName.setText(user.getNickname());
        binding.tvCity.setText(user.getCity());

        binding.tvAge.setText("♀" + UserService.getUserAgeByBirthdayNoSui(user));


        binding.clytChildContainer.setOnClickListener((v) -> {
            RxBus.getInstance().post(DISCOVER_PAUSE_PLAY_EVENT);
        });

        binding.ivMsg.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(container.getContext())) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(container.getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            toChat(user);
        });
        binding.tvMsg.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(container.getContext())) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(container.getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            toChat(user);
        });

        binding.ivFav.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(container.getContext())) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(container.getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            doFav(user.getIs_favs(), user, binding);
        });

        binding.tvFav.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(container.getContext())) {
                return;
            }
            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
                tipDialog = DialogUtils.getFailDialog(container.getContext(), "不能对自己操作", true);
                tipDialog.show();
                return;
            }
            doFav(user.getIs_favs(), user, binding);
        });


        binding.clytChildContainer.setOnClickListener((v) -> {
            RxBus.getInstance().post(DISCOVER_PAUSE_PLAY_EVENT);
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
                    binding.ivFav.setBackgroundResource(R.drawable.icon_discover_fav_on);
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
                    binding.ivFav.setBackgroundResource(R.drawable.icon_discover_fav);
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

    private void toChat(User user) {
        RxBus.getInstance().post(new IMEvent.QUERY_IM_STAUTS_EVENT(MAPP.mapp.getCurrentActivity(), String.valueOf(user.getId())));
    }


    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(container.findViewById(position));
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
