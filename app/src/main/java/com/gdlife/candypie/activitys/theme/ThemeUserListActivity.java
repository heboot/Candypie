package com.gdlife.candypie.activitys.theme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.adapter.theme.ThemeUserListAdapter;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.PayFrom;
import com.gdlife.candypie.common.ServiceSelectUserFrom;
import com.gdlife.candypie.common.VideoChatFrom;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityThemeUserlistBinding;
import com.gdlife.candypie.databinding.LayoutUserAvatarNameBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.ConfigService;
import com.gdlife.candypie.serivce.LeftMenuService;
import com.gdlife.candypie.serivce.OrderService;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UpdateVersionService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.ObserableUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.ani.IndexAniUtils;
import com.gdlife.candypie.utils.rv.RVUtils;
import com.gdlife.candypie.widget.common.TipDialog;
import com.gdlife.candypie.widget.dialog.ServiceTipDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.theme.PostThemeBean;
import com.heboot.bean.theme.PushUserListBean;
import com.heboot.entity.User;
import com.heboot.event.MessageEvent;
import com.heboot.event.NormalEvent;
import com.heboot.event.OrderEvent;
import com.heboot.event.ThemeEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.heboot.utils.ViewUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

/**
 * Created by heboot on 2018/2/5.
 */

public class ThemeUserListActivity extends BaseActivity<ActivityThemeUserlistBinding> {

    private ThemeUserListAdapter adapter;

    private int selectedNum = 0;

    private String userServiceId;

    private PostThemeBean postThemeBean;

    private TipDialog mtipDialog;

    private PushUserListBean mpushUserListBean;

    private ServiceTipDialog serviceTipDialog;

    @Inject
    OrderService orderService;

    private IndexAniUtils indexAniUtils;

    private LeftMenuService leftMenuService = new LeftMenuService();

    //是否是视频订单
    private boolean isVideo;

    private Observable pushObservable, selectObservable;

    private String serverTime;

    private Animation rotateAni;

    private ValueAnimator widthAni, heightAni;

    private ServiceSelectUserFrom from;

    private MHandler timeHandler = new MHandler(new WeakReference<>(this));

    private boolean first = true;

    private Disposable selectDisposable;

    private String serviceType = "", serviceId;


    /**
     * 是否是未完成订单进来的
     */
    private boolean isRunning;

    /**
     * 头像集合
     */
    private List<LayoutUserAvatarNameBinding> avatarViews = new ArrayList<>();

    private List<User> users = new ArrayList<>();

    private List<User> users2 = new ArrayList<>();


    @Override
    public void initUI() {
        QMUIStatusBarHelper.translucent(this);

        binding.rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        binding.rvList.setItemAnimator(new SlideInRightAnimator());

        binding.rvList.getItemAnimator().setAddDuration(500);

        isVideo = getIntent().getBooleanExtra(MKey.TYPE, false);

        from = (ServiceSelectUserFrom) getIntent().getExtras().get(MKey.FROM);


        setSwipeBackEnable(false);
//        binding.ivBack.setVisibility(View.GONE);
        binding.ivLeft.setVisibility(View.VISIBLE);


        if (isVideo) {
            binding.clytBottom.setVisibility(View.GONE);
        }


        initLeftUI(true);

    }

    private void initLeftUI(boolean isFirst) {
        leftMenuService.initLeftUI(binding.includeLeft, UserService.getInstance().getUser(), isFirst);
    }

