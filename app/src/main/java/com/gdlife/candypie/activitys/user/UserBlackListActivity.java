package com.gdlife.candypie.activitys.user;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.user.UserBlackAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityRecyclerviewBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.utils.rv.RVUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.user.UserFavsListBean;
import com.heboot.utils.MStatusBarUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserBlackListActivity extends BaseActivity<ActivityRecyclerviewBinding> {


    private UserBlackAdapter userFavAdapter;

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
        binding.includeToolbar.tvTitle.setText(R.string.page_title_black_userlist);

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RVUtils.initSwiperefreshlayout(binding.spLayout, binding.rvList);
    }

    @Override
    public void initData() {
        initCoupons();
    }


    public void initCoupons() {
        params = SignUtils.getNormalParams();

        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().black_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<UserFavsListBean>() {
            @Override
            public void onSuccess(BaseBean<UserFavsListBean> baseBean) {
                UserFavsListBean systemMessageBean = baseBean.getData();
                total = baseBean.getData().getTotalPages();
                pageSize = baseBean.getData().getPageSize();
                if (systemMessageBean.getSp() == 1) {
                    binding.spLayout.setRefreshing(false);
                } else {
                    binding.rvList.loadMoreComplete();
                }
                if (systemMessageBean != null && systemMessageBean.getList() != null && systemMessageBean.getList().size() > 0) {
                    binding.plytContainer.toMain();
                    if (userFavAdapter == null) {
                        userFavAdapter = new UserBlackAdapter(new WeakReference(UserBlackListActivity.this), systemMessageBean.getList());
                        binding.rvList.setAdapter(userFavAdapter);
                    } else {
                        if (sp == 1) {
                            userFavAdapter.getData().clear();
                        }
                        userFavAdapter.getData().addAll(systemMessageBean.getList());
                        userFavAdapter.notifyDataSetChanged();
                    }

                } else {
                    binding.plytContainer.toNoData();
                    binding.plytContainer.setNoDataInfo(getString(R.string.no_black_userlist));
                }
            }

            @Override
            public void onError(BaseBean<UserFavsListBean> baseBean) {

            }
        });


    }

    public void unBlack(String uid) {
        params = SignUtils.getNormalParams();
        params.put(MKey.BLACK_UID, uid);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().un_black(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                initCoupons();
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {

            }
        });
    }

    @Override
    public void initListener() {
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


        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
    }
}
