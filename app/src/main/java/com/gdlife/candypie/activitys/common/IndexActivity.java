package com.gdlife.candypie.activitys.common;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.faceunity.FURenderer;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.index.IndexPopMenuAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.BaseObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.NumEventKeys;
import com.gdlife.candypie.common.RecordVideoFrom;
import com.gdlife.candypie.databinding.ActivityIndexBinding;
import com.gdlife.candypie.http.HttpCallBack;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.AuthService;
import com.gdlife.candypie.serivce.LeftMenuService;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.GlideImageLoader;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ani.IndexAniUtils;
import com.heboot.base.BaseBean;
import com.heboot.bean.index.IndexBean;
import com.heboot.bean.index.IndexPopTipBean;
import com.heboot.event.MessageEvent;
import com.heboot.event.NormalEvent;
import com.heboot.event.UserEvent;
import com.heboot.utils.LogUtil;
import com.heboot.utils.MStatusBarUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.analytics.MobclickAgent;
import com.yalantis.dialog.TipCustomDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by heboot on 2018/1/31.
 */

public class IndexActivity extends BaseActivity<ActivityIndexBinding> implements EasyPermissions.PermissionCallbacks {

    private IndexPopMenuAdapter indexPopMenuAdapter;

    private IndexAniUtils indexAniUtils;

    private Boolean womenReg = false;

    private TipCustomDialog womenTipDialog;

    private LeftMenuService leftMenuService = new LeftMenuService();

    @Inject
    PermissionUtils permissionUtils;

    @Override
    public void initUI() {
        MValue.IndexBannerEnable = true;
        QMUIStatusBarHelper.translucent(this);
        MStatusBarUtils.setActivityLightMode(this);
        indexAniUtils = new IndexAniUtils();
        initLeftUI(true);
        setSwipeBackEnable(false);

        if (getIntent() != null && getIntent().getExtras() != null) {
            womenReg = (boolean) getIntent().getExtras().get(MKey.SEX);
        }


        if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() == MValue.AUTH_STATUS_SUC && UserService.getInstance().getUser().getRole() == MValue.USER_ROLE_SERVICER) {
            binding.ivToServicerIndex.setVisibility(View.VISIBLE);
            binding.vToServicerIndex.setVisibility(View.VISIBLE);
            binding.tvToAuth.setVisibility(View.GONE);
        } else {
            binding.ivToServicerIndex.setVisibility(View.GONE);
            binding.vToServicerIndex.setVisibility(View.GONE);
            binding.tvToAuth.setVisibility(View.VISIBLE);
        }
        FURenderer.initFURenderer(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 已经登录过了，处理过来的请求
        if (intent != null) {
            if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
//                parseNotifyIntent(intent);
                LogUtil.e(TAG, JSON.toJSONString(intent));
                return;
            }
        }

