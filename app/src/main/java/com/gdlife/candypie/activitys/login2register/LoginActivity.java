package com.gdlife.candypie.activitys.login2register;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.LoginType;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.databinding.ActivityLoginBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.LoginService;
import com.gdlife.candypie.serivce.ServerService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.CheckUtils;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.login2register.RegisterBean;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/3.
 */

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {


    @Inject
    CheckUtils checkUtils;

    //    @Inject
    private LoginService loginService;

    private ServerService serverService;

    private boolean isDelete;

    private int lastContentLength;


    private QMUITipDialog loadingDialog;

    private Observer<HashMap> loginObs;

    @Override
    public void initUI() {
        MobclickAgent.onEvent(LoginActivity.this, NumEventKeys.login_view.toString());
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setShowRight(true);
        binding.includeToolbar.tvRight.setText("注册");
        binding.includeToolbar.tvTitle.setText("登录蜜糖派");
        binding.btnBottom.setText("立即登录");
    }

    @Override
    public void initData() {
        loginService = new LoginService();
        serverService = new ServerService();
        DaggerUtilsComponent.builder().build().inject(this);
        loginObs = new Observer<HashMap>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(HashMap o) {
                loadingDialog.dismiss();
                if (!StringUtils.isEmpty((String) o.get("syncid"))) {
//                    IntentUtils.toRegisterActivity();
                    IntentUtils.toRegisterInfoActivity(LoginActivity.this, (String) o.get("syncid"), (LoginType) o.get("loginType"), o);
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
//        DaggerServiceComponent.builder().build().inject(this);
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

                if (checkUtils.checkMobile(86, charSequence.toString())) {
                    if (checkUtils.checkPwd(binding.etPwd.getText().toString())) {
                        checkUtils.setBottomEnable(binding.btnBottom, true);
                    } else {
                        checkUtils.setBottomEnable(binding.btnBottom, false);
                    }
                }

                lastContentLength = stringBuffer.length();

            }
        });


        //.compose(RxLifecycle.bindUntilEvent(this,ActivityEv))

        RxTextView.textChanges(binding.etPwd).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                if (checkUtils.checkPwd(String.valueOf(binding.etPwd.getText()))) {
                    if (checkUtils.checkMobile(86, binding.etMobile.getText().toString())) {
                        binding.btnBottom.setSelected(true);
                    }
                }
            }
        });

        binding.llytForget.setOnClickListener((v) -> {
            IntentUtils.toRegisterActivity(this, MValue.CHECK_SMS_CODE.FORGET);
        });

        binding.includeToolbar.tvRight.setOnClickListener((v) -> {
            IntentUtils.toRegisterActivity(this, MValue.CHECK_SMS_CODE.REG);
        });


        binding.btnBottom.setOnClickListener((v) -> {
//
//            Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
//            startActivity(intent);

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
            tipDialog = DialogUtils.getLoadingDialog(this, null, false);
            tipDialog.show();
            params = SignUtils.getNormalParams();
            params.put(MKey.MOBILE, binding.etMobile.getText().toString().replaceAll(" ", ""));
            params.put(MKey.PASSWORD, binding.etPwd.getText().toString());
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);

            HttpClient.Builder.getGuodongServer().user_login(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<RegisterBean>() {
                @Override
                public void onSuccess(BaseBean<RegisterBean> baseBean) {
                    MobclickAgent.onEvent(LoginActivity.this, NumEventKeys.login_success.toString());
                    RegisterBean registerBean = baseBean.getData();
                    registerBean.getUser().setMobile(binding.etMobile.getText().toString().replaceAll(" ", ""));
                    tipDialog.dismiss();
                    registerBean.getUser().setPwd(binding.etPwd.getText().toString());
                    UserService.getInstance().setUser(registerBean.getUser());
                    UserService.getInstance().putSPUser(registerBean.getUser());
                    UserService.getInstance().setVideo_first_order(registerBean.getVideo_first_order());
                    loginService.doLoginAgora(baseBean.getData().getVideo_user(), baseBean.getData().getIm_user());

                    RxBus.getInstance().post(UserEvent.LOGIN_SUC);

                    if (baseBean.getData().getRun_service_tip() != null && !StringUtils.isEmpty(baseBean.getData().getRun_service_tip().getUser_service_id())) {
                        serverService.doRuningService(LoginActivity.this, baseBean.getData().getRun_service_tip());
                    } else {

                        if (registerBean.getUser().getRole() == MValue.USER_ROLE_SERVICER && registerBean.getUser().getService_auth_status() != null && registerBean.getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {
                            if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                                IntentUtils.toIndexActivity(LoginActivity.this);
                            } else {
                                IntentUtils.toMainActivity(LoginActivity.this);
                            }
                        } else {
                            if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                                IntentUtils.toIndexActivity(LoginActivity.this);
                            } else {
                                IntentUtils.toIndexUsersActivity(LoginActivity.this);
                            }
                            finish();
                        }

                    }

                    //更新改到配置文件那一步了
//                    if (registerBean.getApp_version_update() != null && !StringUtils.isEmpty(registerBean.getApp_version_update().getContent())) {
//                        MValue.updateVersionBean = registerBean.getApp_version_update();
//                    }


                    finish();

                }

                @Override
                public void onError(BaseBean<RegisterBean> baseBean) {
                    tipDialog.dismiss();
                    tipDialog = DialogUtils.getFailDialog(LoginActivity.this, baseBean.getMessage(), true);
                    tipDialog.show();
                }
            });


//            HttpClient.Builder.getGuodongServer().user_login(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<RegisterBean>() {
//                @Override
//                public void onSubscribe(Disposable disposable) {
//                    addDisposable(disposable);
//                }
//
//                @Override
//                public void onSuccess(RegisterBean baseBean) {
//                    RegisterBean registerBean = baseBean;
//                    registerBean.getUser().setMobile(binding.etMobile.getText().toString());
//                    tipDialog.dismiss();
//                    registerBean.getUser().setPwd(binding.etPwd.getText().toString());
//                    UserService.getInstance().setUser(registerBean.getUser());
//                    UserService.getInstance().putSPUser(registerBean.getUser());
//                    loginService.doLoginAgora(baseBean.getVideo_user(), baseBean.getIm_user());
//                    Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//
//
//                @Override
//                public void onError(Throwable throwable) {
//                    tipDialog.dismiss();
//                    tipDialog = DialogUtils.getFailDialog(LoginActivity.this, throwable.getMessage(), true);
//                    tipDialog.show();
//                }
//
//                @Override
//                public void onError(BaseBean<RegisterBean> basebean) {
//
//                }
//
//            }));

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
                IntentUtils.toIndexActivity(LoginActivity.this);
            } else {
                IntentUtils.toMainActivity(LoginActivity.this);
            }
            finish();
        });

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