    @Override
    public void initData() {


        isRunning = getIntent().getBooleanExtra(MKey.IS_RUNNING, false);

        if (isRunning) {
            UpdateVersionService.checkVersionUpdate(this, null);
        }


        RVUtils.initSwiperefreshlayout(binding.spLayout, null);

        DaggerServiceComponent.builder().build().inject(this);

        indexAniUtils = new IndexAniUtils();

        userServiceId = getIntent().getStringExtra(MKey.SERVICE_ID);

        initUserListData();

        if (from == ServiceSelectUserFrom.NEW_SERVICE) {
            doProgressAni();
            initProgressAniData();
        } else {
            first = false;
            binding.includeProgress.getRoot().setVisibility(View.GONE);
        }


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


    private void initProgressAniData() {
        avatarViews.add(binding.includeProgress.includeAvatar5);
        avatarViews.add(binding.includeProgress.includeAvatar6);
        avatarViews.add(binding.includeProgress.includeAvatar3);
        avatarViews.add(binding.includeProgress.includeAvatar4);
        avatarViews.add(binding.includeProgress.includeAvatar1);
        avatarViews.add(binding.includeProgress.includeAvatar2);
        users.addAll(MAPP.mapp.getConfigBean().getRecommend_user_list());
        users2.addAll(MAPP.mapp.getConfigBean().getRecommend_user_list());
    }

    int imager;

    private void initAvatarAni() {
        for (int i = 0; i < avatarViews.size(); i++) {
            imager = new Random().nextInt(avatarViews.size());
            if (imager == 0) {
                imager = 1;
            }
            ImageUtils.showImage(avatarViews.get(i).ivAvatar, users.get(i).getAvatar());
            avatarViews.get(i).tvName.setText(users.get(i).getNickname());

            View cv = avatarViews.get(i).clytContainer;

            Observable.just("Amit")
                    //延时两秒，第一个参数是数值，第二个参数是事件单位
                    .delay(imager * 800, TimeUnit.MILLISECONDS)
                    // Run on a background thread
                    .subscribeOn(AndroidSchedulers.mainThread())
                    // Be notified on the main thread
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            payInAni(cv, 1500);
                        }
                    });//这里的观察者依然不重要

//            payInAni(avatarViews.get(i).clytContainer, 1500, imager * 1800);
            users2.remove(users.get(i));
        }
    }

    private void initAvatarAni2() {
        for (int i = 0; i < 2; i++) {
            int imager = new Random().nextInt(avatarViews.size());
            int userr = new Random().nextInt(users2.size());

            View cv = avatarViews.get(imager).clytContainer;

            int index = i;

            Observable.just("Amit")
                    //延时两秒，第一个参数是数值，第二个参数是事件单位
                    .delay(imager * 800, TimeUnit.MILLISECONDS)
                    // Run on a background thread
                    .subscribeOn(AndroidSchedulers.mainThread())
                    // Be notified on the main thread
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            payOutAni(avatarViews.get(imager).getRoot(), 1000, users.get(users.size() - index - 1), avatarViews.get(imager).ivAvatar, avatarViews.get(imager).tvName);
                        }
                    });//这里的观察者依然不重要
        }
    }

    private void payInAni(View v, int mis) {
        YoYo.with(Techniques.BounceIn).duration(mis).playOn(v);
    }

    private void payOutAni(View v, int mis, User u, ImageView iv, TextView tv) {
        YoYo.with(Techniques.BounceIn).duration(mis).playOn(v);
        ImageUtils.showImage(iv, u.getAvatar());
        LogUtil.e(TAG, JSON.toJSONString(u));
        tv.setText(u.getNickname());
    }


    private void payInAni(View v, int mis, int day) {
        YoYo.with(Techniques.FadeIn).pivotX(0.5f).pivotY(0.5f).duration(mis).delay(day).playOn(v);
    }

    private void initUserListData() {
        params = SignUtils.getNormalParams();

        params.put(MKey.USER_SERVICE_ID, userServiceId);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);

        HttpClient.Builder.getGuodongServer().push_user_list(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<PushUserListBean>() {
            @Override
            public void onSuccess(BaseBean<PushUserListBean> baseBean) {
                serviceType = baseBean.getData().getService_type();
                serviceId = baseBean.getData().getService_id();
                if (serviceType.equals(MValue.ORDER_TYPE_VIDEO)) {
                    isVideo = true;
                    binding.clytBottom.setVisibility(View.GONE);
                }
                serverTime = baseBean.getTime();
                binding.spLayout.setRefreshing(false);
                mpushUserListBean = baseBean.getData();

                if (adapter != null && adapter.getData() != null && adapter.getData().size() > 0) {
                    if (baseBean.getData().getList() != null && baseBean.getData().getList().size() > 0) {
                        adapter.getData().clear();
                        adapter.notifyDataSetChanged();
                    }
                    if (adapter.getData().size() == 1 && (ConfigService.getInstance().isZL(String.valueOf(adapter.getData().get(0).getId())) || ConfigService.getInstance().isZL(String.valueOf(adapter.getData().get(adapter.getItemCount() - 1).getId())))) {
                        adapter.getData().clear();
                        adapter.notifyDataSetChanged();
                    }
                }

                setUI(mpushUserListBean);
                initTimeUI();
            }

            @Override
            public void onError(BaseBean<PushUserListBean> baseBean) {
                if (tipDialog != null && tipDialog.isShowing()) {
                    tipDialog.dismiss();
                }
                tipDialog = DialogUtils.getFailDialog(ThemeUserListActivity.this, baseBean.getMessage(), true);
                tipDialog.show();
            }
        });
    }

    Long pushTime, selectTime;

    private void initTimeUI() {

        if (selectObservable != null || pushObservable != null) {
            return;
        }

        pushTime = ThemeService.getServiceUserListPushTime(mpushUserListBean.getStart_push_time(), mpushUserListBean.getPush_time_interval(), serverTime);
        selectTime = ThemeService.getServiceUserListSelectTime(false, pushTime, String.valueOf(mpushUserListBean.getSelect_time_interval()));

        if (pushTime <= 0) {
            if (selectTime == null) {
                if (serviceTipDialog == null) {
                    serviceTipDialog = new ServiceTipDialog.Builder(this, new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            if (integer == 1) {
                                IntentUtils.toNewThemeServiceActivity(ThemeUserListActivity.this, ThemeService.getServiceBeanById(serviceId), mpushUserListBean.getService_type().equals(MValue.ORDER_TYPE_VIDEO) ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_NORMAL, null);
                            } else {
                                if (isRunning) {
                                    if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                                        IntentUtils.toIndexActivity(ThemeUserListActivity.this);
                                    } else {
                                        IntentUtils.toIndexUsersActivity(ThemeUserListActivity.this);
                                    }
                                    finish();
                                } else {
                                    finish();
                                }
                            }

                        }
                    }, MValue.TIP_DIALOG_TYPE.TIMEOUT, null, null).create();
                }
                serviceTipDialog.setCancelable(false);
                serviceTipDialog.show();
                binding.tvCountDown.setText(getString(R.string.jinrudaojishi_over));
            } else {
                selectTime = ThemeService.getServiceUserListSelectTime(false, pushTime, String.valueOf(mpushUserListBean.getSelect_time_interval()));
                selectObservable = ObserableUtils.countdownBySECONDS(selectTime);
                selectObservable.subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Long o) {
                        if (o == null || o <= 0) {
                            if (adapter != null && adapter.getData().size() > 0 && adapter.getData().get(0).getId().intValue() != ConfigService.getInstance().getZLUser().getId().intValue()) {
                                serviceTipDialog = new ServiceTipDialog.Builder(ThemeUserListActivity.this, new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer integer) throws Exception {
                                        if (integer == 1) {
                                            IntentUtils.toNewThemeServiceActivity(ThemeUserListActivity.this, ThemeService.getServiceBeanById(serviceId), mpushUserListBean.getService_type().equals(MValue.ORDER_TYPE_VIDEO) ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_NORMAL, null);
                                        } else {
                                            if (isRunning) {
                                                if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                                                    IntentUtils.toIndexActivity(ThemeUserListActivity.this);
                                                } else {
                                                    IntentUtils.toIndexUsersActivity(ThemeUserListActivity.this);
                                                }
                                                finish();
                                            } else {
                                                finish();
                                            }
                                        }

                                    }
                                }, MValue.TIP_DIALOG_TYPE.TIMEOUT, null, null).create();
                            } else {
                                serviceTipDialog = new ServiceTipDialog.Builder(ThemeUserListActivity.this, new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer integer) throws Exception {
                                        if (integer == 1) {
                                            IntentUtils.toNewThemeServiceActivity(ThemeUserListActivity.this, ThemeService.getServiceBeanById(serviceId), mpushUserListBean.getService_type().equals(MValue.ORDER_TYPE_VIDEO) ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_NORMAL, null);
                                        } else {
                                            if (isRunning) {
                                                if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                                                    IntentUtils.toIndexActivity(ThemeUserListActivity.this);
                                                } else {
                                                    IntentUtils.toIndexUsersActivity(ThemeUserListActivity.this);
                                                }
                                                finish();
                                            } else {
                                                finish();
                                            }
                                        }

                                    }
                                }, MValue.TIP_DIALOG_TYPE.NO_USER, null, null).create();
                            }
                            serviceTipDialog.setCancelable(false);
                            serviceTipDialog.show();
