package com.netease.nim.uikit.business.session.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.heboot.dialog.TipCustomOneDialog;
import com.heboot.dialog.TipDialog;
import com.heboot.entity.User;
import com.heboot.event.IMEvent;
import com.heboot.event.MessageEvent;
import com.heboot.event.UserEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MStatusBarUtils;
import com.heboot.utils.PreferencesUtils;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.heboot.event.IMEvent.TO_RECHARGE_BY_IM;


/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends BaseMessageActivity {

    private boolean isResume = false;

    private static Boolean hideBottom = false;

    private Boolean hideAgain;

    private TextView tvAgain, tvReport;

    private ImageView ivAgain;

    private View vAgain;

    private String contactId;

    protected Observable<Object> rxObservable;

    private int is_black = 0;

    private int is_im_chat;

    private Disposable disposable;

    /**
     * 可不可以聊天的状态
     */
    private String chatPrice;

    private User currentuser;

    private User selftUser;


    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    /**
     * @param context
     * @param contactId
     * @param customization
     * @param anchor
     * @param hideBottom
     * @param hideAgain
     * @param price         聊天单价
     * @param user
     * @param selfUser
     */
    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor, boolean hideBottom, boolean hideAgain, String price, int is_im_chat, int isBlack, User user, User selfUser) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (hideBottom) {
            intent.putExtra("hideBottom", true);
        }
        if (hideAgain) {
            intent.putExtra("hideAgain", true);
        }
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.putExtra("chatPrice", price);
        intent.putExtra("chatStatus", is_im_chat);
        intent.putExtra("isBlack", isBlack);
        intent.putExtra("user", user);
        intent.putExtra("selfUser", selfUser);
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        hideBottom = (Boolean) getIntent().getExtras().get("hideBottom");
        hideAgain = (Boolean) getIntent().getExtras().get("hideAgain");
        chatPrice = (String) getIntent().getExtras().get("chatPrice");
        is_im_chat = (Integer) getIntent().getExtras().get("chatStatus");
        is_black = (Integer) getIntent().getExtras().get("isBlack");
        currentuser = (User) getIntent().getExtras().getSerializable("user");
        selftUser = (User) getIntent().getExtras().getSerializable("selfUser");
        contactId = (String) getIntent().getExtras().get(Extras.EXTRA_ACCOUNT);
        tvAgain = (TextView) findViewById(R.id.tv_again);
        ivAgain = (ImageView) findViewById(R.id.iv_again);
        tvReport = (TextView) findViewById(R.id.tv_report);
        vAgain = findViewById(R.id.v_again);
        EventBus.getDefault().register(P2PMessageActivity.this);

        rxObservable = RxBus.getInstance().toObserverable();

        rxObservable.subscribe(new io.reactivex.Observer<Object>() {

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Object o) {
                if (o instanceof UserEvent.UserBlackEvent) {
                    is_im_chat = ((UserEvent.UserBlackEvent) o).getIs_black();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        if (hideAgain != null && hideAgain) {
            vAgain.setVisibility(View.GONE);
            ivAgain.setVisibility(View.GONE);
            tvAgain.setVisibility(View.GONE);
        } else {
            vAgain.setVisibility(View.VISIBLE);
            ivAgain.setVisibility(View.VISIBLE);
            tvAgain.setVisibility(View.VISIBLE);
            vAgain.setOnClickListener((v) -> {
                if (hideBottom != null && hideBottom) {
                    return;
                }

                if (is_black == 1) {
                    TipCustomOneDialog tipCustomOneDialog = new TipCustomOneDialog.Builder(this, "当前你们处于屏蔽状态，无法下单", "知道了").create();
                    tipCustomOneDialog.show();
                    return;
                }

                final UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(contactId);


//            LogUtil.e("测试扩展", user2.getExtension());


//            Toast.makeText(this, contactId + "", Toast.LENGTH_SHORT).show();
//                User user = new User();
                currentuser.setId(Integer.parseInt(contactId.replace("cdp", "")));
//                user.setAvatar(userInfo.getAvatar());
                currentuser.setAvatar(userInfo.getAvatar());
                RxBus.getInstance().post(new MessageEvent.TO_AGAIN_ORDER_EVENT(currentuser));
            });
        }


        RxBus.getInstance().post(MessageEvent.REFRESH_UNREAD_NUM_ENENT);


        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        displayOnlineState();
        registerObservers(true);
        registerOnlineStateChangeListener(true);

        /**
         * 如果不可聊
         */
//        if (chatStatus != null && chatStatus.length() > 1) {
//            TipDialog tipDialog = new TipDialog.Builder(this, new Consumer<Integer>() {
//                @Override
//                public void accept(Integer integer) throws Exception {
//                    if (integer == 1) {
//                        RxBus.getInstance().post(new IMEvent.SHOW_IM_GIFT_EVENT(P2PMessageActivity.this, sessionId));
//                    }
//                }
//            }, chatStatus).create();
//            tipDialog.show();
//        }

        if (currentuser != null && !StringUtil.isEmpty(currentuser.getIm_report_tip())) {
            tvReport.setVisibility(View.VISIBLE);
            tvReport.setText(currentuser.getIm_report_tip());
        } else {
            tvReport.setVisibility(View.GONE);
        }


        if (Integer.parseInt(chatPrice) > 0 && Integer.parseInt(selftUser.getCoin()) < Integer.parseInt(chatPrice)) {
            TipDialog tipDialog = new TipDialog.Builder(this, new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    if (integer == 1) {
                        RxBus.getInstance().post(TO_RECHARGE_BY_IM);
                    }
                }
            }, "每条私信消耗" + chatPrice + "个钻石", "知道了", "去充值").create();
            tipDialog.show();
        } else if (Integer.parseInt(chatPrice) > 0) {
            boolean flag = PreferencesUtils.getBoolean(this, "firstTipCoin", false);
            if (!flag) {
                TipCustomOneDialog tipCustomOneDialog = new TipCustomOneDialog.Builder(this, "每条私信消耗" + chatPrice + "个钻石", "知道了", new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 1) {
                            finish();
                        }
                    }
                }).create();
                PreferencesUtils.putBoolean(this, "firstTipCoin", true);
                tipCustomOneDialog.show();
            }

            //            TipDialog tipDialog = new TipDialog.Builder(this, new Consumer<Integer>() {
//                @Override
//                public void accept(Integer integer) throws Exception {
//                    if (integer == 1) {
//                        RxBus.getInstance().post(new IMEvent.SHOW_IM_GIFT_EVENT(P2PMessageActivity.this, sessionId));
//                    }
//                }
//            }, chatStatus).create();
//            tipDialog.show();
        }

    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        registerObservers(false);
        registerOnlineStateChangeListener(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    private void requestBuddyInfo() {
        setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    private UserInfoObserver uinfoObserver;

    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            // 更新 toolbar
            if (accounts.contains(sessionId)) {
                // 按照交互来展示
                displayOnlineState();
            }
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    private void displayOnlineState() {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(sessionId);
        setSubTitle(detailContent);
    }

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, false);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }

        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                Toast.makeText(P2PMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(P2PMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
//        arguments.putSerializable("chatStatus", chatStatus);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new NimToolBarOptions();
        setToolBar(R.id.toolbar, options);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String o) {
//        if (o.equals(IMEvent.SHOW_IM_GIFT_LAYOUT_EVENT)) {
//            dialog.show();
//        } else if (o.equals(IMEvent.HIDE_IM_GIFT_LAYOUT_EVENT)) {
//            dialog.dismiss();
//        }
    }

}
