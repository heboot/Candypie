package com.gdlife.candypie.activitys.pay;

import android.support.v4.content.ContextCompat;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
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
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.dialog.TipCustomOneDialog;
import com.heboot.event.NormalEvent;
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

    private CashTipDialog tipCustomDialog;

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

        binding.etMoney.setHint(getString(R.string.hint_input_cash_money) + UserService.getInstance().getUser().getCash_balance() + getString(R.string.price_unit));

        binding.tvCharge.append(MAPP.mapp.getConfigBean().getUser_cash_config().getTax() + "%");

        binding.btnBottom.setText(payService.getCashDateStr(MAPP.mapp.getConfigBean().getUser_cash_config().getCash_week()));

        String result = MAPP.mapp.getString(R.string.cash_tip1);
        String result2 = String.format(result, MAPP.mapp.getConfigBean().getUser_cash_config().getMin_amount());
        binding.tvInfo.setText(result2);

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

        RxTextView.textChanges(binding.etMoney).subscribe(new Consumer<CharSequence>() {
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

            if (StringUtils.isEmpty(binding.etMoney.getText().toString())) {
                tipDialog = DialogUtils.getFailDialog(this, getString(R.string.please_input_cash_amount), true);
                tipDialog.show();
                return;
            }


            tipCustomDialog = new CashTipDialog.Builder(this, new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {

                    if (integer == 1) {

                        initLogData();


                    }

                }
            }, getString(R.string.ali_account) + binding.etAccount.getText().toString() + "\n" +
                    getString(R.string.ali_account_name) + binding.etName.getText().toString() + "\n" +
                    getString(R.string.cash_amount) + binding.etMoney.getText().toString() + getString(R.string.price_unit)).create();
            tipCustomDialog.show();
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


    private void initLogData() {
        params = SignUtils.getNormalParams();
        params.put(MKey.ACCOUNT, binding.etAccount.getText().toString());
        params.put(MKey.NAME, binding.etName.getText().toString());
        params.put(MKey.AMOUNT, binding.etMoney.getText().toString());
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().apply_cash(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                tipCustomOneDialog = new TipCustomOneDialog.Builder(CashActivity.this, baseBean.getMessage(), "OK", new Consumer<Integer>() {
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
                tipDialog = DialogUtils.getFailDialog(CashActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }


    private void checkBottom() {
        if (binding.etMoney.getText().toString().length() > 0
                && binding.etName.getText().toString().length() > 0
                && binding.etAccount.getText().toString().length() > 0
                ) {
            binding.btnBottom.setSelected(true);
        } else {
            binding.btnBottom.setSelected(false);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash;
    }
}
