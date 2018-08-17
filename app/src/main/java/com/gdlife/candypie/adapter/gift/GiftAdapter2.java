package com.gdlife.candypie.adapter.gift;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.LayoutGiftItemBinding;
import com.gdlife.candypie.fragments.gift.GiftFragment;
import com.gdlife.candypie.fragments.gift.GiftFragment2;
import com.heboot.bean.gift.GiftBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GiftAdapter2 extends BaseAdapter { private WeakReference<GiftFragment2> giftFragmentWeakReference;
    private LayoutInflater mInflater;
    List<GiftBean> mdata = new ArrayList<>();
    public GiftAdapter2(WeakReference<GiftFragment2> giftFragmentWeakReference, List<GiftBean> ddd) {
        this.giftFragmentWeakReference = giftFragmentWeakReference;
        mInflater = LayoutInflater.from(giftFragmentWeakReference.get().getContext());
        mdata.addAll(ddd);

    }

    public GiftAdapter2(List<GiftBean> ddd) {
        mInflater = LayoutInflater.from(MAPP.mapp);
        mdata.addAll(ddd);

    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutGiftItemBinding viewTag;

        if (convertView == null)
        {
            convertView = DataBindingUtil.inflate(mInflater, R.layout.layout_gift_item,null,false).getRoot();

            // construct an item tag
            viewTag =DataBindingUtil.inflate(mInflater, R.layout.layout_gift_item,null,false);
            convertView.setTag(viewTag);
        } else
        {
            viewTag = (LayoutGiftItemBinding) convertView.getTag();
        }

      viewTag.setGiftBean(mdata.get(position));
        return convertView;
    }


}
