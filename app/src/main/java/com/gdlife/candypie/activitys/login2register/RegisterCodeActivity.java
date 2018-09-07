package com.gdlife.candypie.activitys.login2register;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.databinding.ActivityRegisterCodeBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.ObserableUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.login2register.SendSMSBean;
import com.heboot.entity.User;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/3.
 */

public class RegisterCodeActivity extends BaseActivity<ActivityRegisterCodeBinding> {

    @Inject
    CheckUtils checkUtils;

    private String mobile;

    private Observable timeObservable;
    private Disposable timeDisposable;
    private Observer observer;

    private int interval_time;

    private String type;

    private User user;

    @Override
    public void initUI() {
        MobclickAgent.onEvent(this, NumEventKeys.verification_code_view.toString());
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(true);
        binding.btnBottom.setText(getString(R.string.resend));
        setSwipeBackEnable(false);

        binding.includeCode2.tvCode.setEnabled(false);
        binding.includeCode3.tvCode.setEnabled(false);
        binding.includeCode4.tvCode.setEnabled(false);

//        QMUIKeyboardHelper.showKeyboard(binding.includeCode1.tvCode, true);
    }

    @Override
    public void initData() {
        DaggerUtilsComponent.builder().build().inject(this);
        checkUtils.setBottomEnable(binding.btnBottom, false);
        binding.btnBottom.setEnabled(false);
        mobile = (String) getIntent().getExtras().get(MKey.MOBILE);
        interval_time = (int) getIntent().getExtras().get(MKey.INTERVAL_TIME);
        type = (String) getIntent().getExtras().get(MKey.TYPE);
        binding.tvRegisterSendmobile.setText(mobile);
        countDown();
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
                binding.btnBottom.setText(getString(R.string.resend) + "(" + integer.intValue() + ")");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                binding.btnBottom.setText(getString(R.string.resend));
                checkUtils.setBottomEnable(binding.btnBottom, true);
                binding.btnBottom.setEnabled(true);
            }
        };
    }


    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });
        binding.btnBottom.setOnClickListener((v) -> {
            tipDialog = DialogUtils.getLoadingDialog(this, "", false);
            tipDialog.show();
            params = SignUtils.getNormalParams();
            params.put(MKey.MOBILE, mobile.replaceAll(" ", ""));
            params.put(MKey.TYPE, MValue.CHECK_SMS_CODE.REG);
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);
            HttpClient.Builder.getGuodongServer().send_sms(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<SendSMSBean>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    addDisposable(disposable);
                }

                @Override
                public void onSuccess(SendSMSBean sendSMSBeanBaseBean) {
                    binding.includeCode1.tvCode.setText("");
                    binding.includeCode2.tvCode.setText("");
                    binding.includeCode3.tvCode.setText("");
                    binding.includeCode4.tvCode.setText("");
                    binding.includeCode4.tvCode.setEnabled(false);
                    binding.includeCode3.tvCode.setEnabled(false);
                    binding.includeCode2.tvCode.setEnabled(false);

//                    QMUIKeyboardHelper.showKeyboard(binding.includeCode1.tvCode, false);

                    checkUtils.setBottomEnable(binding.btnBottom, false);
                    binding.btnBottom.setEnabled(false);
                    QMUIKeyboardHelper.showKeyboard(binding.etCode, 0);
                    tipDialog.dismiss();
                    initObserver();
                    timeObservable = ObserableUtils.countdown(interval_time);
                    timeObservable.subscribe(observer);
                }


                @Override
                public void onError(Throwable throwable) {
                    tipDialog.dismiss();
                    tipDialog = DialogUtils.getFailDialog(RegisterCodeActivity.this, throwable.getMessage(), true);
                    tipDialog.show();
                }

                @Override
                public void onError(BaseBean<SendSMSBean> basebean) {

                }

            }));
        });
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkUtils.setEditLastSelection(binding.etCode);
                QMUIKeyboardHelper.showKeyboard(binding.etCode, 0);
            }
        };


        binding.vv.setOnClickListener(clickListener);


        QMUIKeyboardHelper.showKeyboard(binding.etCode, true);
        binding.etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.includeCode1.tvCode.setText(String.valueOf(s.charAt(0)));
                    binding.includeCode2.tvCode.setText("");
                    binding.includeCode3.tvCode.setText("");
                    binding.includeCode4.tvCode.setText("");
                    checkCode();
                } else if (s.length() == 2) {
                    binding.includeCode1.tvCode.setText(String.valueOf(s.charAt(0)));
                    binding.includeCode2.tvCode.setText(String.valueOf(s.charAt(1)));
                    binding.includeCode3.tvCode.setText("");
                    binding.includeCode4.tvCode.setText("");
                    checkCode();
                } else if (s.length() == 3) {
                    binding.includeCode1.tvCode.setText(String.valueOf(s.charAt(0)));
                    binding.includeCode2.tvCode.setText(String.valueOf(s.charAt(1)));
                    binding.includeCode3.tvCode.setText(String.valueOf(s.charAt(2)));
                    binding.includeCode4.tvCode.setText("");
                    checkCode();
                } else if (s.length() == 4) {
                    binding.includeCode1.tvCode.setText(String.valueOf(s.charAt(0)));
                    binding.includeCode2.tvCode.setText(String.valueOf(s.charAt(1)));
                    binding.includeCode3.tvCode.setText(String.valueOf(s.charAt(2)));
                    binding.includeCode4.tvCode.setText(String.valueOf(s.charAt(3)));
                    checkCode();
                } else if (s.length() == 0) {
                    binding.includeCode1.tvCode.setText("");
                    binding.includeCode2.tvCode.setText("");
                    binding.includeCode3.tvCode.setText("");
                    binding.includeCode4.tvCode.setText("");
                    checkCode();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void checkCode() {
        if (checkUtils.checkCode(binding.includeCode1.tvCode.getText().toString(), binding.includeCode2.tvCode.getText().toString(), binding.includeCode3.tvCode.getText().toString(), binding.includeCode4.tvCode.getText().toString())) {
            params = SignUtils.getNormalParams();
            params.put(MKey.MOBILE, mobile.replaceAll(" ", ""));
            params.put(MKey.TYPE, type);
            params.put(MKey.CODE, binding.includeCode1.tvCode.getText().toString()
                    + binding.includeCode2.tvCode.getText().toString()
                    + binding.includeCode3.tvCode.getText().toString()
                    + binding.includeCode4.tvCode.getText().toString());
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
                    user.setMobile(mobile.replaceAll(" ", ""));
                    user.setSmscode(binding.includeCode1.tvCode.getText().toString()
                            + binding.includeCode2.tvCode.getText().toString()
                            + binding.includeCode3.tvCode.getText().toString()
                            + binding.includeCode4.tvCode.getText().toString());
                    UserService.getInstance().setUser(user);
                    // TODO: 2018/2/6  调用接口验证通过后自动提交
                    IntentUtils.toRegisterPwdActivity(RegisterCodeActivity.this, mobile.replaceAll(" ", ""), type);
                    finish();
                }

                @Override
                public void onError(Throwable throwable) {
                    tipDialog.dismiss();
                    tipDialog = DialogUtils.getFailDialog(RegisterCodeActivity.this, throwable.getMessage(), true);
                    tipDialog.show();
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> basebean) {
                    if (tipDialog != null && tipDialog.isShowing()) {
                        tipDialog.dismiss();
                    }

                    tipDialog = DialogUtils.getFailDialog(RegisterCodeActivity.this, basebean.getMessage(), true);
                    tipDialog.show();
                }

            }));

//            checkUtils.setBottomEnable(binding.btnBottom, true);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_code;
    }
}
