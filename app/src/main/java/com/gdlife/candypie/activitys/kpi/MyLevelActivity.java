package com.gdlife.candypie.activitys.kpi;

import android.support.v7.widget.GridLayoutManager;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.kpi.KpiLevelAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityMyLevelBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ConfigService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.kpi.KpiLevelBean;
import com.heboot.bean.pay.ServiceLevelBean;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyLevelActivity extends BaseActivity<ActivityMyLevelBinding> {

    private KpiLevelAdapter kpiLevelAdapter;

    private ServiceLevelBean serviceLevelBean, currentLevelBean;

    @Override

    protected int getLayoutId() {
        return R.layout.activity_my_level;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityNOLightMode(this);
    }

    @Override
    public void initData() {


        kpiLevelAdapter = new KpiLevelAdapter(MAPP.mapp.getConfigBean().getService_level());

        binding.rvList.setLayoutManager(new GridLayoutManager(this, 2));

        binding.rvList.setAdapter(kpiLevelAdapter);


        commit_report();
    }

    @Override
    public void initListener() {
        binding.vBack.setOnClickListener((v) -> {
            finish();
        });
    }

    private void commit_report() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().level(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<KpiLevelBean>() {
            @Override
            public void onSuccess(BaseBean<KpiLevelBean> baseBean) {

                if (baseBean.getData() == null) {
                    return;
                }

                binding.setKpiLevelBean(baseBean.getData());

                serviceLevelBean = ConfigService.getInstance().getCurrentNextServiceLevelBean();

                currentLevelBean = ConfigService.getInstance().getCurrentServiceLevelBean();
//                ConfigBean.KpiConfigBean kpiConfigBean = ConfigService.getInstance().getCurrentKPILevelBean(String.valueOf(baseBean.getData().getLevel()));
//
//
//
                ImageUtils.showImage(binding.ivLevel, currentLevelBean.getImg());

                if (serviceLevelBean != null) {
                    binding.pb.setMax(serviceLevelBean.getOrder_nums());
                    binding.pb.setProgress(serviceLevelBean.getOrder_nums() - baseBean.getData().getUp_order_nums());
                } else if (serviceLevelBean == null && baseBean.getData().getUp_order_nums() == 0) {
                    binding.pb.setMax(10);
                    binding.pb.setProgress(10);
                }


            }

            @Override
            public void onError(BaseBean<KpiLevelBean> baseBean) {
            }
        });
    }
}
