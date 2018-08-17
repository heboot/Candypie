package com.gdlife.candypie.activitys.pay;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityAccountBinding;
import com.gdlife.candypie.serivce.PayService;
import com.gdlife.candypie.serivce.ServiceLevelService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.VipService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.pay.ServiceLevelBean;
import com.heboot.bean.pay.VipLevelBean;
import com.heboot.dialog.TipCustomOneDialog;
import com.heboot.event.NormalEvent;
import com.heboot.event.PayEvent;
import com.heboot.event.UserEvent;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yalantis.dialog.TipCustomDialog;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by heboot on 2018/3/13.
 */

public class AccountActivity extends BaseActivity<ActivityAccountBinding> {

    private boolean playing = false;

    private int flag = 0;

    @Inject
    UIService uiService;

    @Inject
    PayService payService;

    private VipLevelBean vipLevelBean;

    private ServiceLevelBean serviceLevelBean;

    private TipCustomOneDialog tipCustomOneDialog;

    private TipCustomDialog tipCustomDialog;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setShowRight(true);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_account));
        binding.includeToolbar.tvRight.setText(getString(R.string.detail));

        DaggerServiceComponent.builder().build().inject(this);

        uiService.setToolbarRightTextdStyle(binding.includeToolbar.tvRight, false, ContextCompat.getColor(this, R.color.color_343445));

        checkUpLevel(null);

    }

    @Override
    public void initData() {

        binding.includeCoin.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LogUtil.e(TAG, binding.includeBalance.getRoot().getRight() + "");
                binding.includeBalance.getRoot().setX(binding.includeCoin.getRoot().getRight() - 40);
                binding.includeCoin.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        binding.includeBalance.tvPrice.setText(getString(R.string.price_symbol) + UserService.getInstance().getUser().getBalance());

        binding.includeCoin.tvPrice.setText(UserService.getInstance().getUser().getCoin());

    }

    @Override
    public void initListener() {


        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        binding.includeToolbar.tvRight.setOnClickListener((v) -> {
            IntentUtils.toBalanceLogActivity(this);
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
                } else if (o.equals(UserEvent.UPDATE_PROFILE)) {
                    binding.includeBalance.tvPrice.setText(getString(R.string.price_symbol) + UserService.getInstance().getUser().getBalance());

                    binding.includeCoin.tvPrice.setText(UserService.getInstance().getUser().getCoin());
                } else if (o.equals(PayEvent.RechargeGoldVipSUCEvent)) {
                    DialogUtils.showUpGoldVipDialog(AccountActivity.this);
                    checkUpLevel("from");
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        binding.rlytBalance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!playing) {
                    if (flag == 0) {
                        playing = true;
                        binding.includeCoin.getRoot().animate().scaleX(0.8f).scaleY(0.8f).translationX(0 - binding.includeCoin.getRoot().getWidth() + 40).setDuration(400).start();
                        binding.includeBalance.getRoot().animate().scaleX(1f).scaleY(1f).x(QMUIDisplayHelper.dpToPx(30)).setDuration(400).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                playing = false;
                            }
                        }).start();
                        flag = 1;

                    } else {
                        playing = true;
                        binding.includeBalance.getRoot().animate().scaleX(0.8f).scaleY(0.8f).x(binding.includeCoin.getRoot().getRight() - 40).setDuration(400).start();
                        binding.includeCoin.getRoot().animate().scaleX(1f).scaleY(1f).x(QMUIDisplayHelper.dpToPx(30)).setDuration(400).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                playing = false;
                            }
                        }).start();
                        flag = 0;
                    }
                }
                return false;
            }
        });

        binding.includeBalance.tvCash.setOnClickListener((v) -> {

            /**
             * 取消提现身份认证
             */
//            if (UserService.getInstance().getUser().getUser_auth_status() != null && UserService.getInstance().getUser().getUser_auth_status() == MValue.AUTH_STATUS_SUC) {
            IntentUtils.toCashActivity(this);
//            } else if (UserService.getInstance().getUser().getUser_auth_status() != null && UserService.getInstance().getUser().getUser_auth_status() == MValue.AUTH_STATUS_ING) {
//                if (tipCustomOneDialog == null) {
//                    tipCustomOneDialog = new TipCustomOneDialog.Builder(AccountActivity.this, getString(R.string.cash_authid_ing_tip), getString(R.string.iknow)).create();
//                }
//                tipCustomOneDialog.show();
//            } else {
//                if (tipCustomDialog == null) {
//                    tipCustomDialog = new TipCustomDialog.Builder(AccountActivity.this, new Consumer<Integer>() {
//                        @Override
//                        public void accept(Integer integer) throws Exception {
//                            if (integer == 1) {
//                                IntentUtils.toAuthIDActivity(AccountActivity.this, MValue.AUTH_ID_FROM.USER_AUTH);
//                            }
//                        }
//                    }, getString(R.string.cash_authid_tip), getString(R.string.iknow), getString(R.string.goto_auth)).create();
//                }
//                tipCustomDialog.show();
//            }


        });

        binding.includeBalance.tvRecharge.setOnClickListener((v) -> {
            IntentUtils.toRechargeActivity(this, RechargeType.RECHARGE);
        });

        binding.includeCoupon.getRoot().setOnClickListener((v) -> {
            IntentUtils.toCouponsActivity(this, false, null, null);
        });

        binding.includeLevel.getRoot().setOnClickListener((v) -> {
            IntentUtils.toMyLevelActivity(this);
        });

