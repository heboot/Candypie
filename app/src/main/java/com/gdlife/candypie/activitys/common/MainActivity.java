package com.gdlife.candypie.activitys.common;

import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.faceunity.FURenderer;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.databinding.ActivityMainBinding;
import com.gdlife.candypie.fragments.index.IndexFragment;
import com.gdlife.candypie.fragments.message.MessageContainerFragment;
import com.gdlife.candypie.fragments.my.MyFragment;
import com.gdlife.candypie.fragments.rank.RankContainerFragment;
import com.gdlife.candypie.fragments.rank.RankListFragment;
import com.gdlife.candypie.serivce.LeftMenuService;
import com.gdlife.candypie.serivce.PushService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ani.IndexAniUtils;
import com.heboot.event.DiscoverEvent;
import com.heboot.event.MessageEvent;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.utils.MStatusBarUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportHelper;


public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private PermissionUtils permissionUtils;

    private PushService pushService = new PushService();

    private IndexFragment indexFragment = IndexFragment.newInstance();

    private RankContainerFragment rankContainerFragment = RankContainerFragment.newInstance();

    private MessageContainerFragment messageContainerFragment = MessageContainerFragment.newInstance();

    private MyFragment myFragment = MyFragment.newInstance();

    private ISupportFragment currentFragment;

    private List<ImageView> bottomIcons = new ArrayList<>();

    private List<TextView> bottomTvs = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this, 0x80221B33);
        MStatusBarUtils.setActivityNOLightMode(this);
        permissionUtils = new PermissionUtils();
        pushService = new PushService();
        setSwipeBackEnable(false);
        initBottomMenu();

        mDelegate.loadMultipleRootFragment(binding.flytContainer.getId(), 0, indexFragment, rankContainerFragment, messageContainerFragment, myFragment);

        currentFragment = indexFragment;
//        mDelegate.loadRootFragment(binding.flytContainer.getId(), indexFragment);
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
        binding.includeBottomMenu.includeMenuIndex.ivImg.setSelected(true);
    }

    private void checkBottomMenu(int index) {
        switch (index) {
            case 0:
                binding.includeBottomMenu.includeMenuIndex.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_main_fouse);
                binding.includeBottomMenu.includeMenuRank.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_rank_normal);
                binding.includeBottomMenu.includeMenuMsg.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_msg_normal);
                binding.includeBottomMenu.includeMenuMy.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_my_normal);
                break;
            case 1:
                binding.includeBottomMenu.includeMenuIndex.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_main_normal);
                binding.includeBottomMenu.includeMenuRank.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_rank_fouse);
                binding.includeBottomMenu.includeMenuMsg.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_msg_normal);
                binding.includeBottomMenu.includeMenuMy.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_my_normal);
                break;
            case 2:
                binding.includeBottomMenu.includeMenuIndex.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_main_normal);
                binding.includeBottomMenu.includeMenuRank.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_rank_normal);
                binding.includeBottomMenu.includeMenuMsg.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_msg_fouse);
                binding.includeBottomMenu.includeMenuMy.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_my_normal);
                break;
            case 3:
                binding.includeBottomMenu.includeMenuIndex.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_main_normal);
                binding.includeBottomMenu.includeMenuRank.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_rank_normal);
                binding.includeBottomMenu.includeMenuMsg.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_msg_normal);
                binding.includeBottomMenu.includeMenuMy.ivImg.setBackgroundResource(R.drawable.icon_bottom_menu_my_fouse);
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
            binding.includeBottomMenu.tvUnread2.setText(String.valueOf(unread));
            binding.includeBottomMenu.tvUnread2.setVisibility(View.VISIBLE);


        } else {
            binding.includeBottomMenu.tvUnread2.setVisibility(View.INVISIBLE);

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
