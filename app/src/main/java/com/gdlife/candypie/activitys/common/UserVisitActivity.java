package com.gdlife.candypie.activitys.common;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.index.ActiveAdapter;
import com.gdlife.candypie.adapter.order.UserOrderAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityUserVisitListBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.user.UserVisitListBean;
import com.heboot.utils.MStatusBarUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserVisitActivity extends BaseActivity<ActivityUserVisitListBinding> {

    private String to_uid;

    private ActiveAdapter activeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_visit_list;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText(R.string.user_visit_title);


        binding.srytIndex.setEnabled(true);
        binding.srytIndex.setColorSchemeColors(getResources().getColor(R.color.theme_color));
        binding.srytIndex.setDistanceToTriggerSync(700);
        binding.srytIndex.setProgressBackgroundColorSchemeColor(Color.WHITE);
        binding.srytIndex.setSize(SwipeRefreshLayout.DEFAULT);

        binding.rvList.setLoadingMoreProgressStyle(ProgressStyle.BallGridPulse);

        binding.rvList.setPullRefreshEnabled(false);
        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void initData() {
        to_uid = getIntent().getExtras().getString(MKey.TO_UID);

        initLogData();
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.srytIndex.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
    }

    private void initLogData() {
        if (StringUtils.isEmpty(to_uid)) {
            return;
        }
        params = SignUtils.getNormalParams();
        params.put(MKey.TO_UID, to_uid);
        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().visit_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<UserVisitListBean>() {
            @Override
            public void onSuccess(BaseBean<UserVisitListBean> baseBean) {

                if (baseBean.getData().getSp() == 1) {
                    binding.srytIndex.setRefreshing(false);
                } else {
                    binding.rvList.loadMoreComplete();
                }

                if (baseBean.getData() != null && baseBean.getData().getList() != null && baseBean.getData().getList().size() > 0) {
                    binding.plytContainer.toMain();
                    if (activeAdapter == null) {
                        activeAdapter = new ActiveAdapter(baseBean.getData().getList(), false);
                        binding.rvList.setAdapter(activeAdapter);
                    } else {
                        if (sp == 1) {
                            activeAdapter.getData().clear();
                        }
                        activeAdapter.getData().addAll(baseBean.getData().getList());
                        activeAdapter.notifyDataSetChanged();
                    }

                } else {
                    binding.plytContainer.toNoData();
                    binding.plytContainer.setNoDataInfo(MAPP.mapp.getString(R.string.order_null_tip));
                }

            }

            @Override
            public void onError(BaseBean<UserVisitListBean> baseBean) {

            }
        });
    }
}
