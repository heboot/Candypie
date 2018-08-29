package com.gdlife.candypie.activitys.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.faceunity.FURenderer;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.databinding.ActivityMainBinding;
import com.gdlife.candypie.fragments.index.IndexFragment;
import com.gdlife.candypie.fragments.message.MessageContainerFragment;
import com.gdlife.candypie.fragments.my.MyFragment;
import com.gdlife.candypie.fragments.rank.RankContainerFragment;
import com.gdlife.candypie.serivce.PushService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.aservice.ForegroundService;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.utils.AudioUtil;
import com.gdlife.candypie.utils.AudioUtil2;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.ScreenManager;
import com.gdlife.candypie.utils.ScreenReceiverUtil;
import com.heboot.entity.User;
import com.heboot.event.MessageEvent;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MStatusBarUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.ISupportFragment;


public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private PermissionUtils permissionUtils;

    private PushService pushService = new PushService();

    private IndexFragment indexFragment = IndexFragment.newInstance();

    private RankContainerFragment rankContainerFragment = RankContainerFragment.newInstance();

    private MessageContainerFragment messageContainerFragment = MessageContainerFragment.newInstance();

    private MyFragment myFragment = MyFragment.newInstance();

    private ISupportFragment currentFragment;

    private VideoChatService videoChatService;


    /**
     * 保活相关
     */
    // 动态注册锁屏等广播
    private ScreenReceiverUtil mScreenListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initUI() {
        LogUtil.e(TAG, "init ui");
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityNOLightMode(this);
        permissionUtils = new PermissionUtils();
        pushService = new PushService();
        setSwipeBackEnable(false);
        initBottomMenu();
        setUnreadText();
        mDelegate.loadMultipleRootFragment(binding.flytContainer.getId(), 0, indexFragment, rankContainerFragment, messageContainerFragment, myFragment);
        currentFragment = indexFragment;
        startFService();
        AudioUtil2.getInstance(this);
    }

    @Override
    public void initData() {
        pushService.initPushService();
        DialogUtils.showIndexDialog(this, permissionUtils, false, null);

        mScreenListener = new ScreenReceiverUtil(this);
        mScreenManager = ScreenManager.getScreenManagerInstance(this);
        mScreenListener.setScreenReceiverListener(mScreenListenerer);
//



    }

    @Override
    public void initListener() {


        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(UserEvent.LOGIN_SUC)) {
//                    ImageUtils.showAvatar(binding.includeLeft.ivHead, UserService.getInstance().getUser().getAvatar());
                } else if (o.equals(MessageEvent.REFRESH_UNREAD_NUM_ENENT)) {
//                    initLeftUI(false);
                    setUnreadText();
                } else if (o.equals(UserEvent.AUTH_SERVICE_SUC_EVENT)) {
//                    initLeftUI(false);
                } else if (o.equals(NormalEvent.FINISH_INDEX_PAGE)) {
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


        binding.includeBottomMenu.includeMenuRank.getRoot().setOnClickListener((v) -> {
            checkBottomMenu(1);
            mDelegate.showHideFragment(rankContainerFragment, currentFragment);
            currentFragment = rankContainerFragment;
        });


        binding.includeBottomMenu.includeMenuIndex.getRoot().setOnClickListener((v) -> {
            checkBottomMenu(0);
            mDelegate.showHideFragment(indexFragment, currentFragment);
            currentFragment = indexFragment;
        });


        binding.includeBottomMenu.includeMenuMsg.getRoot().setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            checkBottomMenu(2);
            mDelegate.showHideFragment(messageContainerFragment, currentFragment);
            currentFragment = messageContainerFragment;
        });


        binding.includeBottomMenu.includeMenuMy.getRoot().setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            checkBottomMenu(3);
            if (myFragment == null) {
                myFragment = MyFragment.newInstance();
            }
            mDelegate.showHideFragment(myFragment, currentFragment);
            currentFragment = myFragment;
        });

    }

    /**
     * 初始化底部菜单
     */
    private void initBottomMenu() {
        binding.includeBottomMenu.includeMenuIndex.ivImg.setBackgroundResource(R.drawable.selector_bottom_menu_main);
        binding.includeBottomMenu.includeMenuRank.ivImg.setBackgroundResource(R.drawable.selector_bottom_menu_rank);
        binding.includeBottomMenu.includeMenuMsg.ivImg.setBackgroundResource(R.drawable.selector_bottom_menu_msg);
        binding.includeBottomMenu.includeMenuMy.ivImg.setBackgroundResource(R.drawable.selector_bottom_menu_my);


        binding.includeBottomMenu.includeMenuIndex.tvMenuName.setText("首页");
        binding.includeBottomMenu.includeMenuRank.tvMenuName.setText("榜单");
        binding.includeBottomMenu.includeMenuMsg.tvMenuName.setText("消息");
        binding.includeBottomMenu.includeMenuMy.tvMenuName.setText("我的");

        binding.includeBottomMenu.includeMenuIndex.ivImg.setSelected(true);

    }

    /**
     * 选择底部菜单
     *
     * @param index
     */
    private void checkBottomMenu(int index) {
        switch (index) {
            case 0:
                binding.includeBottomMenu.includeMenuIndex.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_main_fouse);
                binding.includeBottomMenu.includeMenuRank.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_rank_normal);
                binding.includeBottomMenu.includeMenuMsg.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_msg_normal);
                binding.includeBottomMenu.includeMenuMy.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_my_normal);
                binding.includeBottomMenu.includeMenuIndex.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                binding.includeBottomMenu.includeMenuRank.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                binding.includeBottomMenu.includeMenuMsg.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                binding.includeBottomMenu.includeMenuMy.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                break;
            case 1:
                binding.includeBottomMenu.includeMenuIndex.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_main_normal);
                binding.includeBottomMenu.includeMenuRank.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_rank_fouse);
                binding.includeBottomMenu.includeMenuMsg.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_msg_normal);
                binding.includeBottomMenu.includeMenuMy.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_my_normal);
                binding.includeBottomMenu.includeMenuIndex.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                binding.includeBottomMenu.includeMenuRank.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                binding.includeBottomMenu.includeMenuMsg.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                binding.includeBottomMenu.includeMenuMy.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                break;
            case 2:
                binding.includeBottomMenu.includeMenuIndex.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_main_normal);
                binding.includeBottomMenu.includeMenuRank.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_rank_normal);
                binding.includeBottomMenu.includeMenuMsg.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_msg_fouse);
                binding.includeBottomMenu.includeMenuMy.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_my_normal);
                binding.includeBottomMenu.includeMenuIndex.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                binding.includeBottomMenu.includeMenuRank.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                binding.includeBottomMenu.includeMenuMsg.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                binding.includeBottomMenu.includeMenuMy.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                break;
            case 3:
                binding.includeBottomMenu.includeMenuIndex.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_main_normal);
                binding.includeBottomMenu.includeMenuRank.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_rank_normal);
                binding.includeBottomMenu.includeMenuMsg.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_msg_normal);
                binding.includeBottomMenu.includeMenuMy.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_my_fouse);
                binding.includeBottomMenu.includeMenuIndex.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                binding.includeBottomMenu.includeMenuRank.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                binding.includeBottomMenu.includeMenuMsg.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.color_898A9E));
                binding.includeBottomMenu.includeMenuMy.tvMenuName.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void setUnreadText() {
        int unread = NIMClient.getService(MsgService.class)
                .getTotalUnreadCount();
        if (unread > 0) {
            if (unread > 99) {
                unread = 99;
            }
            binding.includeBottomMenu.includeMenuMsg.tvUnread2.setText(String.valueOf(unread));
            binding.includeBottomMenu.includeMenuMsg.tvUnread2.setVisibility(View.VISIBLE);


        } else {
            binding.includeBottomMenu.includeMenuMsg.tvUnread2.setVisibility(View.INVISIBLE);

        }
    }


    public void postVideoService(User user) {
        if (videoChatService == null) {
            videoChatService = new VideoChatService();
        }
        videoChatService.postVideoService(permissionUtils, this, user, null);
    }


    @Override
    public void onBackPressedSupport() {

    }

    private void startFService() {
        Intent intent = new Intent(this, ForegroundService.class);
        startService(intent);
    }

    private void stopFService() {
        Intent intent = new Intent(this, ForegroundService.class);
        stopService(intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        stopFService();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (myFragment != null) {
            myFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private ScreenReceiverUtil.SreenStateListener mScreenListenerer = new ScreenReceiverUtil.SreenStateListener() {
        @Override
        public void onSreenOn() {
            // 移除"1像素"
            mScreenManager.finishActivity();
        }

        @Override
        public void onSreenOff() {
            // 接到锁屏广播，将SportsActivity切换到可见模式
            // "咕咚"、"乐动力"、"悦动圈"就是这么做滴
//            Intent intent = new Intent(SportsActivity.this,SportsActivity.class);
//            startActivity(intent);
            // 如果你觉得，直接跳出SportActivity很不爽
            // 那么，我们就制造个"1像素"惨案
            mScreenManager.startActivity();
        }

        @Override
        public void onUserPresent() {
            // 解锁，暂不用，保留
        }
    };

}
