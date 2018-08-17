package com.gdlife.candypie.fragments.rank;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.databinding.FragmentRankContainerBinding;
import com.heboot.bean.config.TopMenuBean;

import java.util.ArrayList;
import java.util.List;

public class RankContainerFragment extends BaseFragment<FragmentRankContainerBinding> {


    public static RankContainerFragment newInstance() {
        Bundle args = new Bundle();
        RankContainerFragment fragment = new RankContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rank_container;
    }

    @Override
    public void initUI() {

    }

    @Override
    public void initData() {
        List<String> titles = new ArrayList();

        ArrayList<Fragment> fragments = new ArrayList<>();

        if (MAPP.mapp.getConfigBean().getRank_config() == null || MAPP.mapp.getConfigBean().getRank_config().size() == 0) {
            return;
        }

        for (TopMenuBean topMenuBean : MAPP.mapp.getConfigBean().getRank_config()) {
            titles.add(topMenuBean.getTitle());
            fragments.add(RankListFragment.newInstance(topMenuBean.getName()));
        }


        String[] strings = new String[titles.size()];

        titles.toArray(strings);

        binding.stTitleRank.setViewPager(binding.rvListRank, strings, _mActivity, fragments);

    }

    @Override
    public void initListener() {

    }
}
