package com.gdlife.candypie.serivce;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.SettingNotificationType;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.InitUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.NotificationsUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.login2register.RegisterBean;
import com.heboot.entity.IMUser;
import com.heboot.entity.User;
import com.heboot.entity.VideoUser;
import com.heboot.utils.LogUtil;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import io.agora.AgoraAPI;
import io.agora.AgoraAPIOnlySignal;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/23.
 */

public class LoginService {

    private PlatformActionListener platformActionListener;

    private String loginType;

    private void initListener() {
        if (platformActionListener == null) {
            platformActionListener = new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            };
        }
    }

    public void doWXLogin() {
        Platform plat = ShareSDK.getPlatform(Wechat.NAME);
        plat.SSOSetting(false);
        plat.showUser(null);
        plat.setPlatformActionListener(platformActionListener);
    }

    private void doThirdLogin(String nickName, String headPic, int sex, String openId, String type, String unionid) {
        Map<String, Object> params = SignUtils.getNormalParams();
        params.put(MKey.NICK_NAME, nickName);
        params.put(MKey.HEAD_PIC, headPic);
        params.put(MKey.SEX, sex);
        params.put(MKey.OPENID, openId);
        params.put(MKey.TYPE, type);
        params.put(MKey.UNIONID, unionid);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().third_login(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
            @Override
            public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                UserService.getInstance().setUser(baseBean.getData().getUser());
            }

            @Override
            public void onError(BaseBean<BaseBeanEntity> baseBean) {
            }
        });
    }


    public void doLoginAgora(VideoUser videoUser, IMUser imUser) {
        // 登录 Agora 信令系统
        MAPP.mapp.getM_agoraAPI().login2("657f3a61e84b4771b568983b2faf09c6", videoUser.getAccount(), videoUser.getToken(), 0, "", 5, 1);

        LoginInfo loginInfo = new LoginInfo(imUser.getCcid(), imUser.getToken());

        NimChatService.getInstance().doLogin(loginInfo);

        //检查通知权限
        boolean noti = NotificationsUtils.isNotificationEnabled(MAPP.mapp);

        if (!noti && noti != (UserService.getInstance().getUser().getEnable_notice() == 0 ? false : true)) {
            Map<String, Object> params = SignUtils.getNormalParams();
            params.put(MKey.TYPE, SettingNotificationType.enable_notice);
            params.put(MKey.STATUS, noti ? 1 : 0);
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);
            HttpClient.Builder.getGuodongServer().setting(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<RegisterBean>() {
                @Override
                public void onSuccess(BaseBean<RegisterBean> baseBean) {
                    UserService.getInstance().setUser(baseBean.getData().getUser());
                }

                @Override
                public void onError(BaseBean<RegisterBean> baseBean) {
                }
            });
        }
    }


}
