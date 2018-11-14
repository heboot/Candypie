package com.gdlife.candypie.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;

import com.faceunity.entity.Effect;
import com.gdlife.candypie.BuildConfig;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.activitys.auth.AuthBootActivity;
import com.gdlife.candypie.activitys.auth.AuthCommitActivity;
import com.gdlife.candypie.activitys.auth.AuthIDActivity;
import com.gdlife.candypie.activitys.auth.AuthIndexActivity;
import com.gdlife.candypie.activitys.auth.AuthSkillActivity;
import com.gdlife.candypie.activitys.common.HTMLActivity;
import com.gdlife.candypie.activitys.common.IndexActivity;
import com.gdlife.candypie.activitys.common.MainActivity;
import com.gdlife.candypie.activitys.common.ReportActivity;
import com.gdlife.candypie.activitys.common.SearchUserActivity;
import com.gdlife.candypie.activitys.common.SettingActivity;
import com.gdlife.candypie.activitys.common.SettingNotificationActivity;
import com.gdlife.candypie.activitys.common.SplashActivity;
import com.gdlife.candypie.activitys.common.UserVisitActivity;
import com.gdlife.candypie.activitys.discover.DiscoverActivity2;
import com.gdlife.candypie.activitys.kpi.BalanceWeeklyLogActivity;
import com.gdlife.candypie.activitys.kpi.KpiActivity;
import com.gdlife.candypie.activitys.kpi.MyLevelActivity;
import com.gdlife.candypie.activitys.kpi.WeeklyActivity;
import com.gdlife.candypie.activitys.login2register.LoginActivity;
import com.gdlife.candypie.activitys.login2register.RegisterCodeActivity;
import com.gdlife.candypie.activitys.login2register.RegisterForgetActivity;
import com.gdlife.candypie.activitys.login2register.RegisterInfoActivity;
import com.gdlife.candypie.activitys.login2register.RegisterPwdActivity;
import com.gdlife.candypie.activitys.message.RecentContactsActivity;
import com.gdlife.candypie.activitys.message.SystemMessageActivity;
import com.gdlife.candypie.activitys.order.OrderDetailActivity;
import com.gdlife.candypie.activitys.order.UserOrderActivity;
import com.gdlife.candypie.activitys.pay.AccountActivity;
import com.gdlife.candypie.activitys.pay.BalanceLogActivity;
import com.gdlife.candypie.activitys.pay.CashActivity;
import com.gdlife.candypie.activitys.pay.CashIndexActivity;
import com.gdlife.candypie.activitys.pay.CouponsActivity;
import com.gdlife.candypie.activitys.pay.PayActivity;
import com.gdlife.candypie.activitys.pay.RechargeActivity;
import com.gdlife.candypie.activitys.theme.ChooseAddressActivity;
import com.gdlife.candypie.activitys.theme.ChooseAddressDetailActivity;
import com.gdlife.candypie.activitys.theme.NewThemeActivity;
import com.gdlife.candypie.activitys.theme.ServerCancelCauseActivity;
import com.gdlife.candypie.activitys.theme.ServiceCancelActivity;
import com.gdlife.candypie.activitys.theme.ServiceUserListActivity;
import com.gdlife.candypie.activitys.theme.ThemeListActivity;
import com.gdlife.candypie.activitys.user.SetPriceActivity;
import com.gdlife.candypie.activitys.user.TagUserListActivity;
import com.gdlife.candypie.activitys.user.UserBlackListActivity;
import com.gdlife.candypie.activitys.user.UserFavsListActivity;
import com.gdlife.candypie.activitys.user.UserInfoActivity;
import com.gdlife.candypie.activitys.user.UserInfoInputActivity;
import com.gdlife.candypie.activitys.user.UserPageActivity;
import com.gdlife.candypie.activitys.user.UserSkillListActivity;
import com.gdlife.candypie.activitys.user.UserVideoAudioPlayActivity;
import com.gdlife.candypie.activitys.user.UserYueAttitudeActivity;
import com.gdlife.candypie.activitys.video.AutoPlayActivity;
import com.gdlife.candypie.activitys.video.PlayerActivity2;
import com.gdlife.candypie.activitys.video.UserUnlockVideosActivity;
import com.gdlife.candypie.activitys.video.UserVideosActivity;
import com.gdlife.candypie.activitys.video.VideoChatActivity;
import com.gdlife.candypie.activitys.video.VideoFrameActivity;
import com.gdlife.candypie.common.LoginType;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.PayFrom;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.common.ReportFromType;
import com.gdlife.candypie.common.UserVideoActivityFrom;
import com.gdlife.candypie.common.VideoChatFrom;
import com.gdlife.candypie.common.VideoPreviewFrom;
import com.gdlife.candypie.serivce.ConfigService;
import com.gdlife.candypie.serivce.UserService;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.pay.CouponsBeanModel;
import com.heboot.bean.theme.CancelServiceBean;
import com.heboot.bean.theme.MeetPoiDataBean;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.bean.theme.PostThemeBean;
import com.heboot.bean.theme.PostVideoChatBean;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.entity.User;
import com.heboot.event.IMEvent;
import com.heboot.event.NormalEvent;
import com.heboot.faceunity_unit.fulivedemo.FUBeautyActivity;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.netease.nim.uikit.api.NimUIKit;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by heboot on 2018/1/17.
 */

