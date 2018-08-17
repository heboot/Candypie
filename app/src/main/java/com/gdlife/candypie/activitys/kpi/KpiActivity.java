package com.gdlife.candypie.activitys.kpi;

import android.support.v4.content.ContextCompat;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityGrowBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ConfigService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.kpi.KpiIndexBean;
import com.heboot.bean.pay.ServiceLevelBean;
import com.heboot.utils.MStatusBarUtils;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class KpiActivity extends BaseActivity<ActivityGrowBinding> {

    private ServiceLevelBean serviceLevelBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_grow;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityNOLightMode(this);
    }

    @Override
    public void initData() {

        binding.dynamicArcView.addSeries(new SeriesItem.Builder(ContextCompat.getColor(this, R.color.grow_progress_bg_color))
                .setRange(0, 100, 100)

                .setLineWidth(32f)
                .build());// .setInitialVisibility(false)

        serviceLevelBean = ConfigService.getInstance().getCurrentServiceLevelBean();

        ImageUtils.showImage(binding.ivLevel, serviceLevelBean.getImg());


        commit_report();
    }


    private void commit_report() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().kpi_index(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<KpiIndexBean>() {
            @Override
            public void onSuccess(BaseBean<KpiIndexBean> baseBean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        SeriesItem seriesItem1 = new SeriesItem.Builder(ContextCompat.getColor(KpiActivity.this, R.color.white))
                                .setRange(0, 100, baseBean.getData().getKpi_rate() * 100)
                                .setLineWidth(32f)
                                .build();
                        binding.dynamicArcView.addSeries(seriesItem1);

                        binding.setKpiBean(baseBean.getData());

                        binding.tvBili.setText((int) (baseBean.getData().getKpi_rate() * 100) + "%");

                        ConfigBean.KpiConfigBean kpiConfigBean = ConfigService.getInstance().getCurrentKPILevelBean(baseBean.getData().getWeek_kpi());

                        if (kpiConfigBean != null) {
                            ImageUtils.showImage(binding.ivWeekly, kpiConfigBean.getImg());
                        }


                    }
                });
            }

            @Override
            public void onError(BaseBean<KpiIndexBean> baseBean) {
            }
        });
    }

    @Override
    public void initListener() {

        binding.vBack.setOnClickListener((v) -> {
            finish();
        });


        binding.vRight.setOnClickListener((v) -> {
            IntentUtils.toWeeklyActivity(this);
        });

        binding.vLeft.setOnClickListener((v) -> {
            IntentUtils.toMyLevelActivity(this);
        });

    }
}
