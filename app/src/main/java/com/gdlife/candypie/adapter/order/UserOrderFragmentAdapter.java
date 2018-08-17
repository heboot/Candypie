package com.gdlife.candypie.adapter.order;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.FragmentHomepageVideoBinding;
import com.gdlife.candypie.fragments.homepage.HomepageVideoFragment;
import com.gdlife.candypie.fragments.order.OrderListFragment;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heboot on 2018/2/28.
 */

public class UserOrderFragmentAdapter extends FragmentStatePagerAdapter {

    private List<OrderListFragment> fragmentList;

    public UserOrderFragmentAdapter(FragmentManager fm, List<OrderListFragment> fragmentList) {
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
