package com.gdlife.candypie.adapter.homepage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.user.HomepageActivity;
import com.gdlife.candypie.activitys.video.UserVideosActivity;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.databinding.FragmentHomepageVideoBinding;
import com.gdlife.candypie.databinding.ItemUserVideoBinding;
import com.gdlife.candypie.databinding.ItemVideoBinding;
import com.gdlife.candypie.fragments.homepage.HomepageVideoFragment;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.bean.video.UserVideosBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heboot on 2018/3/6.
 */

public class HomepageVideosAdapter extends BaseRecyclerViewAdapter {

    private WeakReference<HomepageActivity> weakReference;

    private boolean isMe = false;

    public HomepageVideosAdapter(WeakReference<HomepageActivity> weakReference, boolean isMe, List<HomepageVideoBean> datas) {
        this.isMe = isMe;
        this.weakReference = weakReference;
        data.addAll(datas);
        if (isMe) {
            data.add(0, new HomepageVideoBean());
        }

    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomepageVideosAdapter.ViewHolder(parent, R.layout.item_video);
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
                        weakReference.get().chooseVideo();
                    });
                } else {
                    if (s.getStatus() == MValue.VIDEO_AUTH_STATUS_ING) {
                        binding.tvStatus.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvStatus.setVisibility(View.GONE);
                    }
                    ImageUtils.showImage(binding.ivVideo, s.getCover_img());
                    binding.getRoot().setOnClickListener((v) -> {
                        IntentUtils.toPlayerActivity2(binding.getRoot().getContext(), s.getPath(), VideoPreviewFrom.USER, s.getId(), s.getCover_img());
                    });
                }
            } else {

                ImageUtils.showImage(binding.ivVideo, s.getCover_img());
                binding.getRoot().setOnClickListener((v) -> {
                    IntentUtils.toPlayerActivity2(binding.getRoot().getContext(), s.getPath(), VideoPreviewFrom.USER, s.getId(), s.getCover_img());
                });
            }

        }

    }


}
