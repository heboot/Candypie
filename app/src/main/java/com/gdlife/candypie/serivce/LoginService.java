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
import com.heboot.bean.login2register.RegisterBean;
import com.heboot.entity.IMUser;
import com.heboot.entity.User;
import com.heboot.entity.VideoUser;
import com.heboot.utils.LogUtil;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.Map;

import io.agora.AgoraAPI;
import io.agora.AgoraAPIOnlySignal;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/23.
 */

public class LoginService {

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
