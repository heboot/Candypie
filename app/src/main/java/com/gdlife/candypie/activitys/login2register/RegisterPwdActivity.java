package com.gdlife.candypie.activitys.login2register;

import android.content.Intent;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.databinding.ActivityForgetPasswordBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.utils.MStatusBarUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/5.
 */

public class RegisterPwdActivity extends BaseActivity<ActivityForgetPasswordBinding> {


    @Inject
    CheckUtils checkUtils;

    private String type;




    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(true);
        binding.btnBottom.setText(getString(R.string.mnext));
        type = (String) getIntent().getExtras().get(MKey.TYPE);
        if (type.equals(MValue.CHECK_SMS_CODE.REG)) {
            binding.tvSetPwd.setText(getString(R.string.set_password));
            binding.etPwd.setHint(getString(R.string.set_password_hint));
            binding.btnBottom.setText(getString(R.string.mnext));
        } else if (type.equals(MValue.CHECK_SMS_CODE.FORGET)) {
            binding.tvSetPwd.setText(getString(R.string.set_new_password));
            binding.etPwd.setHint(getString(R.string.set_new_password_hint));
            binding.btnBottom.setText(getString(R.string.to_login));
        }
        setSwipeBackEnable(false);

    }

    @Override
    public void initData() {
        DaggerUtilsComponent.builder().build().inject(this);
    }

    @Override
    public void initListener() {
        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        RxTextView.textChanges(binding.etPwd)//限流时间500ms .debounce(500, TimeUnit.MILLISECONDS)

                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                if (checkUtils.checkPwd(charSequence.toString())) {
                    checkUtils.setBottomEnable(binding.btnBottom, true);
                } else {
                    checkUtils.setBottomEnable(binding.btnBottom, false);
                }
            }
        });

        QMUIKeyboardHelper.showKeyboard(binding.etPwd, true);

        binding.btnBottom.setOnClickListener((v) -> {
            if (!checkUtils.checkPwd(binding.etPwd.getText().toString())) {
                tipDialog = DialogUtils.getFailDialog(this, getString(R.string.check_error_pwd), true);
                tipDialog.show();
                return;
            }

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
                        Intent intent = new Intent(RegisterPwdActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        if (tipDialog != null) {
                            tipDialog.dismiss();
                        }
                        tipDialog = DialogUtils.getFailDialog(RegisterPwdActivity.this, throwable.getMessage(), true);
                        tipDialog.show();
                    }

                    @Override
                    public void onError(BaseBean<BaseBeanEntity> basebean) {
                        if (tipDialog != null && tipDialog.isShowing()) {
                            tipDialog.dismiss();
                        }

                        tipDialog = DialogUtils.getFailDialog(RegisterPwdActivity.this, basebean.getMessage(), true);
                        tipDialog.show();
                    }


                }));
            } else if (type.equals(MValue.CHECK_SMS_CODE.REG)) {
                UserService.getInstance().getUser().setPwd(binding.etPwd.getText().toString());
                Intent intent = new Intent(RegisterPwdActivity.this, RegisterInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }
}