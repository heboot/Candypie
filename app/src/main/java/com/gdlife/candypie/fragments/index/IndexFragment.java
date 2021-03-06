package com.gdlife.candypie.fragments.index;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.common.CustomEvent;
import com.gdlife.candypie.databinding.FragmentIndexBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.CustomEventUtil;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.heboot.bean.config.TopMenuBean;
import com.heboot.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends BaseFragment<FragmentIndexBinding> {

    private int currentTabIndex = 0;


    public static IndexFragment newInstance() {
        Bundle args = new Bundle();
        IndexFragment fragment = new IndexFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    public void initUI() {
    }

    @Override
    public void initData() {
        List<String> titles = new ArrayList();

        ArrayList<Fragment> fragments = new ArrayList<>();

        if (MAPP.mapp.getConfigBean().getIndex_top_menu() == null || MAPP.mapp.getConfigBean().getIndex_top_menu().size() == 0) {
            return;
        }

        int index = -1;

        for (TopMenuBean topMenuBean : MAPP.mapp.getConfigBean().getIndex_top_menu()) {
            titles.add(topMenuBean.getTitle());
            index = index + 1;
            fragments.add(IndexListFragment.newInstance(topMenuBean.getName()));
            if (topMenuBean.getIs_default() == 1) {
                currentTabIndex = index;
            }
        }


        String[] strings = new String[titles.size()];

        titles.toArray(strings);

        binding.stTitle.setViewPager(binding.rvList, strings, _mActivity, fragments);

        binding.stTitle.setCurrentTab(currentTabIndex);

    }

    @Override
    public void initListener() {

        binding.clytFirst.setOnClickListener((v) -> {
            binding.clytFirst.setVisibility(View.GONE);
            UserService.getInstance().setFirstIndex();
        });

        binding.vSearchbg2.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist(getContext())) {
                return;
            }
            CustomEventUtil.onEvent(CustomEvent.INDEX_CLICK_SEARCH);
            IntentUtils.toSearchActivity(getContext());
        });

    }


}
