package com.gdlife.candypie.activitys.theme;

import android.support.v7.widget.LinearLayoutManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.theme.ServiceUserListAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityUserlistServiceBinding;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.entity.User;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.List;

/**
 * Created by heboot on 2018/3/20.
 */

public class ServiceUserListActivity extends BaseActivity<ActivityUserlistServiceBinding> {


    private List<User> users;

    private ServiceUserListAdapter serviceUserListAdapter;

    private List<OrderListBean.ListBean.ActionConfigBean> actionConfigBeans;


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setWhiteBack(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_service_user_list));

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


    }

    @Override
    public void initData() {

        users = (List<User>) getIntent().getExtras().get(MKey.USER_LIST);

        actionConfigBeans = (List<OrderListBean.ListBean.ActionConfigBean>) getIntent().getExtras().get(MKey.SERVICE_ACTION);

        serviceUserListAdapter = new ServiceUserListAdapter(users, actionConfigBeans);

        binding.rvList.setAdapter(serviceUserListAdapter);

    }

    @Override
    public void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userlist_service;
    }
}
