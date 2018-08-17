package com.heboot.utils;

import android.content.Context;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;

public class DisplayUtils {

    public static double change(double a){
        return a * Math.PI  / 180;
    }

    public static int dip2px(Context context, int dpValue){
        return QMUIDisplayHelper.dp2px(context,dpValue);
    }

}
