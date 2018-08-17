package com.gdlife.candypie.adapter.discover;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.LayoutDiscoverVideoBinding;
import com.gdlife.candypie.databinding.ViewPlayerQiniuBinding;
import com.gdlife.candypie.utils.ImageUtils;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.netease.nim.uikit.common.util.media.ImageUtil;

import java.util.List;

public class DiscoverVideoRVAdapter extends BaseRecyclerViewAdapter {


    private ViewHolder mCurViewHolder;

    public DiscoverVideoRVAdapter(List<User> users) {
        this.data = users;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiscoverVideoRVAdapter.ViewHolder(parent, R.layout.layout_discover_video);
    }


    public class ViewHolder extends BaseRecyclerViewHolder<User, LayoutDiscoverVideoBinding> {
        //

        User user;

        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final User s, int position) {

            ImageUtils.showImage(binding.ivCover, s.getMain_video_list().get(0).getCover_img());

            binding.tvName.setText(s.getNickname());

            user = s;
        }

    }

    public void setCurViewHolder(ViewHolder viewHolder) {
        mCurViewHolder = viewHolder;
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mCurViewHolder = (ViewHolder) holder;
        ((ViewHolder) holder).binding.ivCover.setVisibility(View.VISIBLE);
        ((ViewHolder) holder).binding.ivPlay.setVisibility(View.GONE);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        ((ViewHolder) holder).binding.PLVideoView.stopPlayback();
    }


    public void startCurVideoView() {
//        Log.e("startCurVideoView1", "startCurVideoView>" + mCurViewHolder.nameText);
        if (mCurViewHolder != null && !mCurViewHolder.binding.PLVideoView.isPlaying()) {
            Log.e("startCurVideoView2", "startCurVideoView>" + mCurViewHolder.binding.tvName.getText());
            mCurViewHolder.binding.PLVideoView.setVideoPath(mCurViewHolder.user.getMain_video_list().get(0).getPath());
            mCurViewHolder.binding.PLVideoView.start();
            mCurViewHolder.binding.ivPlay.setVisibility(View.GONE);
        }
    }

    public void pauseCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.binding.PLVideoView.pause();
        }
    }

    public void stopCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.binding.PLVideoView.stopPlayback();
            mCurViewHolder.binding.ivCover.setVisibility(View.VISIBLE);
        }
    }

}
