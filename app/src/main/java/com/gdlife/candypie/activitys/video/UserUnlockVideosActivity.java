package com.gdlife.candypie.activitys.video;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.user.UserUnlockVideosAdapter;
import com.gdlife.candypie.adapter.user.UserVideosAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.UserVideoActivityFrom;
import com.gdlife.candypie.databinding.ActivityUserVideosBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.rv.RVUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.bean.video.UserVideosBean;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserUnlockVideosActivity extends BaseActivity<ActivityUserVideosBinding> {

    private UserUnlockVideosAdapter userVideosAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_videos;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.tvTitle.setText("解锁视频");
        binding.rvList.setLayoutManager(new GridLayoutManager(this, 3));
//        binding.loadingView.show(true);
        RVUtils.initSwiperefreshlayout(binding.spLayout, binding.rvList);
    }

    @Override
    public void initData() {
        initVideos();
    }

    @Override
    public void initListener() {

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

    }

    private void initVideos() {
        params = SignUtils.getNormalParams();

        params.put(MKey.SP, sp);
        params.put(MKey.PAGESIZE, pageSize);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().unlock_video_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<UserVideosBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(UserVideosBean sendSMSBeanBaseBean) {
                total = sendSMSBeanBaseBean.getTotalPages();
                pageSize = sendSMSBeanBaseBean.getPageSize();

                if (sendSMSBeanBaseBean.getSp() == 1) {
                    binding.spLayout.setRefreshing(false);
                } else {
                    binding.rvList.loadMoreComplete();
                }


                if (sendSMSBeanBaseBean != null && sendSMSBeanBaseBean.getList() != null && sendSMSBeanBaseBean.getList().size() > 0) {
                    binding.plytContainer.toMain();
                    if (userVideosAdapter == null) {
                        if (UserService.getInstance().getUser() == null) {
                            userVideosAdapter = new UserUnlockVideosAdapter(new WeakReference<UserUnlockVideosActivity>(UserUnlockVideosActivity.this), sendSMSBeanBaseBean.getList(), sendSMSBeanBaseBean.getTotal());
                        } else {
                            userVideosAdapter = new UserUnlockVideosAdapter(new WeakReference<UserUnlockVideosActivity>(UserUnlockVideosActivity.this), sendSMSBeanBaseBean.getList(), sendSMSBeanBaseBean.getTotal());


                            binding.rvList.setAdapter(userVideosAdapter);
                        }
//                        else{
                        if (sp == 1) {
                            userVideosAdapter.getData().clear();
                        }
                        userVideosAdapter.getData().addAll(sendSMSBeanBaseBean.getList());
                        userVideosAdapter.notifyDataSetChanged();
//                        }

                    } else {
                        binding.plytContainer.toMain();
                        binding.rvList.setVisibility(View.VISIBLE);
                        binding.rvList.setAdapter(new UserUnlockVideosAdapter(new WeakReference<UserUnlockVideosActivity>(UserUnlockVideosActivity.this), sendSMSBeanBaseBean.getList(), sendSMSBeanBaseBean.getTotal()));
                    }


                } else {
                    binding.plytContainer.toNoData();
                }
            }


            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onError(BaseBean<UserVideosBean> baseBeanEntity) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }

                tipDialog = DialogUtils.getFailDialog(UserUnlockVideosActivity.this, baseBeanEntity.getMessage(), true);
                tipDialog.show();
            }

        }));
    }
}
