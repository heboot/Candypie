package com.gdlife.candypie.activitys.pay;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.account.RechargeAdapter;
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
import com.gdlife.candypie.widget.dialog.account.ChoosePayTypeDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.pay.RechargeConfigBean;
import com.heboot.bean.pay.ServicePaymentBean;
import com.heboot.event.NormalEvent;
import com.heboot.event.PayEvent;
import com.heboot.utils.LogUtil;
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
 * Created by heboot on 2018/3/14.
 */

public class RechargeActivity extends BaseActivity<ActivityRechargeBinding> {

    private RechargeType rechargeType;


    private RechargeAdapter rechargeAdapter;


    private ChoosePayTypeDialog choosePayTypeDialog;

    private Consumer<String> consumer;

    private RechargeConfigBean.ConfigBean rechargeConfigBean;

    @Inject
    PayService payService;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setShowRight(false);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.account_recharge));

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void initData() {

        DaggerServiceComponent.builder().build().inject(this);
        binding.tvBalance.setText(UserService.getInstance().getUser().getBalance());
        rechargeType = RechargeType.RECHARGE;
        initRechargeConfig();


    }


    private void doRecharge(String payType, RechargeConfigBean.ConfigBean rechargeConfigBean) {
        params = SignUtils.getNormalParams();
        params.put(MKey.PRODUCT_ID, rechargeConfigBean.getProduct_id());
        params.put(MKey.TYPE, rechargeType.toString().toLowerCase());
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
                if (payType.equals(PayType.ALIPAY.toString().toLowerCase())) {
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
                } else if (payType.equals(PayType.WEIXIN.toString().toLowerCase())) {
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
                    rechargeAdapter = new RechargeAdapter(R.layout.item_recharge_money, baseBean.getData().getConfig());
                    rechargeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            rechargeConfigBean = (RechargeConfigBean.ConfigBean) adapter.getData().get(position);
                            choosePayTypeDialog = new ChoosePayTypeDialog(consumer);
                            choosePayTypeDialog.show(getSupportFragmentManager(), "");
                        }
                    });
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


    @Override
    public void initListener() {

        consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                doRecharge(s, rechargeConfigBean);
            }
        };

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


    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }
}