//
        binding.includeCoin.tvRechargeCoin.setOnClickListener((v) -> {
            IntentUtils.toRechargeActivity(this, RechargeType.COIN);
        });

        binding.ivUpVip.setOnClickListener((v) -> {
            VipLevelBean vipLevelBean = VipService.getNextVipLevel(UserService.getInstance().getUser().getVip_level());
            if (vipLevelBean != null) {
                IntentUtils.toHTMLActivity(this, null, vipLevelBean.getDetail_url());
            }
        });

        binding.includeGoldRights.getRoot().setOnClickListener((v) -> {
            VipLevelBean vipLevelBean = VipService.getGoldVipLevel();
            if (vipLevelBean != null) {
                IntentUtils.toHTMLActivity(this, null, vipLevelBean.getRights_url());
            }
        });


    }


    private void checkUpLevel(String from) {


        vipLevelBean = VipService.getCurrentVipLevel(UserService.getInstance().getUser().getVip_level());


        binding.includeBalance.getRoot().setScaleX(0.8f);
        binding.includeBalance.getRoot().setScaleY(0.8f);


        binding.includeBalance.setBg(vipLevelBean.getBg_img());
        binding.includeBalance.setFColor(Color.parseColor(vipLevelBean.getFont_color()));
        binding.includeBalance.line1.setAlpha(0.5f);
        binding.includeBalance.line2.setAlpha(0.5f);
        binding.includeBalance.tvName.setText(getString(R.string.account_balance));
        binding.includeBalance.tvCash.setText(getString(R.string.account_cash));
        if (UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {

            if (MAPP.mapp.getConfigBean().getFunction_module_switch() != null && MAPP.mapp.getConfigBean().getFunction_module_switch().getServicer_rechage() == 1) {
                binding.includeBalance.tvRecharge.setVisibility(View.VISIBLE);
                binding.includeBalance.line2.setVisibility(View.VISIBLE);
            } else {
                binding.includeBalance.tvRecharge.setVisibility(View.GONE);
                binding.includeBalance.line2.setVisibility(View.GONE);
            }

        }
        binding.includeBalance.tvRecharge.setText(getString(R.string.account_recharge));
        binding.includeGoldRights.tvTitle.setText(getString(R.string.gold_card_rights));
        binding.includeGoldRights.ivIcon.setBackgroundResource(R.drawable.icon_vip_gold);

//        binding.includeCoin.tv.setText(getString(R.string.account_recharge));

        binding.includeCoupon.ivIcon.setBackgroundResource(R.drawable.icon_account_coupons);
        binding.includeCoupon.tvTitle.setText(getString(R.string.coupons) + "  （" + UserService.getInstance().getUser().getCoupons_nums() + getString(R.string.unit_coupon) + ")");

        if (!StringUtils.isEmpty(from)) {
            UserService.getInstance().getUser().setVip_level(2);
        }

        if (UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {
            if (UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {

                if (MAPP.mapp.getConfigBean().getFunction_module_switch() != null && MAPP.mapp.getConfigBean().getFunction_module_switch().getServicer_kpi() == 1) {
                    serviceLevelBean = ServiceLevelService.getCurrentServiceLevel(UserService.getInstance().getUser().getLevel());
                    binding.includeLevel.getRoot().setVisibility(View.VISIBLE);
                    binding.includeLevel.tvTitle.setText(getString(R.string.level));
                    binding.includeLevel.ivIcon.setBackgroundResource(R.drawable.icon_level);
                    binding.includeLevel.tvTitle.setText(getString(R.string.level) + "\t" + serviceLevelBean.getTitle());
                } else {
                    binding.lineLevel.setVisibility(View.GONE);
                    binding.includeLevel.getRoot().setVisibility(View.GONE);
                }
            }

        } else {
            binding.lineLevel.setVisibility(View.GONE);
            binding.includeLevel.getRoot().setVisibility(View.GONE);
        }

        if (UserService.getInstance().getUser().getVip_level().intValue() == 2 || !StringUtils.isEmpty(from)) {
            binding.ivUpVip.setVisibility(View.GONE);
            binding.includeGoldRights.getRoot().setVisibility(View.VISIBLE);
            binding.lineGoldRights.setVisibility(View.VISIBLE);
        } else {
            binding.ivUpVip.setVisibility(View.VISIBLE);
            binding.includeGoldRights.getRoot().setVisibility(View.GONE);
            binding.lineGoldRights.setVisibility(View.GONE);
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }
}
