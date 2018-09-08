package com.gdlife.candypie.activitys.common;

import android.content.Intent;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.login2register.LoginActivity;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.LoginType;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.databinding.ActivitySplashBinding;
import com.gdlife.candypie.serivce.LoginService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.utils.MStatusBarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by heboot on 2018/1/18.
 */

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {//EasyPermissions.PermissionCallbacks,

    //    String uri = "android.resource://" + getPackageName() + "/" + R.raw.ttt;
    //    private String uri = Environment.getExternalStorageDirectory() + "/1.mp4";//视频路径


    private LoginService loginService = new LoginService();

    private QMUITipDialog loadingDialog;

    private Observer<HashMap> observer;

    @Override
    public void initUI() {

        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        setSwipeBackEnable(false);

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
                    IntentUtils.toRegisterInfoActivity(SplashActivity.this, (String) o.get("syncid"), (LoginType) o.get("loginType"), o);
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
        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(NormalEvent.FINISH_PAGE)) {
                    finish();
                } else if (o.equals(UserEvent.LOGIN_SUC)) {
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
        binding.includeOtherLogin.llytPhone.setOnClickListener((v) -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
        binding.includeOtherLogin.llytWx.setOnClickListener((v) -> {
            if (loadingDialog == null) {
                loadingDialog = DialogUtils.getLoadingDialog(this, "", false);
            }
            loadingDialog.show();
            loginService.doWXLogin(observer);
        });
        binding.includeOtherLogin.llytYk.setOnClickListener((v) -> {
            MobclickAgent.onEvent(this, NumEventKeys.tourist_login.toString());
            UserService.getInstance().doTouristPreview();
            if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                IntentUtils.toIndexActivity(SplashActivity.this);
            } else {
                IntentUtils.toMainActivity(SplashActivity.this);
            }
            finish();
        });
//        binding.btnRegister.setOnClickListener((v) -> {
//            IntentUtils.toRegisterActivity(this, MValue.CHECK_SMS_CODE.REG);
//            finish();
////            RxBus.getInstance().post(NormalEvent.FINISH_PAGE);
//        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
