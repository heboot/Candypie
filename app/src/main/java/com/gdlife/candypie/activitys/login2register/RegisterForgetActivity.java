package com.gdlife.candypie.activitys.login2register;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.common.SplashActivity;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.LoginType;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.common.RegisterForgetFrom;
import com.gdlife.candypie.databinding.ActivityRegisterForgetBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.LoginService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.ObserableUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.login2register.SendSMSBean;
import com.heboot.entity.User;
import com.heboot.event.NormalEvent;
import com.heboot.utils.MStatusBarUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterForgetActivity extends BaseActivity<ActivityRegisterForgetBinding> {


    private String type;

    private boolean isDelete;

    private int lastContentLength;

    private CheckUtils checkUtils = new CheckUtils();

    private int interval_time;
    private Observable timeObservable;
    private Disposable timeDisposable;
    private Observer observer;

    private User user;


    private LoginService loginService = new LoginService();

    private QMUITipDialog loadingDialog;

    private Observer<HashMap> loginObs;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_forget;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        type = (String) getIntent().getExtras().get(MKey.TYPE);
        if (type.equals(MValue.CHECK_SMS_CODE.REG)) {
            binding.includeToolbar.tvTitle.setText("注册");
            binding.btnBottom.setText("下一步");
        } else {
            binding.includeToolbar.tvTitle.setText("找回密码");
            binding.btnBottom.setText("去登录");
        }
        QMUIKeyboardHelper.showKeyboard(binding.etMobile, true);
        setSendBtn(true);
    }

    /**
     * 发送按钮
     */
    private void setSendBtn(boolean enable) {
        if (enable) {
            binding.tvSend.setEnabled(true);
            binding.tvSend.setText("获取验证码");
            binding.tvSend.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.tvSend.setBackgroundResource(R.drawable.bg_rect_bottom_button);
        } else {
            binding.tvSend.setEnabled(false);
            binding.tvSend.setBackgroundResource(R.drawable.bg_rect_d6d6df);
            binding.tvSend.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    @Override
    public void initData() {
        observer = new Observer<HashMap>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(HashMap o) {
                loadingDialog.dismiss();
                if (!StringUtils.isEmpty((String) o.get("syncid"))) {
//                    IntentUtils.toRegisterActivity();
                    IntentUtils.toRegisterInfoActivity(RegisterForgetActivity.this, (String) o.get("syncid"), (LoginType) o.get("loginType"), o);
                }
            }

            @Override
            public void onError(Throwable e) {
                loadingDialog.dismiss();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
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
        binding.btnBottom.setOnClickListener((v) -> {
            checkCode();
        });
        binding.tvSend.setOnClickListener((v) -> {

            if (!checkUtils.checkMobile(86, binding.etMobile.getText().toString())) {
                tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_error_mobile), true);
                tipDialog.show();
                return;
            }


            tipDialog = DialogUtils.getLoadingDialog(this, null, false);
            tipDialog.show();
            params = SignUtils.getNormalParams();
            params.put(MKey.MOBILE, binding.etMobile.getText().toString().replaceAll(" ", ""));
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
                    interval_time = sendSMSBeanBaseBean.getInterval_time();
                    setSendBtn(false);
                    countDown();
                }


                @Override
                public void onError(Throwable throwable) {
                    if (tipDialog != null && tipDialog.isShowing()) {
                        tipDialog.dismiss();
                    }
                    tipDialog.dismiss();
                    tipDialog = DialogUtils.getFailDialog(RegisterForgetActivity.this, throwable.getMessage(), true);
                    tipDialog.show();
                }

                @Override
                public void onError(BaseBean<SendSMSBean> basebean) {

                }


            }));

        });
        RxTextView.textChanges(binding.etMobile).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                StringBuffer stringBuffer = new StringBuffer(charSequence);

                isDelete = stringBuffer.length() > lastContentLength ? false : true;

                if (!isDelete && charSequence.length() == 3) {
                    stringBuffer.insert(3, " ");
                    binding.etMobile.setText(stringBuffer.toString());
                    binding.etMobile.setSelection(stringBuffer.length());
                } else if (!isDelete && charSequence.length() == 8) {
                    stringBuffer.insert(8, " ");
                    binding.etMobile.setText(stringBuffer.toString());
                    binding.etMobile.setSelection(stringBuffer.length());
                }
                if (isDelete && (stringBuffer.length() == 3 || stringBuffer.length() == 8)) {
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    binding.etMobile.setText(stringBuffer.toString());
                    binding.etMobile.setSelection(stringBuffer.length());
                }


