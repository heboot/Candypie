package com.gdlife.candypie.serivce;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.coloros.mcssdk.PushManager;
import com.coloros.mcssdk.callback.PushAdapter;
import com.coloros.mcssdk.callback.PushCallback;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.user.UserBlackListActivity;
import com.gdlife.candypie.adapter.user.UserBlackAdapter;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.user.UserFavsListBean;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MOSUtils;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.lang.ref.WeakReference;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        if (UserService.getInstance().getUser() != null && !StringUtils.isEmpty(UserService.getInstance().getToken())) {
            if (MzSystemUtils.isBrandMeizu()) {
                com.meizu.cloud.pushsdk.PushManager.register(MAPP.mapp.getCurrentActivity(), "1000031", "29fc2e1946014c8d99eafe44fc89db7b");
            } else if (MOSUtils.isEMUI()) {
                initHuawei(MAPP.mapp.getCurrentActivity());
            } else if (PushManager.isSupportPush(MAPP.mapp)) {
                ToastUtils.showToast("init oppo");
                initOPPO();
            } else if (!MOSUtils.isMIUI()) {
                PushAgent mPushAgent = PushAgent.getInstance(MAPP.mapp);
                //注册推送服务，每次调用register方法都会回调该接口
                mPushAgent.register(new IUmengRegisterCallback() {
                    @Override
                    public void onSuccess(String deviceToken) {
                        //注册成功会返回device token
                        uploadPushToken(deviceToken);
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                    }
                });
            }
        }
    }

    private void uploadPushToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return;
        }
        Map<String, Object> params = SignUtils.getNormalParams();
        params.put(MKey.PUSH_TOKEN, token);
        HttpClient.Builder.getGuodongServer().update_push_token(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {

            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {

            }
        });
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
            PushManager.getInstance().register(MAPP.mapp, "74D3ab2mN400wc8KSCk4O08OC", "4E54C7895aE010Ca76DeFDEeaa736877", new PushAdapter() {

                @Override
                public void onRegister(int i, String s) {
                    super.onRegister(i, s);
                    ToastUtils.showToast("init oppo " + s);
                    uploadPushToken(s);
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
