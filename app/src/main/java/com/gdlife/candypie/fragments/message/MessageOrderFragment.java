package com.gdlife.candypie.fragments.message;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.message.MessageOrderAdapter;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.FragmentMessageOrderBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ServerService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.utils.PreferencesUtils;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MessageOrderFragment extends BaseFragment<FragmentMessageOrderBinding> {

    private MessageOrderAdapter messageOrderAdapter;

    private PermissionUtils permissionUtils;

    private ServerService serverService = new ServerService();


    public static MessageOrderFragment newInstance() {
        Bundle args = new Bundle();
        MessageOrderFragment fragment = new MessageOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_order;
    }

    @Override
    public void initUI() {
        permissionUtils = new PermissionUtils();
        binding.rList.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (messageOrderAdapter == null || messageOrderAdapter.getData() == null || messageOrderAdapter.getData().size() == 0) {
//            if (binding.nodataLayout.getVisibility() != View.VISIBLE) {
            binding.srytIndex.setRefreshing(true);
            initOrders();
        }
//
//        }

    }

    @Override
    public void initData() {
        messageOrderAdapter = new MessageOrderAdapter(R.layout.item_order_rob, new ArrayList<>());
        binding.rList.setAdapter(messageOrderAdapter);
    }

    @Override
    public void initListener() {

        binding.srytIndex.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sp = 1;
                initOrders();

            }
        });

        messageOrderAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (sp >= total) {
                    messageOrderAdapter.loadMoreEnd();
                    return;
                }
                sp = sp + 1;
                initOrders();
            }
        }, binding.rList);

    }

    private void initOrders() {
        params = SignUtils.getNormalParams();
        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().push_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<OrderListBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(OrderListBean orderListBean) {

                DialogUtils.showIndexDialog(getActivity(), permissionUtils, PreferencesUtils.getBoolean(MAPP.mapp, MKey.FIRST_ROB, true), serverService, orderListBean.getInit_setting() == 1, false);

                total = orderListBean.getTotalPages();
                pageSize = orderListBean.getPageSize();
                if (orderListBean.getSp() == 1) {
                    binding.srytIndex.setRefreshing(false);
                } else {
                    messageOrderAdapter.loadMoreComplete();
                }

                if (orderListBean != null && orderListBean.getList() != null && orderListBean.getList().size() > 0) {
                    if (messageOrderAdapter == null) {
                        messageOrderAdapter = new MessageOrderAdapter(R.layout.item_order_rob, orderListBean.getList());
                        binding.rList.setAdapter(messageOrderAdapter);
                    } else {
                        if (sp == 1) {
                            messageOrderAdapter.getData().clear();
                            messageOrderAdapter.notifyDataSetChanged();
                        }
//                        for (OrderListBean.ListBean listBean : orderListBean.getList()) {
//                            indexOrderAdapter.doAdd(listBean);
//                        }
                        messageOrderAdapter.addData(orderListBean.getList());
                    }

                } else {
                    if (messageOrderAdapter != null) {
                        if (messageOrderAdapter.getData() != null) {
                            messageOrderAdapter.getData().clear();
                            messageOrderAdapter.notifyDataSetChanged();
                        }
                    } else {
                        messageOrderAdapter = new MessageOrderAdapter(R.layout.item_order_rob, new ArrayList<>());
                        binding.rList.setAdapter(messageOrderAdapter);

                    }

//                    binding.nodataLayout.setVisibility(View.VISIBLE);
//                    binding.tvNodateIntro.setText("目前没有新的订单");
//                    binding.rvList.setVisibility(View.GONE);
//                    binding.plytContainer.toNoData();
                }


            }


            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onError(BaseBean<OrderListBean> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(getContext(), basebean.getMessage(), true);
                tipDialog.show();
            }


        }));
    }

}
