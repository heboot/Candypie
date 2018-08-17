package com.gdlife.candypie.utils;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.gdlife.candypie.MAPP;
import com.heboot.utils.LogUtil;

/**
 * Created by Heboot on 2017/6/28.
 */

public class ToastUtils {

    private static Toast toast = null; //Toast的对象！

    public static void showToast(Context context, String id) {


        LogUtil.e("context测试", context.getClass().getSimpleName() + ">>>>" + MAPP.mapp.getClass().getSimpleName());

        if (toast == null) {//weakReference.get() == null ? mContext : weakReference.get()
            toast = Toast.makeText(context, id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    }

    public static void showToast(String id) {
        if (toast == null) {//weakReference.get() == null ? mContext : weakReference.get()
            toast = Toast.makeText(MAPP.mapp, id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    }

    public static void showLoadMoreToast(String id) {
        if (toast == null) {//weakReference.get() == null ? mContext : weakReference.get()
            toast = Toast.makeText(MAPP.mapp, id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    }


}

