package com.gdlife.candypie.fragments.order;

import android.annotation.SuppressLint;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.order.UserOrderAdapter;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.FragmentRecyclerviewBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.utils.rv.RVUtils;
import com.gdlife.candypie.widget.rv.TransparentItemDecoration;
import com.heboot.base.BaseBean;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.event.OrderEvent;
import com.heboot.utils.LogUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/1.
 */

public class OrderListFragment extends BaseFragment<FragmentRecyclerviewBinding> {
    //QMUIDisplayHelper.px2dp(getContext(), 10)), getContext().getColor(R.color.color_EDEDF3)

    private UserOrderAdapter userOrderAdapter;

    private int status = -3;

    private boolean isFirst = true;

    public OrderListFragment(int status) {
        this.status = status;
    }

    public OrderListFragment() {
    }


    @Override
    public void initUI() {
        RVUtils.initSwiperefreshlayout(binding.spLayout, binding.rvList);
        binding.rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvList.addItemDecoration(new TransparentItemDecoration(QMUIDisplayHelper.dp2px(getContext(), 5), ContextCompat.getColor(getContext(), R.color.color_EDEDF3)));
    }

    @Override
    public void initData() {
//        initOrderData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && status > -3) {//&& !isFirst
            initOrderData();
        }
    }

    private void initOrderData() {

        params = SignUtils.getNormalParams();

        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        params.put(MKey.STATUS, status);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);

        HttpClient.Builder.getGuodongServer().order_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<OrderListBean>() {
            @Override
            public void onSuccess(BaseBean<OrderListBean> baseBean) {
                OrderListBean orderListBean = baseBean.getData();
                isFirst = false;
                total = orderListBean.getTotalPages();
                pageSize = orderListBean.getPageSize();
                if (orderListBean.getSp() == 1) {
                    binding.spLayout.setRefreshing(false);
                } else {
                    binding.rvList.loadMoreComplete();
                }

                if (orderListBean != null && orderListBean.getList() != null && orderListBean.getList().size() > 0) {
                    binding.plytContainer.toMain();
                    if (userOrderAdapter == null) {
                        userOrderAdapter = new UserOrderAdapter(orderListBean.getList());
                        binding.rvList.setAdapter(userOrderAdapter);
                    } else {
                        if (sp == 1) {
                            userOrderAdapter.getData().clear();
                        }
                        userOrderAdapter.getData().addAll(orderListBean.getList());
                        userOrderAdapter.notifyDataSetChanged();
                    }

                } else {
                    binding.plytContainer.toNoData();
                    binding.plytContainer.setNoDataInfo(MAPP.mapp.getString(R.string.order_null_tip));
                }
            }

            @Override
            public void onError(BaseBean<OrderListBean> baseBean) {

            }
        });


    }

    @Override
    public void initListener() {
        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(OrderEvent.CANCEL_ORDER_ENENT)) {
                    initOrderData();
                } else if (o.equals(OrderEvent.COMPLETE_ORDER_ENENT)) {
                    initOrderData();
                } else if (o.equals(OrderEvent.REFRESH_ORDER_ENENT)) {
                    initOrderData();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        binding.spLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sp = 1;
                initOrderData();
            }
        });
        binding.rvList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (sp >= total) {
                    binding.rvList.loadMoreComplete();
                    ToastUtils.showLoadMoreToast(getString(R.string.load_more_end));
                    return;
                }
                sp = sp + 1;
                initOrderData();

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }
}
