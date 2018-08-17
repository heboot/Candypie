package com.gdlife.candypie.adapter.common;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.auth.AuthSkillActivity;
import com.gdlife.candypie.activitys.common.ReportActivity;
import com.gdlife.candypie.databinding.ItemAuthSkillPhotoBinding;
import com.gdlife.candypie.utils.ImageUtils;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.netease.nim.uikit.common.util.string.StringUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by heboot on 2018/2/28.
 */

public class ReoprtPhotoAdapter extends BaseRecyclerViewAdapter<String> {

    private WeakReference<ReportActivity> authSkillActivityWeakReference;

    public ReoprtPhotoAdapter(List<String> mdata, WeakReference<ReportActivity> authSkillActivityWeakReference) {
        data = mdata;
        this.authSkillActivityWeakReference = authSkillActivityWeakReference;
        if (data == null || data.size() == 0) {
            data.add(0, "");
        }

    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReoprtPhotoAdapter.ViewHolder(parent, R.layout.item_auth_skill_photo);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<String, ItemAuthSkillPhotoBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final String s, int position) {
            if (StringUtil.isEmpty(s)) {
                binding.getRoot().setOnClickListener((v) -> {
                    authSkillActivityWeakReference.get().choosePic();
                });
                binding.ivClose.setVisibility(View.GONE);
                binding.ivPic.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageUtils.showImage(binding.ivPic, R.drawable.icon_add_rect);
            } else {
                binding.getRoot().setOnClickListener((v) -> {
                });
                binding.ivPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageUtils.showImage(binding.ivPic, s);
                binding.ivClose.setVisibility(View.VISIBLE);
                binding.ivClose.setOnClickListener((v) -> {
                    if (data.size() == 1) {
                        data.set(0, "");
                        notifyDataSetChanged();
                        authSkillActivityWeakReference.get().checkBottom(false);
                    } else {
                        authSkillActivityWeakReference.get().checkBottom(true);
                        data.remove(position);
                        notifyDataSetChanged();
                    }

                });
            }
        }

    }
}
