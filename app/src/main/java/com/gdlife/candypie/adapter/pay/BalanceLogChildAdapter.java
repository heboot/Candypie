package com.gdlife.candypie.adapter.pay;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.ItemBalanceLog2Binding;
import com.gdlife.candypie.databinding.ItemBalanceLogBinding;
import com.heboot.bean.pay.InComeListBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.heboot.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by heboot on 2018/3/15.
 */

public class BalanceLogChildAdapter extends BaseRecyclerViewAdapter {

    public BalanceLogChildAdapter(List<InComeListBean.ItemsBean> listBeanList) {
        data.addAll(listBeanList);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BalanceLogChildAdapter.ViewHolder(parent, R.layout.item_balance_log2);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<InComeListBean.ItemsBean, ItemBalanceLog2Binding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final InComeListBean.ItemsBean s, int position) {

            if (s.getAmount().indexOf("-") > -1) {
                binding.tvPrice.setTextColor(ContextCompat.getColor(MAPP.mapp, R.color.color_FF5252));
                binding.tvPrice.setText(s.getAmount());
            } else if (s.getAmount().indexOf("-") == -1) {
                binding.tvPrice.setTextColor(ContextCompat.getColor(MAPP.mapp, R.color.color_00BC44));
                binding.tvPrice.setText("+" + s.getAmount());
            }

            binding.tvTime.setText(DateUtil.date2Str(new Date(Long.parseLong(s.getCreate_time()) * 1000), DateUtil.FORMAT_YMDHM));

            binding.setItemBean(s);
        }


    }
}
