package com.heboot.utils;

import android.util.Log;

/**
 * Created by heboot on 2018/1/17.
 */

public class LogUtil {

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }

    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

}
