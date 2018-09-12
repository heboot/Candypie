package com.gdlife.candypie.adapter.discover;

import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.databinding.ItemVideoBinding;
import com.gdlife.candypie.fragments.homepage.HomepageBottomFragment;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heboot on 2018/3/6.
 */

public class HomepageBottomVideosAdapter extends BaseRecyclerViewAdapter {

    private WeakReference<HomepageBottomFragment> weakReference;

    private boolean isMe = false;

    private User user;
    private List<User> videoUsers = new ArrayList();

    public HomepageBottomVideosAdapter(WeakReference<HomepageBottomFragment> weakReference, boolean isMe, List<HomepageVideoBean> datas, User user) {
        this.isMe = isMe;
        this.weakReference = weakReference;
        this.user
                = user;
        data.addAll(datas);
        if (isMe) {
            data.add(0, new HomepageVideoBean());
        }

    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomepageBottomVideosAdapter.ViewHolder(parent, R.layout.item_video);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<HomepageVideoBean, ItemVideoBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final HomepageVideoBean s, int position) {
            if (isMe) {
                if (position == 0) {
                    binding.ivPlay.setVisibility(View.GONE);
                    ImageUtils.showImage(binding.ivVideo, R.drawable.icon_add_video);
                    binding.getRoot().setOnClickListener((v) -> {
//                        weakReference.get().chooseVideo();
                    });
                } else {
                    ImageUtils.showImage(binding.ivVideo, s.getCover_img());
                    binding.getRoot().setOnClickListener((v) -> {
                        IntentUtils.toPlayerActivity2(binding.getRoot().getContext(), s.getPath(), VideoPreviewFrom.USER, s.getId(), s.getCover_img());
                    });
                }
            } else {
                ImageUtils.showImage(binding.ivVideo, s.getCover_img());
                binding.getRoot().setOnClickListener((v) -> {
                    videoUsers.clear();
                    for (HomepageVideoBean homepageVideoBean : user.getUser_video().getList()) {
                        videoUsers.add(user);
                    }
                    IntentUtils.toUserVideoAudioPlayActivity(binding.getRoot().getContext(), position, user.getUser_video().getTotal(), videoUsers);

//                    IntentUtils.toPlayerActivity2(binding.getRoot().getContext(), s.getPath(), VideoPreviewFrom.USER, s.getId(), s.getCover_img());
//                    IntentUtils.toPlayerActivity3(binding.getRoot().getContext(), data, position, user.getNickname());
                });
            }

        }

    }


}