public class IntentUtils {

    public static void toSearchActivity(Context context) {
        Intent intent = new Intent(context, SearchUserActivity.class);
        context.startActivity(intent);
    }

    public static void toMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void toIndexActivity(Context context) {
        Intent intent = new Intent(context, IndexActivity.class);
        context.startActivity(intent);
    }

    public static void toSetPriceActivity(Context context, Boolean fromIndex) {
        Intent intent = new Intent(context, SetPriceActivity.class);
        intent.putExtra(MKey.FROM, fromIndex);
        context.startActivity(intent);
    }

    public static void toTagUserlistActivity(Context context, String tag_id, String title) {
        Intent intent = new Intent(context, TagUserListActivity.class);
        intent.putExtra(MKey.TAG_ID, tag_id);
        intent.putExtra(MKey.TITLE, title);
        context.startActivity(intent);
    }

    public static void toUserPageActivity(Context context, String uid) {
        Intent intent = new Intent(context, UserPageActivity.class);
        intent.putExtra(MKey.UID, uid);
        context.startActivity(intent);
    }

    public static void toUserVideoAudioPlayActivity(Context context, int position, int total, List<User> users) {
        Intent intent = new Intent(context, UserVideoAudioPlayActivity.class);
        intent.putExtra(MKey.INDEX, position);
        intent.putExtra(MKey.TOTAL_PAGES, total);
//        intent.putExtra(MKey.USER_LIST, (Serializable) users);
        MValue.currentUserVideosList = users;
        context.startActivity(intent);
    }

    public static void toPlayerActivity3(Context context, List<HomepageVideoBean> user, int clickPosition, String nickname) {
        Intent intent = new Intent(context, AutoPlayActivity.class);
        intent.putExtra(MKey.USER, (Serializable) user);
        intent.putExtra(MKey.INDEX, clickPosition);
        intent.putExtra(MKey.NAME, nickname);
        context.startActivity(intent);
    }


    public static void toIndexUsersActivity(Context context) {
//        Intent intent = new Intent(context, IndexUsersActivity.class);
//        context.startActivity(intent);
        toMainActivity(context);
    }

    /**
     * 如果是从注册页面过来并且是女性
     *
     * @param context
     * @param womenReg
     */
    public static void toIndexActivity(Context context, boolean womenReg) {
        Intent intent = new Intent(context, IndexActivity.class);
        intent.putExtra(MKey.SEX, womenReg);
        context.startActivity(intent);
    }

