package com.gdlife.candypie.adapter.homepage;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gdlife.candypie.fragments.homepage.HomepageVideoFragment;

import java.util.List;

/**
 * Created by heboot on 2018/3/1.
 */

public class HomepageVideoAdapter extends FragmentStatePagerAdapter {


    private List<HomepageVideoFragment> fragmentList;

    public HomepageVideoAdapter(FragmentManager fm, List<HomepageVideoFragment> fragmentList) {
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
