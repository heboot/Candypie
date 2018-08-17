package com.gdlife.candypie.adapter.pay;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.ItemBalanceBinding;
import com.gdlife.candypie.databinding.ItemBalanceLogBinding;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.pay.CouponsBeanModel;
import com.heboot.bean.pay.InComeListBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by heboot on 2018/3/15.
 */

public class BalanceLogAdapter extends BaseRecyclerViewAdapter {

    public BalanceLogAdapter(List<InComeListBean> listBeanList) {
        data.addAll(listBeanList);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BalanceLogAdapter.ViewHolder(parent, R.layout.item_balance_log);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<InComeListBean, ItemBalanceLogBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final InComeListBean s, int position) {


            binding.rvList.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }

                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });

            binding.rvList.setAdapter(new BalanceLogChildAdapter(s.getItems()));

           if(!StringUtils.isEmpty(s.getTitle())){
                binding.tvDate.setVisibility(View.VISIBLE);
                binding.tvDate.setText(s.getTitle());
            }else{
                binding.tvDate.setVisibility(View.GONE);
            }
        }


    }
}
