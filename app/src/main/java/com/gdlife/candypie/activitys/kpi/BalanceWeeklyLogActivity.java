package com.gdlife.candypie.activitys.kpi;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.kpi.KpiLogAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityRecyclerviewBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.utils.rv.RVUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.kpi.KpiLogBean;
import com.heboot.utils.MStatusBarUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/14.
 */

public class BalanceWeeklyLogActivity extends BaseActivity<ActivityRecyclerviewBinding> {

    private KpiLogAdapter balanceLogAdapter;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityNOLightMode(this);
        binding.includeToolbar.setShowRight(false);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText(R.string.page_title_weekly_log);

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

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
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().score_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<KpiLogBean>() {
            @Override
            public void onSuccess(BaseBean<KpiLogBean> baseBean) {
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
                            balanceLogAdapter = new KpiLogAdapter(baseBean.getData().getList());
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
            public void onError(BaseBean<KpiLogBean> baseBean) {

            }
        });
    }


    @Override
    public void initListener() {


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


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recyclerview;
    }
}
