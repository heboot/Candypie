package com.gdlife.candypie.utils;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.common.CustomEvent;
import com.umeng.analytics.MobclickAgent;

public class CustomEventUtil {

    public static void onEvent(CustomEvent customEvent) {
        MobclickAgent.onEvent(MAPP.mapp, customEvent.toString());
    }

}
