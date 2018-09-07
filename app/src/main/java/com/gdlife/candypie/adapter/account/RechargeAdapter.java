package com.gdlife.candypie.adapter.account;

import android.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdlife.candypie.databinding.ItemRechargeMoneyBinding;
import com.heboot.bean.pay.RechargeConfigBean;

import java.util.List;

public class RechargeAdapter extends BaseQuickAdapter<RechargeConfigBean.ConfigBean, BaseViewHolder> {


    public RechargeAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, RechargeConfigBean.ConfigBean s) {

        ItemRechargeMoneyBinding binding = DataBindingUtil.bind(helper.itemView);

        binding.tvMoney.setText(s.getAmount() + "元");

    }
}
