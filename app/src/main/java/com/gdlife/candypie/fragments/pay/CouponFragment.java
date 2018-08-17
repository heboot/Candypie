package com.gdlife.candypie.fragments.pay;

import android.annotation.SuppressLint;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.auth.AuthIDActivity;
import com.gdlife.candypie.activitys.pay.CouponsActivity;
import com.gdlife.candypie.adapter.pay.CouponsAdapter;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.FragmentRecyclerviewBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.utils.rv.RVUtils;
import com.gdlife.candypie.widget.rv.TransparentItemDecoration;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.pay.CouponsBean;
import com.heboot.bean.pay.CouponsBeanModel;
import com.heboot.bean.theme.PostThemeBean;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/13.
 */

@SuppressLint("ValidFragment")
public class CouponFragment extends BaseFragment<FragmentRecyclerviewBinding> {

    private int status;

    private int used;

    private CouponsAdapter couponsAdapter;

    private PostThemeBean postThemeBean;

    private CouponsBeanModel selectedCouponsBeanModel;

    public CouponFragment(int s, int used, PostThemeBean postThemeBean, CouponsBeanModel couponsBeanModel) {
        this.status = s;
        this.used = used;
        this.postThemeBean = postThemeBean;
        this.selectedCouponsBeanModel = couponsBeanModel;
    }

    @Override
    public void initUI() {
        RVUtils.initSwiperefreshlayout(binding.spLayout, binding.rvList);
        binding.rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvList.addItemDecoration(new TransparentItemDecoration(QMUIDisplayHelper.dp2px(getContext(), 15), ContextCompat.getColor(getContext(), R.color.color_EDEDF3)));

    }

    @Override
    public void initData() {
        initCoupons();
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
    }

    private void initCoupons() {
        params = SignUtils.getNormalParams();
        if (postThemeBean != null) {
            if (postThemeBean.getCoupons_list() != null && postThemeBean.getCoupons_list().size() > 0) {
                binding.plytContainer.toMain();
                couponsAdapter = new CouponsAdapter(postThemeBean.getCoupons_list(), new WeakReference<CouponsActivity>((CouponsActivity) getActivity()), true, false, selectedCouponsBeanModel);
                binding.rvList.setAdapter(couponsAdapter);
                binding.spLayout.setEnabled(false);
            } else {
                binding.plytContainer.toNoData();
            }
        } else {
            params.put(MKey.STATUS, status);
            params.put(MKey.SP, sp);
            params.put(MKey.USED, used);
            params.put(MKey.PAGESIZE, pageSize);
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);
            HttpClient.Builder.getGuodongServer().coupons_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<CouponsBean>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    addDisposable(disposable);
                }

                @Override
                public void onSuccess(CouponsBean couponsBean) {
                    total = couponsBean.getTotalPages();
                    pageSize = couponsBean.getPageSize();
                    if (couponsBean.getSp() == 1) {
                        binding.spLayout.setRefreshing(false);
                    } else {
                        binding.rvList.loadMoreComplete();
                    }
                    if (couponsBean != null && couponsBean.getList() != null && couponsBean.getList().size() > 0) {
                        binding.plytContainer.toMain();
                        if (couponsAdapter == null) {
                            couponsAdapter = new CouponsAdapter(couponsBean.getList(), new WeakReference<CouponsActivity>((CouponsActivity) getActivity()), used == 1, status == -1 ? true : false, null);
                            binding.rvList.setAdapter(couponsAdapter);
                        } else {
                            if (sp == 1) {
                                couponsAdapter.getData().clear();
                            }
                            couponsAdapter.getData().addAll(couponsBean.getList());
                            couponsAdapter.notifyDataSetChanged();
                        }

                    } else {
                        binding.plytContainer.toNoData();
                    }


                }

                @Override
                public void onError(BaseBean<CouponsBean> basebean) {
                    if (tipDialog != null && tipDialog.isShowing()) {
                        tipDialog.dismiss();
                    }
                    tipDialog = DialogUtils.getFailDialog(getContext(), basebean.getMessage(), true);
                    tipDialog.show();
                }

                @Override
                public void onError(Throwable throwable) {
                    binding.spLayout.setRefreshing(false);
                }


            }));
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }
}
