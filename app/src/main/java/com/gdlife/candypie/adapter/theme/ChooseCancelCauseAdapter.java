package com.gdlife.candypie.adapter.theme;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.theme.ChooseAddressActivity;
import com.gdlife.candypie.databinding.ItemCancelServerCauseBinding;
import com.gdlife.candypie.databinding.ItemChooseAddressNearBinding;
import com.heboot.bean.theme.CancelCauseChildBean;
import com.heboot.bean.theme.MeetPoiDataBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by heboot on 2018/2/26.
 */

public class ChooseCancelCauseAdapter extends BaseRecyclerViewAdapter {

    private int checkIndex = -1;

    private String checkId;

    public ChooseCancelCauseAdapter(List<CancelCauseChildBean> list) {
        data.addAll(list);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChooseCancelCauseAdapter.ViewHolder(parent, R.layout.item_cancel_server_cause);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<CancelCauseChildBean, ItemCancelServerCauseBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final CancelCauseChildBean s, int position) {
            binding.tvText.setText(s.getContent());
            binding.getRoot().setOnClickListener((v) -> {
                if (binding.cbCheck.isChecked()) {
                    checkId = null;
                    checkIndex = -1;
                    notifyDataSetChanged();
                    binding.cbCheck.setChecked(false);
                } else {
                    binding.cbCheck.setChecked(true);
                    checkId = s.getId();
                    checkIndex = position;
                    notifyDataSetChanged();
                }
            });
            binding.cbCheck.setOnClickListener((v) -> {
                if (!binding.cbCheck.isChecked()) {
                    checkId = null;
                    checkIndex = -1;
                    notifyDataSetChanged();
                    binding.cbCheck.setChecked(false);
                } else {
                    binding.cbCheck.setChecked(true);
                    checkId = s.getId();
                    checkIndex = position;
                    notifyDataSetChanged();
                }
            });
//            binding.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        checkId = null;
//                        checkIndex = -1;
//                        notifyDataSetChanged();
//                    } else {
//                        checkId = s.getId();
//                        checkIndex = position;
//                        notifyDataSetChanged();
//                    }
//                }
//            });

            if (checkIndex == position) {
                binding.cbCheck.setChecked(true);
            } else {
                binding.cbCheck.setChecked(false);
            }

        }
    }

    public String getCheckId() {
        return checkId;
    }


}
