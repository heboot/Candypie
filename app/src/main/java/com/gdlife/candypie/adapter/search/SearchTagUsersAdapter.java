package com.gdlife.candypie.adapter.search;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.ItemSearchUserTagsBinding;
import com.gdlife.candypie.databinding.LayoutIndexUserBinding;
import com.gdlife.candypie.databinding.LayoutIndexUserHuoyueBinding;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.entity.User;
import com.heboot.utils.ViewUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.List;

public class SearchTagUsersAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    private String name;

    private int normalHeight;


    public SearchTagUsersAdapter(int layoutResId, List data, String name) {
        super(layoutResId, data);
        this.name = name;
        normalHeight = (QMUIDisplayHelper.getScreenWidth(MAPP.mapp) - MAPP.mapp.getResources().getDimensionPixelOffset(R.dimen.x9)) / 2;
    }


    @Override
    protected void convert(BaseViewHolder helper, User s) {
        if (name.equals("r")) {
            LayoutIndexUserBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setUser(s);

            if (s.getOnline_status() != null) {
                binding.tvOnline.getRoot().setVisibility(View.VISIBLE);
                binding.tvOnline.setOnlineStatus(s.getOnline_status());
                ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(s.getOnline_status().getIcon_color()));
                binding.tvOnline.ivPoint.setImageDrawable(colorDrawable);
            } else {
                binding.tvOnline.getRoot().setVisibility(View.GONE);
            }
            binding.includeSexage.setUser(s);
            binding.tvName.setText(s.getNickname());
            ImageUtils.showIndexRecommendUserImage(binding.ivImg, s.getAvatar());
        } else {
            ItemSearchUserTagsBinding binding = DataBindingUtil.bind(helper.itemView);

            ViewUtils.setViewHeight(binding.getRoot(), normalHeight);

            binding.setUser(s);

            if (s.getOnline_status() != null) {
                binding.tvOnline.getRoot().setVisibility(View.VISIBLE);
                binding.tvOnline.setOnlineStatus(s.getOnline_status());
                ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(s.getOnline_status().getIcon_color()));
                binding.tvOnline.ivPoint.setImageDrawable(colorDrawable);
            } else {
                binding.tvOnline.getRoot().setVisibility(View.GONE);
            }

            binding.tvName.setText(s.getNickname());
            binding.includeSexage.setUser(s);
            ImageUtils.showIndexUserImage(binding.ivImg, s.getAvatar());

            binding.getRoot().setOnClickListener((v -> {
                IntentUtils.toUserPageActivity(binding.getRoot().getContext(), String.valueOf(s.getId()));
            }));
        }


    }
}