//                            LogUtil.e(TAG, "弹窗1》" + o == null ? "null" : o.toString());
                            binding.tvCountDown.setText(getString(R.string.jinrudaojishi_over));
                        } else {
                            binding.tvCountDown.setText(getString(R.string.jinrudaojishi) + ThemeService.getMiL(o) + ":" + ThemeService.getMiR(o) + getString(R.string.bimianchaoshi));
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
        } else {

            pushObservable = ObserableUtils.countdownBySECONDS(pushTime);

            pushObservable.subscribe(new Observer<Long>() {
                @Override
                public void onSubscribe(Disposable d) {
                    addDisposable(d);
                }

                @Override
                public void onNext(Long o1) {
                    binding.tvCountDown.setText(getString(R.string.qiangdandaojishi) + ThemeService.getMi(o1) + getString(R.string.qiangdandaojishi2) + mpushUserListBean.getSelect_time_interval() + getString(R.string.select_user_time_tip));
                    if (o1 == 0) {
                        selectTime = ThemeService.getServiceUserListSelectTime(true, pushTime, String.valueOf(mpushUserListBean.getSelect_time_interval()));
                        if (selectTime != null) {

                            selectObservable = ObserableUtils.countdownBySECONDS(selectTime);

                            selectObservable.subscribe(new Observer<Long>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    selectDisposable = d;
                                    addDisposable(selectDisposable);
                                }

                                @Override
                                public void onNext(Long o) {
                                    if (o == null || o <= 0) {
                                        if (adapter != null && adapter.getData().size() > 0 && adapter.getData().get(0).getId().intValue() != ConfigService.getInstance().getZLUser().getId().intValue()) {
                                            serviceTipDialog = new ServiceTipDialog.Builder(ThemeUserListActivity.this, new Consumer<Integer>() {
                                                @Override
                                                public void accept(Integer integer) throws Exception {
                                                    if (integer == 1) {
                                                        IntentUtils.toNewThemeServiceActivity(ThemeUserListActivity.this, ThemeService.getServiceBeanById(serviceId), isVideo ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_NORMAL, null);
                                                    }
                                                    if (isRunning) {
                                                        if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                                                            IntentUtils.toIndexActivity(ThemeUserListActivity.this);
                                                        } else {
                                                            IntentUtils.toIndexUsersActivity(ThemeUserListActivity.this);
                                                        }
                                                        finish();
                                                    } else {
                                                        finish();
                                                    }
                                                }
                                            }, MValue.TIP_DIALOG_TYPE.TIMEOUT, null, null).create();
                                        } else {
                                            serviceTipDialog = new ServiceTipDialog.Builder(ThemeUserListActivity.this, new Consumer<Integer>() {
                                                @Override
                                                public void accept(Integer integer) throws Exception {
                                                    if (integer == 1) {
                                                        IntentUtils.toNewThemeServiceActivity(ThemeUserListActivity.this, ThemeService.getServiceBeanById(serviceId), isVideo ? MValue.NEW_SERVICE_TYPE_VIDEO : MValue.NEW_SERVICE_TYPE_NORMAL, null);
                                                    }
                                                    if (isRunning) {
                                                        if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                                                            IntentUtils.toIndexActivity(ThemeUserListActivity.this);
                                                        } else {
                                                            IntentUtils.toIndexUsersActivity(ThemeUserListActivity.this);
                                                        }
                                                        finish();
                                                    } else {
                                                        finish();
                                                    }
                                                }
                                            }, MValue.TIP_DIALOG_TYPE.NO_USER, null, null).create();
                                        }

                                        serviceTipDialog.setCancelable(false);
                                        serviceTipDialog.show();
//                                        LogUtil.e(TAG, "弹窗2》" + o == null ? "null" : o.toString());
                                        binding.tvCountDown.setText(getString(R.string.jinrudaojishi_over));
                                    } else {
                                        binding.tvCountDown.setText(getString(R.string.jinrudaojishi) + ThemeService.getMi(o) + getString(R.string.bimianchaoshi));
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

    }

    private void setUI(PushUserListBean pushUserListBean) {


        if (adapter == null) {
            adapter = new ThemeUserListAdapter(new WeakReference<ThemeUserListActivity>(this), pushUserListBean.getMax_select_nums(), isVideo);
            binding.rvList.setAdapter(adapter);
        } else {
            adapter.setMax(pushUserListBean.getMax_select_nums());
        }

        if (first) {
            binding.rvList.setX(QMUIDisplayHelper.getScreenWidth(this));
            binding.rvList.setVisibility(View.INVISIBLE);
        } else {
            binding.rvList.setAlpha(1f);
            binding.rvList.setVisibility(View.VISIBLE);
        }


        if (pushUserListBean.getList() != null && pushUserListBean.getList().size() > 0) {
//            if (adapter.getData() != null && adapter.getData().size() > 0) {
//                adapter.getData().remove(0);
//            }
            for (User user : pushUserListBean.getList()) {
//                LogUtil.e(TAG, "测试塞用户5>" + adapter.getItemCount());
                adapter.addUser(user);
            }

        } else {

            if (adapter.getData() == null || adapter.getData().size() == 0) {
//                LogUtil.e(TAG, "测试塞用户2>" + adapter.getItemCount());
                adapter.addUser(ConfigService.getInstance().getZLUser());
            }


        }

        //title
        String result = MAPP.mapp.getString(R.string.yitongzhi) + pushUserListBean.getPush_nums() + "人";
        binding.tvTitle.setText(result);

        binding.setIsFirstOrder(pushUserListBean.getIs_first_order() == 1);

        if (pushUserListBean.getIs_first_order() == 1) {
            binding.tvPayMoney.setText(pushUserListBean.getPayable_amount() + getString(R.string.price_unit));
            String apply = MAPP.mapp.getString(R.string.select_apply_user);
            String applyResult = String.format(apply, selectedNum + "");
            binding.tvPayNums.setText(applyResult);

        }
    }


    public void doFirstUserListData() {
        if (mpushUserListBean != null && mpushUserListBean.getList() != null && mpushUserListBean.getList().size() > 0) {
            if (adapter.getData() != null && adapter.getData().size() > 0) {
                adapter.getData().remove(0);
            }
            for (User user : mpushUserListBean.getList()) {
                adapter.addUser(user);
            }
        } else {
            binding.rvList.setAlpha(0f);
//            adapter.clear();
//            adapter.notifyDataSetChanged();
            binding.rvList.setAlpha(0f);
            if (adapter != null && (adapter.getData() == null || adapter.getData().size() == 0)) {
                adapter.addUser(ConfigService.getInstance().getZLUser());
            }
            binding.rvList.setAlpha(0f);
            binding.rvList.setVisibility(View.VISIBLE);
            binding.rvList.animate().x(0).alpha(1f).setDuration(500).withEndAction(new Runnable() {
                @Override
                public void run() {
                    first = false;
                }
            }).start();


        }
    }


    @Override
    public void initListener() {

        binding.vBack.setOnClickListener((v) -> {
            binding.dlytContainer.openDrawer(Gravity.LEFT);
        });

        binding.includeProgress.vBack.setOnClickListener((v) -> {
            binding.dlytContainer.openDrawer(Gravity.LEFT);
        });

        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(OrderEvent.CANCEL_ORDER_ENENT)) {
                    if (isRunning) {
                        if (MAPP.mapp.getConfigBean().getIs_review_status() == 1) {
                            IntentUtils.toIndexActivity(ThemeUserListActivity.this);
                        } else {
                            IntentUtils.toIndexUsersActivity(ThemeUserListActivity.this);
                        }
                        finish();
                    } else {
                        finish();
                    }
                } else if (o instanceof MessageEvent.NewApplyUserEvent) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (((MessageEvent.NewApplyUserEvent) o).getUserServiceId().equals(userServiceId)) {
//                                LogUtil.e("填充测试", "消息返回》" + adapter == null && adapter.getData() != null ? "null" : adapter.getData().size() + " >>>>>" + JSON.toJSONString(((MessageEvent.NewApplyUserEvent) o).getUser()));
                                if (adapter == null) {
                                    adapter = new ThemeUserListAdapter(new WeakReference<ThemeUserListActivity>(ThemeUserListActivity.this), mpushUserListBean != null ? mpushUserListBean.getMax_select_nums() : 1, isVideo);
                                    binding.rvList.setAdapter(adapter);
                                }
                                if (adapter.getData() != null && adapter.getData().size() > 0 && ConfigService.getInstance().isZL(String.valueOf(adapter.getData().get(0).getId()))) {
                                    adapter.getData().remove(0);
                                    adapter.notifyDataSetChanged();
                                }
                                adapter.addUser(((MessageEvent.NewApplyUserEvent) o).getUser());
                            }
                        }
                    });

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

                } else if (o.equals(NormalEvent.FINISH_SELECT_USER_PAGE)) {
                    finish();
                } else if (o instanceof ThemeEvent.ChooseUserByHomepageEvent) {
                    if (adapter != null) {
                        adapter.doSelect(((ThemeEvent.ChooseUserByHomepageEvent) o).getUid());
                    }
                } else if (o instanceof ThemeEvent.PushUsersEvent) {
//                    ((ThemeEvent.PushUsersEvent) o).getPushUserTipBean()
                    if (users != null) {
                        users.addAll(((ThemeEvent.PushUsersEvent) o).getPushUserTipBean().getPush_users());
                        initAvatarAni2();
                    }
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
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                showLeftAni();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
//                indexAniUtils.stopLeftAni(binding.includeLeft.includeMenu1.getRoot(),
//                        binding.includeLeft.includeMenu2.getRoot(),
//                        binding.includeLeft.includeMenu3.getRoot(),
//                        binding.includeLeft.includeMenu4.getRoot(),
//                        binding.includeLeft.includeMenu5.getRoot(), binding.includeLeft.includeMenuEnableVideo.getRoot());
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });


        binding.tvRight.setOnClickListener((v) -> {
            if (isVideo) {
                mtipDialog = new TipDialog.Builder(this, new Consumer<Integer>() {

                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 1) {
                            orderService.cancelOrder(userServiceId);
                        }
                    }
                }, "服务者正在参与抢单，是否取消邀约？").create();
                mtipDialog.show();
            } else {

                mtipDialog = new TipDialog.Builder(this, new Consumer<Integer>() {

                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 1) {
                            orderService.initCancelData(ThemeUserListActivity.this, userServiceId, false);
                        }
                    }
                }, "服务者正在参与抢单，是否取消邀约？").create();
                mtipDialog.show();


            }

        });

        binding.tvPay.setOnClickListener((v) -> {
            doSelect(isVideo);
        });

        binding.spLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initUserListData();
            }
        });

    }

    private void showLeftAni() {
        indexAniUtils.showLeftAni2(
                binding.includeLeft.includeMenu1.getRoot(),
                binding.includeLeft.includeMenu2.getRoot(),
                binding.includeLeft.includeMenu3.getRoot(),
                binding.includeLeft.includeMenu4.getRoot(),
                binding.includeLeft.includeMenu5.getRoot(), binding.includeLeft.includeMenuSetprice.getRoot(), binding.includeLeft.includeMenuEnableVideo.getRoot()
        );//  binding.includeLeft.includeMenu6.getRoot()
    }

    public void checkSelect(int nums) {
        selectedNum = nums;
        String apply = MAPP.mapp.getString(R.string.select_apply_user);
        String applyResult = String.format(apply, selectedNum + "");
        binding.tvPayNums.setText(applyResult);

        binding.tvSelectNumsNormal.setText(nums + getString(R.string.unit_person));

    }

    public void doSelect(boolean isV) {

        if (isV) {

        } else {
            if (adapter.getSelectedIds() == null || adapter.getSelectedIds().size() <= 0) {
                tipDialog = DialogUtils.getFailDialog(this, "请选择服务者", true);
                tipDialog.show();
                return;
            }
        }


        mtipDialog = new TipDialog.Builder(this, new Consumer<Integer>() {

            @Override
            public void accept(Integer integer) throws Exception {
                if (integer == 1) {
                    params = SignUtils.getNormalParams();

                    params.put(MKey.USER_SERVICE_ID, userServiceId);
                    params.put(MKey.SELECT_UIDS, JSON.toJSONString(adapter.getSelectedIds()).replace("[", "").replace("]", ""));
                    String sign = SignUtils.doSign(params);
                    params.put(MKey.SIGN, sign);


                    HttpClient.Builder.getGuodongServer().select_apply_user(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<PushUserListBean>() {
                        @Override
                        public void onSuccess(BaseBean<PushUserListBean> baseBean) {
                            mpushUserListBean = baseBean.getData();
                            RxBus.getInstance().post(OrderEvent.REFRESH_ORDER_ENENT);
                            if (mpushUserListBean.getIs_first_order() == 1) {
                                mpushUserListBean = baseBean.getData();
                                postThemeBean = new PostThemeBean();
                                postThemeBean.setUser_service_id(mpushUserListBean.getUser_service_id());
                                postThemeBean.setAmount(mpushUserListBean.getAmount());
                                postThemeBean.setCoupons_list(mpushUserListBean.getCoupons_list());
                                postThemeBean.setIs_first_order(mpushUserListBean.getIs_first_order());
                                postThemeBean.setPayable_amount(mpushUserListBean.getPayable_amount());
                                postThemeBean.setUsed_balance(mpushUserListBean.getUsed_balance());
                                postThemeBean.setUsed_coupons(mpushUserListBean.getUsed_coupons());
                                postThemeBean.setPayment_config(mpushUserListBean.getPayment_config());
                                postThemeBean.setSelectedUserNum(adapter.getSelectedIds().size());
                                postThemeBean.setSelectedUserId(adapter.getSelectedIds().toArray()[0].toString());//.get(0).toString()
                                postThemeBean.setChat_room_config(mpushUserListBean.getChat_room_config());
                                IntentUtils.toPayActivity(ThemeUserListActivity.this, postThemeBean, PayFrom.SELECT_USER, isVideo);
                                finish();
                            } else if (isVideo) {
                                IntentUtils.toVideoChatActivity(ThemeUserListActivity.this, userServiceId, mpushUserListBean.getChat_room_config(), VideoChatFrom.USER);
                                finish();
                            } else {
                                if (adapter.getSelectedIds() != null && adapter.getSelectedIds().size() == 1) {
                                    IntentUtils.intent2ChatActivity(ThemeUserListActivity.this, MValue.CHAT_PRIEX + adapter.getSelectedIds().toArray()[0].toString());
                                    finish();
                                } else {
                                    IntentUtils.toOrderDetailActivity(ThemeUserListActivity.this, userServiceId, false);
                                    finish();
                                }

                            }
                        }

                        @Override
                        public void onError(BaseBean<PushUserListBean> baseBean) {
                            if (tipDialog != null && tipDialog.isShowing()) {
                                tipDialog.dismiss();
                            }
                            tipDialog = DialogUtils.getFailDialog(ThemeUserListActivity.this, baseBean.getMessage(), true);
                            tipDialog.show();
                        }
                    });
                }
            }
        }, getString(R.string.confirm_select_user)).create();
        mtipDialog.show();


    }

    private void doProgressAni() {
        rotateAni = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAni.setDuration(1000);
        rotateAni.setRepeatCount(3000);//动画的重复次数
        rotateAni.setFillAfter(true);//设置为true，动画转化结束后被应用

        LinearInterpolator lir = new LinearInterpolator();
        rotateAni.setInterpolator(lir);

        binding.includeProgress.ivLeida.startAnimation(rotateAni);//开始动画


        AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f, 0f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setRepeatCount(2000);
        alphaAnimation.setRepeatMode(Animation.RESTART);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                binding.includeProgress.ivLeidaquan.setAlpha(0.7f);
            }
        });
        alphaAnimation.setDuration(2000);
        binding.includeProgress.ivLeidaquan.startAnimation(alphaAnimation);
        widthAni = ValueAnimator.ofInt(50, 900);
        widthAni.setRepeatMode(ValueAnimator.RESTART);
        widthAni.setRepeatCount(200);
        widthAni.setDuration(2000);

        heightAni = ValueAnimator.ofInt(50, 900);
        heightAni.setDuration(2000);
        heightAni.setRepeatMode(ValueAnimator.RESTART);
        heightAni.setRepeatCount(200);

        widthAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewUtils.setViewWidth(binding.includeProgress.ivLeidaquan, (Integer) animation.getAnimatedValue());

            }
        });

        heightAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewUtils.setViewHeight(binding.includeProgress.ivLeidaquan, (Integer) animation.getAnimatedValue());

            }
        });

        widthAni.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }
        });

        widthAni.start();
        heightAni.start();

        timeHandler.sendEmptyMessage(1);

    }

    public static class MHandler extends Handler {

        private WeakReference<ThemeUserListActivity> weakReference;

        private long person = 1;

        public MHandler(WeakReference<ThemeUserListActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {

            person = person + 1;

            if (person >= 70) {
                weakReference.get().doAni();
            } else if (person == 20) {
                weakReference.get().initAvatarAni();
                weakReference.get().binding.includeProgress.tvPerson.setText(weakReference.get().getString(R.string.yitongzhi) + person + weakReference.get().getString(R.string.weiyonghu));
                sendEmptyMessageDelayed(1, 100);
            } else if (person == 40) {//|| person == 60
                weakReference.get().initAvatarAni2();
                weakReference.get().binding.includeProgress.tvPerson.setText(weakReference.get().getString(R.string.yitongzhi) + person + weakReference.get().getString(R.string.weiyonghu));
                sendEmptyMessageDelayed(1, 100);
            } else {
                weakReference.get().binding.includeProgress.tvPerson.setText(weakReference.get().getString(R.string.yitongzhi) + person + weakReference.get().getString(R.string.weiyonghu));
                sendEmptyMessageDelayed(1, 100);
            }


        }
    }


    public void doAni() {
        binding.ivTopbg.setY(-200);
        if (!isVideo) {
            binding.clytBottom.setY(QMUIDisplayHelper.getScreenHeight(this));
        }
        binding.includeProgress.getRoot().animate().alpha(0f).y(-200).setDuration(500).withEndAction(new Runnable() {
            @Override
            public void run() {
                binding.includeProgress.getRoot().setVisibility(View.GONE);
                binding.ivTopbg.animate().alpha(1f).y(0).setDuration(500).start();
                if (!isVideo) {
                    binding.clytBottom.animate().alpha(1f).y(QMUIDisplayHelper.getScreenHeight(ThemeUserListActivity.this) - ThemeUserListActivity.this.getResources().getDimensionPixelOffset(R.dimen.y56)).setDuration(300).start();
                } else {
                    binding.clytBottom.setVisibility(View.GONE);
                }

                doFirstUserListData();
            }
        }).start();
        heightAni.end();
        widthAni.end();
    }


    @Override
    protected void onDestroy() {
        DialogUtils.dimissDialog(tipDialog);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_theme_userlist;
    }
}
