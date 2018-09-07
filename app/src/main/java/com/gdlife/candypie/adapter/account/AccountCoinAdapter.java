package com.gdlife.candypie.adapter.account;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdlife.candypie.databinding.ItemAccountCoinBinding;
import com.gdlife.candypie.databinding.ItemUserpageGiftBinding;
import com.gdlife.candypie.repository.GiftRepository;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.pay.RechargeConfigBean;
import com.heboot.bean.user.UserGiftChildBean;

import java.util.List;

public class AccountCoinAdapter extends BaseQuickAdapter<RechargeConfigBean.ConfigBean, BaseViewHolder> {


    public AccountCoinAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, RechargeConfigBean.ConfigBean s) {

        ItemAccountCoinBinding binding = DataBindingUtil.bind(helper.itemView);

        if (!StringUtils.isEmpty(s.getIcon())) {
            ImageUtils.showImage(binding.ivZhi, s.getIcon());
            binding.ivZhi.setVisibility(View.VISIBLE);
        } else {
            binding.ivZhi.setVisibility(View.GONE);
        }

        binding.tvCoin.setText(s.getPre_coin());

        binding.tvMoney.setText("¥ " + s.getAmount() + "元");

        if (s.getPlus_coin() > 0) {
            binding.tvCoinPlus.setText("+" + s.getPlus_coin());
            binding.tvCoinPlus.setVisibility(View.VISIBLE);
        } else {
            binding.tvCoinPlus.setVisibility(View.GONE);
        }

    }
}
