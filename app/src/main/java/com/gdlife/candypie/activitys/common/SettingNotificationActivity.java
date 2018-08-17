package com.gdlife.candypie.activitys.common;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;

import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MCode;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.SettingNotificationType;
import com.gdlife.candypie.databinding.ActivitySettingNotificationBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.login2register.RegisterBean;
import com.heboot.utils.MStatusBarUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.suke.widget.SwitchButton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SettingNotificationActivity extends BaseActivity<ActivitySettingNotificationBinding> {

    private boolean noti;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_notification;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setWhiteBack(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.msg_notification_tip));
    }

    @Override
    public void initData() {

        checkNotificationPermission();

        if (UserService.getInstance().getUser().getSound() == 1) {
            binding.sbNotificationVoice.setChecked(true);
        } else {
            binding.sbNotificationVoice.setChecked(false);
        }

        if (UserService.getInstance().getUser().getDisplay_content() == 1) {
            binding.sbNotificationDetai.setChecked(true);
        } else {
            binding.sbNotificationDetai.setChecked(false);
        }


    }


    private void doSetting(String type, int value) {params = SignUtils.getNormalParams();
        params.put(MKey.TYPE, type);
        params.put(MKey.STATUS, value);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().setting(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<RegisterBean>() {
            @Override
            public void onSuccess(BaseBean<RegisterBean> baseBean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        UserService.getInstance().setUser(baseBean.getData().getUser());

                    }
                });
            }

            @Override
            public void onError(BaseBean<RegisterBean> baseBean) {
            }
        });
    }


    private void checkNotificationPermission() {
        //检查通知权限
        noti = NotificationManagerCompat.from(this).areNotificationsEnabled();// NotificationsUtils.isNotificationEnabled(this);
        if (noti) {
            NIMClient.getService(MixPushService.class).enable(true);
            NIMClient.toggleNotification(true);
            binding.sbNoname.setChecked(true);
            binding.sbNotificationDetai.setEnabled(true);
            binding.sbNotificationVoice.setEnabled(true);
        } else {
            binding.sbNoname.setChecked(false);

            binding.sbNotificationDetai.setChecked(false);
            binding.sbNotificationVoice.setChecked(false);

            binding.sbNotificationDetai.setEnabled(false);
            binding.sbNotificationVoice.setEnabled(false);
        }

    }

    @Override
    public void initListener() {

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        binding.sbNoname.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    noti = NotificationManagerCompat.from(SettingNotificationActivity.this).areNotificationsEnabled();// NotificationsUtils.isNotificationEnabled(this);
                    if (noti) {
                        NIMClient.toggleNotification(true);
                        binding.sbNotificationDetai.setEnabled(true);
                        binding.sbNotificationVoice.setEnabled(true);
                        NIMClient.getService(MixPushService.class).enable(true);
                        doSetting(SettingNotificationType.enable_notice.toString(), 1);
                    } else {
                        NIMClient.toggleNotification(false);
                        NIMClient.getService(MixPushService.class).enable(false);
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, MCode.REQUEST_CODE.GET_PERMISSION_LOC);
                    }


                } else {
                    NIMClient.toggleNotification(false);
                    NIMClient.getService(MixPushService.class).enable(false);
                    doSetting(SettingNotificationType.enable_notice.toString(), 0);
                    binding.sbNotificationDetai.setChecked(false);
                    binding.sbNotificationVoice.setChecked(false);
                    binding.sbNotificationDetai.setEnabled(false);
                    binding.sbNotificationVoice.setEnabled(false);
                }
            }
        });


        binding.sbNotificationVoice.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    doSetting(SettingNotificationType.sound.toString(), 1);
                } else {
                    doSetting(SettingNotificationType.sound.toString(), 0);
                }
            }
        });


        binding.sbNotificationDetai.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    doSetting(SettingNotificationType.display_content.toString(), 1);
                } else {
                    doSetting(SettingNotificationType.display_content.toString(), 0);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNotificationPermission();
    }
}
