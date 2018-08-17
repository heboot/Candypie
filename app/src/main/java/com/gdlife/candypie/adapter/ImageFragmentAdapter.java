package com.gdlife.candypie.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gdlife.candypie.fragments.ImageFragment;
import com.gdlife.candypie.fragments.order.OrderListFragment;

import java.util.List;

/**
 * Created by heboot on 2018/2/28.
 */

public class ImageFragmentAdapter extends FragmentStatePagerAdapter {

    private List<ImageFragment> fragmentList;

    public ImageFragmentAdapter(FragmentManager fm, List<ImageFragment> fragmentList) {
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
