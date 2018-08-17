package com.gdlife.candypie.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MCode;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.widget.common.PermissionDialog;
import com.heboot.dialog.TipCustomOneDialog;
import com.luck.picture.lib.tools.RomUtils;
import com.yalantis.dialog.TipCustomDialog;

import java.lang.reflect.Method;

import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by heboot on 2018/1/18.
 */

public class PermissionUtils {

    private String TAG = "PermissionUtils";

    private String[] perms = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };


    private String[] storagePerms = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private String[] locPerms = {
//            Manifest.permission.LOCATION_HARDWARE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };


    /**
     * 检查是否拥有存储权限
     * 增加读取手机状态权限 为了获得IMEI
     *
     * @return
     */
    public boolean hasStoragePermission() {
        if (EasyPermissions.hasPermissions(MAPP.mapp, storagePerms)) {
            return true;
        }
        return false;
    }

//    public boolean hasPopupWindowPermission() {
////        if (EasyPermissions.hasPermissions(MAPP.mapp, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
////            return true;
////        }
////        return false;
//
//        return checkFloatWindowPermission();
//
//    }

    public void requestPopupWindowPermission(Activity activity) {

        TipCustomOneDialog tipCustomDialog = new TipCustomOneDialog.Builder(activity, "建议打开悬浮窗口权限，获得更好的视频体验", "去打开", new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivityForResult(intent, MCode.REQUEST_CODE.GET_PERMISSION_ALERTWINDOW);
                } catch (Exception e) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    activity.startActivityForResult(intent, MCode.REQUEST_CODE.GET_PERMISSION_ALERTWINDOW);
                }

            }
        }).create();
        tipCustomDialog.show();


//        EasyPermissions.requestPermissions(activity, "建议打开悬浮窗口权限，获得更好的视频体验",
//                MCode.REQUEST_CODE.GET_PERMISSION_ALERTWINDOW, Manifest.permission.SYSTEM_ALERT_WINDOW);
    }

    /**
     * 请求存储权限
     *
     * @param activity
     */
    public void requestStorgePermission(Activity activity) {
        EasyPermissions.requestPermissions(activity, "我们需要您的存储/拨打电话权限才能正常运行",
                MCode.REQUEST_CODE.GET_PERMISSION_STORAGE, storagePerms);
    }

    public boolean hasLocPermission() {
        if (EasyPermissions.hasPermissions(MAPP.mapp, locPerms)) {
            return true;
        }
        return false;
    }


    public void requestLocationPermission(Activity activity) {
        EasyPermissions.requestPermissions(activity, "我们需要获取您的位置",
                MCode.REQUEST_CODE.GET_PERMISSION_LOC, locPerms);
    }

    public void showPermissionDialog(Activity context, Consumer<String> consumer) {

        boolean loc = hasLocPermission();

        boolean noti = NotificationManagerCompat.from(context).areNotificationsEnabled();// NotificationsUtils.isNotificationEnabled(context);

        if (!loc || !noti) {
            PermissionDialog permissionDialog = new PermissionDialog.Builder(context, new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    if (integer == 1) {
                        if (!loc && !noti) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent.setData(uri);
                            context.startActivityForResult(intent, MCode.REQUEST_CODE.GET_PERMISSION_LOC);
                        } else if (!loc) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent.setData(uri);
                            context.startActivityForResult(intent, MCode.REQUEST_CODE.GET_PERMISSION_LOC);
                        } else if (!noti) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent.setData(uri);
                            context.startActivityForResult(intent, MCode.REQUEST_CODE.GET_PERMISSION_LOC);
                        }
                    }
                }
            }, loc, noti).create();
            if (consumer != null) {
                permissionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        try {
                            consumer.accept("permission");
                        } catch (Exception e) {
                        }
                    }
                });
            }
            if (!noti && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status().intValue() == MValue.AUTH_STATUS_SUC) {
                permissionDialog.setCancelable(false);
                permissionDialog.setCanceledOnTouchOutside(false);
            }
            permissionDialog.show();
        } else if (loc && noti) {
            if (consumer != null) {
                try {
                    consumer.accept("permission");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 检查是否开启通知
     *
     * @return
     */
    public boolean hasNoticicationPermission() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MAPP.mapp);
        boolean isOpened = managerCompat.areNotificationsEnabled();
        return isOpened;
    }

    public boolean hasCameraPermission(Application application) {
        if (EasyPermissions.hasPermissions(application, perms)) {
            return true;
        }
        return false;
    }


    public void getCameraPermission(Activity activity) {
        EasyPermissions.requestPermissions(activity, "拍照需要摄像头权限",
                MCode.REQUEST_CODE.GET_PERMISSION_CAMERA, perms);
    }


    public boolean checkFloatWindowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(MAPP.mapp);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //AppOpsManager添加于API 19
            return checkOps();
        } else {
            //4.4以下一般都可以直接添加悬浮窗
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkOps() {
        try {
            Object object = MAPP.mapp.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = MAPP.mapp.getPackageName();
            int m = (Integer) method.invoke(object, arrayOfObject1);
            //4.4至6.0之间的非国产手机，例如samsung，sony一般都可以直接添加悬浮窗
            return m == AppOpsManager.MODE_ALLOWED;//|| !RomUtils.isDomesticSpecialRom()
        } catch (Exception ignore) {
        }
        return false;
    }

}
