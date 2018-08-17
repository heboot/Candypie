package com.gdlife.candypie.adapter.recharge;

import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.pay.CouponsActivity;
import com.gdlife.candypie.activitys.pay.RechargeActivity;
import com.gdlife.candypie.databinding.ItemCouponBinding;
import com.gdlife.candypie.databinding.ItemRechargeCoinBinding;
import com.heboot.bean.pay.CouponsBeanModel;
import com.heboot.bean.pay.RechargeConfigBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by heboot on 2018/3/6.
 */

public class RechargeCoinAdapter extends BaseRecyclerViewAdapter {

    private int select = 0;

    private RechargeConfigBean.ConfigBean selectBean;

    private WeakReference<RechargeActivity> weakReference;

    public RechargeCoinAdapter(List<RechargeConfigBean.ConfigBean> list, WeakReference<RechargeActivity> weakReference) {
        this.data = list;
        this.weakReference = weakReference;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RechargeCoinAdapter.ViewHolder(parent, R.layout.item_recharge_coin);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<RechargeConfigBean.ConfigBean, ItemRechargeCoinBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final RechargeConfigBean.ConfigBean s, int position) {

            binding.tvMoney.setText(s.getCoin());

            binding.tvCoin.setText(binding.getRoot().getContext().getString(R.string.price_symbol) + " " + s.getAmount());

            if (select == position) {
                binding.clytContainer.setSelected(true);
                binding.tvMoney.setSelected(true);
                binding.tvCoin.setSelected(true);
                selectBean = s;
                weakReference.get().setBottom();
            } else {
                binding.clytContainer.setSelected(false);
                binding.tvMoney.setSelected(false);
                binding.tvCoin.setSelected(false);
            }

            binding.getRoot().setOnClickListener((v) -> {
                select = position;
                selectBean = s;
                notifyDataSetChanged();
                weakReference.get().setBottom();

            });


        }

    }

    public RechargeConfigBean.ConfigBean getSelectBean() {
        return selectBean;
    }
}
