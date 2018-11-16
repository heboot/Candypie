package com.gdlife.candypie;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentActivity;

import com.alivc.player.AliVcMediaPlayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.http.HttpUtils;
import com.gdlife.candypie.activitys.common.MainActivity;
import com.gdlife.candypie.activitys.order.OrderDetailActivity;
import com.gdlife.candypie.activitys.pay.BalanceLogActivity;
import com.gdlife.candypie.activitys.pay.CouponsActivity;
import com.gdlife.candypie.activitys.user.UserPageActivity;
import com.gdlife.candypie.activitys.video.VideoChatActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.VideoChatFrom;
import com.gdlife.candypie.common.VideoUploadType;
import com.gdlife.candypie.component.DaggerUtilsComponent;
import com.gdlife.candypie.component.UtilsComponent;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.nim.NIMInitManager;
import com.gdlife.candypie.nim.NimSDKOptionConfig;
import com.gdlife.candypie.serivce.AuthService;
import com.gdlife.candypie.serivce.MessageService;
import com.gdlife.candypie.serivce.OrderService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.VideoService;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.utils.APPUtils;
import com.gdlife.candypie.utils.InitUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.NotificationsUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SDCardUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.widget.PopupNotificationSnack;
import com.gdlife.candypie.widget.gift.BottomVideoGiftSheetDialogHehe;
import com.heboot.base.BaseBean;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.im.ImChatStatusBean;
import com.heboot.common.ConfigValue;
import com.heboot.entity.User;
import com.heboot.event.IMEvent;
import com.heboot.event.MessageEvent;
import com.heboot.event.OrderEvent;
import com.heboot.event.VideoEvent;
import com.heboot.faceunity_unit.fulivedemo.utils.ToastUtil;
import com.heboot.message.MessageToAction;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.PreferencesUtils;
import com.mob.MobSDK;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.agora.AgoraAPIOnlySignal;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.heboot.event.IMEvent.TO_RECHARGE_BY_IM;

/**
 * Created by heboot on 2018/1/16.
 */

public class MAPP extends Application {

    private static UtilsComponent utilsComponent;

    @Inject
    InitUtils initUtils;

    @Inject
    PermissionUtils permissionUtils;

    @Inject
    VideoService videoService;

    private MessageService messageService;

    protected Observable<Object> rxObservable;

    public static MAPP mapp;

    public static LinkedList<Activity> activityLinkedList;

    private AgoraAPIOnlySignal m_agoraAPI;


    private ConfigBean configBean;

    private Activity currentActivity;

    private VideoChatService videoChatService;

    private OrderService orderService;


    @Override
    public void onCreate() {
        super.onCreate();
        mapp = this;
        utilsComponent = DaggerUtilsComponent.create();
        utilsComponent.inject(this);
        messageService = new MessageService();
//        initUtils.registerActivityLifecycleCallbacks(this);
        initUtils.initSDK(this, messageService);

        UMConfigure.init(MAPP.mapp, UMConfigure.DEVICE_TYPE_PHONE, "a84de7f77834b3084be55710846d4f48");
        //初始化播放器（只需调用一次即可，建议在application中初始化）
        AliVcMediaPlayer.init(getApplicationContext());
        SDCardUtils.getRootPathPrivateVideo();

        initObservable();

        try {
            Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(HttpUtils.getUnsafeOkHttpClient()));
        } catch (Exception e) {
        }


        MobSDK.init(this);


