package com.gdlife.candypie.serivce;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.heboot.utils.LogUtil;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;

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

    public void initHuawei(Activity context) {


        huaweiApiClient = new HuaweiApiClient.Builder(context).addApi(HuaweiPush.PUSH_API).addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .build();
        huaweiApiClient.connect(context);


        getTokenAsyn();
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
