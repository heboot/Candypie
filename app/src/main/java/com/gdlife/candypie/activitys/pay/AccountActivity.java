package com.gdlife.candypie.activitys.pay;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.account.AccountCoinAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.PayType;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityAccountBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.PayService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.NumberUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.widget.dialog.account.ChoosePayTypeDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.pay.RechargeConfigBean;
import com.heboot.bean.pay.ServicePaymentBean;
import com.heboot.event.NormalEvent;
import com.heboot.event.PayEvent;
import com.heboot.event.UserEvent;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/13.
 */

public class AccountActivity extends BaseActivity<ActivityAccountBinding> {


    @Inject
    UIService uiService;

    @Inject
    PayService payService;

    private AccountCoinAdapter accountCoinAdapter;

    private ChoosePayTypeDialog choosePayTypeDialog;

    private Consumer<String> consumer;

    private RechargeConfigBean.ConfigBean rechargeConfigBean;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
//        MStatusBarUtils.setActivityNOLightMode(this);
        binding.includeToolbar.setShowRight(true);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setWhiteBack(true);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_account));
        binding.includeToolbar.tvRight.setText(getString(R.string.detail));
        binding.includeToolbar.tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        DaggerServiceComponent.builder().build().inject(this);

        uiService.setToolbarRightTextdStyle(binding.includeToolbar.tvRight, false, ContextCompat.getColor(this, R.color.white));

        checkUpLevel(null);

    }

    @Override
    public void initData() {

        initRechargeCoinConfig();


        binding.tvCoinBalance.setText(NumberUtils.qianweifengecoin(Double.parseDouble(UserService.getInstance().getUser().getCoin())));

        binding.tvMoneyBalance.setText("¥" + NumberUtils.qianweifenge(Double.parseDouble(UserService.getInstance().getUser().getBalance())));

        binding.rvList.setLayoutManager(new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });

    }

    @Override
    public void initListener() {

        consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                doRecharge(s, rechargeConfigBean);
            }

        };

        binding.vCoupon.setOnClickListener((v) -> {
            IntentUtils.toCouponsActivity(this, false, null, null);
        });


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
                    binding.tvMoneyBalance.setText(getString(R.string.price_symbol) + (NumberUtils.qianweifenge(Double.parseDouble(UserService.getInstance().getUser().getBalance()))));
                    binding.tvCoinBalance.setText((NumberUtils.qianweifengecoin(Double.parseDouble(UserService.getInstance().getUser().getCoin()))));
                } else if (o.equals(PayEvent.RechargeGoldVipSUCEvent)) {
                    DialogUtils.showUpGoldVipDialog(AccountActivity.this);
                    checkUpLevel("from");
                } else if (o.equals(PayEvent.RechargeSUCEvent)) {
                    tipDialog = DialogUtils.getSuclDialog(MAPP.mapp.getCurrentActivity(), "充值成功", true);
                    tipDialog.show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        binding.qrbCash.setOnClickListener((v) -> {

            /**
             * 取消提现身份认证
             */
            IntentUtils.toCashIndexActivity(this);
        });

        binding.qrbRecharge.setOnClickListener((v) -> {
            IntentUtils.toRechargeActivity(this, RechargeType.RECHARGE);
        });

        binding.tvCoupon.setOnClickListener((v) -> {
            IntentUtils.toCouponsActivity(this, false, null, null);
        });

        binding.tvCouponNum.setOnClickListener((v) -> {
            IntentUtils.toCouponsActivity(this, false, null, null);
        });


    }


    private void initRechargeCoinConfig() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);


        HttpClient.Builder.getGuodongServer().recharge_coin_config(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<RechargeConfigBean>() {
            @Override
            public void onSuccess(BaseBean<RechargeConfigBean> baseBean) {
                if (baseBean.getData().getConfig() != null && baseBean.getData().getConfig().size() > 0) {
                    binding.tvSubTitle.setText(baseBean.getData().getTitle());
                    accountCoinAdapter = new AccountCoinAdapter(R.layout.item_account_coin, baseBean.getData().getConfig());
                    accountCoinAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            rechargeConfigBean = (RechargeConfigBean.ConfigBean) adapter.getData().get(position);
                            choosePayTypeDialog = new ChoosePayTypeDialog(consumer);
                            choosePayTypeDialog.show(getSupportFragmentManager(), "");
                        }
                    });
                    binding.rvList.setAdapter(accountCoinAdapter);
                }
            }

            @Override
            public void onError(BaseBean<RechargeConfigBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(AccountActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }


    private void checkUpLevel(String from) {

        if (UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {

            if (MAPP.mapp.getConfigBean().getFunction_module_switch() != null && MAPP.mapp.getConfigBean().getFunction_module_switch().getServicer_rechage() == 1) {
                binding.qrbRecharge.setVisibility(View.VISIBLE);
            } else {
                binding.qrbRecharge.setVisibility(View.GONE);
            }

        }

        binding.tvCouponNum.setText("（" + UserService.getInstance().getUser().getCoupons_nums() + getString(R.string.unit_coupon) + ")");

//        if (!StringUtils.isEmpty(from)) {
//            UserService.getInstance().getUser().setVip_level(2);
//        }


    }


    private void doRecharge(String payType, RechargeConfigBean.ConfigBean rechargeConfigBean) {
        Map params = SignUtils.getNormalParams();
        params.put(MKey.PRODUCT_ID, rechargeConfigBean.getProduct_id());
        params.put(MKey.TYPE, RechargeType.COIN.toString().toLowerCase());
//        params.put(MKey.USE_BALANCE, binding.includeType.includeBalance.sbWine.isChecked() ? 1 : 0);
        params.put(MKey.PAYMENT_TYPE, payType);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);


        HttpClient.Builder.getGuodongServer().recharge_payment(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<ServicePaymentBean>() {

            @Override
            public void onSuccess(BaseBean<ServicePaymentBean> baseBean) {
                if (payType.equals(PayType.ALIPAY.toString().toLowerCase())) {
                    payService.doPay(PayType.ALIPAY, AccountActivity.this, baseBean.getData().getPayment_params(), new Consumer<Map<String, String>>() {
                        @Override
                        public void accept(Map<String, String> stringStringMap) throws Exception {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    payService.checkAliPayResult(stringStringMap, RechargeType.COIN);
                                }
                            });
                        }
                    });
                } else if (payType.equals(PayType.WEIXIN.toString().toLowerCase())) {
                    MValue.currentRechargeType = RechargeType.RECHARGE;
                    payService.doPay(PayType.WEIXIN, AccountActivity.this, baseBean.getData().getPayment_params(), new Consumer<Map<String, String>>() {
                        @Override
                        public void accept(Map<String, String> stringStringMap) throws Exception {
                        }
                    });
                }
            }

            @Override
            public void onError(BaseBean<ServicePaymentBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }

                tipDialog = DialogUtils.getFailDialog(AccountActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }
}
