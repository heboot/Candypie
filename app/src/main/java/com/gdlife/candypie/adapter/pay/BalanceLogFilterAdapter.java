package com.gdlife.candypie.adapter.pay;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.pay.BalanceLogActivity;
import com.gdlife.candypie.databinding.ItemBalanceLogFilterBinding;
import com.gdlife.candypie.databinding.ItemCancelServerCauseBinding;
import com.heboot.bean.config.IncomeFilterConfigBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by heboot on 2018/2/26.
 */

public class BalanceLogFilterAdapter extends BaseRecyclerViewAdapter {

    private WeakReference<BalanceLogActivity> weakReference;

    private int currentIndex = -1;


    public BalanceLogFilterAdapter(WeakReference<BalanceLogActivity> weakReference, List<IncomeFilterConfigBean> datas) {
        this.weakReference = weakReference;
        this.data.addAll(datas);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BalanceLogFilterAdapter.ViewHolder(parent, R.layout.item_balance_log_filter);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<IncomeFilterConfigBean, ItemBalanceLogFilterBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final IncomeFilterConfigBean s, int position) {
            binding.tv1.setText(s.getTitle());
            binding.getRoot().setOnClickListener((v) -> {
                currentIndex = position;
                weakReference.get().doFilter(s.getName());
                notifyDataSetChanged();
            });

            if (currentIndex == position) {
                binding.tv1.setTextColor(ContextCompat.getColor(weakReference.get(), R.color.theme_color));
            }else{
                binding.tv1.setTextColor(ContextCompat.getColor(weakReference.get(), R.color.color_343445));
            }
        }
    }
}
