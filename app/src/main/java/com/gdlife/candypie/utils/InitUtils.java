package com.gdlife.candypie.utils;

import android.app.Application;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.gdlife.candypie.BuildConfig;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.common.CustomCrashActivity;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.nim.NIMInitManager;
import com.gdlife.candypie.nim.NimSDKOptionConfig;
import com.gdlife.candypie.serivce.ConfigService;
import com.gdlife.candypie.serivce.MessageService;
import com.gdlife.candypie.serivce.UserService;
import com.heboot.bean.message.SystemNotification;
import com.heboot.entity.User;
import com.heboot.event.MessageEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.api.model.contact.ContactEventListener;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nim.uikit.custom.CustomAttachParser;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/1/18.
 */

public class InitUtils {


    public void registerActivityLifecycleCallbacks(final MAPP mapp) {
        mapp.activityLinkedList = new LinkedList<>();
    }


    public void initSDK(Application application, MessageService messageService) {
//        initLeakCanary(application);
//        initQuPai();
        initBugly(application);
        initNIM(application, messageService);
        initCrash();
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(true) //default: true
                .showErrorDetails(true) //default: true
                .showRestartButton(true) //default: true
                .logErrorOnRestart(true) //default: true
                .trackActivities(true) //default: false
                .minTimeBetweenCrashesMs(2000) //default: 3000
                .errorDrawable(R.drawable.icon_order_null) //default: bug image
//                .restartActivity(CustomCrashActivity.class) //default: null (your app's launch activity)
                .errorActivity(CustomCrashActivity.class) //default: null (default error activity)
//                .eventListener(new YourCustomEventListener()) //default: null
                .apply();
    }

