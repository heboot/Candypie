package com.gdlife.candypie.activitys.message;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.message.SystemMessageAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityRecyclerviewBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.utils.rv.RVUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.message.SystemMessageBean;
import com.heboot.event.MessageEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SystemMessageActivity extends BaseActivity<ActivityRecyclerviewBinding> {


    private SystemMessageAdapter systemMessageAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_recyclerview;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setShowRight(false);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_systemmsg));

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RVUtils.initSwiperefreshlayout(binding.spLayout, binding.rvList);
    }

    @Override
    public void initData() {
        RxBus.getInstance().post(MessageEvent.REFRESH_UNREAD_NUM_ENENT);
        initCoupons();
    }

    private void initCoupons() {
        params = SignUtils.getNormalParams();

        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().message_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<SystemMessageBean>() {
            @Override
            public void onSuccess(BaseBean<SystemMessageBean> baseBean) {
                SystemMessageBean systemMessageBean = baseBean.getData();
                total = baseBean.getData().getTotalPages();
                pageSize = baseBean.getData().getPageSize();
                if (systemMessageBean.getSp() == 1) {
                    binding.spLayout.setRefreshing(false);
                } else {
                    binding.rvList.loadMoreComplete();
                }
                if (systemMessageBean != null && systemMessageBean.getList() != null && systemMessageBean.getList().size() > 0) {
                    binding.plytContainer.toMain();
                    if (systemMessageAdapter == null) {
                        systemMessageAdapter = new SystemMessageAdapter(systemMessageBean.getList());
                        binding.rvList.setAdapter(systemMessageAdapter);
                    } else {
                        if (sp == 1) {
                            systemMessageAdapter.getData().clear();
                        }
                        systemMessageAdapter.getData().addAll(systemMessageBean.getList());
                        systemMessageAdapter.notifyDataSetChanged();
                    }

                } else {
                    binding.plytContainer.toNoData();
                }

            }

            @Override
            public void onError(BaseBean<SystemMessageBean> baseBean) {

            }
        });


    }


    @Override
    public void initListener() {

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        binding.spLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sp = 1;
                initCoupons();
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
                initCoupons();

            }
        });
    }
}
