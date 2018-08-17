package com.gdlife.candypie.activitys.pay;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.pay.BalanceLogAdapter;
import com.gdlife.candypie.adapter.pay.BalanceLogFilterAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.InComeType;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityBalanceLogBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.utils.rv.RVUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.pay.InComeBean;
import com.heboot.event.NormalEvent;
import com.heboot.utils.MStatusBarUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/14.
 */

public class BalanceLogActivity extends BaseActivity<ActivityBalanceLogBinding> {

    private BalanceLogAdapter balanceLogAdapter;

    private String type = InComeType.all.toString();

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setShowRight(true);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.detail));
        binding.includeToolbar.tvRight.setText(getString(R.string.filtrate));

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rvFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        binding.rvFilter.setAdapter(new BalanceLogFilterAdapter(new WeakReference<>(this), MAPP.mapp.getConfigBean().getIncome_filter_config()));

        RVUtils.initSwiperefreshlayout(binding.spLayout, binding.rvList);
    }

    @Override
    public void initData() {

        binding.plytContainer.toMain();

        initLogData();

    }

    private void initLogData() {
        params = SignUtils.getNormalParams();
        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        params.put(MKey.TYPE, type);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().income_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<InComeBean>() {
            @Override
            public void onSuccess(BaseBean<InComeBean> baseBean) {
                if (baseBean.getData().getList() == null || baseBean.getData().getList().size() <= 0) {
                    binding.plytContainer.toNoData();
                } else {
                    total = baseBean.getData().getTotalPages();
                    pageSize = baseBean.getData().getPageSize();
                    if (baseBean.getData().getSp() == 1) {
                        binding.spLayout.setRefreshing(false);
                    } else {
                        binding.rvList.loadMoreComplete();
                    }
                    if (baseBean != null && baseBean.getData().getList() != null && baseBean.getData().getList().size() > 0) {
                        binding.plytContainer.toMain();
                        if (balanceLogAdapter == null) {
                            balanceLogAdapter = new BalanceLogAdapter(baseBean.getData().getList());
                            binding.rvList.setAdapter(balanceLogAdapter);
                        } else {
                            if (sp == 1) {
                                balanceLogAdapter.getData().clear();
                            }
                            balanceLogAdapter.getData().addAll(baseBean.getData().getList());
                            balanceLogAdapter.notifyDataSetChanged();
                        }

                    } else {
                        binding.plytContainer.toNoData();
                    }

                }
            }

            @Override
            public void onError(BaseBean<InComeBean> baseBean) {

            }
        });
    }

    public void doFilter(String t) {
        this.type = t;
        sp = 1;
        binding.rvFilter.setVisibility(View.GONE);
        binding.vline.setVisibility(View.GONE);
        binding.vBg.setVisibility(View.GONE);
        initLogData();
    }


    @Override
    public void initListener() {

        binding.vBg.setOnClickListener((v) -> {
            if (binding.rvFilter.getVisibility() == View.GONE) {
                binding.vline.setVisibility(View.VISIBLE);
                binding.rvFilter.setVisibility(View.VISIBLE);
                binding.vBg.setVisibility(View.VISIBLE);
            } else {
                binding.vline.setVisibility(View.GONE);
                binding.rvFilter.setVisibility(View.GONE);
                binding.vBg.setVisibility(View.GONE);
            }
        });

        binding.includeToolbar.tvRight.setOnClickListener((v) -> {
            if (binding.rvFilter.getVisibility() == View.GONE) {
                binding.vline.setVisibility(View.VISIBLE);
                binding.rvFilter.setVisibility(View.VISIBLE);
                binding.vBg.setVisibility(View.VISIBLE);
            } else {
                binding.vline.setVisibility(View.GONE);
                binding.rvFilter.setVisibility(View.GONE);
                binding.vBg.setVisibility(View.GONE);
            }
        });

        binding.spLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sp = 1;
                initLogData();
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
                initLogData();
            }
        });


        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });


        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(NormalEvent.FINISH_PAGE_BY_SELECT_USER)) {
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_balance_log;
    }
}
