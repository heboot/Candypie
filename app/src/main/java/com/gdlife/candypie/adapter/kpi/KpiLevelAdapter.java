package com.gdlife.candypie.adapter.kpi;

import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.ItemLevelRateBinding;
import com.heboot.bean.pay.ServiceLevelBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.List;

public class KpiLevelAdapter extends BaseRecyclerViewAdapter {


    public KpiLevelAdapter(List<ServiceLevelBean> list) {
        this.data = list;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KpiLevelAdapter.ViewHolder(parent, R.layout.item_level_rate);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<ServiceLevelBean, ItemLevelRateBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final ServiceLevelBean s, int position) {

            binding.tvLevel.setText("Lv." + s.getId());

            binding.tvLevelRate.setText(s.getRate());

        }


    }

}