//                if (checkUtils.checkMobile(86, charSequence.toString())) {
//                    checkUtils.setBottomEnable(binding.btnBottom, true);
//                } else {
//                    checkUtils.setBottomEnable(binding.btnBottom, false);
//                }

                lastContentLength = stringBuffer.length();
                checkBottom();

            }
        });
        RxTextView.textChanges(binding.etPwd).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                checkBottom();
            }
        });
        RxTextView.textChanges(binding.etVcode).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                checkBottom();
            }
        });

        binding.includeOtherLogin.llytWx.setOnClickListener((v) -> {
            if (loadingDialog == null) {
                loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
            }
            loadingDialog.show();
            loginService.doWXLogin(loginObs);
        });
        binding.includeOtherLogin.llytQq.setOnClickListener((v) -> {
            if (loadingDialog == null) {
                loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
            }
            loadingDialog.show();
            loginService.doQQLogin(loginObs);
        });
        binding.includeOtherLogin.llytYk.setOnClickListener((v) -> {
            MobclickAgent.onEvent(this, NumEventKeys.tourist_login.toString());
            UserService.getInstance().doTouristPreview();
            if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                IntentUtils.toIndexActivity(RegisterForgetActivity.this);
            } else {
                IntentUtils.toMainActivity(RegisterForgetActivity.this);
            }
            finish();
        });

    }

    private void checkBottom() {
        if (checkUtils.checkMobile(86, binding.etMobile.getText().toString())
                && checkUtils.checkPwd(binding.etPwd.getText().toString())
                && checkUtils.checkCode(binding.etVcode.getText().toString())
                ) {
            binding.btnBottom.setSelected(true);
        } else {
            binding.btnBottom.setSelected(false);
        }
    }

    private void doReg() {
        if (type.equals(MValue.CHECK_SMS_CODE.FORGET)) {
            MobclickAgent.onEvent(this, NumEventKeys.retrieve_password.toString());
            params = SignUtils.getNormalParams();
            params.put(MKey.MOBILE, UserService.getInstance().getUser().getMobile());
            params.put(MKey.CODE, UserService.getInstance().getUser().getSmscode());
            params.put(MKey.PASSWORD, binding.etPwd.getText().toString());
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);
            HttpClient.Builder.getGuodongServer().user_find_password(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<BaseBeanEntity>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    addDisposable(disposable);
                }

                @Override
                public void onSuccess(BaseBeanEntity o) {
                    Intent intent = new Intent(RegisterForgetActivity.this, LoginActivity.class);
                    startActivity(intent);
                }


                @Override
                public void onError(Throwable throwable) {
                    if (tipDialog != null) {
                        tipDialog.dismiss();
                    }
                    tipDialog = DialogUtils.getFailDialog(RegisterForgetActivity.this, throwable.getMessage(), true);
                    tipDialog.show();
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> basebean) {
                    if (tipDialog != null && tipDialog.isShowing()) {
                        tipDialog.dismiss();
                    }

                    tipDialog = DialogUtils.getFailDialog(RegisterForgetActivity.this, basebean.getMessage(), true);
                    tipDialog.show();
                }


            }));
        } else if (type.equals(MValue.CHECK_SMS_CODE.REG)) {
            UserService.getInstance().getUser().setPwd(binding.etPwd.getText().toString());
            Intent intent = new Intent(RegisterForgetActivity.this, RegisterInfoActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkCode() {

        if (!checkUtils.checkMobile(86, binding.etMobile.getText().toString())) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_error_mobile), true);
            tipDialog.show();
            return;
        }

        if (!checkUtils.checkPwd(binding.etPwd.getText().toString())) {
            tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_error_pwd), true);
            tipDialog.show();
            return;
        }

        if (!checkUtils.checkCode(binding.etVcode.getText().toString())) {
            tipDialog = DialogUtils.getFailDialog(this, "验证码不正确", true);
            tipDialog.show();
            return;
        }


        if (checkUtils.checkCode(binding.etVcode.getText().toString())) {
            params = SignUtils.getNormalParams();
            params.put(MKey.MOBILE, binding.etMobile.getText().toString().replaceAll(" ", ""));
            params.put(MKey.TYPE, type);
            params.put(MKey.CODE, binding.etVcode.getText().toString());
            tipDialog = DialogUtils.getLoadingDialog(this, "", false);
            tipDialog.show();
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);
            HttpClient.Builder.getGuodongServer().send_sms_check(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<BaseBeanEntity>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    addDisposable(disposable);
                }

                @Override
                public void onSuccess(BaseBeanEntity o) {
                    tipDialog.dismiss();
                    user = new User();
                    user.setMobile(binding.etMobile.getText().toString().replaceAll(" ", ""));
                    user.setSmscode(binding.etVcode.getText().toString());
                    UserService.getInstance().setUser(user);
                    doReg();
                }

                @Override
                public void onError(Throwable throwable) {
                    tipDialog.dismiss();
                    tipDialog = DialogUtils.getFailDialog(RegisterForgetActivity.this, throwable.getMessage(), true);
                    tipDialog.show();
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> basebean) {
                    if (tipDialog != null && tipDialog.isShowing()) {
                        tipDialog.dismiss();
                    }

                    tipDialog = DialogUtils.getFailDialog(RegisterForgetActivity.this, basebean.getMessage(), true);
                    tipDialog.show();
                }

            }));

//            checkUtils.setBottomEnable(binding.btnBottom, true);
        }
    }


    private void countDown() {

        timeObservable = ObserableUtils.countdown(interval_time);

        timeObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());

        initObserver();

        timeObservable.subscribe(observer);
    }

    private void initObserver() {
        observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                timeDisposable = d;
                addDisposable(timeDisposable);
            }

            @Override
            public void onNext(Integer integer) {
                binding.tvSend.setText(integer.intValue() + "s");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                binding.tvSend.setText("获取验证码");
                binding.tvSend.setEnabled(true);
            }
        };
    }

}
