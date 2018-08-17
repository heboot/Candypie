package com.gdlife.candypie.adapter.theme;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

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

public class ChooseAddressZoneChildAdapter extends BaseRecyclerViewAdapter {

    private int selectedIndex = 0;

    private WeakReference<ChooseAddressActivity> chooseAddressActivity;

    private String pid;

    public ChooseAddressZoneChildAdapter(WeakReference<ChooseAddressActivity> chooseAddressActivity, List<MeetPoiDataBean.ZoneConfigBean.ItemsBeanX.ItemsBean> listBeans, String pid) {
        data = listBeans;
        this.chooseAddressActivity = chooseAddressActivity;
        this.pid = pid;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChooseAddressZoneChildAdapter.ViewHolder(parent, R.layout.item_choose_address_near);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<MeetPoiDataBean.ZoneConfigBean.ItemsBeanX.ItemsBean, ItemChooseAddressNearBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final MeetPoiDataBean.ZoneConfigBean.ItemsBeanX.ItemsBean s, int position) {

            binding.getRoot().setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));

            binding.tvName.setText(s.getName());

            binding.getRoot().setOnClickListener((v) -> {
                selectedIndex = position;
//                binding.tvName.setSelected(true);

                if (pid.equals("-1000")) {
                    chooseAddressActivity.get().selectZone(true, s);
                } else {
                    chooseAddressActivity.get().selectZone(false, s);
                }


            });

        }
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
