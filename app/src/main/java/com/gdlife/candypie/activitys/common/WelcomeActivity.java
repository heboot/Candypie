package com.gdlife.candypie.activitys.common;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.example.http.HttpRequest;
import com.example.http.HttpResponse;
import com.example.http.HttpUtils;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MCode;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.databinding.ActivityWelcomeBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.LoginService;
import com.gdlife.candypie.serivce.ServerService;
import com.gdlife.candypie.serivce.UpdateVersionService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.LocationUtils;
import com.gdlife.candypie.utils.ObserableUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.config.StartPageConfigBean;
import com.heboot.bean.config.VConfigBean;
import com.heboot.bean.login2register.RegisterBean;
import com.heboot.dialog.TipCustomOneDialog;
import com.heboot.dialog.TipCustomOnePermissionDialog;
import com.heboot.entity.User;
import com.heboot.utils.MStatusBarUtils;
import com.heboot.utils.PreferencesUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by heboot on 2018/2/8.
 */

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding> implements EasyPermissions.PermissionCallbacks {

    private StartPageConfigBean startPageConfigBean;

    @Inject
    PermissionUtils permissionUtils;

    @Inject
    LocationUtils locationUtils;

    LoginService loginService;

    private ServerService serverService;

    private TipCustomOneDialog timeoutDialog;

    @Override
    public void initUI() {
        MobclickAgent.onEvent(this, NumEventKeys.start_app.toString());
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        setSwipeBackEnable(false);
    }

    @Override
    public void initData() {
        loginService = new LoginService();
        serverService = new ServerService();
        timeoutDialog = new TipCustomOneDialog.Builder(this, "非常抱歉，网络连接出现异常，请重试", "重试", new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                initStartpageData();
            }
        }).create();
        MAPP.mapp.getUtilsComponent().inject(this);
        /**
         * 必须要有存储读写权限
         */
        if (permissionUtils.hasStoragePermission()) {
            initStartpageData();
        } else {
            permissionUtils.requestStorgePermission(this);
        }

    }

    @Override
    public void initListener() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initStartpageData() {

        params = SignUtils.getNormalParams();

        locationUtils.requestLocation();


        HttpClient.Builder.getGuodongServer().start_page(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<StartPageConfigBean>() {

            @Override
            public void onSuccess(BaseBean<StartPageConfigBean> baseBean) {
                startPageConfigBean = baseBean.getData();
                if (startPageConfigBean != null && startPageConfigBean.getStart_page_config() != null) {
                    ImageUtils.showImage(binding.iv, startPageConfigBean.getStart_page_config().getImg());
                }
                checkConfigData();
            }

            @Override
            public void onError(BaseBean<StartPageConfigBean> baseBean) {
                CrashReport.postCatchedException(new Throwable("start_page 出错" + baseBean.getMessage()));
//                if (timeoutDialog != null && !timeoutDialog.isShowing()) {
//                    timeoutDialog.show();
//                }
                checkConfigData();
            }
        });
    }

    private void autoLogin() {

        int second;
        if (startPageConfigBean != null && startPageConfigBean.getStart_page_config() != null) {
            second = startPageConfigBean.getStart_page_config().getSecond();
        } else {
            second = 0;
        }

        ObserableUtils.countdown(second).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                User user = UserService.getInstance().getSPUser();
                if (user != null && user.getPwd() != null) {
                    params.put(MKey.MOBILE, user.getMobile());
                    params.put(MKey.PASSWORD, user.getPwd());
                    String sign = SignUtils.doSign(params);
                    params.put(MKey.SIGN, sign);

                    HttpClient.Builder.getGuodongServer().user_login(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<RegisterBean>() {
                        @Override
                        public void onSuccess(BaseBean<RegisterBean> baseBean) {
                            MobclickAgent.onEvent(WelcomeActivity.this, NumEventKeys.auto_login_success.toString());
                            RegisterBean registerBean = baseBean.getData();
                            registerBean.getUser().setMobile(user.getMobile().replaceAll(" ", ""));
                            registerBean.getUser().setPwd(user.getPwd());
                            UserService.getInstance().setUser(registerBean.getUser());
                            UserService.getInstance().putSPUser(registerBean.getUser());
                            UserService.getInstance().setVideo_first_order(registerBean.getVideo_first_order());
//                            loginService.doLoginAgora(registerBean.getVideo_user(), registerBean.getIm_user());
                            loginService.doLoginAgora(baseBean.getData().getVideo_user(), baseBean.getData().getIm_user());
                            if (baseBean.getData().getRun_service_tip() != null && !StringUtils.isEmpty(baseBean.getData().getRun_service_tip().getUser_service_id())) {
                                serverService.doRuningService(WelcomeActivity.this, baseBean.getData().getRun_service_tip());
                            } else {

                                if (registerBean.getUser().getRole() == MValue.USER_ROLE_SERVICER && registerBean.getUser().getService_auth_status() != null && registerBean.getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {
                                    if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                                        IntentUtils.toIndexActivity(WelcomeActivity.this);
                                    } else {
                                        IntentUtils.toMainActivity(WelcomeActivity.this);
                                    }
                                } else {
                                    if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                                        IntentUtils.toIndexActivity(WelcomeActivity.this);
                                    } else {
                                        IntentUtils.toMainActivity(WelcomeActivity.this);
                                    }
                                }
                            }

                            //更新改到配置文件那一步了
//                            if (registerBean.getApp_version_update() != null && !StringUtils.isEmpty(registerBean.getApp_version_update().getContent())) {
//                                MValue.updateVersionBean = registerBean.getApp_version_update();
//                            }


                            finish();

                        }

                        @Override
                        public void onError(BaseBean<RegisterBean> baseBean) {
                            IntentUtils.toSplashActivity(WelcomeActivity.this);
                            finish();
                        }
                    });


                } else {
                    toSplash();
                    finish();
                }

            }
        });


    }


    private void checkConfigData() {
        String v = PreferencesUtils.getString(this, MKey.V);
        params = SignUtils.getNormalParams();
        params.put(MKey.CV, v == null ? "" : "");
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);


        HttpClient.Builder.getGuodongServer().check_config(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<VConfigBean>() {

            @Override
            public void onSuccess(BaseBean<VConfigBean> baseBean) {

                if (timeoutDialog != null && !timeoutDialog.isShowing()) {
                    timeoutDialog.dismiss();
                }

                VConfigBean vConfigBean = baseBean.getData();

                if (vConfigBean.getApp_version_update() != null && !StringUtils.isEmpty(vConfigBean.getApp_version_update().getContent())) {
                    MValue.updateVersionBean = vConfigBean.getApp_version_update();
                    UpdateVersionService.checkVersionUpdate(WelcomeActivity.this, new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            if (v != null && v.equals(vConfigBean.getV_config().getV()) && PreferencesUtils.getObj(WelcomeActivity.this, MKey.CONFIGDATA) != null) {
                                ConfigBean configBean = (ConfigBean) PreferencesUtils.getObj(WelcomeActivity.this, MKey.CONFIGDATA);

                                // TODO: 2018/5/31  更改set config 方法
                                MAPP.mapp.setConfigBean(configBean);
//                    MValue.configBean = configBean;
                                autoLogin();
                            } else {
                                PreferencesUtils.putString(WelcomeActivity.this, MKey.V, vConfigBean.getV_config().getV());
                                HttpRequest request = new HttpRequest(vConfigBean.getV_config().getConfig_url());
                                HttpUtils.getInstance().execute(request, new HttpResponse() {

                                    @Override
                                    public void onResponse(String result) {
                                        ConfigBean configBean = JSON.parseObject(result, ConfigBean.class);
                                        PreferencesUtils.saveObj(WelcomeActivity.this, MKey.CONFIGDATA, configBean);
                                        // TODO: 2018/5/31  更改set config 方法
                                        MAPP.mapp.setConfigBean(configBean);
//                            MValue.configBean = configBean;
                                        autoLogin();
                                    }

                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        CrashReport.postCatchedException(new Throwable("checkConfigData execute 出错" + e.getMessage()));
                                    }
                                });

                            }
                        }
                    });
                    return;
                }

                if (v != null && v.equals(vConfigBean.getV_config().getV()) && PreferencesUtils.getObj(WelcomeActivity.this, MKey.CONFIGDATA) != null) {
                    ConfigBean configBean = (ConfigBean) PreferencesUtils.getObj(WelcomeActivity.this, MKey.CONFIGDATA);

                    // TODO: 2018/5/31  更改set config 方法
                    MAPP.mapp.setConfigBean(configBean);
//                    MValue.configBean = configBean;
                    autoLogin();
                } else {
                    PreferencesUtils.putString(WelcomeActivity.this, MKey.V, vConfigBean.getV_config().getV());
                    HttpRequest request = new HttpRequest(vConfigBean.getV_config().getConfig_url());
                    HttpUtils.getInstance().execute(request, new HttpResponse() {

                        @Override
                        public void onResponse(String result) {
                            ConfigBean configBean = JSON.parseObject(result, ConfigBean.class);
                            PreferencesUtils.saveObj(WelcomeActivity.this, MKey.CONFIGDATA, configBean);
                            // TODO: 2018/5/31  更改set config 方法
                            MAPP.mapp.setConfigBean(configBean);
//                            MValue.configBean = configBean;
                            autoLogin();
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            CrashReport.postCatchedException(new Throwable("checkConfigData execute 出错" + e.getMessage()));
                        }
                    });

                }


            }

            @Override
            public void onError(BaseBean<VConfigBean> basebean) {

                if (timeoutDialog != null && !timeoutDialog.isShowing()) {
                    timeoutDialog.show();
                }

                CrashReport.postCatchedException(new Throwable("checkConfigData 出错" + basebean.getMessage()));
            }

        });


