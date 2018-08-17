package com.gdlife.candypie.activitys.pay;

import android.support.v7.widget.GridLayoutManager;
import android.widget.CompoundButton;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.recharge.RechargeAdapter;
import com.gdlife.candypie.adapter.recharge.RechargeCoinAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.PayType;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityRechargeBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.PayService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.pay.RechargeConfigBean;
import com.heboot.bean.pay.ServicePaymentBean;
import com.heboot.event.NormalEvent;
import com.heboot.event.PayEvent;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.lang.ref.WeakReference;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/14.
 */

public class RechargeActivity extends BaseActivity<ActivityRechargeBinding> {

    private RechargeType rechargeType;

    private RechargeCoinAdapter rechargeCoinAdapter;

    private RechargeAdapter rechargeAdapter;

    private RechargeConfigBean.ConfigBean rechargeConfigBean;

    private String payType;

    @Inject
    PayService payService;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setShowRight(false);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.account_recharge));

        binding.rvList.setLayoutManager(new GridLayoutManager(this, 3));

    }

    @Override
    public void initData() {

        DaggerServiceComponent.builder().build().inject(this);

        rechargeType = (RechargeType) getIntent().getExtras().get(MKey.TYPE);

        if (rechargeType == RechargeType.COIN) {
            binding.tvBalanceType.setText(getString(R.string.coin_balance));
            binding.tvBalance.setText(UserService.getInstance().getUser().getCoin());
            initRechargeCoinConfig();
        } else {
            binding.tvBalanceType.setText(getString(R.string.account_balance));
            binding.tvBalance.setText(UserService.getInstance().getUser().getBalance());
            initRechargeConfig();
        }


    }


    private void doRecharge() {
        params = SignUtils.getNormalParams();
        params.put(MKey.PRODUCT_ID, rechargeConfigBean.getProduct_id());
        params.put(MKey.TYPE, rechargeType.toString().toLowerCase());
        params.put(MKey.USE_BALANCE, binding.includeType.includeBalance.sbWine.isChecked() ? 1 : 0);
        params.put(MKey.PAYMENT_TYPE, payType);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().recharge_payment(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<ServicePaymentBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(ServicePaymentBean servicePaymentBean) {
                if (binding.includeType.includeAli.cbCheck.isChecked()) {
                    payService.doPay(PayType.ALIPAY, RechargeActivity.this, servicePaymentBean.getPayment_params(), new Consumer<Map<String, String>>() {
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
                } else if (binding.includeType.includeWx.cbCheck.isChecked()) {
                    MValue.currentRechargeType = RechargeType.RECHARGE;
                    payService.doPay(PayType.WEIXIN, RechargeActivity.this, servicePaymentBean.getPayment_params(), new Consumer<Map<String, String>>() {
                        @Override
                        public void accept(Map<String, String> stringStringMap) throws Exception {
                            LogUtil.e(TAG, JSON.toJSONString(stringStringMap));
                        }
                    });
                }

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onError(BaseBean<ServicePaymentBean> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }

                tipDialog = DialogUtils.getFailDialog(RechargeActivity.this, basebean.getMessage(), true);
                tipDialog.show();
            }
        }));
    }

    private void initRechargeConfig() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);

        HttpClient.Builder.getGuodongServer().recharge_config(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<RechargeConfigBean>() {
            @Override
            public void onSuccess(BaseBean<RechargeConfigBean> baseBean) {
                if (baseBean.getData().getConfig() != null && baseBean.getData().getConfig().size() > 0) {
                    setUI(baseBean.getData());
                    rechargeAdapter = new RechargeAdapter(baseBean.getData().getConfig(), new WeakReference<RechargeActivity>(RechargeActivity.this));
                    binding.rvList.setAdapter(rechargeAdapter);
                }
            }

            @Override
            public void onError(BaseBean<RechargeConfigBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(RechargeActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
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
                    setUI(baseBean.getData());
                    rechargeCoinAdapter = new RechargeCoinAdapter(baseBean.getData().getConfig(), new WeakReference<RechargeActivity>(RechargeActivity.this));
                    binding.rvList.setAdapter(rechargeCoinAdapter);
                }
            }

            @Override
            public void onError(BaseBean<RechargeConfigBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(RechargeActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }

    private void setUI(RechargeConfigBean o) {
        payService.setPayTypesUI(binding.includeType, o.getPayment_config());
        binding.includeType.setUsedBalance(o.getUsed_balance() == 1 ? true : false);
        binding.includeType.setUsedCoupons(o.getUsed_coupons() == 1 ? true : false);
    }

    @Override
    public void initListener() {

        binding.includeType.includeAli.cbCheck.setChecked(true);

        rxObservable.subscribe(new Observer<Object>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                if (o.equals(PayEvent.RechargeSUCEvent)) {
                    finish();
                } else if (o.equals(NormalEvent.FINISH_PAGE_BY_SELECT_USER)) {
                    finish();
                } else if (o.equals(NormalEvent.FINISH_REC_PAGE)) {
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        binding.includeType.includeAli.getRoot().setOnClickListener((v) -> {
            if (binding.includeType.includeWx.cbCheck.isChecked()) {
                binding.includeType.includeWx.cbCheck.setChecked(false);
            }
            binding.includeType.includeAli.cbCheck.setChecked(true);
        });
        binding.includeType.includeWx.getRoot().setOnClickListener((v) -> {
            if (binding.includeType.includeAli.cbCheck.isChecked()) {
                binding.includeType.includeAli.cbCheck.setChecked(false);
            }
            binding.includeType.includeWx.cbCheck.setChecked(true);
        });
        binding.includeType.includeAli.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (binding.includeType.includeWx.cbCheck.isChecked()) {
                        binding.includeType.includeWx.cbCheck.setChecked(false);
                    }
                    binding.includeType.includeAli.cbCheck.setChecked(true);
                }
//                else {
//                    if (binding.includeType.includeWx.cbCheck.isChecked()) {
//                        binding.includeType.includeWx.cbCheck.setChecked(false);
//                    }
//                    binding.includeType.includeAli.cbCheck.setChecked(true);
//                }

            }
        });
        binding.includeType.includeWx.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (binding.includeType.includeAli.cbCheck.isChecked()) {
                        binding.includeType.includeAli.cbCheck.setChecked(false);
                    }
                    binding.includeType.includeWx.cbCheck.setChecked(true);
                }

//                else {
//                    if (binding.includeType.includeAli.cbCheck.isChecked()) {
//                        binding.includeType.includeAli.cbCheck.setChecked(false);
//                    }
//                    binding.includeType.includeWx.cbCheck.setChecked(true);
//                }

            }
        });
        binding.btnBottom.setOnClickListener((v) -> {
            doPay();
        });

    }

    public void setBottom() {
        if (rechargeType == RechargeType.COIN) {
            if (rechargeCoinAdapter != null && rechargeCoinAdapter.getSelectBean() != null) {
                rechargeConfigBean = rechargeCoinAdapter.getSelectBean();
//                if (binding.includeType.includeWx.cbCheck.isSelected() || binding.includeType.includeAli.cbCheck.isSelected()) {
                binding.btnBottom.setText(getString(R.string.confirm_recharge) + getString(R.string.price_symbol) + rechargeCoinAdapter.getSelectBean().getAmount());
                binding.btnBottom.setSelected(true);
//                }
            }
        } else if (rechargeType == RechargeType.RECHARGE) {
            if (rechargeAdapter != null && rechargeAdapter.getSelectBean() != null) {
                rechargeConfigBean = rechargeAdapter.getSelectBean();
//                if (binding.includeType.includeWx.cbCheck.isSelected() || binding.includeType.includeAli.cbCheck.isSelected()) {
                binding.btnBottom.setText(getString(R.string.confirm_recharge) + getString(R.string.price_symbol) + rechargeAdapter.getSelectBean().getAmount());
                binding.btnBottom.setSelected(true);
//                }
            }
        }
    }

    private void doPay() {

        if (!binding.includeType.includeAli.cbCheck.isChecked() && !binding.includeType.includeWx.cbCheck.isChecked()) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.choose_pay_type), true);
            tipDialog.show();
            return;
        }

        if (binding.includeType.includeAli.cbCheck.isChecked()) {
            payType = PayType.ALIPAY.toString().toLowerCase();
        } else if (binding.includeType.includeWx.cbCheck.isChecked()) {
            payType = PayType.WEIXIN.toString().toLowerCase();
        }

        if (rechargeType == RechargeType.COIN) {
            if (rechargeCoinAdapter != null && rechargeCoinAdapter.getSelectBean() != null) {
                doRecharge();
            }
        } else if (rechargeType == RechargeType.RECHARGE) {
            if (rechargeAdapter != null && rechargeAdapter.getSelectBean() != null) {
                doRecharge();
            }
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }
}
