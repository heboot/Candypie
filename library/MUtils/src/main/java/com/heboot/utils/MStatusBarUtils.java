package com.heboot.utils;

import android.app.Activity;

import com.qmuiteam.qmui.util.QMUIDeviceHelper;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

/**
 * Created by heboot on 2018/1/31.
 */

public class MStatusBarUtils {

    public static void setActivityLightMode(Activity act) {
        if (QMUIDeviceHelper.isFlyme() || MOSUtils.isFlyme()) {
            QMUIStatusBarHelper.FlymeSetStatusBarLightMode(act.getWindow(), true);
        } else if (QMUIDeviceHelper.isMIUI() || MOSUtils.isMIUI()) {
            QMUIStatusBarHelper.MIUISetStatusBarLightMode(act.getWindow(), true);
            QMUIStatusBarHelper.setStatusBarLightMode(act);
        } else {
            QMUIStatusBarHelper.setStatusBarLightMode(act);
        }

    }

    public static void setActivityNOLightMode(Activity act) {
        if (QMUIDeviceHelper.isFlyme() || MOSUtils.isFlyme()) {
            QMUIStatusBarHelper.setStatusBarDarkMode(act);
        } else if (QMUIDeviceHelper.isMIUI() || MOSUtils.isMIUI()) {
            QMUIStatusBarHelper.setStatusBarDarkMode(act);
        } else {
            QMUIStatusBarHelper.setStatusBarDarkMode(act);
        }

    }



}
