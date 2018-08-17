package com.gdlife.candypie.activitys.common;

import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;

import com.faceunity.FURenderer;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.databinding.ActivityMainBinding;
import com.gdlife.candypie.fragments.index.IndexFragment;
import com.gdlife.candypie.fragments.message.MessageContainerFragment;
import com.gdlife.candypie.fragments.my.MyFragment;
import com.gdlife.candypie.fragments.rank.RankContainerFragment;
import com.gdlife.candypie.serivce.PushService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.heboot.event.MessageEvent;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.utils.MStatusBarUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.ISupportFragment;


public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private PermissionUtils permissionUtils;

    private PushService pushService = new PushService();

    private IndexFragment indexFragment = IndexFragment.newInstance();

    private RankContainerFragment rankContainerFragment = RankContainerFragment.newInstance();

    private MessageContainerFragment messageContainerFragment = MessageContainerFragment.newInstance();

    private MyFragment myFragment = MyFragment.newInstance();

    private ISupportFragment currentFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityNOLightMode(this);
        permissionUtils = new PermissionUtils();
        pushService = new PushService();
        setSwipeBackEnable(false);
        initBottomMenu();
        setUnreadText();
        mDelegate.loadMultipleRootFragment(binding.flytContainer.getId(), 0, indexFragment, rankContainerFragment, messageContainerFragment, myFragment);
        currentFragment = indexFragment;
    }

    @Override
    public void initData() {
        FURenderer.initFURenderer(this);
        pushService.initHuawei(this);
        DialogUtils.showIndexDialog(this, permissionUtils, false, null);

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
                } else if (o.equals(UserEvent.UPDATE_PROFILE)) {
//                    initLeftUI(false);
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


    @Override
    public void onBackPressedSupport() {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return true;
    }
}
