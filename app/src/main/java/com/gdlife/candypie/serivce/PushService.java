package com.gdlife.candypie.serivce;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.coloros.mcssdk.PushManager;
import com.coloros.mcssdk.callback.PushAdapter;
import com.coloros.mcssdk.callback.PushCallback;
import com.gdlife.candypie.MAPP;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MOSUtils;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.tencent.bugly.crashreport.CrashReport;

public class PushService {

    private HuaweiApiClient huaweiApiClient;

    private HuaweiApiClient.ConnectionCallbacks callbacks = new HuaweiApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected() {
            LogUtil.e("PushService", "onConnected");
            getTokenAsyn();
        }

        @Override
        public void onConnectionSuspended(int cause) {
            LogUtil.e("PushService", "onConnectionSuspended");
        }
    };

    private HuaweiApiClient.OnConnectionFailedListener onConnectionFailedListener = new HuaweiApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            LogUtil.e("PushService", "onConnectionFailed");
        }
    };

    public void initPushService() {
        if (MzSystemUtils.isBrandMeizu()) {
            com.meizu.cloud.pushsdk.PushManager.register(MAPP.mapp.getCurrentActivity(), "1000031", "29fc2e1946014c8d99eafe44fc89db7b");
        } else if (MOSUtils.isEMUI()) {
            initHuawei(MAPP.mapp.getCurrentActivity());
        } else if (PushManager.isSupportPush(MAPP.mapp)) {
            initOPPO();
        }
    }

    public void initHuawei(Activity context) {

        huaweiApiClient = new HuaweiApiClient.Builder(context).addApi(HuaweiPush.PUSH_API).addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .build();
        huaweiApiClient.connect(context);

        getTokenAsyn();
    }

    public void initOPPO() {
        //由于不是所有平台都支持OPPO PUSH，提供接口PushManager.isSupportPush(Context)方便应用判断是否支持，支持才能继续后续操作，否则后续操作会抛出异常。
        if (PushManager.isSupportPush(MAPP.mapp)) {
            PushManager.getInstance().register(MAPP.mapp, "appkey", " a  ppSecret", new PushAdapter() {

                @Override
                public void onRegister(int i, String s) {
                    super.onRegister(i, s);
                }
            });
        }
    }


    private void getTokenAsyn() {
        if (!huaweiApiClient.isConnected()) {
            LogUtil.e("mapp", "获取TOKEN失败，原因：HuaweiApiClient未连接");
            return;
        }

        LogUtil.e("mapp", "异步接口获取PUSH TOKEN");
        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(huaweiApiClient);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {

            @Override
            public void onResult(TokenResult result) {
                LogUtil.e("mapp", "异步接口获取PUSH TOKEN > " + JSON.toJSONString(result));
            }
        });
    }
}
