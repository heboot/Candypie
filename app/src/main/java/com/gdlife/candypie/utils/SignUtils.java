package com.gdlife.candypie.utils;

/**
 * Created by heboot on 2018/2/7.
 */

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.serivce.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SignUtils {
//    private static Map<String, Object> params;

    /**
     * 生成请求需要的sign串
     * 所有的请求参数按key升序排列，
     * k1=v1&k2=v2&.....
     * 用MD5加密生成加密串sign把sign=sign加在请求的参数后面发到服务器端校验.
     *
     * @param params
     * @return
     */
    public static String doSign(Map<String, Object> params) {
//        if (StringUtils.isEmpty(UserService.getInstance().getToken())) {
//            return "";
//        }
        if (StringUtils.isEmpty(UserService.getInstance().getSign_key())) {
            return "";
        }
        String sign = "";
        Collection<String> keyset = params.keySet();
        List<String> list = new ArrayList<String>(keyset);
        //对key键值按字典升序排序
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
//            if (params.get(list.get(i)) != null && !StringUtils.isEmpty(params.get(list.get(i)).toString())) {
            sign += list.get(i) + "=" + params.get(list.get(i)) + "&";
//            }

        }
        sign += UserService.getInstance().getSign_key();
        return MD5.getMessageDigest(sign);
    }

    public static String getParams(Map<String, Object> params) {
        if (StringUtils.isEmpty(UserService.getInstance().getToken())) {
            return "";
        }
        String sign = "";
        Collection<String> keyset = params.keySet();
        List<String> list = new ArrayList<String>(keyset);
        //对key键值按字典升序排序
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            sign += list.get(i) + "=" + params.get(list.get(i)) + "&";
        }
        sign += UserService.getInstance().getSign_key();
        return sign;
    }


    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

//    public static String doPaySign(Map<String, Object> params) {
//        if (StringUtils.isEmpty(UserService.getInstance().getToken())) {
//            return null;
//        }
//        String sign = "";
//        Collection<String> keyset = params.keySet();
//        List<String> list = new ArrayList<String>(keyset);
//        //对key键值按字典升序排序
//        Collections.sort(list);
//        for (int i = 0; i < list.size(); i++) {
//            if (i == list.size() - 1) {
//                sign += list.get(i) + "=" + params.get(list.get(i));
//            } else {
//                sign += list.get(i) + "=" + params.get(list.get(i)) + "&";
//            }
//
//        }
//
//
//        LogUtil.e("doPaySign", sign);
//        String sign2 = sign + "&key=said84sj90e950joirof0934jOIDkdsj";
//        sign = MD5.getMessageDigest(sign).toUpperCase();
//        sign2 = MD5.getMessageDigest(sign2).toUpperCase();
//        LogUtil.e("doPaySign2>>", sign2);
//        return sign;
//    }
    //g=Web&c=Mock&o=simple&projectID=5&uri=app/

    public static Map<String, Object> getNormalParams() {
        Map<String, Object> params = new HashMap<>();
//        if (params == null) {
//            params = new HashMap<>();
//        } else {
//            params.clear();
//        }
        params.put(MKey.ORIGIN, "app_android");

//
        try {
            params.put(MKey.DEVICE_INFO, com.gdlife.candypie.utils.MiscUtils.getIMEI(MAPP.mapp));
        } catch (Exception e) {

        }
        if (!StringUtils.isEmpty(UserService.getInstance().getToken())) {
            params.put(MKey.TOKEN, UserService.getInstance().getToken());
        }

        if (MValue.currentLocation != null) {
            params.put(MKey.LAT, MValue.currentLocation.getLatitude());
            params.put(MKey.LNG, MValue.currentLocation.getLongitude());
        }


        params.put(MKey.VERSION, com.gdlife.candypie.utils.MiscUtils.getAppVersion(MAPP.mapp));


        ApplicationInfo info = null;
        try {
            info = MAPP.mapp.getPackageManager()
                    .getApplicationInfo(MAPP.mapp.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
        }
        String msg = null;
        if (info != null) {
            msg = info.metaData.getString("UMENG_CHANNEL");
        }


        params.put(MKey.TO_CHANNEL, StringUtils.isEmpty(msg) ? "guodong" : msg);

        params.put(MKey.LAN, com.gdlife.candypie.utils.MiscUtils.getLanguage());


        return params;
    }

}
