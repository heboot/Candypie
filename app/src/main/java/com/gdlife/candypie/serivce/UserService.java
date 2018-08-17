package com.gdlife.candypie.serivce;


import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.pay.ServiceLevelBean;
import com.heboot.entity.IMUser;
import com.heboot.entity.User;
import com.heboot.entity.VideoFirstOrder;
import com.heboot.entity.VideoUser;
import com.heboot.event.UserEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.ConvertUtils;
import com.heboot.utils.DateUtil;
import com.heboot.utils.PreferencesUtils;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * Created by heboot on 2018/2/7.
 */

public class UserService {

    private static UserService userService;

    private String token;

    private String sign_key;

    private static User user;

    private IMUser imUser;

    private VideoUser videoUser;

    private VideoFirstOrder video_first_order;

    private UserService() {
    }


    /**
     * 是否是服务者
     *
     * @return
     */
    public boolean isServicer() {
        if (getUser() != null && getUser().getId() != null && getUser().getService_auth_status() != null && getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC) {
            return true;
        }
        return false;
    }

    /**
     * 分享哦
     */
    public void doRecommendFriend() {
        if (UserService.getInstance().checkTourist()) {
            return;
        }
        if (isServicer()) {
            IntentUtils.toHTMLActivity(MAPP.mapp.getCurrentActivity(), null, MAPP.mapp.getConfigBean().getInvite_url_config().getS_u() + "?token=" + UserService.getInstance().getToken());
        } else {
            IntentUtils.toHTMLActivity(MAPP.mapp.getCurrentActivity(), null, MAPP.mapp.getConfigBean().getInvite_url_config().getU_u() + "?token=" + UserService.getInstance().getToken());
        }

    }


    public synchronized static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
            return userService;
        }
        return userService;
    }


    public static boolean isMe(User puser) {
        if (puser == null || user == null) {
            return false;
        }
        if (puser.getId() != null && puser.getId() == user.getId()) {
            return true;
        }
        return false;
    }


    public boolean checkTourist() {
        if (getUser() == null) {
            IntentUtils.toLoginActivity(MAPP.mapp);
            return true;
        } else {
            return false;
        }
    }

    public boolean checkTourist(Context context) {
        if (getUser() == null) {
            IntentUtils.toLoginActivity(context);
            return true;
        } else {
            return false;
        }
    }


    /**
     * 根据生日获取年龄
     *
     * @param user
     * @return
     */
    public static String getUserAgeByBirthday(User user) {
        return DateUtil.getCurrentAgeByBirthdate(DateUtil.str2Date(user.getBirthday(), DateUtil.FORMAT_YMD)) + MAPP.mapp.getString(R.string.age_unit);
    }

    public static String getUserAgeByBirthdayNoSui(User user) {
        if (user == null) {
            return "";
        }
        if (StringUtils.isEmpty(user.getBirthday())) {
            return "";
        }
        return DateUtil.getCurrentAgeByBirthdate(DateUtil.str2Date(user.getBirthday(), DateUtil.FORMAT_YMD)) +
                "";
    }


    /**
     * 显示年龄 身高 体重
     *
     * @param textView
     * @param user
     */
    @BindingAdapter("android:showUserInfo")
    public static void getUserInfo(TextView textView, User user) {
        if (user == null)
            return;
        textView.setText(
                user.getConstellation()
                        + (StringUtil.isEmpty(user.getHeight()) ? "" : " · " + user.getHeight() + textView.getContext().getString(R.string.unit_height))
                        + (StringUtil.isEmpty(user.getWeight()) ? "" : " · " + user.getWeight() + textView.getContext().getString(R.string.unit_kg)));
    }

    /**
     * 获取云信登录信息
     *
     * @return
     */
    public LoginInfo getLoginInfo() {
        if (imUser != null) {
            return new LoginInfo(imUser.getCcid(), imUser.getToken());
        }
        return null;
    }

    /**
     * 注销登录
     *
     * @param context
     */
    public void logout(Context context) {
        putSPUser(null);
        user = null;
        PreferencesUtils.putString(MAPP.mapp, "userToken", "");
        setToken("");
        RxBus.getInstance().post(UserEvent.LOGOUT);
        MAPP.mapp.getM_agoraAPI().logout();
        NimChatService.getInstance().doLogout();
        IntentUtils.toSplashActivity(context);
    }

    public void doTouristPreview() {
        putSPUser(null);
        user = null;
        MAPP.mapp.getM_agoraAPI().logout();
        NimChatService.getInstance().doLogout();
    }


    public boolean getFirstDiscover() {
        return PreferencesUtils.getBoolean(MAPP.mapp, MKey.DISCOVER_FIRST, true);
    }

    public boolean setFirstDiscover() {
        return PreferencesUtils.putBoolean(MAPP.mapp, MKey.DISCOVER_FIRST, false);
    }

    public boolean getFirstIndex() {
        return PreferencesUtils.getBoolean(MAPP.mapp, MKey.FIRST_INDEX, true);
    }

    public boolean setFirstIndex() {
        return PreferencesUtils.putBoolean(MAPP.mapp, MKey.FIRST_INDEX, false);
    }


    /**
     * 本地写入用户信息
     */
    public void putSPUser(User user) {
        PreferencesUtils.saveObj(MAPP.mapp, MKey.USER, user);
    }

    /**
     * 获取本地用户信息
     *
     * @return
     */
    public User getSPUser() {
        return PreferencesUtils.getObj(MAPP.mapp, MKey.USER) == null ? null : (User) PreferencesUtils.getObj(MAPP.mapp, MKey.USER);
    }

    public User getUser() {
        if (user == null) {
            if (getSPUser() != null) {
                user = getSPUser();
            }
        }
        return user;
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user = ConvertUtils.converUserModel(this.user, user);
        } else {
            this.user = user;
        }
    }

    public String getToken() {
        if (StringUtils.isEmpty(token)) {
            return PreferencesUtils.getString(MAPP.mapp, "userToken");
        }
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        PreferencesUtils.putString(MAPP.mapp, "userToken", token);
    }

    public String getSign_key() {
        return sign_key;
    }

    public void setSign_key(String sign_key) {
        this.sign_key = sign_key;
    }

    public static UserService getUserService() {
        return userService;
    }

    public static void setUserService(UserService userService) {
        UserService.userService = userService;
    }

    public VideoFirstOrder getVideo_first_order() {
        return video_first_order;
    }

    public void setVideo_first_order(VideoFirstOrder video_first_order) {
        this.video_first_order = video_first_order;
    }
}
