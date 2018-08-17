package com.gdlife.candypie.adapter.gift;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gdlife.candypie.fragments.gift.GiftRVFragment;

import java.util.List;

/**
 * Created by heboot on 2018/3/1.
 */

public class GiftFragmentAdapter extends FragmentStatePagerAdapter {


    private List<GiftRVFragment> fragmentList;

    public GiftFragmentAdapter(FragmentManager fm, List<GiftRVFragment> fragmentList) {
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