    public static void toRegisterActivity(Context context, String type) {
//        Intent intent = new Intent(context, RegisterActivity.class);
//        intent.putExtra(MKey.TYPE, type);
//        context.startActivity(intent);
        Intent intent = new Intent(context, RegisterForgetActivity.class);
        intent.putExtra(MKey.TYPE, type);
        context.startActivity(intent);

    }

    public static void toRegisterCodeActivity(Context context, String mobile, int interval_time, String type) {
        Intent intent = new Intent(context, RegisterCodeActivity.class);
        intent.putExtra(MKey.MOBILE, mobile);
        intent.putExtra(MKey.INTERVAL_TIME, interval_time);
        intent.putExtra(MKey.TYPE, type);
        context.startActivity(intent);
    }

    public static void toRegisterPwdActivity(Context context, String mobile, String type) {
        Intent intent = new Intent(context, RegisterPwdActivity.class);
        intent.putExtra(MKey.MOBILE, mobile);
        intent.putExtra(MKey.TYPE, type);
        context.startActivity(intent);
    }

    public static void toRegisterInfoActivity(Context context, String syncid, LoginType type, HashMap hashMap) {
        Intent intent = new Intent(context, RegisterInfoActivity.class);
        intent.putExtra(MKey.ACTION_ID, syncid);
        intent.putExtra(MKey.TYPE, type);
        intent.putExtra(MKey.MAP, hashMap);
        context.startActivity(intent);
    }

    public static void toNewThemeServiceActivity(Context context, ConfigBean.ServiceItemsConfigBean.ListBean listBean, String type, User user) {
        Intent intent = new Intent(context, NewThemeActivity.class);
        intent.putExtra(MKey.SERVICE_ITEM, listBean);
        intent.putExtra(MKey.TYPE, type);
        intent.putExtra(MKey.USER, user);
        context.startActivity(intent);
    }

