package com.gdlife.candypie.activitys.pay;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.account.AccountCoinAdapter;
import com.gdlife.candypie.adapter.recharge.RechargeCoinAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityAccountBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.PayService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.pay.RechargeConfigBean;
import com.heboot.event.NormalEvent;
import com.heboot.event.PayEvent;
import com.heboot.event.UserEvent;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

        initRechargeCoinConfig();

        binding.tvCoinBalance.setText(UserService.getInstance().getUser().getCoin());

        binding.tvMoneyBalance.setText("¥" + UserService.getInstance().getUser().getBalance());

        binding.rvList.setLayoutManager(new GridLayoutManager(this, 2));

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
//                    binding.includeBalance.tvPrice.setText(getString(R.string.price_symbol) + UserService.getInstance().getUser().getBalance());
//
//                    binding.includeCoin.tvPrice.setText(UserService.getInstance().getUser().getCoin());
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


        binding.qrbCash.setOnClickListener((v) -> {

            /**
             * 取消提现身份认证
             */
            IntentUtils.toCashActivity(this);
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
                    accountCoinAdapter = new AccountCoinAdapter(R.layout.item_account_coin, baseBean.getData().getConfig());
                    accountCoinAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

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

        binding.tvCouponNum.setText(getString(R.string.coupons) + "  （" + UserService.getInstance().getUser().getCoupons_nums() + getString(R.string.unit_coupon) + ")");

//        if (!StringUtils.isEmpty(from)) {
//            UserService.getInstance().getUser().setVip_level(2);
//        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }
}
