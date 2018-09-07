package com.gdlife.candypie.activitys.pay;

import android.support.v4.content.ContextCompat;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityCashBinding;
import com.gdlife.candypie.databinding.ActivityCashIndexBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.PayService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.account.UserCashAccountBean;
import com.heboot.bean.account.UserCashAccountChildBean;
import com.heboot.dialog.TipCustomOneDialog;
import com.heboot.event.AccountEvent;
import com.heboot.event.NormalEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/14.
 */

public class CashIndexActivity extends BaseActivity<ActivityCashIndexBinding> {

    @Inject
    UIService uiService = new UIService();


    private TipCustomOneDialog tipCustomOneDialog;

    private PayService payService;

    private UserCashAccountChildBean user_account;


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setWhiteBack(false);
        binding.includeToolbar.setShowRight(true);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_cash));
        binding.includeToolbar.tvRight.setText(getString(R.string.rule));
//        DaggerServiceComponent.builder().build().inject(this);
        uiService.setToolbarRightTextdStyle(binding.includeToolbar.tvRight, false, ContextCompat.getColor(this, R.color.color_343445));
    }

    @Override
    public void initData() {

        payService = new PayService();
        cash_account();
        binding.tvBalance.setText(UserService.getInstance().getUser().getCash_balance());

        binding.tvInfo.setText
                ("系统将扣除支付宝手续费" + MAPP.mapp.getConfigBean().getUser_cash_config().getTax() + "%");

        binding.btnBottom.setText(MAPP.mapp.getConfigBean().getUser_cash_config().getTip());


    }

    @Override
    public void initListener() {
        binding.includeToolbar.tvRight.setOnClickListener((v) -> {
            IntentUtils.toHTMLActivity(this, null, MAPP.mapp.getConfigBean().getStatic_url_config().getCash_rule());
        });

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        RxTextView.textChanges(binding.etMoney).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                checkBottom();
            }
        });


        binding.btnBottom.setOnClickListener((v) -> {
            cash();
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
                } else if (o instanceof AccountEvent.SET_CASH_ACCOUNT_EVENT) {
                    if (((AccountEvent.SET_CASH_ACCOUNT_EVENT) o).getUserCashAccountChildBean() != null) {
                        user_account = ((AccountEvent.SET_CASH_ACCOUNT_EVENT) o).getUserCashAccountChildBean();
                        setAliUI();
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
        binding.v2.setOnClickListener((v) -> {
            IntentUtils.toCashActivity(this);
        });
    }

    private void setAliUI() {
        binding.ivIcon.setBackgroundResource(R.drawable.icon_accout_alipay);
        binding.tvAccount.setText(user_account.getName() + "(" + user_account.getAccount() + "");
    }

    private void checkBottom() {
        if (binding.etMoney.getText().toString().length() > 0
                && user_account != null
                ) {
            binding.btnBottom.setSelected(true);
        } else {
            binding.btnBottom.setSelected(false);
        }
    }

    private void cash_account() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);


        HttpClient.Builder.getGuodongServer().cash_account(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<UserCashAccountBean>() {
            @Override
            public void onSuccess(BaseBean<UserCashAccountBean> baseBean) {
                if (baseBean.getData() != null && baseBean.getData().getUser_account() != null) {
                    user_account = baseBean.getData().getUser_account();
                    setAliUI();
                }
            }

            @Override
            public void onError(BaseBean<UserCashAccountBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(CashIndexActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }

    private void cash() {
        if (StringUtils.isEmpty(user_account.getAccount())) {
            tipDialog = DialogUtils.getFailDialog(this, "请添加支付宝账号", true);
            tipDialog.show();
            return;

        }
        if (StringUtils.isEmpty(binding.etMoney.getText().toString())) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.please_input_cash_amount), true);
            tipDialog.show();
            return;

        }
        params = SignUtils.getNormalParams();
        params.put(MKey.ACCOUNT, user_account.getAccount());
        params.put(MKey.NAME, user_account.getName());
        params.put(MKey.AMOUNT, binding.etMoney.getText().toString());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().apply_cash(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                tipCustomOneDialog = new TipCustomOneDialog.Builder(CashIndexActivity.this, baseBean.getMessage(), "OK", new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        finish();
                    }
                }).create();
                tipCustomOneDialog.show();

            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(CashIndexActivity.this, baseBean.getMessage(), true);
                tipDialog.show();

            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash_index;
    }
}
