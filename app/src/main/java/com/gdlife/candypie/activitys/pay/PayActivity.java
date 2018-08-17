package com.gdlife.candypie.activitys.pay;

import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.PayFrom;
import com.gdlife.candypie.common.PayType;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.common.ServiceSelectUserFrom;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityPayBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.OrderService;
import com.gdlife.candypie.serivce.PayService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.pay.CouponsBeanModel;
import com.heboot.bean.pay.ServicePaymentBean;
import com.heboot.bean.pay.UpVipBean;
import com.heboot.bean.pay.UpdatePaymentStatusBean;
import com.heboot.bean.theme.PostThemeBean;
import com.heboot.event.NormalEvent;
import com.heboot.event.PayEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.suke.widget.SwitchButton;
import com.yalantis.dialog.TipCustomDialog;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/12.
 */

public class PayActivity extends BaseActivity<ActivityPayBinding> {

    private PostThemeBean postThemeBean;

    @Inject
    PayService payService;

    @Inject
    OrderService orderService;

    private CouponsBeanModel selectedCouponModel;

    private float totalPayMoney;

    private boolean useBalance = true;

    private PayFrom payFrom;

    private boolean isVideo;

    private TipCustomDialog cancelTipDialog;

    private TipCustomDialog payStatusTipDialog;

    private String orderId;

