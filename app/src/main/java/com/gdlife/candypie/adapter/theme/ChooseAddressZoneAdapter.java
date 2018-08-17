package com.gdlife.candypie.adapter.theme;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.theme.ChooseAddressActivity;
import com.gdlife.candypie.databinding.ItemChooseAddressNearBinding;
import com.heboot.bean.theme.MeetPoiDataBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by heboot on 2018/2/26.
 */

public class ChooseAddressZoneAdapter extends BaseRecyclerViewAdapter {

    private WeakReference<ChooseAddressActivity> chooseAddressActivity;

    private int selectedIndex = 0;

    public ChooseAddressZoneAdapter(WeakReference<ChooseAddressActivity> chooseAddressActivity, List<MeetPoiDataBean.ZoneConfigBean.ItemsBeanX> listBeans) {
        data = listBeans;
        this.chooseAddressActivity = chooseAddressActivity;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChooseAddressZoneAdapter.ViewHolder(parent, R.layout.item_choose_address_near);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<MeetPoiDataBean.ZoneConfigBean.ItemsBeanX, ItemChooseAddressNearBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final MeetPoiDataBean.ZoneConfigBean.ItemsBeanX s, int position) {
            binding.tvName.setText(s.getName());
            binding.getRoot().setOnClickListener((v) -> {
                selectedIndex = position;
                chooseAddressActivity.get().initZoneChildData(position, s.getId() + "");
                notifyDataSetChanged();
            });
            if (selectedIndex == position) {
                binding.tvName.setSelected(true);
                binding.getRoot().setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
            } else {
                binding.tvName.setSelected(false);
                binding.getRoot().setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_F7F7FC));
            }
        }
    }
}
