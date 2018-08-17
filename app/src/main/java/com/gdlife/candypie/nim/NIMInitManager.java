package com.gdlife.candypie.nim;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.BroadcastMessage;

/**
 * Created by hzchenkang on 2017/9/26.
 * 用于初始化时，注册全局的广播、云信观察者等等云信相关业务
 */

public class NIMInitManager {

    private static final String TAG = "NIMInitManager";

    private NIMInitManager() {
    }

    private static class InstanceHolder {
        static NIMInitManager receivers = new NIMInitManager();
    }

    public static NIMInitManager getInstance() {
        return InstanceHolder.receivers;
    }

    public void init(boolean register) {


        // 注册全局云信sdk 观察者
        registerGlobalObservers(register);

        // 初始化在线状态事件
//        OnlineStateEventManager.init();
    }

    private void registerGlobalObservers(boolean register) {
        // 注册白板会话
//        registerRTSIncomingObserver(register);

        // 注册云信全员广播
        registerBroadcastMessages(register);
    }

    /**
     * 注册白板来电观察者
     *
     * @param register
     */
//    private void registerRTSIncomingObserver(boolean register) {
//        RTSManager.getInstance().observeIncomingSession(new Observer<RTSData>() {
//            @Override
//            public void onEvent(RTSData rtsData) {
//                RTSActivity.incomingSession(DemoCache.getContext(), rtsData, RTSActivity.FROM_BROADCAST_RECEIVER);
//            }
//        }, register);
//    }


    /**
     * 注册云信全服广播接收器
     *
     * @param register
     */
    private void registerBroadcastMessages(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeBroadcastMessage(new Observer<BroadcastMessage>() {
            @Override
            public void onEvent(BroadcastMessage broadcastMessage) {
            }
        }, register);
    }

}
