package com.gdlife.candypie.fragments.gift;

import android.support.v7.widget.GridLayoutManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.gift.GiftAdapter;
import com.gdlife.candypie.adapter.gift.GiftAdapter2;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.databinding.FragmentGift2Binding;
import com.gdlife.candypie.databinding.FragmentGiftBinding;
import com.gdlife.candypie.repository.GiftRepository;

import java.lang.ref.WeakReference;


public class GiftFragment2 extends BaseFragment<FragmentGift2Binding> {

    private int page;

    GiftRepository giftRepository;

    public GiftFragment2(){}

    public GiftFragment2(int p) {
        this.page = p;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift2;
    }

    @Override
    public void initUI() {
//        binding.rvList.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }

    @Override
    public void initData() {
            giftRepository = new GiftRepository();
            if (page == 1) {
                binding.rvList.setAdapter(new GiftAdapter2(new WeakReference<>(this), giftRepository.getVideoGiftsByPage1()));
            } else {
                binding.rvList.setAdapter(new GiftAdapter2(new WeakReference<>(this), giftRepository.getVideoGiftsByPage1()));
            }
    }



    public void sendGift() {

    }

    @Override
    public void initListener() {

    }
}
