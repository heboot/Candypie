package com.gdlife.candypie.adapter.pay;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.pay.CouponsActivity;
import com.gdlife.candypie.databinding.ItemCouponBinding;
import com.gdlife.candypie.databinding.ItemOrderBinding;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.bean.pay.CouponsBeanModel;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.heboot.utils.DateUtil;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

/**
 * Created by heboot on 2018/3/6.
 */

public class CouponsAdapter extends BaseRecyclerViewAdapter {

    private WeakReference<CouponsActivity> weakReference;

    private boolean used;

    private boolean isValid;

    private CouponsBeanModel selectedCouponsBeanModel;

    public CouponsAdapter(List<CouponsBeanModel> list, WeakReference<CouponsActivity> weakReference, boolean used, boolean valid, CouponsBeanModel couponsBeanModel) {
        data = list;
        this.weakReference = weakReference;
        this.used = used;
        this.isValid = valid;
        this.selectedCouponsBeanModel = couponsBeanModel;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CouponsAdapter.ViewHolder(parent, R.layout.item_coupon);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<CouponsBeanModel, ItemCouponBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final CouponsBeanModel s, int position) {

            if (isValid) {
                binding.ivValid.setVisibility(View.VISIBLE);
            } else {
                binding.ivValid.setVisibility(View.GONE);
            }


            binding.setCouponModel(s);

            binding.tvTime.setText(binding.getRoot().getContext().getString(R.string.valid_time)
                    + DateUtil.date2Str(new Date(s.getValid_time() * 1000l), DateUtil.FORMAT_YMD_POINT));


            if (used) {
                if (selectedCouponsBeanModel != null && s.getId() == selectedCouponsBeanModel.getId()) {
                    binding.cbCheck.setChecked(true);
                }
                binding.cbCheck.setVisibility(View.VISIBLE);
                binding.getRoot().setOnClickListener((v) -> {
                    if (selectedCouponsBeanModel != null && selectedCouponsBeanModel.getId() == s.getId()) {
                        selectedCouponsBeanModel = null;
                        weakReference.get().selectCoupon(null);
                    } else {
                        selectedCouponsBeanModel = s;
                        weakReference.get().selectCoupon(s);
                    }
                });

                binding.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (selectedCouponsBeanModel != null && selectedCouponsBeanModel.getId() == s.getId()) {
                            selectedCouponsBeanModel = null;
                            weakReference.get().selectCoupon(null);
                        } else {
                            selectedCouponsBeanModel = s;
                            weakReference.get().selectCoupon(s);
                        }
                    }
                });

            } else {
                binding.cbCheck.setVisibility(View.GONE);
                binding.getRoot().setOnClickListener((v) -> {
                });

            }

        }


    }
}
