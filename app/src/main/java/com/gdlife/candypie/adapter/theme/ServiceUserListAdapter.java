package com.gdlife.candypie.adapter.theme;

import android.view.View;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.common.ServiceAction;
import com.gdlife.candypie.databinding.ItemServiceUsersBinding;
import com.gdlife.candypie.serivce.OrderService;
import com.gdlife.candypie.utils.DialogUtils;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by heboot on 2018/3/20.
 */

public class ServiceUserListAdapter extends BaseRecyclerViewAdapter {

    private List<OrderListBean.ListBean.ActionConfigBean> actionConfigBeans;


    private boolean showChat, showMobile = false;

    public ServiceUserListAdapter(List<User> mdata, List<OrderListBean.ListBean.ActionConfigBean> actionConfigBeans) {
        this.actionConfigBeans = actionConfigBeans;

        for (OrderListBean.ListBean.ActionConfigBean actionConfigBean : actionConfigBeans) {
            if (actionConfigBean.getAction().equals(ServiceAction.TEL.toString().toLowerCase())) {
                showMobile = true;
            } else if (actionConfigBean.getAction().equals(ServiceAction.IM.toString().toLowerCase())) {
                showChat = true;
            }
        }
        data = mdata;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ServiceUserListAdapter.ViewHolder(parent, R.layout.item_service_users);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<User, ItemServiceUsersBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final User s, int position) {
            binding.setShowChat(showChat);
            binding.setShowMobile(showMobile);
            binding.setUser(s);
            binding.includeSexage.setUser(s);
            binding.ivMobile.setOnClickListener((v -> {
                DialogUtils.getCallMobileDialog(binding.getRoot().getContext(), s.getTel()).show();
            }));

        }
    }
}
