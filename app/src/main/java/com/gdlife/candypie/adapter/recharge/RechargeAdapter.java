package com.gdlife.candypie.adapter.recharge;

import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.pay.RechargeActivity;
import com.gdlife.candypie.databinding.ItemRechargeBinding;
import com.gdlife.candypie.databinding.ItemRechargeCoinBinding;
import com.heboot.bean.pay.RechargeConfigBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by heboot on 2018/3/6.
 */

public class RechargeAdapter extends BaseRecyclerViewAdapter {

    private int select = 0;
    private RechargeConfigBean.ConfigBean selectBean;
    private WeakReference<RechargeActivity> weakReference;


    public RechargeAdapter(List<RechargeConfigBean.ConfigBean> list, WeakReference<RechargeActivity> weakReference) {
        this.data = list;
        this.weakReference = weakReference;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RechargeAdapter.ViewHolder(parent, R.layout.item_recharge);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<RechargeConfigBean.ConfigBean, ItemRechargeBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final RechargeConfigBean.ConfigBean s, int position) {

            binding.tvMoney.setText(s.getAmount() + "");

            if (select == position) {
                binding.clytContainer.setSelected(true);
                binding.tvMoney.setSelected(true);
                selectBean = s;
                weakReference.get().setBottom();
            } else {
                binding.clytContainer.setSelected(false);
                binding.tvMoney.setSelected(false);
            }

            binding.getRoot().setOnClickListener((v) -> {
                select = position;
                selectBean = s;
                weakReference.get().setBottom();
                notifyDataSetChanged();

            });

        }


    }

    public RechargeConfigBean.ConfigBean getSelectBean() {
        return selectBean;
    }
}
