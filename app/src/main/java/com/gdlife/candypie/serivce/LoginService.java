package com.gdlife.candypie.serivce;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.activitys.login2register.LoginActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.LoginType;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.SettingNotificationType;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.utils.InitUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.NotificationsUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.login2register.RegisterBean;
import com.heboot.entity.IMUser;
import com.heboot.entity.User;
import com.heboot.entity.VideoUser;
import com.heboot.event.UserEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import io.agora.AgoraAPI;
import io.agora.AgoraAPIOnlySignal;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/23.
 */

public class LoginService {

    private PlatformActionListener platformActionListener;

    private LoginType loginType;

    private Observer<HashMap> observer;


    private void initListener() {
        if (platformActionListener == null) {
            platformActionListener = new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
                    LogUtil.e("LoginService", JSON.toJSONString(hashMap));
                    LogUtil.e("LoginService", JSON.toJSONString(platform.getDb()));
                    if (action == Platform.ACTION_USER_INFOR) {
                        //通过DB获取各种数据
                        hashMap.put("loginType", loginType);
                        if (loginType == LoginType.WX) {
                            doThirdLogin((String) hashMap.get("nickname"), (String) hashMap.get("headimgurl"), (int) hashMap.get("sex"), (String) hashMap.get("openid"), "weixin", (String) hashMap.get("unionid"), hashMap);
                        } else if (loginType == LoginType.QQ) {
                            hashMap.put("openid", platform.getDb().getToken());
                            doThirdLogin((String) hashMap.get("nickname"), (String) hashMap.get("figureurl_qq_2"), hashMap.get("gender").equals("男") ? 1 : 0, platform.getDb().getToken(), "qq", null, hashMap);
                        }
                    }
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    LogUtil.e("LoginService", JSON.toJSONString(throwable));
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    LogUtil.e("LoginService", "cancel");
                }
            };
        }
    }

    public void doWXLogin(Observer observer) {
        this.observer = observer;
        loginType = LoginType.WX;
        initListener();
        Platform plat = ShareSDK.getPlatform(Wechat.NAME);
        plat.SSOSetting(false);
        plat.showUser(null);
        plat.setPlatformActionListener(platformActionListener);
    }

    public void doQQLogin(Observer observer) {
        this.observer = observer;
        loginType = LoginType.QQ;
        initListener();
        Platform plat = ShareSDK.getPlatform(QQ.NAME);
        plat.SSOSetting(false);
        plat.showUser(null);
        plat.setPlatformActionListener(platformActionListener);
    }

    public void doThirdLogin(String nickName, String headPic, int sex, String openId, String type, String unionid, HashMap map) {
        Map<String, Object> params = SignUtils.getNormalParams();
        params.put(MKey.NICK_NAME, nickName);
        params.put(MKey.HEAD_PIC, headPic);
        params.put(MKey.SEX, sex);
        params.put(MKey.OPENID, openId);
        params.put(MKey.TYPE, type);
        if (!StringUtils.isEmpty(unionid)) {
            params.put(MKey.UNIONID, unionid);
        }
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().third_login(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<RegisterBean>() {
            @Override
            public void onSuccess(BaseBean<RegisterBean> baseBean) {
//                UserService.getInstance().setUser(baseBean.getData().getUser());
                if (baseBean != null && baseBean.getData() != null && !StringUtils.isEmpty(baseBean.getData().getSync_login_id())) {
                    map.put("syncid", baseBean.getData().getSync_login_id());
                    map.put("loginType", loginType);
                    observer.onNext(map);
                } else if (baseBean != null && baseBean.getData() != null && baseBean.getData().getUser() != null) {

                    baseBean.getData().getUser().setSyncMap(map);
                    UserService.getInstance().putSPUser(baseBean.getData().getUser());

                    RxBus.getInstance().post(UserEvent.LOGIN_SUC);
                    doLoginAgora(baseBean.getData().getVideo_user(), baseBean.getData().getIm_user());

                    UserService.getInstance().setUser(baseBean.getData().getUser());
                    RxBus.getInstance().post(UserEvent.LOGIN_SUC);
                    if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                        IntentUtils.toIndexActivity(MAPP.mapp.getCurrentActivity());
                    } else {
                        IntentUtils.toMainActivity(MAPP.mapp.getCurrentActivity());
                    }
                }

            }

            @Override
            public void onError(BaseBean<RegisterBean> baseBean) {
                observer.onError(new Throwable(baseBean.getMessage()));
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
