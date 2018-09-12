package com.gdlife.candypie.common;

import android.app.Activity;

import com.baidu.location.BDLocation;
import com.example.http.BuildConfig;
import com.gdlife.candypie.widget.luckpan.LuckpanDialog;
import com.heboot.bean.gift.GiftBean;
import com.heboot.bean.login2register.UpdateVersionBean;
import com.heboot.bean.message.TurntableResultBean;
import com.heboot.entity.User;

import java.util.List;

/**
 * Created by heboot on 2018/2/3.
 */

public class MValue {

    public static BDLocation currentLocation;

    public static final String FROM_MY = "my";
    public static final String FROM_OTHER = "other";

    public static final String USER_INFO_TYPE_AUTH = "user_auth";
    public static final String USER_INFO_TYPE_NORMAL = "normal";
    public static final String USER_INFO_TYPE_EDIT = "edit";
    public static final String USER_INFO_TYPE_AUTH_COMMIT = "auth_commit";

    public static final String USER_INFO_FROM_HOMEPAGE = "homepage";

    public static final String NEW_SERVICE_TYPE_NORMAL = "normal";
    public static final String NEW_SERVICE_TYPE_ONE = "one";
    public static final String NEW_SERVICE_TYPE_VIDEO = "video";

    public static final String SD_PATH_VIDEO = "video/";
    public static final String SD_PATH_PIC = "pic/";
    public static final String SD_PATH_FRAME = "frame/";
    public static final String SD_PATH_ROOT = "candypie";

    public static final String ORDER_TYPE_VIDEO = "video";

    public static final String CHAT_PRIEX = "cdp";

    public static final int AUTH_STATUS_NO = 0;
    public static final int AUTH_STATUS_ING = 1;
    public static final int AUTH_STATUS_SUC = 2;
    public static final int AUTH_STATUS_FAIL = 3;


    public static final int USER_ROLE_NORMAL = 2;
    public static final int USER_ROLE_SERVICER = 3;

    public static final int RECORD_VIDEO_MIN = 10000;
    public static final int RECORD_VIDEO_MAX = 15000;

    public static final String WX_APPID = "";

    public static Activity currentActivity = null;

    public static boolean IS_RESUME = true;

    public static boolean SEND_VIDEO_INVITE = true;

    public static int VIDEO_AUTH_STATUS_ING = 0;

    public static String CURRENT_UPDATE_AVATAR_VIDEO_ID = "";

    public static RechargeType currentRechargeType = null;

    public static boolean IndexBannerEnable = true;

    //转盘是否显示中
    public static boolean PAN_SHOWING = false;

    public static LuckpanDialog luckpanDialog = null;

    public static TurntableResultBean turntableResultBean = null;

    public static int currentVideoCoinAmount = 200;

    public static int currentVideoTime = 0;


//    public static boolean IS_FROM_TOURIST = false;


    //是否 从相册过去的 替换视频
    public static boolean REPLACE_VIDEO = false;

    //是否从中断页面过去 的 上传视频
    public static boolean COMMIT_UPLOAD_VIDEO = false;

    public static final String MAP_URL = BuildConfig.HTTP_SERVER + "app/poi/point";


    //reg:注册,forget:找回密码,blinding:绑定手机,,invite_valid:邀请验证,默认reg
    public static class CHECK_SMS_CODE {
        public static final String REG = "reg";
        public static final String FORGET = "forget";
        public static final String BLINDING = "blinding";
        public static final String INVITE_VALID = "invite_valid";
    }


    public static int ORDER_LIST_STATUS_ALL = 0;
    public static int ORDER_LIST_STATUS_DAICHULI = 1;
    public static int ORDER_LIST_STATUS_DAIWANCHENG = 2;
    public static int ORDER_LIST_STATUS_FINISH = 3;
    public static int ORDER_LIST_STATUS_CANCEL = -2;
    public static int ORDER_LIST_STATUS_DENGDAIQUEREN = 4;
    public static int ORDER_LIST_STATUS_YIWANCHENG = 6;

    public static int ORDER_STATUS_DAICHULI = 3;


    public enum HOMEPAG_FROM {
        RECOMMEND_USER,
        ONE_ONE,
        CHOOSE_USER
    }

    public enum CHOOSE_SERVICE_USER_FROM {
        FIRST,//首单
        NORMAL,
    }

    public enum AUTH_ID_FROM {
        EDIT,       //保存
        SERVICE_AUTH,    //提交服务和身份一块提交
        USER_AUTH        //仅提交身份认证
    }

    public enum TIP_DIALOG_TYPE {
        TIMEOUT,//超时
        FIRST,//首单
        NO_USER,//没有服务者抢单
        CONFINE,//取消发单后限制发单
        ONE,//一对一发单成功了
        FIRST_ROB, //第一次抢单,
        FIRST_VIDEO_ORDER,//第一次1V1视频发单
        UPDATE_GLOD_VIP
    }

    public static class VIDEO_STATUS {
        public static final int ING = 1;
        public static final int SUC = 0;
    }


//    public static ConfigBean configBean;

    public static UpdateVersionBean updateVersionBean;


    /**
     * 当前选择要送出去的礼物
     */
    public static GiftBean currentSelectedGiftBean = null;

    public static List<User> currentUserVideosList;

}