//        HttpClient.Builder.getGuodongServer().check_config(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<VConfigBean>() {
//            @Override
//            public void onSubscribe(Disposable disposable) {
//                addDisposable(disposable);
//            }
//
//            @Override
//            public void onSuccess(VConfigBean baseBean) {
//
//                VConfigBean vConfigBean = baseBean;
//
//                if (v != null && v.equals(vConfigBean.getV_config().getV()) && PreferencesUtils.getObj(WelcomeActivity.this, MKey.CONFIGDATA) != null) {
//                    ConfigBean configBean = (ConfigBean) PreferencesUtils.getObj(WelcomeActivity.this, MKey.CONFIGDATA);
//                    // TODO: 2018/5/31  更改set config 方法
//                    MAPP.mapp.setConfigBean(configBean);
////                    MValue.configBean = configBean;
//                    autoLogin();
//                } else {
//                    PreferencesUtils.putString(WelcomeActivity.this, MKey.V, vConfigBean.getV_config().getV());
//                    HttpRequest request = new HttpRequest(vConfigBean.getV_config().getConfig_url());
//                    HttpUtils.getInstance().execute(request, new HttpResponse() {
//
//                        @Override
//                        public void onResponse(String result) {
//                            ConfigBean configBean = JSON.parseObject(result, ConfigBean.class);
//                            PreferencesUtils.saveObj(WelcomeActivity.this, MKey.CONFIGDATA, configBean);
//                            // TODO: 2018/5/31  更改set config 方法
//                            MAPP.mapp.setConfigBean(configBean);
////                            MValue.configBean = configBean;
//                            autoLogin();
//                        }
//
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                        }
//                    });
//
//                }
//
//
//            }
//
//            @Override
//            public void onError(BaseBean<VConfigBean> basebean) {
//
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//            }
//
//
//        }));
    }

    private void toSplash() {
        Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }


    //某些权限已被授予
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        if (tipCustomOneDialog != null && tipCustomOneDialog.isShowing()) {
            tipCustomOneDialog.dismiss();
        }

        initStartpageData();
    }

    private TipCustomOnePermissionDialog tipCustomOneDialog;

    //某些权限已被拒绝
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            permissionUtils.requestStorgePermission(this);

        }


        if (requestCode == 4002) {
            if (tipCustomOneDialog == null || !tipCustomOneDialog.isShowing()) {
                tipCustomOneDialog = new TipCustomOnePermissionDialog.Builder(this, getString(R.string.permission_tip_storage), getString(R.string.goto_setting), new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);

                        // Start for result
                        startActivityForResult(intent, MCode.REQUEST_CODE.GET_PERMISSION_LOC);
                    }
                }).create();
                tipCustomOneDialog.show();
            }
        }


//
    }

}
