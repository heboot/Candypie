package com.gdlife.candypie.component;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.activitys.auth.AuthIDActivity;
import com.gdlife.candypie.activitys.auth.AuthIndexActivity;
import com.gdlife.candypie.activitys.auth.AuthSkillActivity;
import com.gdlife.candypie.activitys.common.HTMLActivity;
import com.gdlife.candypie.activitys.common.IndexActivity;
import com.gdlife.candypie.activitys.common.SplashActivity;
import com.gdlife.candypie.activitys.common.WelcomeActivity;
import com.gdlife.candypie.activitys.login2register.LoginActivity;
import com.gdlife.candypie.activitys.login2register.RegisterActivity;
import com.gdlife.candypie.activitys.login2register.RegisterCodeActivity;
import com.gdlife.candypie.activitys.login2register.RegisterInfoActivity;
import com.gdlife.candypie.activitys.login2register.RegisterPwdActivity;
import com.gdlife.candypie.activitys.user.UserInfoActivity;
import com.gdlife.candypie.activitys.video.UserVideosActivity;
import com.gdlife.candypie.listener.MyLocationListener;
import com.gdlife.candypie.module.UtilsModule;
import com.gdlife.candypie.module.VideoServiceModule;
import com.gdlife.candypie.serivce.VideoService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by heboot on 2018/1/17.
 */
@Singleton
@Component(modules = {UtilsModule.class, VideoServiceModule.class})
public interface UtilsComponent {

//    void inject(BaseActivity baseActivity);


    void inject(LoginActivity loginActivity);

    void inject(MAPP mapp);

    void inject(SplashActivity splashActivity);

    void inject(VideoService videoService);

    void inject(RegisterPwdActivity registerPwdActivity);

    void inject(RegisterCodeActivity registerCodeActivity);

    void inject(RegisterInfoActivity registerInfoActivity);

    void inject(WelcomeActivity welcomeActivity);

    void inject(RegisterActivity forgetPwdActivity);

    void inject(MyLocationListener myLocationListener);

    void inject(UserInfoActivity userInfoActivity);

    void inject(AuthIDActivity authIDActivity);

    void inject(AuthIndexActivity authIndexActivity);

    void inject(AuthSkillActivity authSkillActivity);

    void inject(UserVideosActivity userVideosActivity);


    void inject(HTMLActivity htmlActivity);

    void inject(IndexActivity indexActivity);

}
