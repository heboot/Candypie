package com.gdlife.candypie.activitys.message;

import android.support.v4.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityRecentcontactsBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ConfigService;
import com.gdlife.candypie.serivce.UIService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.user.BlackBean;
import com.heboot.entity.User;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.MStatusBarUtils;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/3/19.
 */

public class RecentContactsActivity extends BaseActivity<ActivityRecentcontactsBinding> {

    @Inject
    UIService uiService;
    private RecentContactsFragment recentContactsFragment;

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        binding.includeToolbar.setHideTitle(false);
        binding.includeToolbar.setShowRight(false);
        binding.includeToolbar.tvTitle.setText(getString(R.string.page_title_msg));

        DaggerServiceComponent.builder().build().inject(this);

        uiService.setToolbarRightTextdStyle(binding.includeToolbar.tvRight, false, ContextCompat.getColor(this, R.color.theme_color));


    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

        binding.includeToolbar.vBack.setOnClickListener((v) -> {
            finish();
        });

        recentContactsFragment = (RecentContactsFragment) getSupportFragmentManager().findFragmentById(R.id.recent_contacts_fragment);

        recentContactsFragment.setCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {

            }

            @Override
            public void onItemClick(RecentContact recent) {

                if (ConfigService.getInstance().isZs(recent.getContactId())) {
                    NIMClient.getService(MsgService.class).clearUnreadCount(recent.getContactId(), SessionTypeEnum.P2P);
                    IntentUtils.toSystemMessageActivity(RecentContactsActivity.this);
                } else {
                    IntentUtils.intent2ChatActivity(RecentContactsActivity.this, recent.getContactId());
//                    initLogData(recent.getContactId().replace(MValue.CHAT_PRIEX, ""));

                }


            }

            @Override
            public void onAvatarClick(String id) {


                NimUserInfo user2 = NIMClient.getService(com.netease.nimlib.sdk.uinfo.UserService.class).getUserInfo(id);

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

            }

            @Override
            public String getDigestOfAttachment(RecentContact recent, MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }

        });

        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(NormalEvent.FINISH_PAGE_BY_SELECT_USER)) {
                    finish();
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


    private void initLogData(String uid) {
        params = SignUtils.getNormalParams();
        params.put(MKey.TO_UID, uid);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().black_user_status(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BlackBean>() {
            @Override
            public void onSuccess(BaseBean<BlackBean> baseBean) {
                RxBus.getInstance().post(new UserEvent.UserBlackEvent(baseBean.getData().getIs_black()));
            }

            @Override
            public void onError(BaseBean<BlackBean> baseBean) {
            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_recentcontacts;
    }
}