    private UpVipBean upVipBean;


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setWhiteBack(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_pay));
    }

    @Override
    public void initData() {

        DaggerServiceComponent.builder().build().inject(this);

        payFrom = (PayFrom) getIntent().getExtras().get(MKey.FROM);

        if (payFrom == PayFrom.RECHARGE_GOLD_VIP_ACTION) {
            binding.tvPayTip.setText(getString(R.string.gold_cash_tip));
            binding.clytSafe.setVisibility(View.INVISIBLE);
            initRechargeConfig();
        } else {
            binding.clytSafe.setVisibility(View.VISIBLE);
            binding.tvPayTip.setText(getString(R.string.pay_tip));
            postThemeBean = (PostThemeBean) getIntent().getExtras().get(MKey.POST_THEME_BEAN);

            isVideo = getIntent().getBooleanExtra(MKey.TYPE, false);


            binding.setPostServiceBean(postThemeBean);

            totalPayMoney = Float.parseFloat(postThemeBean.getPayable_amount());

            binding.tvMoney.setText(getString(R.string.pay_money) + postThemeBean.getPayable_amount());

            binding.btnBottom.setText(getString(R.string.tip_confirm_pay) + "   " + getString(R.string.price_symbol) + postThemeBean.getPayable_amount());

            binding.btnBottom.setSelected(true);

            payService.setPayTypesUI(binding.includeType, postThemeBean.getPayment_config());

            binding.includeType.setUsedBalance(postThemeBean.getUsed_balance() == 1 ? true : false);

            binding.includeType.setUsedCoupons(postThemeBean.getUsed_coupons() == 1 ? true : false);

            if (postThemeBean.getUsed_coupons() == 1 && postThemeBean.getCoupons_list() != null && postThemeBean.getCoupons_list().size() > 0) {
                selectedCouponModel = postThemeBean.getCoupons_list().get(0);
            }

            setPrice();
        }


    }

    private void setPrice() {


        //如果不可用现金支付 或者现金支付里就没钱
        if (postThemeBean != null && postThemeBean.getUsed_balance() == 0) {
            binding.includeType.includeBalance.getRoot().setVisibility(View.GONE);
            binding.includeType.lineBalance.setVisibility(View.GONE);
        } else {

            if (useBalance) {
                binding.includeType.includeBalance.sbWine.setChecked(true);

                if (payFrom == PayFrom.RECHARGE_GOLD_VIP_ACTION) {
                    binding.includeType.includeBalance.tvContent.setText(getString(R.string.balance_paymoney_tip) + payService.getBalanceMoney(selectedCouponModel == null ? 0 : payService.covertCouponValue(selectedCouponModel.getValue()), upVipBean == null ? 0 : payService.covertCouponValue(upVipBean.getPayable_amount())));
                } else {
                    binding.includeType.includeBalance.tvContent.setText(getString(R.string.balance_paymoney_tip) + payService.getBalanceMoney(selectedCouponModel == null ? 0 : payService.covertCouponValue(selectedCouponModel.getValue()), postThemeBean == null ? 0 : payService.covertCouponValue(postThemeBean.getPayable_amount())));
                }


            } else {
                binding.includeType.includeBalance.sbWine.setChecked(false);
            }
        }


        //如果选择了优惠券 并且 是可以用优惠券支付 去显示优惠券的UI
        if (selectedCouponModel != null && postThemeBean.getUsed_coupons() == 1) {
//            totalPayMoney = payService.getPayMoney(payService.covertCouponValue(selectedCouponModel.getValue()), postThemeBean.getUsed_balance() == 1 ? 0 : 0, payService.covertCouponValue(postThemeBean.getPayable_amount()));
            binding.includeType.includeCoupon.tvContent.setText(getString(R.string.coupon_paymoney_tip) + selectedCouponModel.getValue());
        } else if (selectedCouponModel == null && postThemeBean != null && postThemeBean.getUsed_coupons() == 1) {
            binding.includeType.includeCoupon.tvContent.setText(getString(R.string.coupons));
        }

//        totalPayMoney = payService.getPayMoney(selectedCouponModel == null ? 0 : payService.covertCouponValue(selectedCouponModel.getValue()), postThemeBean.getUsed_balance() == 1 ? 0 : 0, payService.covertCouponValue(postThemeBean.getPayable_amount()));
        if (payFrom == PayFrom.RECHARGE_GOLD_VIP_ACTION) {
            totalPayMoney = payService.getPayMoney(selectedCouponModel == null ? 0 : payService.covertCouponValue(selectedCouponModel.getValue()), binding.includeType.includeBalance.sbWine.isChecked() ? Float.parseFloat(UserService.getInstance().getUser().getBalance()) : 0, upVipBean == null ? 0 : payService.covertCouponValue(upVipBean.getPayable_amount()));
        } else {
            totalPayMoney = payService.getPayMoney(selectedCouponModel == null ? 0 : payService.covertCouponValue(selectedCouponModel.getValue()), binding.includeType.includeBalance.sbWine.isChecked() ? Float.parseFloat(UserService.getInstance().getUser().getBalance()) : 0, postThemeBean == null ? 0 : payService.covertCouponValue(postThemeBean.getPayable_amount()));
        }


        if (totalPayMoney > 0) {
            binding.includeType.includeAli.cbCheck.setChecked(true);
        }

        binding.btnBottom.setText(getString(R.string.tip_confirm_pay) + "   " + getString(R.string.price_symbol) + totalPayMoney);

    }

    @Override
    public void initListener() {

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            showCancelDialog();

        });

        rxObservable.subscribe(new Observer<Object>() {

            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o instanceof PayEvent.SelectCouponEvent) {
                    PayEvent.SelectCouponEvent selectCouponEvent = (PayEvent.SelectCouponEvent) o;
                    selectedCouponModel = selectCouponEvent.getCouponsBeanModel();
                    setPrice();
                } else if (o.equals(PayEvent.PaySUCEvent)) {

                    RxBus.getInstance().post(NormalEvent.FINISH_NEW_THEME_PAGE);

                    if (payStatusTipDialog != null) {
                        payStatusTipDialog.dismiss();
                    }
                    if (payFrom == PayFrom.SELECT_USER) {
                        if (postThemeBean.getSelectedUserNum() == 1) {
                            IntentUtils.intent2ChatActivity(PayActivity.this, MValue.CHAT_PRIEX + postThemeBean.getSelectedUserId());
                            finish();
                        } else {
                            IntentUtils.toOrderDetailActivity(PayActivity.this, postThemeBean.getUser_service_id(), false);
                            finish();
                        }
                    } else if (payFrom == PayFrom.NEW_SERVICE) {
                        IntentUtils.toThemeUserListActivity(PayActivity.this, postThemeBean.getUser_service_id(), isVideo, ServiceSelectUserFrom.NEW_SERVICE);
                        finish();
                    } else if (payFrom == PayFrom.NEW_SERVICE_ONE_USER) {
                        IntentUtils.toOrderDetailActivity(PayActivity.this, postThemeBean.getUser_service_id(), true);
                        finish();
                    } else if (payFrom == PayFrom.RECHARGE_GOLD_VIP_ACTION) {
                        finish();
                    }
                } else if (o.equals(PayEvent.RechargeGoldVipSUCEvent)) {
                    if (payFrom == PayFrom.RECHARGE_GOLD_VIP_ACTION) {
                        finish();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        binding.includeType.includeCoupon.getRoot().setOnClickListener((v) -> {
            IntentUtils.toCouponsActivity(this, true, postThemeBean, selectedCouponModel);
        });

//        payService.setPayTypesListener(binding.includeType, binding.includeType.includeBalance.sbWine.isChecked());

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


        binding.includeType.includeBalance.sbWine.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (view.isPressed()) {
                    return;
                }
                useBalance = isChecked;
                setPrice();
            }
        });

        binding.btnBottom.setOnClickListener((v) -> {
            if (payFrom == PayFrom.RECHARGE_GOLD_VIP_ACTION) {
                doRecharge();
            } else {
                service_payment();
            }

        });
    }

    private void service_payment() {

        if (totalPayMoney > 0) {
            if (!binding.includeType.includeAli.cbCheck.isChecked() && !binding.includeType.includeWx.cbCheck.isChecked()) {
                tipDialog = DialogUtils.getFailDialog(this, getString(R.string.choose_pay_type), true);
                tipDialog.show();
                return;
            }
        }

        params = SignUtils.getNormalParams();
        params.put(MKey.USER_SERVICE_ID, postThemeBean.getUser_service_id());
        params.put(MKey.COUPONS_ID, selectedCouponModel != null ? selectedCouponModel.getId() : "");
        params.put(MKey.USE_BALANCE, binding.includeType.includeBalance.sbWine.isChecked() ? 1 : 0);
        if (binding.includeType.includeAli.cbCheck.isChecked()) {
            params.put(MKey.PAYMENT_TYPE, PayType.ALIPAY.toString().toLowerCase());
        } else if (binding.includeType.includeWx.cbCheck.isChecked()) {
            params.put(MKey.PAYMENT_TYPE, PayType.WEIXIN.toString().toLowerCase());
        }
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().service_payment(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<ServicePaymentBean>() {
            @Override
            public void onSuccess(BaseBean<ServicePaymentBean> baseBean) {
                ServicePaymentBean servicePaymentBean = baseBean.getData();
                if (servicePaymentBean != null && servicePaymentBean.getPayment_params() != null && !StringUtils.isEmpty(servicePaymentBean.getPayment_params().getOrder_id())) {
                    orderId = baseBean.getData().getPayment_params().getOrder_id();
                }
                if (servicePaymentBean == null && baseBean.getError_code() == 0) {
                    RxBus.getInstance().post(PayEvent.PaySUCEvent);
                    return;
                }

                showPayStatusDialog();

                if (binding.includeType.includeAli.cbCheck.isChecked()) {
                    payService.doPay(PayType.ALIPAY, PayActivity.this, servicePaymentBean.getPayment_params(), new Consumer<Map<String, String>>() {
                        @Override
                        public void accept(Map<String, String> stringStringMap) throws Exception {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    payService.checkAliPayResult(stringStringMap, null);
                                }
                            });

                        }
                    });
                } else if (binding.includeType.includeWx.cbCheck.isChecked()) {
                    payService.doPay(PayType.WEIXIN, PayActivity.this, servicePaymentBean.getPayment_params(), new Consumer<Map<String, String>>() {
                        @Override
                        public void accept(Map<String, String> stringStringMap) throws Exception {
                        }
                    });
                }
            }

            @Override
            public void onError(BaseBean<ServicePaymentBean> baseBean) {

            }
        });

    }


    private void doRecharge() {
        params = SignUtils.getNormalParams();
//        params.put(MKey.PRODUCT_ID, rechargeConfigBean.getProduct_id());
        params.put(MKey.TYPE, "gold_vip");
        params.put(MKey.USE_BALANCE, binding.includeType.includeBalance.sbWine.isChecked() ? 1 : 0);
        if (binding.includeType.includeAli.cbCheck.isChecked()) {
            params.put(MKey.PAYMENT_TYPE, PayType.ALIPAY.toString().toLowerCase());
        } else if (binding.includeType.includeWx.cbCheck.isChecked()) {
            params.put(MKey.PAYMENT_TYPE, PayType.WEIXIN.toString().toLowerCase());
        }
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);


        HttpClient.Builder.getGuodongServer().recharge_payment(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<ServicePaymentBean>() {
            @Override
            public void onSuccess(BaseBean<ServicePaymentBean> baseBean) {

                ServicePaymentBean servicePaymentBean = baseBean.getData();

                if (servicePaymentBean != null && servicePaymentBean.getPayment_params() != null && !StringUtils.isEmpty(servicePaymentBean.getPayment_params().getOrder_id())) {
                    orderId = baseBean.getData().getPayment_params().getOrder_id();
                }
                if (servicePaymentBean == null && baseBean.getError_code() == 0) {
                    RxBus.getInstance().post(PayEvent.RechargeGoldVipSUCEvent);
                    return;
                }

                showPayStatusDialog();

                //
                if (binding.includeType.includeAli.cbCheck.isChecked()) {
                    payService.doPay(PayType.ALIPAY, PayActivity.this, baseBean.getData().getPayment_params(), new Consumer<Map<String, String>>() {
                        @Override
                        public void accept(Map<String, String> stringStringMap) throws Exception {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    payService.checkAliPayResult(stringStringMap, RechargeType.GOLD_VIP);
                                }
                            });
                        }
                    });
                } else if (binding.includeType.includeWx.cbCheck.isChecked()) {
                    MValue.currentRechargeType = RechargeType.GOLD_VIP;
                    payService.doPay(PayType.WEIXIN, PayActivity.this, baseBean.getData().getPayment_params(), new Consumer<Map<String, String>>() {
                        @Override
                        public void accept(Map<String, String> stringStringMap) throws Exception {
                            LogUtil.e(TAG, JSON.toJSONString(stringStringMap));
                        }
                    });
                }
            }

            @Override
            public void onError(BaseBean<ServicePaymentBean> baseBean) {

            }
        });

    }


    private void initRechargeConfig() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().vip_config(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<UpVipBean>() {
            @Override
            public void onSuccess(BaseBean<UpVipBean> baseBean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        upVipBean = baseBean.getData();
                        totalPayMoney = Float.parseFloat(baseBean.getData().getPayable_amount());

                        binding.tvMoney.setText(getString(R.string.pay_money) + baseBean.getData().getPayable_amount());

                        binding.btnBottom.setText(getString(R.string.tip_confirm_pay) + "   " + getString(R.string.price_symbol) + baseBean.getData().getPayable_amount());

                        binding.btnBottom.setSelected(true);

                        payService.setPayTypesUI(binding.includeType, baseBean.getData().getPayment_config());

                        binding.includeType.setUsedBalance(baseBean.getData().getUsed_balance() == 1 ? true : false);

                        binding.includeType.setUsedCoupons(baseBean.getData().getUsed_coupons() == 1 ? true : false);

                        setPrice();
                    }
                });
            }

            @Override
            public void onError(BaseBean<UpVipBean> baseBean) {

            }
        });
    }


    private void showCancelDialog() {
        cancelTipDialog = new TipCustomDialog.Builder(this, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if (integer == 1) {
                    if (payFrom == PayFrom.RECHARGE_GOLD_VIP_ACTION) {
                        if (StringUtils.isEmpty(orderId)) {
                            finish();
                        } else {
                            cancel_payment();
                        }

                    } else {
                        orderService.cancelOrder(postThemeBean.getUser_service_id());
                    }

                    finish();
                }
            }
        }, getString(R.string.pay_back_tip), getString(R.string.dengyideng), getString(R.string.abandon)).create();
        cancelTipDialog.show();
    }

    private void showPayStatusDialog() {
        payStatusTipDialog = new TipCustomDialog.Builder(this, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                if (integer == 1) {
                    if (payFrom == PayFrom.RECHARGE_GOLD_VIP_ACTION) {
                        update_recharge_status();
                    } else {
                        update_payment_status();
                    }

                }
                return;
            }
        }, getString(R.string.pay_status_tip), getString(R.string.pay_status_nopay), getString(R.string.pay_status_suc)).create();
        payStatusTipDialog.show();
    }


    private void cancel_payment() {
        params = SignUtils.getNormalParams();
        params.put(MKey.ORDER_ID, orderId);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().cancel_payment(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {

            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {

            }
        });
    }


    private void update_payment_status() {
        params = SignUtils.getNormalParams();
        params.put(MKey.ORDER_ID, orderId);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().update_payment_status(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<UpdatePaymentStatusBean>() {
            @Override
            public void onSuccess(BaseBean<UpdatePaymentStatusBean> baseBean) {
                if (baseBean.getData() != null && baseBean.getData().getStatus() == 1) {
                    RxBus.getInstance().post(PayEvent.PaySUCEvent);
                }
            }

            @Override
            public void onError(BaseBean<UpdatePaymentStatusBean> baseBean) {

            }
        });
    }

    private void update_recharge_status() {
        params = SignUtils.getNormalParams();
        params.put(MKey.ORDER_ID, orderId);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().update_recharge_payment_status(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<UpdatePaymentStatusBean>() {
            @Override
            public void onSuccess(BaseBean<UpdatePaymentStatusBean> baseBean) {
                if (baseBean.getData() != null && baseBean.getData().getStatus() == 1) {
                    RxBus.getInstance().post(PayEvent.RechargeSUCEvent);
                }
            }

            @Override
            public void onError(BaseBean<UpdatePaymentStatusBean> baseBean) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showCancelDialog();
            return false;
        }
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }
}