    public static void toThemeListActivity(Context context, boolean isOne2One, User user) {
        Intent intent = new Intent(context, ThemeListActivity.class);
        intent.putExtra(MKey.TYPE, isOne2One);
        intent.putExtra(MKey.USER, user);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void toThemeListActivityByVideo(Context context, boolean isOne2One, User user, boolean video) {
        Intent intent = new Intent(context, ThemeListActivity.class);
        intent.putExtra(MKey.TYPE, isOne2One);
        intent.putExtra(MKey.USER, user);
        intent.putExtra(MKey.VIDEO_ID, video);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void toVideoChatActivity(Context context, String userServiceId, PostVideoChatBean postThemeBean, VideoChatFrom from, boolean isMe) {
        Intent intent = new Intent(context, VideoChatActivity.class);
        intent.putExtra(MKey.POST_THEME_BEAN, postThemeBean);
        intent.putExtra(MKey.USER_SERVICE_ID, userServiceId);
        intent.putExtra(MKey.IS_AUTH, isMe);
        intent.putExtra(MKey.FROM, from);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    public static void toChooseAddressActivity(Context context, String service_id, String tags, String cityId) {
        Intent intent = new Intent(context, ChooseAddressActivity.class);
        intent.putExtra(MKey.SERVICE_ID, service_id);
        intent.putExtra(MKey.DP_TAGS, tags);
        intent.putExtra(MKey.CITY_ID, cityId);
        context.startActivity(intent);
    }

    public static void toChooseAddressDetailActivity(Context context, MeetPoiDataBean.ListBean listBean, String type) {
        Intent intent = new Intent(context, ChooseAddressDetailActivity.class);
        intent.putExtra(MKey.MEET_POI_LISTBEAN, listBean);
        intent.putExtra(MKey.TYPE, type);
        context.startActivity(intent);
    }

    public static void toBalanceLogActivity(Context context) {
        Intent intent = new Intent(context, BalanceLogActivity.class);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void toBalanceWeeklyLogActivity(Context context) {
        Intent intent = new Intent(context, BalanceWeeklyLogActivity.class);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void toUserInfoActivity(Context context, String from, String type, User user, MValue.HOMEPAG_FROM pageFrom, ConfigBean.ServiceItemsConfigBean.ListBean listBean) {

//        if (user.getService_auth_status() != null && user.getService_auth_status() != MValue.AUTH_STATUS_SUC || type.equals(MValue.USER_INFO_TYPE_AUTH) || type.equals(MValue.USER_INFO_TYPE_EDIT)) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra(MKey.FROM, from);
        intent.putExtra(MKey.USER, user);
        intent.putExtra(MKey.TYPE, type);
        intent.putExtra(MKey.PAGE_FROM, pageFrom);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
//        } else {
//            toHomepageActivity(context, from, user, pageFrom, listBean);
//        }

    }

    public static void toHomepageActivity(Context context, String from, User user, MValue.HOMEPAG_FROM pageFrom, ConfigBean.ServiceItemsConfigBean.ListBean listBean) {

//        if (user != null && UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId() != null && user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
//            Intent intent = new Intent(context, HomepageActivity.class);
//            intent.putExtra(MKey.FROM, MValue.FROM_MY);
//            intent.putExtra(MKey.USER, user);
//            intent.putExtra(MKey.PAGE_FROM, pageFrom);
//            intent.putExtra(MKey.SERVICE_ITEM, listBean);
//            if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//            context.startActivity(intent);
//        } else {
//            Intent intent = new Intent(context, HomepageOtherActivity.class);
//            intent.putExtra(MKey.FROM, from);
//            intent.putExtra(MKey.USER, user);
//            intent.putExtra(MKey.PAGE_FROM, pageFrom);
//            intent.putExtra(MKey.SERVICE_ITEM, listBean);
//            if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//            context.startActivity(intent);
//
//        }
        toUserPageActivity(context, String.valueOf(user.getId()));
    }

    public static void toHomepageActivity2(Context context, String from, User user, MValue.HOMEPAG_FROM pageFrom, ConfigBean.ServiceItemsConfigBean.ListBean listBean) {

//        if (user != null && UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId() != null && user.getId().intValue() == UserService.getInstance().getUser().getId().intValue()) {
//            Intent intent = new Intent(context, HomepageActivity.class);
//            intent.putExtra(MKey.FROM, from);
//            intent.putExtra(MKey.USER, user);
//            intent.putExtra(MKey.PAGE_FROM, pageFrom);
//            intent.putExtra(MKey.SERVICE_ITEM, listBean);
//            if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//            context.startActivity(intent);
//        } else {
//            Intent intent = new Intent(context, HomepageOtherActivity2.class);
//            intent.putExtra(MKey.FROM, from);
//            intent.putExtra(MKey.USER, user);
//            intent.putExtra(MKey.PAGE_FROM, pageFrom);
//            intent.putExtra(MKey.SERVICE_ITEM, listBean);
//            if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//            context.startActivity(intent);
//
//        }
        toUserPageActivity(context, String.valueOf(user.getId()));
    }

    public static void toUserInfoInputActivity(Context context, String content) {
        Intent intent = new Intent(context, UserInfoInputActivity.class);
        intent.putExtra(MKey.CONTENT, content);
        context.startActivity(intent);
    }

    public static void toCashActivity(Context context) {
        Intent intent = new Intent(context, CashActivity.class);
        context.startActivity(intent);
    }

    public static void toCashIndexActivity(Context context) {
        Intent intent = new Intent(context, CashIndexActivity.class);
        context.startActivity(intent);
    }

    public static void toAuthIndexActivity(Context context, RecordVideoFrom recordVideoFrom) {
        Intent intent = new Intent(context, AuthIndexActivity.class);
        intent.putExtra(MKey.FROM, recordVideoFrom);
        context.startActivity(intent);
    }


    /**
     * 去身份认证页面
     * 无论什么情况都先读预览接口哦
     * <p>
     * //     * @param context
     *
     * @param from
     */
    public static void toAuthIDActivity(Context context, MValue.AUTH_ID_FROM from) {
        Intent intent = new Intent(context, AuthIDActivity.class);
        intent.putExtra(MKey.FROM, from);
        context.startActivity(intent);
    }

    public static void toServiceCancelActivity(Context context, String userServiceId, CancelServiceBean cancelServiceBean) {
        Intent intent = new Intent(context, ServiceCancelActivity.class);
        intent.putExtra(MKey.CANCEL_SERVICE_BEAN, cancelServiceBean);
        intent.putExtra(MKey.USER_SERVICE_ID, userServiceId);
        context.startActivity(intent);
    }

    public static void toAuthCommitActivity(Context context) {
        Intent intent = new Intent(context, AuthCommitActivity.class);
        context.startActivity(intent);
    }

    public static void toUserSkillListActivity(Context context) {
        Intent intent = new Intent(context, UserSkillListActivity.class);
        context.startActivity(intent);
    }

    public static void toAuthSkillActivity(Context context, ConfigBean.ServiceItemsConfigBean.ListBean listBean) {
        Intent intent = new Intent(context, AuthSkillActivity.class);
        intent.putExtra(MKey.SERVICE_ITEM, listBean);
        context.startActivity(intent);
    }

    public static void toUserVideosActivity(Context context, User user, UserVideoActivityFrom from) {
        Intent intent = new Intent(context, UserVideosActivity.class);
        intent.putExtra(MKey.USER, (Serializable) user);
        intent.putExtra(MKey.FROM, from);
        context.startActivity(intent);
    }


    public static void toOrderDetailActivity(Context context, String userServiceId, boolean v1) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(MKey.USER_SERVICE_ID, userServiceId);
        intent.putExtra(MKey.TYPE, v1);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void toUserOrderActivity(Context context) {
        Intent intent = new Intent(context, UserOrderActivity.class);
        context.startActivity(intent);
    }

    public static void toSettingActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
//        intent.putExtra("EffectType", Effect.EFFECT_TYPE_NONE);
        context.startActivity(intent);
    }

    public static void toFaceSettingActivity(Context context) {
        Intent intent = new Intent(context, FUBeautyActivity.class);
        intent.putExtra("EffectType", Effect.EFFECT_TYPE_NONE);
        context.startActivity(intent);
    }

    public static void toLoginActivity(Context context) {
//        RxBus.getInstance().post(NormalEvent.FINISH_INDEX_PAGE);
        Intent intent = new Intent(context, LoginActivity.class);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    public static void toSplashActivity(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    public static void toAccountActivity(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

    public static void toSystemMessageActivity(Context context) {
        Intent intent = new Intent(context, SystemMessageActivity.class);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void toPlayerActivity2(Context context, String url, VideoPreviewFrom from, String videoId) {
        Intent intent = new Intent(context, PlayerActivity2.class);
        intent.putExtra(MKey.URL, url);
        intent.putExtra(MKey.FROM, from);
        intent.putExtra(MKey.VIDEO_ID, videoId);
        context.startActivity(intent);
    }

    public static void toPlayerActivity2(Context context, String url, VideoPreviewFrom from, String videoId, String coverUrl) {
        Intent intent = new Intent(context, PlayerActivity2.class);
        intent.putExtra(MKey.URL, url);
        intent.putExtra(MKey.FROM, from);
        intent.putExtra(MKey.VIDEO_ID, videoId);
        intent.putExtra(MKey.COVER_URL, coverUrl);
        context.startActivity(intent);
    }

    public static void toPlayerActivity2(Context context, String url, VideoPreviewFrom from, String videoId, String coverUrl, boolean isOvalBack) {
        Intent intent = new Intent(context, PlayerActivity2.class);
        intent.putExtra(MKey.URL, url);
        intent.putExtra(MKey.FROM, from);
        intent.putExtra(MKey.VIDEO_ID, videoId);
        intent.putExtra(MKey.COVER_URL, coverUrl);
        intent.putExtra(MKey.TYPE, isOvalBack);
        context.startActivity(intent);
    }

    public static void toDiscoverActivity(Context context, int postion, List<User> users, int sp, int totalPages) {
        Intent intent = new Intent(context, DiscoverActivity2.class);
        intent.putExtra(MKey.INDEX, postion);
        intent.putExtra(MKey.SP, sp);
        intent.putExtra(MKey.USER_LIST, (Serializable) users);
        intent.putExtra(MKey.TOTAL_PAGES, totalPages);
        context.startActivity(intent);
    }

    public static void toPayActivity(Context context, PostThemeBean postThemeBean, PayFrom payFrom, boolean isVideo) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(MKey.POST_THEME_BEAN, postThemeBean);
        intent.putExtra(MKey.FROM, payFrom);
        intent.putExtra(MKey.TYPE, isVideo);
        context.startActivity(intent);
    }

    public static void toServiceUserListActivity(Context context, List<User> users, List<OrderListBean.ListBean.ActionConfigBean> actionConfigBeans) {
        Intent intent = new Intent(context, ServiceUserListActivity.class);
        intent.putExtra(MKey.USER_LIST, (Serializable) users);
        intent.putExtra(MKey.SERVICE_ACTION, (Serializable) actionConfigBeans);
        context.startActivity(intent);
    }


    public static void toHTMLActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, HTMLActivity.class);
        intent.putExtra(MKey.URL, url);
        intent.putExtra(MKey.TITLE, title);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void toHTMLActivity(Context context, String title, String url, boolean nav, double lat, double lng) {
        Intent intent = new Intent(context, HTMLActivity.class);
        intent.putExtra(MKey.URL, url);
        intent.putExtra(MKey.TITLE, title);
        intent.putExtra(MKey.TYPE, nav);
        intent.putExtra(MKey.LAT, lat);
        intent.putExtra(MKey.LNG, lng);
        context.startActivity(intent);
    }

    public static void toCouponsActivity(Context context, boolean used, PostThemeBean postThemeBean, CouponsBeanModel selectedCouponsBeanModel) {
        Intent intent = new Intent(context, CouponsActivity.class);
        intent.putExtra(MKey.USED, used);
        intent.putExtra(MKey.POST_THEME_BEAN, postThemeBean);
        intent.putExtra(MKey.COUPONS_ID, selectedCouponsBeanModel);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void toRechargeActivity(Context context, RechargeType rechargeType) {
        Intent intent = new Intent(context, RechargeActivity.class);
        intent.putExtra(MKey.TYPE, rechargeType);
        context.startActivity(intent);
    }


    public static void toSettingNotificationActivity(Context context) {
        Intent intent = new Intent(context, SettingNotificationActivity.class);
//        intent.putExtra(MKey.NUM, num);
//        intent.putExtra(MKey.FROM, from);
        context.startActivity(intent);

    }


    public static void toVideoFrameActivity(Context context, String videoPath) {
        Intent intent = new Intent(context, VideoFrameActivity.class);
        intent.putExtra(MKey.PATH, videoPath);
        context.startActivity(intent);
    }

    public static void toAuthBootActivity(Context context) {
        Intent intent = new Intent(context, AuthBootActivity.class);
        context.startActivity(intent);
    }

    public static void toRecentContactsActivity(Context context) {
        Intent intent = new Intent(context, RecentContactsActivity.class);
        context.startActivity(intent);
    }


    public static void toKpiActivity(Context context) {
        Intent intent = new Intent(context, KpiActivity.class);
        context.startActivity(intent);
    }

    public static void toUserFavsListActivity(Context context) {
        Intent intent = new Intent(context, UserFavsListActivity.class);
        context.startActivity(intent);
    }

    public static void toUserBlackListActivity(Context context) {
        Intent intent = new Intent(context, UserBlackListActivity.class);
        context.startActivity(intent);
    }

    public static void toWeeklyActivity(Context context) {
        Intent intent = new Intent(context, WeeklyActivity.class);
        context.startActivity(intent);
    }

    public static void toMyLevelActivity(Context context) {
        Intent intent = new Intent(context, MyLevelActivity.class);
        context.startActivity(intent);
    }

    public static void toUserYueAttitudeActivity(Context context) {
        Intent intent = new Intent(context, UserYueAttitudeActivity.class);
        context.startActivity(intent);
    }

    /**
     * 前往最近来访页面
     *
     * @param context
     */
    public static void toUserVisitActivity(Context context, String toUid) {
        Intent intent = new Intent(context, UserVisitActivity.class);
        intent.putExtra(MKey.TO_UID, toUid);
        context.startActivity(intent);
    }

    public static void toServerCancelCauseActivity(Context context, String userServiceId) {
        Intent intent = new Intent(context, ServerCancelCauseActivity.class);
        intent.putExtra(MKey.USER_SERVICE_ID, userServiceId);
        if (context.getClass().getSimpleName().equals(MAPP.mapp.getClass().getSimpleName())) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    public static void toReportActivity(Context context, String uid, ReportFromType reportFromType) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra(MKey.UID, uid);
        intent.putExtra(MKey.TYPE, reportFromType);
        context.startActivity(intent);
    }

    public static void installAPP(Context context, String url) {
        // 核心是下面几句代码
        Intent intent;
        File file = new File(url);

        LogUtil.e("installAPP", "installAPP>" + file.exists());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } else {
            Uri apkUri = Uri.fromFile(file);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }


    public static void intent2ChatActivity(Context context, String account) {
//        if (ConfigService.getInstance().isZs(account)) {
//            NimUIKit.startP2PSession(context, account, "hide", "hide", null);
//        } else {
//            if (ConfigService.getInstance().isKF(account)) {
//                NimUIKit.startP2PSession(context, account, null, "hide", null);
//            } else {
//                String hide = null;
//                if (!BuildConfig.DEBUG && UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER) {
//                    hide = "hide";
//                }
//                NimUIKit.startP2PSession(context, account, null, hide, null);
//            }
//
//        }
        RxBus.getInstance().post(new IMEvent.QUERY_IM_STAUTS_EVENT(context, account.replace(MValue.CHAT_PRIEX, "")));
    }

    public static void intent2ChatActivity(Context context, String account, String price, int is_im_chat, int isBlack, User user) {
        if (ConfigService.getInstance().isZs(account)) {
            NimUIKit.startP2PSession(context, account, "hide", "hide", "0", is_im_chat, isBlack, user, UserService.getInstance().getUser());
        } else {
            if (ConfigService.getInstance().isKF(account)) {
                NimUIKit.startP2PSession(context, account, null, "hide", "0", is_im_chat, isBlack, user, UserService.getInstance().getUser());
            } else {
                String hide = null;
                if ((UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC)) {
                    hide = "hide";
                }
                if (StringUtils.isEmpty(price)) {
                    price = "0";
                }
                NimUIKit.startP2PSession(context, account, null, null, price, is_im_chat, isBlack, user, UserService.getInstance().getUser());
            }

        }
    }


    public static void toUserUnlockVideosActivity(Context context) {
        Intent intent = new Intent(context, UserUnlockVideosActivity.class);
        context.startActivity(intent);
    }

    public static void callMobile(Context context, String mobile) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + mobile);
        intent.setData(data);
        context.startActivity(intent);
    }

    public static void toAPPSetting() {
        // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", MAPP.mapp.getPackageName(), null);
        intent.setData(uri);
        MAPP.mapp.startActivity(intent);
    }

    public static void toNav() {

    }


//    public static void toForgetCodeActivity(Context context, String mobile, int interval_time) {
//        Intent intent = new Intent(context, ForgetCodeActivity.class);
//        intent.putExtra(MKey.MOBILE, mobile);
//        intent.putExtra(MKey.INTERVAL_TIME, interval_time);
//        context.startActivity(intent);
//    }

}
