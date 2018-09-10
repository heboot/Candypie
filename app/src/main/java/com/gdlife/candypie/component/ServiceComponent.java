package com.gdlife.candypie.component;

import com.gdlife.candypie.activitys.auth.AuthCommitActivity;
import com.gdlife.candypie.activitys.auth.AuthSkillActivity;
import com.gdlife.candypie.activitys.message.RecentContactsActivity;
import com.gdlife.candypie.activitys.order.OrderDetailActivity;
import com.gdlife.candypie.activitys.pay.AccountActivity;
import com.gdlife.candypie.activitys.pay.CashActivity;
import com.gdlife.candypie.activitys.pay.CouponsActivity;
import com.gdlife.candypie.activitys.pay.PayActivity;
import com.gdlife.candypie.activitys.pay.RechargeActivity;
import com.gdlife.candypie.activitys.theme.NewThemeActivity;
import com.gdlife.candypie.activitys.theme.ServiceCancelActivity;
import com.gdlife.candypie.activitys.user.HomepageActivity;
import com.gdlife.candypie.activitys.video.AutoPlayActivity;
import com.gdlife.candypie.activitys.video.PlayerActivity2;
import com.gdlife.candypie.activitys.video.UserVideosActivity;
import com.gdlife.candypie.activitys.video.VideoChatActivity;
import com.gdlife.candypie.activitys.video.VideoChatActivity2;
import com.gdlife.candypie.adapter.order.UserOrderAdapter;
import com.gdlife.candypie.module.CommonServiceModule;
import com.gdlife.candypie.module.PayServiceModule;
import com.gdlife.candypie.module.VideoServiceModule;
import com.gdlife.candypie.serivce.OrderService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by heboot on 2018/1/25.
 */
@Singleton
@Component(modules = {VideoServiceModule.class, PayServiceModule.class, CommonServiceModule.class})
public interface ServiceComponent {

    void inject(PayActivity payActivity);

    void inject(RechargeActivity rechargeActivity);

    void inject(CashActivity cashActivity);

    void inject(AccountActivity accountActivity);

    void inject(UserOrderAdapter userOrderAdapter);

    void inject(CouponsActivity couponsActivity);

    void inject(OrderService orderService);

    void inject(OrderDetailActivity orderDetailActivity);

    void inject(ServiceCancelActivity serviceCancelActivity);


    void inject(NewThemeActivity newThemeActivity);

    void inject(AuthCommitActivity authCommitActivity);

    void inject(VideoChatActivity2 authCommitActivity);

    void inject(UserVideosActivity userVideosActivity);

    void inject(HomepageActivity homepageActivity);

    void inject(AuthSkillActivity authSkillActivity);

    void inject(PlayerActivity2 playerActivity2);

    void inject(AutoPlayActivity playerActivity2);

    void inject(VideoChatActivity videoChatActivity);


    void inject(RecentContactsActivity recentContactsActivity);


//    void inject(LoginActivity loginActivity);

//    void inject(WelcomeActivity welcomeActivity);

}
