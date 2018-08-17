package com.gdlife.candypie.adapter.theme;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.theme.ChooseAddressActivity;
import com.gdlife.candypie.databinding.ItemChooseAddressSortBinding;
import com.heboot.bean.theme.MeetPoiDataBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by heboot on 2018/2/26.
 */

public class ChooseAddressSortAdapter extends BaseRecyclerViewAdapter {

    private WeakReference<ChooseAddressActivity> chooseAddressActivity;

    private int selectedIndex = 0;

    public ChooseAddressSortAdapter(WeakReference<ChooseAddressActivity> chooseAddressActivity, List<MeetPoiDataBean.SortConfigBean> listBeans) {
        data = listBeans;
        this.chooseAddressActivity = chooseAddressActivity;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChooseAddressSortAdapter.ViewHolder(parent, R.layout.item_choose_address_sort);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<MeetPoiDataBean.SortConfigBean, ItemChooseAddressSortBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final MeetPoiDataBean.SortConfigBean s, int position) {
            binding.tvName.setText(s.getName());
            binding.getRoot().setOnClickListener((v) -> {
                selectedIndex = position;
                binding.tvName.setSelected(true);
                chooseAddressActivity.get().selectSort(s);
                notifyDataSetChanged();
            });
            if (selectedIndex == position) {
                binding.tvName.setSelected(true);
            } else {
                binding.tvName.setSelected(false);
            }
        }
    }
}