        EventBus.getDefault().register(this);
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        initNIM(this, messageService);
//        // 异常处理，不需要处理时注释掉这两句即可！
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        // 注册crashHandler
//        crashHandler.init(getApplicationContext());

    }

    public void initNIM(Application application, MessageService messageService) {
        NIMClient.init(application, null, NimSDKOptionConfig.getSDKOptions(MAPP.mapp));// NimSDKOptionConfig.getSDKOptions(MAPP.mapp)
        if (NIMUtil.isMainProcess(application)) {
            // 注册自定义推送消息处理，这个是可选项
//            NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());

            // init pinyin
            PinYin.init(application);
            PinYin.validate();
            // 初始化UIKit模块
//            initUIKit(application, messageService);
            initUtils.initUIKit(this, messageService);
            // 初始化消息提醒
            NIMClient.toggleNotification(false);
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true);
        }


    }

    private void initObservable() {
        rxObservable = RxBus.getInstance().toObserverable();
        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                if (o instanceof VideoEvent.VideoUploadEvent) {
                    videoService.doUploadVideo(((VideoEvent.VideoUploadEvent) o).getImagePath(), ((VideoEvent.VideoUploadEvent) o).getAvatarPath(), 1, VideoUploadType.USER, ((VideoEvent.VideoUploadEvent) o).getVideoPath());
                } else if (o.equals(VideoEvent.VIDEO_UPLOAD_SUC_EVENT_TO_USERINFO)) {
                    IntentUtils.toUserInfoActivity(mapp, MValue.USER_INFO_TYPE_AUTH, MValue.USER_INFO_TYPE_AUTH, UserService.getInstance().getUser(), null, null);
                } else if (o instanceof MessageEvent.ToOrderDetailByMessageEvent) {
                    IntentUtils.toOrderDetailActivity(mapp, ((MessageEvent.ToOrderDetailByMessageEvent) o).getUserServiceId(), true);
                } else if (o instanceof MessageEvent.DoSystemMessageActionEvent) {
                    messageService.doSystemMessageAction(((MessageEvent.DoSystemMessageActionEvent) o).getToAction(), ((MessageEvent.DoSystemMessageActionEvent) o).getSystemMessage());
                } else if (o instanceof MessageEvent.TO_AGAIN_ORDER_EVENT) {
                    if (videoChatService == null) {
                        videoChatService = new VideoChatService();
                    }
                    videoChatService.postVideoService(permissionUtils, getCurrentActivity(), ((MessageEvent.TO_AGAIN_ORDER_EVENT) o).getUser(), null);
                } else if (o.equals(MessageEvent.TO_SYSTEM_MESSAGE_PAGE_EVENT)) {
                    IntentUtils.toSystemMessageActivity(mapp);
                } else if (o instanceof VideoEvent.UPDATE_USER_AVATAR_EVENT) {
                    videoService.doUpdateAvatar(((VideoEvent.UPDATE_USER_AVATAR_EVENT) o).getAvatarPath());
                } else if (o instanceof IMEvent.SHOW_IM_GIFT_EVENT) {
//                    BottomVideoGiftSheetDialogByIM bottomVideoGiftSheetDialog = new BottomVideoGiftSheetDialogByIM.Builder(((IMEvent.SHOW_IM_GIFT_EVENT) o).getContext(), new Consumer<GiftBean>() {
//                        @Override
//                        public void accept(GiftBean giftBean) throws Exception {
//                            EventBus.getDefault().post(new GiftEvent.SendGiftEvent(giftBean));
//                        }
//                    }, ((IMEvent.SHOW_IM_GIFT_EVENT) o).getToUid()).create();
//                    bottomVideoGiftSheetDialog.show();
                    try {
                        BottomVideoGiftSheetDialogHehe bottomVideoGiftSheetDialogHehe = new BottomVideoGiftSheetDialogHehe(((IMEvent.SHOW_IM_GIFT_EVENT) o).getToUid().replace(MValue.CHAT_PRIEX, ""), null);
                        bottomVideoGiftSheetDialogHehe.show(((FragmentActivity) MAPP.mapp.getCurrentActivity()).getSupportFragmentManager(), "");
                    } catch (Exception e) {

                    }

                } else if (o instanceof IMEvent.QUERY_IM_STAUTS_EVENT) {
                    Map<String, Object> params = new HashMap<>();
                    params = SignUtils.getNormalParams();
                    params.put(MKey.TO_UID, ((IMEvent.QUERY_IM_STAUTS_EVENT) o).getToUid());
                    String sign = SignUtils.doSign(params);
                    params.put(MKey.SIGN, sign);
                    HttpClient.Builder.getGuodongServer().imchat_status(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<ImChatStatusBean>() {
                        @Override
                        public void onSuccess(BaseBean<ImChatStatusBean> baseBean) {
                            User user = new User();
                            user.setId(Integer.parseInt(((IMEvent.QUERY_IM_STAUTS_EVENT) o).getToUid()));
                            user.setCity_id(baseBean.getData().getCity_id());
                            user.setIm_danger_tip(baseBean.getData().getIm_danger_tip());
                            user.setIm_report_tip(baseBean.getData().getIm_report_tip());
                            user.setLocal_video_tags(baseBean.getData().getVideo_tags());
                            IntentUtils.intent2ChatActivity(((IMEvent.QUERY_IM_STAUTS_EVENT) o).getContext(), MValue.CHAT_PRIEX + ((IMEvent.QUERY_IM_STAUTS_EVENT) o).getToUid(), baseBean.getData().getIm_price(), baseBean.getData().getIs_im_chat(), baseBean.getData().getIs_black(), user);


                        }

                        @Override
                        public void onError(BaseBean<ImChatStatusBean> baseBean) {

                        }
                    });
                } else if (o.equals(TO_RECHARGE_BY_IM)) {
                    IntentUtils.toAccountActivity(MAPP.mapp.getCurrentActivity());
                } else if (o instanceof IMEvent.UPDATE_COIN_BALANCE_EVENT) {
//                    UserService.getInstance().getUser().setCoin(String.valueOf(Integer.parseInt(UserService.getInstance().getUser().getCoin()) - ((IMEvent.UPDATE_COIN_BALANCE_EVENT) o).getCoinAmount()));
                } else if (o instanceof OrderEvent.DO_COMMENT_VIDEO_EVENT) {
                    if (orderService == null) {
                        orderService = new OrderService();
                    }
                    Observable.just("Amit")
                            //延时两秒，第一个参数是数值，第二个参数是事件单位
                            .delay(1100, TimeUnit.MILLISECONDS)
                            // Run on a background thread
                            .subscribeOn(Schedulers.io())
                            // Be notified on the main thread
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    orderService.doCommentOrder(getCurrentActivity(), ((OrderEvent.DO_COMMENT_VIDEO_EVENT) o).getUserServiceId(), null);
                                }
                            });//这里的观察者依然不重要
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private PopupNotificationSnack popupNotificationSnack;

    private String title;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent.ShowMessageNotiEvent o) {


        if (APPUtils.isBackground(getApplicationContext())) {
            //跳转意图
            Intent intent = null;
            PendingIntent pendingIntent = null;
            if (!StringUtils.isEmpty(o.getSystemNotification().getValue().getTo_action())) {
                if (((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getValue().getTo_action().equals(MessageToAction.push_list.toString())) {
                    // TODO: 2018/8/15 收到新的订单 应该跳转到新的消息页面 不去老的抢单页面了
                    AuthService.newOrderToMessageRobPage();
//                    intent = new Intent(currentActivity, IndexServerContainerActivity.class);
//                    pendingIntent = PendingIntent.getActivity(currentActivity, 0, intent, 0);
                } else if (((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getType().equals(MessageToAction.push_user_list.toString())) {
//                    intent = new Intent(MAPP.mapp, ThemeUserListActivity.class);
//                    pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                    // TODO: 2018/4/9 站外信打开选人页面 还得想想
                } else if (((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getValue().getTo_action().equals(MessageToAction.income_list.toString())) {
                    intent = new Intent(currentActivity, BalanceLogActivity.class);
                    pendingIntent = PendingIntent.getActivity(currentActivity, 0, intent, 0);
                } else if (((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getValue().getTo_action().equals(MessageToAction.order_detail.toString())) {
                    intent = new Intent(currentActivity, OrderDetailActivity.class);
                    intent.putExtra(MKey.USER_SERVICE_ID, o.getSystemNotification().getValue().getUser_service_id());
                    pendingIntent = PendingIntent.getActivity(currentActivity, 0, intent, 0);
                } else if (((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getValue().getTo_action().equals(MessageToAction.coupons_list.toString())) {
                    intent = new Intent(currentActivity, CouponsActivity.class);
                    intent.putExtra(MKey.USED, false);
                    pendingIntent = PendingIntent.getActivity(currentActivity, 0, intent, 0);
                } else if (((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getValue().getTo_action().equals(MessageToAction.start_video_chat.toString())) {

//                    intent.putExtra(MKey.USER_SERVICE_ID, o.getSystemNotification().getValue().getUser_service_id());
//                    intent.putExtra(MKey.POST_THEME_BEAN, o.getSystemNotification().getValue().getChat_room_config());
                    if (!(currentActivity instanceof MainActivity)) {
                        intent = new Intent(currentActivity, MainActivity.class);
                        intent.putExtra(MKey.FROM, VideoChatFrom.SERVICER);
                        pendingIntent = PendingIntent.getActivity(currentActivity, 0, intent, 0);
                    }

                } else if (((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getValue().getTo_action().equals(MessageToAction.service_profile.toString())) {
                    intent = new Intent(currentActivity, UserPageActivity.class);
                    intent.putExtra(MKey.UID, UserService.getInstance().getUser().getId() + "");
                    pendingIntent = PendingIntent.getActivity(currentActivity, 0, intent, 0);
                }
            }
            title = ((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getValue().getMsg();
            if (!((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getValue().getTo_action().equals(MessageToAction.start_video_chat.toString())) {
                NotificationsUtils.showNotification(title, intent, pendingIntent);
            }

        } else {
            try {
                popupNotificationSnack = new PopupNotificationSnack(currentActivity.findViewById(android.R.id.content), getApplicationContext(), ((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getValue());
                popupNotificationSnack.setModel(((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getValue());
                if (!((MessageEvent.ShowMessageNotiEvent) o).getSystemNotification().getValue().getTo_action().equals(MessageToAction.start_video_chat.toString())) {
                    if (!(getCurrentActivity() instanceof VideoChatActivity)) {
                        popupNotificationSnack.show();
                    }
                }
            } catch (Exception e) {
                CrashReport.postCatchedException(new Throwable("弹5秒窗出现问题"));
                CrashReport.postCatchedException(e);
            }
        }

    }


    public static UtilsComponent getUtilsComponent() {
        return utilsComponent;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public AgoraAPIOnlySignal getM_agoraAPI() {
        if (m_agoraAPI == null) {
            m_agoraAPI = AgoraAPIOnlySignal.getInstance(MAPP.mapp, "657f3a61e84b4771b568983b2faf09c6");
        }
        return m_agoraAPI;
    }

    public ConfigBean getConfigBean() {
        if (configBean == null) {
            if (PreferencesUtils.getObj(this, MKey.CONFIGDATA) != null) {
                return (ConfigBean) PreferencesUtils.getObj(this, MKey.CONFIGDATA);
            }
            CrashReport.postCatchedException(new Throwable("配置数据 内存和缓存都没有读取到"));
        }
        return configBean;
    }

    public void setConfigBean(ConfigBean configBean) {
        this.configBean = configBean;
        //把客服相关的数据 给UIKIT 做最近联系人置顶排序
        ConfigValue.setKf_uids(configBean.getKf_uids());
    }
}
