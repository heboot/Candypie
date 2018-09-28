package com.gdlife.candypie.adapter.user;

import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.user.UserPageActivity;
import com.gdlife.candypie.activitys.video.UserUnlockVideosActivity;
import com.gdlife.candypie.activitys.video.UserVideosActivity;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.databinding.ItemUserVideoBinding;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.bean.video.UserVideosBean;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/3/6.
 */

public class UserUnlockVideosAdapter extends BaseRecyclerViewAdapter<HomepageVideoBean> {

    private boolean isMe = false;

    private WeakReference<UserUnlockVideosActivity> weakReference;


    private List<User> videoUsers = new ArrayList();

    private int total;

    public UserUnlockVideosAdapter(WeakReference<UserUnlockVideosActivity> weakReference, List<HomepageVideoBean> datas, int total) {
        this.isMe = false;
        this.weakReference = weakReference;
        this.total = total;
        data.addAll(datas);
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserUnlockVideosAdapter.ViewHolder(parent, R.layout.item_user_video);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<HomepageVideoBean, ItemUserVideoBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final HomepageVideoBean s, int position) {

            binding.tvStatus.setVisibility(View.GONE);

            ImageUtils.showImage(binding.ivImg, s.getCover_img());

            RxView.clicks(binding.getRoot()).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {

                    videoUsers.clear();
                    for (HomepageVideoBean homepageVideoBean : data) {
                        homepageVideoBean.setUnlock(1);
                        UserVideosBean userVideosBean = new UserVideosBean();
                        List<HomepageVideoBean> list = new ArrayList<>();
                        list.add(homepageVideoBean);
                        userVideosBean.setList(list);
                        homepageVideoBean.getUser().setUser_video(userVideosBean);
                        videoUsers.add(homepageVideoBean.getUser());
                    }
                    IntentUtils.toUserVideoAudioPlayActivity(binding.getRoot().getContext(), position, total, videoUsers);

//                        IntentUtils.toPlayerActivity3(binding.getRoot().getContext(), data, position, user.getNickname());
                }
            });

            binding.tvPriceTip.setVisibility(View.GONE);
            binding.tvUnlockTip.setVisibility(View.GONE);


//            binding.getRoot().setOnClickListener((v) -> {
//                IntentUtils.toPlayerActivity(binding.getRoot().getContext(), s.getPath());
//            });

        }

    }


}
