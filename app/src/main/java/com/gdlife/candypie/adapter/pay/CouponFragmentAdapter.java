package com.gdlife.candypie.adapter.pay;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gdlife.candypie.fragments.pay.CouponFragment;

import java.util.List;

/**
 * Created by heboot on 2018/2/28.
 */

public class CouponFragmentAdapter extends FragmentStatePagerAdapter {

    private List<CouponFragment> fragmentList;

    public CouponFragmentAdapter(FragmentManager fm, List<CouponFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
