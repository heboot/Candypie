package com.gdlife.candypie.fragments.message;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.message.RecentContactsActivity;
import com.gdlife.candypie.base.BaseFragment;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.FragmentMessageBinding;
import com.gdlife.candypie.serivce.ConfigService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.heboot.entity.User;
import com.heboot.event.MessageEvent;
import com.heboot.event.NormalEvent;
import com.heboot.faceunity_unit.fulivedemo.utils.ToastUtil;
import com.heboot.rxbus.RxBus;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MessageFragment extends BaseFragment<FragmentMessageBinding> {

    private RecentContactsFragment recentContactsFragment;

    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        RxBus.getInstance().post(MessageEvent.REFRESH_UNREAD_NUM_ENENT);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void initUI() {


        recentContactsFragment = (RecentContactsFragment) getChildFragmentManager().findFragmentById(R.id.recent_contacts_fragment);
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
                    IntentUtils.toSystemMessageActivity(_mActivity);
                } else {
                    IntentUtils.intent2ChatActivity(_mActivity, recent.getContactId());
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


    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
