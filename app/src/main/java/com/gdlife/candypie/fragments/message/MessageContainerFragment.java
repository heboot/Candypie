package com.gdlife.candypie.fragments.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.FragmentMessageContainerBinding;
import com.gdlife.candypie.serivce.UserService;

import java.util.ArrayList;
import java.util.List;

public class MessageContainerFragment extends BaseFragment<FragmentMessageContainerBinding> {

    private List<String> titles = new ArrayList();

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public static MessageContainerFragment newInstance() {
        Bundle args = new Bundle();
        MessageContainerFragment fragment = new MessageContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_container;
    }

    @Override
    public void initUI() {

    }

    @Override
    public void initData() {
        getTopMenu();
    }

    private void getTopMenu() {

        titles.add("消息");
        fragments.add(MessageFragment.newInstance());

        /**
         * 如果是游客 或者是未认证成功的，只给两个菜单
         * 认证成功的给3个菜单
         */
        if (UserService.getInstance().getUser() == null || UserService.getInstance().getUser().getService_auth_status() == null || UserService.getInstance().getUser().getService_auth_status() != MValue.AUTH_STATUS_SUC) {
            titles.add("活跃");
            fragments.add(MessageActiveFragment.newInstance());
        } else {
            titles.add("活跃");
            fragments.add(MessageActiveFragment.newInstance());
            titles.add("通话");
            fragments.add(MessageOrderFragment.newInstance());
        }

        String[] strings = new String[titles.size()];

        titles.toArray(strings);

        binding.stTitleMsg.setViewPager(binding.vpListMsg, strings, _mActivity, fragments);


    }

    @Override
    public void initListener() {

    }
}
