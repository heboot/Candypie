package com.gdlife.candypie.activitys.login2register;

import android.view.View;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.databinding.ActivityRegisterBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.login2register.SendSMSBean;
import com.heboot.event.NormalEvent;
import com.heboot.utils.MStatusBarUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/3.
 */

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {

    @Inject
    CheckUtils checkUtils;

    private String type;

    private boolean isDelete;

    private int lastContentLength;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(true);
        binding.includeToolbar.setWhiteBack(false);
        binding.btnBottom.setText(getString(R.string.mnext));
    }

    @Override
    public void initData() {
        DaggerUtilsComponent.builder().build().inject(this);
        type = (String) getIntent().getExtras().get(MKey.TYPE);
        if (type.equals(MValue.CHECK_SMS_CODE.REG)) {
            binding.tvLogin.setText(getString(R.string.login_by_mobile));
        } else if (type.equals(MValue.CHECK_SMS_CODE.FORGET)) {
            binding.tvLogin.setText(getString(R.string.fogget_mobile_tip));
            binding.llytProtocaol.setVisibility(View.INVISIBLE);
        }


        QMUIKeyboardHelper.showKeyboard(binding.etMobile, true);


    }

    @Override
    public void initListener() {

        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(NormalEvent.FINISH_PAGE)) {
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
        RxTextView.textChanges(binding.etMobile).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                StringBuffer stringBuffer = new StringBuffer(charSequence);

                isDelete = stringBuffer.length() > lastContentLength?false:true;

                if (!isDelete && charSequence.length() == 3) {
                    stringBuffer.insert(3," ");
                    binding.etMobile.setText(stringBuffer.toString());
                    binding.etMobile.setSelection(stringBuffer.length());
                } else if (!isDelete && charSequence.length() == 8) {
                    stringBuffer.insert(8," ");
                    binding.etMobile.setText(stringBuffer.toString());
                    binding.etMobile.setSelection(stringBuffer.length());
                }
                if(isDelete && (stringBuffer.length() == 3 || stringBuffer.length() == 8)){
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    binding.etMobile.setText(stringBuffer.toString());
                    binding.etMobile.setSelection(stringBuffer.length());
                }


                if (checkUtils.checkMobile(86, charSequence.toString())) {
                    checkUtils.setBottomEnable(binding.btnBottom, true);
                } else {
                    checkUtils.setBottomEnable(binding.btnBottom, false);
                }

                lastContentLength = stringBuffer.length();

            }
        });
        binding.btnBottom.setOnClickListener((v) -> {

            if (!checkUtils.checkMobile(86, binding.etMobile.getText().toString())) {
                tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_error_mobile), true);
                tipDialog.show();
                return;
            }


            if (type.equals(MValue.CHECK_SMS_CODE.REG)) {
                if (!binding.cb.isChecked()) {
                    tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_error_protol), true);
                    tipDialog.show();
                    return;
                }
            }

            tipDialog = DialogUtils.getLoadingDialog(this, null, false);
            tipDialog.show();
            params = SignUtils.getNormalParams();
            params.put(MKey.MOBILE, binding.etMobile.getText().toString().replaceAll(" ",""));
            params.put(MKey.TYPE, type);
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);
            HttpClient.Builder.getGuodongServer().send_sms(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<SendSMSBean>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    addDisposable(disposable);
                }

                @Override
                public void onSuccess(SendSMSBean baseBean) {
                    SendSMSBean sendSMSBeanBaseBean = baseBean;
                    tipDialog.dismiss();
                    IntentUtils.toRegisterCodeActivity(RegisterActivity.this, binding.etMobile.getText().toString(), sendSMSBeanBaseBean.getInterval_time(), type);
                }


                @Override
                public void onError(Throwable throwable) {
                    if (tipDialog != null && tipDialog.isShowing()) {
                        tipDialog.dismiss();
                    }
                    tipDialog.dismiss();
                    tipDialog = DialogUtils.getFailDialog(RegisterActivity.this, throwable.getMessage(), true);
                    tipDialog.show();
                }

                @Override
                public void onError(BaseBean<SendSMSBean> basebean) {

                }


            }));

        });

        binding.tvProtocaol.setOnClickListener((v) -> {
            IntentUtils.toHTMLActivity(this, "", MAPP.mapp.getConfigBean().getStatic_url_config().getReg_protocol());
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }
}