    public void initNIM(Application application, MessageService messageService) {
        NIMClient.init(application, UserService.getInstance().getLoginInfo(), NimSDKOptionConfig.getSDKOptions(MAPP.mapp));
        if (NIMUtil.isMainProcess(application)) {


            // 注册自定义推送消息处理，这个是可选项
//            NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());

            // init pinyin
            PinYin.init(application);
            PinYin.validate();
            // 初始化UIKit模块
            initUIKit(application, messageService);
            // 初始化消息提醒
            NIMClient.toggleNotification(false);
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true);
        }


    }

    private void initUIKit(Application application, MessageService messageService) {
        // 初始化
        NimUIKit.init(application, buildUIKitOptions());

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。

//        // IM 会话窗口的定制初始化。
//        SessionHelper.init();
//
//        // 聊天室聊天窗口的定制初始化。
//        ChatRoomSessionHelper.init();
//
//        // 通讯录列表定制初始化
//        ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
//        NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());

//        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());


        NimUIKit.setContactEventListener(new ContactEventListener() {
            @Override
            public void onItemClick(Context context, String account) {
            }

            @Override
            public void onItemLongClick(Context context, String account) {
            }

            @Override
            public void onAvatarClick(Context context, String account) {
            }
        });
        NimUIKit.setSessionListener(new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                if (message.getDirect() == MsgDirectionEnum.In) {

                    NimUserInfo user2 = NIMClient.getService(com.netease.nimlib.sdk.uinfo.UserService.class).getUserInfo(message.getFromAccount());

                    if (ConfigService.getInstance().isKF(user2.getAccount())) {
                        return;
                    }

                    Map<String, String> map = JSON.parseObject(user2.getExtension(), Map.class);

                    String role = "2";
                    if (map.get("role") != null) {
                        role = String.valueOf(map.get("role"));
                    }


                    User user = new User();

                    user.setId(Integer.parseInt(user2.getAccount().replace(MValue.CHAT_PRIEX, "")));


                    if (Integer.parseInt(role) == MValue.USER_ROLE_NORMAL) {
                        IntentUtils.toUserInfoActivity(MAPP.mapp, MValue.USER_INFO_TYPE_NORMAL, MValue.USER_INFO_TYPE_NORMAL, user, null, null);
                    } else if (Integer.parseInt(role) == MValue.USER_ROLE_SERVICER) {
                        IntentUtils.toHomepageActivity(MAPP.mapp, MValue.FROM_OTHER, user, MValue.HOMEPAG_FROM.ONE_ONE, null);
                    }

//                    IntentUtils.intent2UserHomepageActivityByString(GuoDongAPP.getInstance(), message.getFromAccount().replace("yhc", ""));
                } else if (message.getDirect() == MsgDirectionEnum.Out) {
                    if (UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {
                        IntentUtils.toHomepageActivity(MAPP.mapp, MValue.FROM_MY, UserService.getInstance().getUser(), null, null);
                    } else {
                        IntentUtils.toUserInfoActivity(MAPP.mapp, MValue.USER_INFO_TYPE_NORMAL, MValue.USER_INFO_TYPE_NORMAL, UserService.getInstance().getUser(), null, null);
                    }
                }
            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {

            }
        });

        initOtherLogin();

        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getSound() == 1) {
                    AudioUtil.playSound(R.raw.new_message, 1, 0);
//                    AudioUtil2.getInstance(MAPP.mapp).playNewMsgSound();
                }
                RxBus.getInstance().post(MessageEvent.REFRESH_UNREAD_NUM_ENENT);
            }
        }, true);


        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(new Observer<CustomNotification>() {
            @Override
            public void onEvent(CustomNotification customNotification) {


                try {
                    SystemNotification systemNotification = JSON.parseObject(customNotification.getContent(), SystemNotification.class);
                    messageService.doSystemNotificationAction(systemNotification);
                    RxBus.getInstance().post(MessageEvent.REFRESH_UNREAD_NUM_ENENT);
                } catch (Exception e) {
                    LogUtil.e("云信新通知来了", "new CustomNotification onEvent " + JSON.toJSONString(e));
                }


                LogUtil.e("云信新通知来了", customNotification.isSendToOnlineUserOnly() + "new CustomNotification onEvent " + JSON.toJSONString(customNotification));
            }
        }, true);

        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());

    }

    private void initOtherLogin() {
//        Observer<List<OnlineClient>> clientsObserver = new Observer<List<OnlineClient>>() {
//            @Override
//            public void onEvent(List<OnlineClient> onlineClients) {
//                if (onlineClients == null || onlineClients.size() == 0) {
//                    return;
//                }
////                NimUIKit.logout();
////                RxBus.getInstance().post(UserEvent.OTHER_LOGIN);
////                EventBus.getDefault().post(UserEvent.OTHER_LOGIN);
////                RxBus.getInstance().post(UserEvent.OTHER_LOGIN);
////                EventBus.getDefault().post(UserEvent.OTHER_LOGIN);
//            }
//        };
//
//        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
//                new Observer<StatusCode>() {
//                    public void onEvent(StatusCode status) {
//                        LogUtil.e("nim otherlogin",JSON.toJSONString(status));
//                        if (status.wontAutoLogin()) {
//                            // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
//                            if (status == StatusCode.KICKOUT || status == StatusCode.KICK_BY_OTHER_CLIENT) {
//                                NimUIKit.logout();
//                                RxBus.getInstance().post(UserEvent.OTHER_LOGIN);
//                                EventBus.getDefault().post(UserEvent.OTHER_LOGIN);
//                            }
//                        }
//                    }
//                }, true);
//        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver, true);
    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(MAPP.mapp) + "/app";
        return options;
    }

    private void initBugly(final Context context) {
//        Observable.create(new ObservableOnSubscribe<Object>() {
//            @Override
//            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
        CrashReport.initCrashReport(context, BuildConfig.BUGLY_APPID, BuildConfig.DEBUG);
//            }
//        }).subscribe();
    }

    private void initLeakCanary(Application application) {
//        if (LeakCanary.isInAnalyzerProcess(application)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(application);
    }

    private void initQuPai() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                System.loadLibrary("QuCore-ThirdParty");
                System.loadLibrary("QuCore");
                QupaiHttpFinal.getInstance().initOkHttpFinal();
//                Logger.setDebug(true);
            }
        }).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }


}
