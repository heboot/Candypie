package com.gdlife.candypie.adapter.discover;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.CalendarContract;
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
import com.gdlife.candypie.databinding.LayoutDiscoverVideoBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserInfoService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
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

public class DiscoverVPAdapter extends PagerAdapter {


    private List<User> userList;

    protected QMUITipDialog tipDialog;

    private Context context;

    public DiscoverVPAdapter(Context context, List<User> s) {
        this.userList = s;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        User user = userList.get(position);

        LayoutDiscoverVideoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.layout_discover_video, null, false);

        if (user.getMain_video_list() != null) {
            ImageUtils.showImage(binding.ivCover, user.getMain_video_list().get(0).getCover_img());
        }

        binding.setUser(user);

//        ImageUtils.showImage(binding.ivAvatar, user.getAvatar());

        if (user.getVideo_tags() !=
                null && user.getVideo_tags().size() > 0) {
            binding.tvTeseContent.setVisibility(View.VISIBLE);
            binding.tvTeseTitle.setVisibility(View.VISIBLE);
            binding.tvTeseContent.setSelected(true);
            binding.tvTeseContent.setText(UserInfoService.getTagsString(user.getVideo_tags()));

        } else {
            binding.tvTeseContent.setVisibility(View.GONE);
            binding.tvTeseTitle.setVisibility(View.GONE);
        }

        if (user.getIs_favs() == 1) {
            binding.tvFav.setText("已收藏");
            binding.tvFav.setBackgroundResource(R.drawable.bg_gradient_white);
            binding.tvFav.setTextColor(ContextCompat.getColor(context, R.color.color_FF5252));
        } else {
            binding.tvFav.setText("收藏");
            binding.tvFav.setBackgroundResource(R.drawable.bg_gradient_fav);
            binding.tvFav.setTextColor(Color.WHITE);
        }


        binding.tvName.setText(user.getNickname());
        binding.tvCity.setText(user.getCity());

//        binding.tvAge.setText("♀" + UserService.getUserAgeByBirthdayNoSui(user));

        if (user.getOnline_status() != null) {
            binding.tvOnline.getRoot().setVisibility(View.VISIBLE);
            binding.tvOnline.setOnlineStatus(user.getOnline_status());
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(user.getOnline_status().getIcon_color()));
            binding.tvOnline.ivPoint.setImageDrawable(colorDrawable);
        } else {
            binding.tvOnline.getRoot().setVisibility(View.GONE);
        }

        binding.ivAvatar.setOnClickListener((v) -> {
            IntentUtils.toHomepageActivity2(v.getContext(), MValue.FROM_OTHER, user, null, null);
        });

        binding.clytChildContainer.setOnClickListener((v) -> {
            RxBus.getInstance().post(DISCOVER_PAUSE_PLAY_EVENT);
        });


//        binding.tvFav.setOnClickListener((v) -> {
//            if (UserService.getInstance().checkTourist(container.getContext())) {
//                return;
//            }
//            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
//                tipDialog = DialogUtils.getFailDialog(container.getContext(), "不能对自己操作", true);
//                tipDialog.show();
//                return;
//            }
//            doFav(user.getIs_favs(), user, binding);
//        });
//
//        binding.tvFav.setOnClickListener((v) -> {
//            if (UserService.getInstance().checkTourist(container.getContext())) {
//                return;
//            }
//            if (user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
//                tipDialog = DialogUtils.getFailDialog(container.getContext(), "不能对自己操作", true);
//                tipDialog.show();
//                return;
//            }
//            doFav(user.getIs_favs(), user, binding);
//        });


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
                    binding.tvFav.setText(context.getString(R.string.fav_status_on));
                    binding.tvFav.setTextColor(ContextCompat.getColor(context, R.color.color_FF5252));
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
                    binding.tvFav.setTextColor(Color.WHITE);
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
//        Map<String, Object> params = new HashMap<>();
//        params = SignUtils.getNormalParams();
//        params.put(MKey.TO_UID, user.getId());
//        String sign = SignUtils.doSign(params);
//        params.put(MKey.SIGN, sign);
//        HttpClient.Builder.getGuodongServer().imchat_status(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<ImChatStatusBean>() {
//            @Override
//            public void onSuccess(BaseBean<ImChatStatusBean> baseBean) {
//                IntentUtils.intent2ChatActivity(context, MValue.CHAT_PRIEX + user.getId(), baseBean.getData().getIm_price(), user);
//            }
//
//            @Override
//            public void onError(BaseBean<ImChatStatusBean> baseBean) {
//
//            }
//        });
    }


    @Override
    public int getCount() {
        if (userList == null) {
            return 0;
        }
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
