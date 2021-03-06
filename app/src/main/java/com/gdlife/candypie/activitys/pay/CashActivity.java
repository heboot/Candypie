package com.gdlife.candypie.activitys.pay;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.account.AccountCoinAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityCashBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.PayService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.common.CashTipDialog;
import com.gdlife.candypie.widget.dialog.account.ChoosePayTypeDialog;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.account.UserCashAccountBean;
import com.heboot.bean.account.UserCashAccountChildBean;
import com.heboot.bean.pay.RechargeConfigBean;
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

public class CashActivity extends BaseActivity<ActivityCashBinding> {

    @Inject
    UIService uiService;


    private TipCustomOneDialog tipCustomOneDialog;

    private PayService payService;


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setWhiteBack(false);
        binding.includeToolbar.setShowRight(true);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_cash));
        binding.includeToolbar.tvRight.setText(getString(R.string.rule));
        DaggerServiceComponent.builder().build().inject(this);
        uiService.setToolbarRightTextdStyle(binding.includeToolbar.tvRight, false, ContextCompat.getColor(this, R.color.color_343445));
    }

    @Override
    public void initData() {

        payService = new PayService();


//        binding.tvCharge.append(MAPP.mapp.getConfigBean().getUser_cash_config().getTax() + "%");

//        binding.btnBottom.setText(payService.getCashDateStr(MAPP.mapp.getConfigBean().getUser_cash_config().getCash_week()));


    }

    @Override
    public void initListener() {
        binding.includeToolbar.tvRight.setOnClickListener((v) -> {
            IntentUtils.toHTMLActivity(this, null, MAPP.mapp.getConfigBean().getStatic_url_config().getCash_rule());
        });

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        RxTextView.textChanges(binding.etAccount).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                checkBottom();
            }
        });


        RxTextView.textChanges(binding.etName).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                checkBottom();
            }
        });

        binding.btnBottom.setOnClickListener((v) -> {

            if (StringUtils.isEmpty(binding.etAccount.getText().toString())) {
                tipDialog = DialogUtils.getFailDialog(this, getString(R.string.hint_input_ali_account), true);
                tipDialog.show();
                return;
            }

            if (StringUtils.isEmpty(binding.etName.getText().toString())) {
                tipDialog = DialogUtils.getFailDialog(this, getString(R.string.please_input_cash_name), true);
                tipDialog.show();
                return;
            }

            UserCashAccountChildBean userCashAccountChildBean = new UserCashAccountChildBean();
            userCashAccountChildBean.setAccount(binding.etAccount.getText().toString());
            userCashAccountChildBean.setName(binding.etName.getText().toString());

            RxBus.getInstance().post(new AccountEvent.SET_CASH_ACCOUNT_EVENT(userCashAccountChildBean));
            finish();

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
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void checkBottom() {
        if (binding.etName.getText().toString().length() > 0
                && binding.etAccount.getText().toString().length() > 0
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

            }

            @Override
            public void onError(BaseBean<UserCashAccountBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(CashActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash;
    }
}
