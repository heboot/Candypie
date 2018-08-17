package com.gdlife.candypie.adapter.theme;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ItemChooseAddressBinding;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.bean.theme.MeetPoiDataBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.heboot.utils.DistanceUtil;

import java.util.List;

/**
 * Created by heboot on 2018/2/26.
 */

public class ChooseAddressItemAdapter extends BaseRecyclerViewAdapter {

    private String cityId;

    public ChooseAddressItemAdapter(List<MeetPoiDataBean.ListBean> listBeans) {
        data = listBeans;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChooseAddressItemAdapter.ViewHolder(parent, R.layout.item_choose_address);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<MeetPoiDataBean.ListBean, ItemChooseAddressBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final MeetPoiDataBean.ListBean s, int position) {
            ImageUtils.showImage(binding.ivImg, s.getS_photo_urls());
            binding.tvName.setText(s.getName());
            binding.tvInfo.setText(MAPP.mapp.getString(R.string.theme_address_avg) + s.getAvg_price());

            if (s.getAvg_rating() == 0) {
                binding.rb.ivStar1.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
                binding.rb.ivStar2.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
                binding.rb.ivStar3.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
                binding.rb.ivStar4.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
                binding.rb.ivStar5.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
            } else if ((int) s.getAvg_rating() <= 1) {
                binding.rb.ivStar2.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
                binding.rb.ivStar3.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
                binding.rb.ivStar4.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
                binding.rb.ivStar5.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
            } else if ((int) s.getAvg_rating() <= 2) {
                binding.rb.ivStar3.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
                binding.rb.ivStar4.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
                binding.rb.ivStar5.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
            } else if ((int) s.getAvg_rating() <= 3) {
                binding.rb.ivStar4.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
                binding.rb.ivStar5.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
            } else if ((int) s.getAvg_rating() == 4) {
                binding.rb.ivStar5.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.color_898A9E));
            }

            binding.tvInfo2.setText(s.getCategories());
            if (MValue.currentLocation != null) {
                binding.distance.setText(DistanceUtil.getDistanceStr(s.getLat(), s.getLng(), MValue.currentLocation.getLatitude(), MValue.currentLocation.getLongitude()));
            }
//            binding.distance.setText(LocationUtils.getDistance(s.getLat(), s.getLng()) + "");
            binding.getRoot().setOnClickListener((v) -> {
                s.setCity_id(cityId);
                IntentUtils.toChooseAddressDetailActivity(binding.getRoot().getContext(), s, null);
            });
        }
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String id) {
        cityId = id;
    }
}
