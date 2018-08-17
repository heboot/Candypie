package com.gdlife.candypie.adapter.kpi;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.pay.BalanceLogChildAdapter;
import com.gdlife.candypie.databinding.ItemBalanceLogBinding;
import com.gdlife.candypie.databinding.ItemKpiLogBinding;
import com.heboot.bean.kpi.KpiLogBeanModel;
import com.heboot.bean.pay.InComeListBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.heboot.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by heboot on 2018/3/15.
 */

public class KpiLogAdapter extends BaseRecyclerViewAdapter {

    public KpiLogAdapter(List<KpiLogBeanModel> listBeanList) {
        data.addAll(listBeanList);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KpiLogAdapter.ViewHolder(parent, R.layout.item_kpi_log);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<KpiLogBeanModel, ItemKpiLogBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final KpiLogBeanModel s, int position) {

            binding.setKpiLogBeanModel(s);

            if (s.getScore().indexOf("-") > -1) {
                binding.tvPrice.setTextColor(ContextCompat.getColor(MAPP.mapp, R.color.color_FF5252));
                binding.tvPrice.setText(s.getScore());
            } else if (s.getScore().indexOf("-") == -1) {
                binding.tvPrice.setTextColor(ContextCompat.getColor(MAPP.mapp, R.color.color_00BC44));
                binding.tvPrice.setText("+" + s.getScore());
            }
            binding.tvTime.setText(DateUtil.date2Str(new Date(Long.parseLong(s.getCreate_time()) * 1000), DateUtil.FORMAT_YMDHM));

        }


    }
}
