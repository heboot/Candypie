package com.gdlife.candypie.adapter.rank;

import android.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.LayoutRankItemBinding;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.entity.User;

import java.util.List;

public class RankUserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {


    private String rank_title;


    public RankUserAdapter(int layoutResId, List data, String rank_title) {
        super(layoutResId, data);
        this.rank_title = rank_title;
    }


    @Override
    protected void convert(BaseViewHolder helper, User s) {

        LayoutRankItemBinding layoutRankItemBinding = DataBindingUtil.bind(helper.itemView);

        layoutRankItemBinding.includeSexage.setUser(s);

        helper.setText(R.id.tv_name, s.getNickname());

        helper.setText(R.id.tv_num, s.getRank() + "");


        ImageUtils.showAvatar(layoutRankItemBinding.ivAvatar, s.getAvatar());

        helper.setText(R.id.tv_abst, s.getAbst());

        if (rank_title.equals("consume")) {
            helper.setText(R.id.tv_action, "充值钻石");
        } else if (rank_title.equals("rq")) {
            helper.setText(R.id.tv_action, "人气值");
        } else if (rank_title.equals("wealth")) {
            helper.setText(R.id.tv_action, "财富值");
        }


        helper.setText(R.id.tv_value, s.getRank_value());

        layoutRankItemBinding.getRoot().setOnClickListener((v) -> {
            IntentUtils.toUserPageActivity(MAPP.mapp.getCurrentActivity(), String.valueOf(s.getId()));
        });

    }
}
