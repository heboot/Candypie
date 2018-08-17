package com.gdlife.candypie.fragments.gift;

import android.support.v7.widget.GridLayoutManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.gift.GiftAdapter;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.databinding.FragmentGiftBinding;
import com.gdlife.candypie.repository.GiftRepository;

import java.lang.ref.WeakReference;


public class GiftFragment extends BaseFragment<FragmentGiftBinding> {

    private int page;

    GiftRepository giftRepository;

    public GiftFragment(){}

    public GiftFragment(int p) {
        this.page = p;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    @Override
    public void initUI() {
        binding.rvList.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }

    @Override
    public void initData() {
            giftRepository = new GiftRepository();
            if (page == 1) {
                binding.rvList.setAdapter(new GiftAdapter(new WeakReference<>(this), giftRepository.getVideoGiftsByPage1()));
            } else {
                binding.rvList.setAdapter(new GiftAdapter(new WeakReference<>(this), giftRepository.getVideoGiftsByPage2()));
            }
    }



    public void sendGift() {

    }

    @Override
    public void initListener() {

    }
}
