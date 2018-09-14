package com.gdlife.candypie.adapter.search;

import android.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdlife.candypie.databinding.ItemSearchRecommendUserBinding;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.entity.User;

import java.util.List;

public class SearchRecommendUserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {


    public SearchRecommendUserAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, User s) {

        ItemSearchRecommendUserBinding binding = DataBindingUtil.bind(helper.itemView);

        ImageUtils.showAvatar(binding.ivAvatar, s.getAvatar());

        binding.tvName.setText(s.getNickname());

        binding.getRoot().setOnClickListener((v) -> {
            IntentUtils.toUserPageActivity(binding.getRoot().getContext(), String.valueOf(s.getId()));
        });


    }

}
