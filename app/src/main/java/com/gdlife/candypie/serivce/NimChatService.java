package com.gdlife.candypie.serivce;

import android.content.Context;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.im.ImChatStatusBean;
import com.heboot.bean.message.SystemNotification;
import com.heboot.bean.message.SystemNotificationValueBean;
import com.heboot.entity.User;
import com.heboot.event.IMEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Heboot on 2017/8/18.
 */

public class NimChatService {

    private static NimChatService nimChatService;

    private QMUITipDialog qmuiTipDialog;

    public static NimChatService getInstance() {
        if (nimChatService == null) {
            nimChatService = new NimChatService();
        }
        return nimChatService;
    }


    /**
     * 登录云信
     *
     * @param loginInfo
     */
    public void doLogin(LoginInfo loginInfo) {

        NimUIKit.login(loginInfo, new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
            }

            @Override
            public void onFailed(int i) {
                CrashReport.postCatchedException(new Throwable("云信登录出现异常 onFailed" + i));
            }

            @Override
            public void onException(Throwable throwable) {
                CrashReport.postCatchedException(new Throwable("云信登录出现异常 onException"));
                CrashReport.postCatchedException(throwable);
            }
        });
    }

    public void toChat(Context context, User user) {
        RxBus.getInstance().post(new IMEvent.QUERY_IM_STAUTS_EVENT(context, String.valueOf(user.getId())));
    }


    /**
     * 发送摄像头开启和关闭事件
     *
     * @param enable
     * @param toID
     */
    public void updateUserCameraStatus(boolean enable, int toID, String userServiceId) {
        CustomNotification notification = new CustomNotification();
        SystemNotification systemNotification = new SystemNotification();
        SystemNotificationValueBean valueBean = new SystemNotificationValueBean();
        valueBean.setCamera_status(enable ? 1 : 0);
        valueBean.setUser_service_id(userServiceId);
        systemNotification.setType("update_camera_status");
        systemNotification.setValue(valueBean);
        notification.setContent(JSON.toJSONString(systemNotification));
        notification.setSessionId(MValue.CHAT_PRIEX + toID);
        notification.setSessionType(SessionTypeEnum.P2P);
        // 发送自定义通知
        NIMClient.getService(MsgService.class).sendCustomNotification(notification);


    }


    public void doLogout() {
        NimUIKit.logout();
    }


}
