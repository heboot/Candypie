package com.gdlife.candypie.adapter.discover;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gdlife.candypie.fragments.order.OrderListFragment;

import java.util.List;

/**
 * Created by heboot on 2018/2/28.
 */

public class DiscoverFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;

    public DiscoverFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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
