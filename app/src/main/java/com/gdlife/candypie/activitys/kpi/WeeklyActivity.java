package com.gdlife.candypie.activitys.kpi;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.RelativeLayout;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.ActivityWeeklyBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ConfigService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.view.WeeklyOrderProgressView;
import com.heboot.base.BaseBean;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.kpi.KpiWeeklyBean;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WeeklyActivity extends BaseActivity<ActivityWeeklyBinding> {

    private int currentSelect = 0;

    private ObjectAnimator animator;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weekly;
    }


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityNOLightMode(this);
    }

    @Override
    public void initData() {

        binding.includeWeeklyMenu.tvCurrent.setSelected(true);
        binding.includeWeeklyMenu.tvLast.setSelected(false);

        commit_report();
    }

    @Override
    public void initListener() {
        binding.vBack.setOnClickListener((v) -> {
            finish();
        });

        binding.includeWeeklyMenu.tvCurrent.setOnClickListener((v) -> {
            doAni(binding.includeWeeklyMenu.v1, 0);
        });

        binding.includeWeeklyMenu.tvLast.setOnClickListener((v) -> {
            doAni(binding.includeWeeklyMenu.v1, 1);
        });

        binding.llytLog.setOnClickListener((v) -> {
            IntentUtils.toBalanceWeeklyLogActivity(this);
        });

        binding.vHelp.setOnClickListener((v) -> {
            IntentUtils.toHTMLActivity(this, null, MAPP.mapp.getConfigBean().getStatic_url_config().getKpi());
        });

    }

    private void doAni(View v, int goSelect) {
        if (goSelect == currentSelect) {
            return;
        }

        switch (currentSelect) {
            case 0:
                if (goSelect == 1) {
                    animator = ObjectAnimator.ofFloat(v, "x", v.getX(), v.getX() + binding.includeWeeklyMenu.v1.getWidth());
                    animator.start();
                }
                break;
            case 1:
                if (goSelect == 0) {
                    animator = ObjectAnimator.ofFloat(v, "x", v.getX(), v.getX() - binding.includeWeeklyMenu.v1.getWidth());
                    animator.start();
                }
                break;
        }
        currentSelect = goSelect;
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (currentSelect == 0) {
                    binding.includeWeeklyMenu.tvCurrent.setSelected(true);
                    binding.includeWeeklyMenu.tvLast.setSelected(false);
                } else {
                    binding.includeWeeklyMenu.tvCurrent.setSelected(false);
                    binding.includeWeeklyMenu.tvLast.setSelected(true);
                }
                commit_report();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }


    private void commit_report() {
        params = SignUtils.getNormalParams();

        params.put(MKey.IS_LAST_WEEK, currentSelect);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().week_kpi(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<KpiWeeklyBean>() {
            @Override
            public void onSuccess(BaseBean<KpiWeeklyBean> baseBean) {
                binding.includeActionStatistics.setKpiWeeklyBeanModel(baseBean.getData().getWeek_api());
                binding.includeOrderStatistics.setKpiWeeklyBeanModel(baseBean.getData().getWeek_api());
                binding.setKpiWeeklyBeanModel(baseBean.getData().getWeek_api());

                WeeklyOrderProgressView weeklyOrderProgressView = new WeeklyOrderProgressView(WeeklyActivity.this, baseBean.getData().getWeek_api().getMeet_order(), baseBean.getData().getWeek_api().getVideo_chat());
                weeklyOrderProgressView.setLayoutParams(new RelativeLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.x200), getResources().getDimensionPixelOffset(R.dimen.y200)));
                binding.includeOrderStatistics.rlyt.addView(weeklyOrderProgressView);


                binding.includeOrderStatistics.tvOrderNum.setText((Integer.parseInt(baseBean.getData().getWeek_api().getMeet_order()) + Integer.parseInt(baseBean.getData().getWeek_api().getVideo_chat())) + "");


                ConfigBean.KpiConfigBean kpiConfigBean = ConfigService.getInstance().getCurrentKPILevelBean(baseBean.getData().getWeek_api().getKpi());


                if (currentSelect == 0) {
//                    binding.includeWeeklyMenu.tvCurrent.setSelected(true);
//                    binding.includeWeeklyMenu.tvLast.setSelected(false);
                    binding.llytIncoming.setVisibility(View.INVISIBLE);
                    binding.tvMoney.setVisibility(View.VISIBLE);
                    binding.tvMoneyTip.setVisibility(View.VISIBLE);
                    binding.ivWeeklyLevel.setVisibility(View.INVISIBLE);

                } else {
//                    binding.includeWeeklyMenu.tvCurrent.setSelected(false);
//                    binding.includeWeeklyMenu.tvLast.setSelected(true);


                    binding.llytIncoming.setVisibility(View.VISIBLE);
                    binding.tvMoney.setVisibility(View.INVISIBLE);
                    binding.tvMoneyTip.setVisibility(View.INVISIBLE);
                    binding.ivWeeklyLevel.setVisibility(View.VISIBLE);
                    if (kpiConfigBean != null) {
                        ImageUtils.showImage(binding.ivWeeklyLevel, kpiConfigBean.getImg());
                    }
                }

            }

            @Override
            public void onError(BaseBean<KpiWeeklyBean> baseBean) {
            }
        });
    }
}