        super.onNewIntent(intent);
    }


    private void initNotifacationUI(IndexBean indexBean) {
        binding.ttt.setTipList(generateTips(indexBean));
    }

    private void initLeftUI(boolean isFirst) {
        leftMenuService.initLeftUI(binding.includeLeft, UserService.getInstance().getUser(), isFirst);
//        ImageUtils.showImage(binding.includeLeft.ivHead, UserService.getInstance().getUser() == null ? "" : UserService.getInstance().getUser().getAvatar());
    }

    @Override
    public void initData() {

        MAPP.getUtilsComponent().inject(this);

        if (!womenReg) {
            DialogUtils.showIndexDialog(this, permissionUtils, false, null);
        }

        if (UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getSex() == 0 && UserService.getInstance().getUser().getService_auth_status() != null && UserService.getInstance().getUser().getService_auth_status() != MValue.AUTH_STATUS_SUC) {
            if (womenTipDialog == null) {
                womenTipDialog = new TipCustomDialog.Builder(this, new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 0) {
                            womenTipDialog.dismiss();
                        } else {
                            IntentUtils.toAuthIndexActivity(IndexActivity.this, RecordVideoFrom.AUTH);
                        }
                    }
                }, "终于等到你。完成视频认证，躺着也能赚钱啦", "知道了", "去认证").create();
            }
            womenTipDialog.show();
        }


        initIndexData();


        binding.includeMenu.rvList.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        binding.includeMenu.rvList.setItemAnimator(new SlideInUpAnimator());

        binding.includeMenu.rvList.getItemAnimator().setAddDuration(200);


        int unread = NIMClient.getService(MsgService.class)
                .getTotalUnreadCount();

        if (unread > 0) {
            binding.tvUnread.setText(String.valueOf(unread));
            binding.tvUnread.setVisibility(View.VISIBLE);
            binding.ivUnread.setVisibility(View.VISIBLE);
        } else {
            binding.tvUnread.setVisibility(View.INVISIBLE);
            binding.ivUnread.setVisibility(View.INVISIBLE);
        }


    }

    private void initIndexData() {
        params = SignUtils.getNormalParams();
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().index(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new BaseObserver(new HttpCallBack<IndexBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onSuccess(IndexBean indexBean) {
                if (indexPopMenuAdapter == null) {
                    indexPopMenuAdapter = new IndexPopMenuAdapter(true, ThemeService.getHotThemeList(), indexBean.getVideo_chat_avatar_list()); //ThemeService.getHotThemeList(),
                    binding.includeMenu.rvList.setAdapter(new AlphaInAnimationAdapter(indexPopMenuAdapter));
                }
                initBanner(indexBean);
                initNotifacationUI(indexBean);
                /**
                 * 因为这个页面以后展示的话只有审核的时候  不出理这个弹窗
                 */
//                if (indexBean.getPopup_tip() != null && !womenReg) {
//                    FreeVideoDialog freeVideoDialog = new FreeVideoDialog.Builder(IndexActivity.this, indexBean.getPopup_tip(), ).create();
//                    freeVideoDialog.show();
//                }
            }


            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onError(BaseBean<IndexBean> basebean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(IndexActivity.this, basebean.getMessage(), true);
                tipDialog.show();
            }

        }));
    }

    private void showLeftAni() {
        indexAniUtils.showLeftAni2(
                binding.includeLeft.includeMenu1.getRoot(),
                binding.includeLeft.includeMenu2.getRoot(),
                binding.includeLeft.includeMenu3.getRoot(),
                binding.includeLeft.includeMenu4.getRoot(),
                binding.includeLeft.includeMenu5.getRoot(), binding.includeLeft.includeMenuSetprice.getRoot(), binding.includeLeft.includeMenuEnableVideo.getRoot());
//                binding.includeLeft.includeMenu6.getRoot());
    }


    private void initBanner(IndexBean indexBean) {

        List<IndexPopTipBean> images = new ArrayList<>();

        for (IndexPopTipBean bean : indexBean.getTop_ad_list()) {
            images.add(bean);
        }

        //设置图片加载器
        binding.ivBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        binding.ivBanner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        binding.ivBanner.start();


    }


    private List<String> generateTips(IndexBean indexBean) {
        List<String> result = ThemeService.getIndexBottomTip(indexBean.getTt_message_list());
        return result;
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
                    ImageUtils.showAvatar(binding.includeLeft.ivHead, UserService.getInstance().getUser().getAvatar());
                } else if (o.equals(UserEvent.UPDATE_PROFILE)) {
                    initLeftUI(true);
                } else if (o.equals(MessageEvent.REFRESH_UNREAD_NUM_ENENT)) {

                    int unread = NIMClient.getService(MsgService.class)
                            .getTotalUnreadCount();

                    if (unread > 0) {
                        binding.tvUnread.setText(String.valueOf(unread));
                        binding.tvUnread.setVisibility(View.VISIBLE);
                        binding.ivUnread.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvUnread.setVisibility(View.INVISIBLE);
                        binding.ivUnread.setVisibility(View.INVISIBLE);
                    }


                    initLeftUI(false);

                } else if (o.equals(UserEvent.AUTH_SERVICE_SUC_EVENT)) {
                    binding.ivToServicerIndex.setVisibility(View.VISIBLE);
                    binding.vToServicerIndex.setVisibility(View.VISIBLE);
                    binding.tvToAuth.setVisibility(View.GONE);
                    initLeftUI(false);
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


        binding.dlytContainer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if (slideOffset > 0.5f) {
                    int unread = NIMClient.getService(MsgService.class)
                            .getTotalUnreadCount();

                    if (unread > 0) {
                        binding.tvUnread.setText(String.valueOf(unread));
                        binding.tvUnread.setVisibility(View.VISIBLE);
                        binding.ivUnread.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvUnread.setVisibility(View.INVISIBLE);
                        binding.ivUnread.setVisibility(View.INVISIBLE);
                    }
                }

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                MobclickAgent.onEvent(IndexActivity.this, NumEventKeys.left_menu_show.toString());
                indexPopMenuAdapter.setEnable(false);
                binding.includeMenu.getRoot().setEnabled(false);
                binding.includeMenu.rvList.setEnabled(false);
                MValue.IndexBannerEnable = false;
                showLeftAni();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                indexPopMenuAdapter.setEnable(true);
                binding.ivBanner.setEnabled(true);
                MValue.IndexBannerEnable = true;
                int unread = NIMClient.getService(MsgService.class)
                        .getTotalUnreadCount();

                if (unread > 0) {
                    binding.tvUnread.setText(String.valueOf(unread));
                    binding.tvUnread.setVisibility(View.VISIBLE);
                    binding.ivUnread.setVisibility(View.VISIBLE);
                } else {
                    binding.tvUnread.setVisibility(View.INVISIBLE);
                    binding.ivUnread.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        binding.vMy.setOnClickListener((v) -> {
//            String edata = "{\"turntable_config\":{\"user_turntable_id\":\"1200\",\"status\":\"0\",\"status_message\":\"发起\",\"items\":[{\"type\":\"gift\",\"goods_id\":\"2\",\"radian\":\"72\",\"goods\":{\"id\":\"2\",\"img\":\"http://img.guodonglife.cn/public/uploads/images/20180522/d160d49283485517859009ac4dcfd816.png\",\"price\":\"5\",\"title\":\"得到\"}},{\"type\":\"action\",\"goods_id\":\"2\",\"radian\":\"72\",\"goods\":{\"id\":\"2\",\"img\":\"http://img.guodonglife.cn/public/uploads/images/20180608/94bb4fcdcea8004c2835c5009c6e7c56.png\",\"title\":\"真心话\"}},{\"type\":\"gift\",\"goods_id\":\"3\",\"radian\":\"72\",\"goods\":{\"id\":\"3\",\"img\":\"http://img.guodonglife.cn/public/uploads/images/20180522/3a74bf43429bf64211613ddb7e46b94f.png\",\"price\":\"10\",\"title\":\"得到\"}},{\"type\":\"action\",\"goods_id\":\"3\",\"radian\":\"72\",\"goods\":{\"id\":\"3\",\"title\":\"你来唱首歌\"}},{\"type\":\"gift\",\"goods_id\":\"4\",\"radian\":\"36\",\"goods\":{\"id\":\"4\",\"img\":\"http://img.guodonglife.cn/public/uploads/images/20180522/277b8a48a9e2a72d89658aec0f2d9334.png\",\"price\":\"20\",\"title\":\"得到\"}},{\"type\":\"action\",\"goods_id\":\"1\",\"radian\":\"36\",\"goods\":{\"id\":\"1\",\"title\":\"飞吻\"}}]}}";
//            TurntableConfigBean turntableConfigBean = new Gson().fromJson(edata,TurntableConfigBean.class);
//            LuckpanDialog luckpanDialog = new LuckpanDialog.Builder(this,"",turntableConfigBean.getTurntable_config(),false,0,3).create();
//            luckpanDialog.show();
            binding.dlytContainer.openDrawer(Gravity.LEFT);
        });

        binding.vToServicerIndex.setOnClickListener((v) -> {
            if (!permissionUtils.hasNoticicationPermission()) {
                permissionUtils.showPermissionDialog(this, null);
                return;
            }
//            IntentUtils.toIndexServicerContainerActivity(this, null);
        });

        binding.tvToAuth.setOnClickListener((v) -> {
            if (UserService.getInstance().checkTourist()) {
                return;
            }
            AuthService.toAuthPageByIndex(this);
        });

    }


    @Override
    protected void onResume() {
        int unread = NIMClient.getService(SystemMessageService.class)
                .querySystemMessageUnreadCountBlock();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (indexPopMenuAdapter != null) {
            indexPopMenuAdapter.stopAni();
        }
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    //某些权限已被授予
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        LogUtil.e(TAG, perms.size() + "---" + JSON.toJSONString(perms));
    }

    //某些权限已被拒绝
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this, "what").build().show();
            permissionUtils.showPermissionDialog(this, null);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_index;
    }
}
