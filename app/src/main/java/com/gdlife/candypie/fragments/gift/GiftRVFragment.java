package com.gdlife.candypie.fragments.gift;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.gift.GiftAdapter;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.BaseFragment2;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.FragmentGiftBinding;
import com.gdlife.candypie.utils.GlideImageLoader;
import com.heboot.bean.gift.GiftBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class GiftRVFragment extends BaseFragment2<FragmentGiftBinding> {

    private GiftAdapter giftAdapter;

    private List<GiftBean> mdata = new ArrayList<>();

    public GiftRVFragment(List<GiftBean> mdata) {
        this.mdata.addAll(mdata);
    }

//    public static GiftRVFragment newInstance(List<GiftBean> mdata) {
//        Bundle args = new Bundle();
//        args.putSerializable(MKey.TYPE, (Serializable) mdata);
//        GiftRVFragment fragment = new GiftRVFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    @Override
    public void initUI() {
        binding.rvList.setLayoutManager(new GridLayoutManager(getContext(), 4));
//        mdata = (List<GiftBean>) getArguments().getSerializable(MKey.TYPE);
        giftAdapter = new GiftAdapter(mdata);
        binding.rvList.setAdapter(giftAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
