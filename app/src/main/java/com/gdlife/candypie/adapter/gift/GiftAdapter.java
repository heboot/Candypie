package com.gdlife.candypie.adapter.gift;

import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.LayoutGiftItemBinding;
import com.gdlife.candypie.fragments.gift.GiftFragment;
import com.gdlife.candypie.fragments.gift.GiftRVFragment;
import com.heboot.bean.gift.GiftBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

public class GiftAdapter extends BaseRecyclerViewAdapter {


    private GiftBean selectedGiftBean;

    private WeakReference<GiftRVFragment> giftFragmentWeakReference;

    private int select = -1;

    public GiftAdapter(WeakReference<GiftFragment> giftFragmentWeakReference, List<GiftBean> mdata) {
        data.addAll(mdata);

    }

    public GiftAdapter(WeakReference<GiftRVFragment> giftFragmentWeakReference, List<GiftBean> mdata, boolean t) {
        data.addAll(mdata);
        giftFragmentWeakReference = giftFragmentWeakReference;
    }

    public GiftAdapter(List<GiftBean> mdata) {
        data.addAll(mdata);

    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.layout_gift_item);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<GiftBean, LayoutGiftItemBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final GiftBean s, int position) {

            binding.setGiftBean(s);

            binding.getRoot().setOnClickListener((v) -> {
                select = position;
                selectedGiftBean = s;
                notifyDataSetChanged();
                MValue.currentSelectedGiftBean = s;
//                giftFragmentWeakReference.get().sendGift();
            });

            if (select == position) {
                binding.ivBorder.setVisibility(View.VISIBLE);
            } else {
                binding.ivBorder.setVisibility(View.GONE);
            }

        }

    }

    public GiftBean getSelectedGiftBean() {
        return selectedGiftBean;
    }

    public void setSelectedGiftBean(GiftBean selectedGiftBean) {
        this.selectedGiftBean = selectedGiftBean;
    }
}
