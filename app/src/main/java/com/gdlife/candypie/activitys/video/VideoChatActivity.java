package com.gdlife.candypie.activitys.video;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.faceunity.FURenderer;
import com.gdlife.candypie.BuildConfig;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.BaseActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.common.VideoChatFrom;
import com.gdlife.candypie.component.DaggerServiceComponent;
import com.gdlife.candypie.databinding.ActivityVideoChatBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.repository.GiftRepository;
import com.gdlife.candypie.serivce.NimChatService;
import com.gdlife.candypie.serivce.OrderService;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UserInfoService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.serivce.theme.VideoChatService;
import com.gdlife.candypie.utils.AudioUtil;
import com.gdlife.candypie.utils.AudioUtil2;
import com.gdlife.candypie.utils.DialogUtils;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.ObserableUtils;
import com.gdlife.candypie.utils.PermissionUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.widget.gift.BottomVideoGiftSheetDialogHehe;
import com.gdlife.candypie.widget.luckpan.LuckpanDialog;
import com.gdlife.candypie.widget.luckpan.TurntableIntiveTipDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.gift.GiftBean;
import com.heboot.bean.luckypan.TurntableConfigBean;
import com.heboot.bean.theme.PostVideoChatBean;
import com.heboot.bean.video.VideoChatStratEndBean;
import com.heboot.common.VideoCatState;
import com.heboot.event.GiftEvent;
import com.heboot.event.OrderEvent;
import com.heboot.event.TurntableEvent;
import com.heboot.event.VideoChatEvent;
import com.heboot.faceunity_unit.fulivedemo.renderer.CameraRenderer;
import com.heboot.rxbus.RxBus;
import com.jakewharton.rxbinding2.view.RxView;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yalantis.dialog.TipCustomDialog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.agora.AgoraAPI;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.AgoraVideoFrame;
import io.agora.rtc.video.VideoCanvas;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by heboot on 2018/3/23.
 */

public class VideoChatActivity extends BaseActivity<ActivityVideoChatBinding> implements SensorEventListener, EasyPermissions.PermissionCallbacks {


    private RtcEngine mRtcEngine;// Tutorial Step 1

    private PostVideoChatBean postVideoChatBean;

    private String userServiceId;


    @Inject
    OrderService orderService;

    private VideoChatFrom videoChatFrom;

    private boolean timeFlag = false;

    private Observable<Long> timeCancelObserable;

    private MHandler timeHandler;

    private TipCustomDialog tipCustomOneDialog;

    private Observable timeoutObserable;

    private Disposable disposable;


    private TipCustomDialog tipCustomDialog;

    private DialogInterface.OnDismissListener onDismissListener;

    private long cancelTime;

    private VideoCatState currentState = VideoCatState.before;

    private PermissionUtils permissionUtils;

    private MyAgoraCallback callback;

    private String serviceTime;

    public TurntableConfigBean turntableConfigBean;

    //拨打超时弹窗
    private TipCustomDialog timeoutTipDialog;

    private VideoChatService videoChatService = new VideoChatService();

    private BeforeCancelHandler beforeCancelHandler = new BeforeCancelHandler(new WeakReference<>(this));

    private boolean localVideoEnable = false;

    private boolean balanceTipflag = false;

    private FrameLayout remoteContainer;

    private boolean isTimeout = false;

    /**
     * 美颜相关
     */
    private FURenderer mCustomizedCameraRenderer; //渲染器
    protected CameraRenderer mCameraRenderer; //摄像头采集
    private boolean userJoined = false;


    @Override
    public void initUI() {
        setSwipeBackEnable(false);
        QMUIStatusBarHelper.translucent(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    public void initData() {

        permissionUtils = new PermissionUtils();
        timeHandler = new MHandler(new WeakReference<VideoChatActivity>(this));

        DaggerServiceComponent.builder().build().inject(this);

        postVideoChatBean = (PostVideoChatBean) getIntent().getExtras().get(MKey.POST_THEME_BEAN);

        MValue.currentVideoCoinAmount = postVideoChatBean.getCoin();

        userServiceId = (String) getIntent().getExtras().get(MKey.USER_SERVICE_ID);

        videoChatFrom = (VideoChatFrom) getIntent().getExtras().get(MKey.FROM);

        initAgoraEngineAndJoinChannel();


        MAPP.mapp.getM_agoraAPI().channelJoin(postVideoChatBean.getChannel_name());

        callback = new MyAgoraCallback(new WeakReference<>(this));

        MAPP.mapp.getM_agoraAPI().callbackSet(callback);

        ImageUtils.showImage(binding.ivAvatarBg, postVideoChatBean.getUser().getAvatar());

        if (videoChatFrom == VideoChatFrom.SERVICER) {
            binding.ivPublishCancel.setVisibility(View.GONE);
            binding.ivReject.setVisibility(View.VISIBLE);
            binding.tvReject.setVisibility(View.VISIBLE);
            binding.ivApply.setVisibility(View.VISIBLE);
            binding.tvApply.setVisibility(View.VISIBLE);
            binding.tvWait.setText("蜜糖号：" + postVideoChatBean.getUser().getId());

            binding.tvWaitInfo.setText("对方正在邀请您视频聊天～");
//            AudioUtil.playSound(R.raw.voip_called, 1, -1);
            AudioUtil2.getInstance(this).playServicerCalling();
            if (permissionUtils.hasCameraPermission(MAPP.mapp)) {
                initGLSurfaceView();
            } else {
                permissionUtils.getCameraPermission(this);
                initGLSurfaceView();
            }

        } else {
            setCancelEnable(false);
//            AudioUtil.playSound(R.raw.voip_calling_stay, 1, 0);
            AudioUtil2.getInstance(this).playUserCalling();
//            joinChannel();
            initGLSurfaceView();
            binding.ivPublishCancel.setVisibility(View.VISIBLE);
            binding.ivReject.setVisibility(View.GONE);
            binding.tvReject.setVisibility(View.GONE);
            binding.ivApply.setVisibility(View.GONE);
            binding.tvApply.setVisibility(View.GONE);
            binding.tvWait.setText("蜜糖号：" + postVideoChatBean.getUser().getId());
            binding.tvWaitInfo.setText("正在接通，请耐心等待～");
            /**
             * 未接通之前如果对方有特色 显示特色
             */
            showTeseTip();
            /**
             * 未接通之前10秒 不可取消
             */
            beforeCancelHandler.sendEmptyMessage(400001);
            timeoutObserable = ObserableUtils.countdownBySECONDS(120);
            timeoutObserable.subscribe(new Observer<Long>() {
                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                    addDisposable(disposable);
                }

                @Override
                public void onNext(Long o) {
                    if (o == 0) {
                        MAPP.mapp.getM_agoraAPI().channelInviteEnd(postVideoChatBean.getChannel_name(), String.valueOf(postVideoChatBean.getUser().getId()), 0);
                        isTimeout = true;
                        orderService.cancelOrder(userServiceId);
                        timeoutTipDialog = new TipCustomDialog.Builder(VideoChatActivity.this, new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                timeoutTipDialog.dismiss();
                                if (integer == 1) {
                                    IntentUtils.intent2ChatActivity(VideoChatActivity.this, MValue.CHAT_PRIEX + postVideoChatBean.getUser().getId());
                                }

                            }
                        }, "对方不方便接听，可直接和对方私信", "取消", "确认").create();
                        timeoutTipDialog.setOnDismissListener(onDismissListener);
                        timeoutTipDialog.show();
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

        ImageUtils.showImage(binding.ivAvatar, postVideoChatBean.getUser().getAvatar());

        binding.tvName.setText(postVideoChatBean.getUser().getNickname());


    }

    @Override
    public void initListener() {

        onDismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        };

        rxObservable.subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Object o) {
                if (o.equals(OrderEvent.CANCEL_ORDER_ENENT)) {
                    if (!isTimeout) {
                        finish();
                    }

                } else if (o.equals(OrderEvent.COMPLETE_ORDER_ENENT)) {
                    finish();
                } else if (o instanceof VideoChatEvent.UPDATE_CAMERA_STATUS_EVENT) {
                    if (videoChatFrom == VideoChatFrom.SERVICER) {
                        if (((VideoChatEvent.UPDATE_CAMERA_STATUS_EVENT) o).getSystemNotification().getValue().getCamera_status() == 1) {
                            showUserVideoView();
                        } else {
                            hideUserVideoView();
                        }
                    }
                } else if (o.equals(VideoChatEvent.VIDEO_CHAT_BALANCE_TIP_EVENT)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tipCustomOneDialog = new TipCustomDialog.Builder(VideoChatActivity.this, new Consumer<Integer>() {
                                @Override
                                public void accept(Integer integer) throws Exception {
                                    if (integer == 1) {
                                        IntentUtils.toAccountActivity(MAPP.mapp.getCurrentActivity());
                                    }
                                }
                            }, getString(R.string.video_balance_tip), "知道了", "充值").create();
                            tipCustomOneDialog.show();
                        }
                    });
                } else if (o.equals(VideoChatEvent.VIDEO_CHAT_END_EVENT)) {
                    timeHandler.removeCallbacksAndMessages(null);
                    completeOrder();
                } else if (o instanceof VideoChatEvent.UPDATE_VIDEO_STATE_EVENT) {
                    if (((VideoChatEvent.UPDATE_VIDEO_STATE_EVENT) o).getUser_service_id().equals(userServiceId)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (((VideoChatEvent.UPDATE_VIDEO_STATE_EVENT) o).getState() == VideoCatState.cancel) {
                                    before2cancel();
                                } else if (((VideoChatEvent.UPDATE_VIDEO_STATE_EVENT) o).getState() == VideoCatState.refuse) {
                                    before2refuse();
                                } else if (((VideoChatEvent.UPDATE_VIDEO_STATE_EVENT) o).getState() == VideoCatState.shutdown) {
                                    ing2shutdown();
                                }
                            }
                        });
                    }
                }
//                else if (o instanceof GiftEvent.SendGiftEvent) {
//                    if (!StringUtils.isEmpty(((GiftEvent.SendGiftEvent) o).getGiftBean().getTip_msg()) && !StringUtils.isEmpty(((GiftEvent.SendGiftEvent) o).getGiftBean().getIncome_msg())) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                showTipView(((GiftEvent.SendGiftEvent) o).getGiftBean().getTip_msg(), ((GiftEvent.SendGiftEvent) o).getGiftBean().getIncome_msg());
//                            }
//                        });
//
//                    }
//                    loadAnimation(((GiftEvent.SendGiftEvent) o).getGiftBean().getPlay_url());
//                }
                else if (o instanceof GiftEvent.SendGiftEventByTurntable) {
                    if (!StringUtils.isEmpty(((GiftEvent.SendGiftEventByTurntable) o).getGiftBean().getTip_msg())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showTipView(((GiftEvent.SendGiftEventByTurntable) o).getGiftBean().getTip_msg(), "");
                            }
                        });

                    }
                    loadAnimation(((GiftEvent.SendGiftEventByTurntable) o).getGiftBean().getPlay_url());
                } else if (o instanceof TurntableEvent.ShowTurntableActionMSGEvent) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.includeHehe.setInfo1((((TurntableEvent.ShowTurntableActionMSGEvent) o).getMsg()));
//                            binding.includeHehe.setInfo2((((TurntableEvent.ShowTurntableActionMSGEvent) o).getMsg()));
                            YoYo.with(Techniques.SlideInLeft).duration(3000).withListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    ObserableUtils.countdown(3).subscribe(new Consumer<Integer>() {
                                        @Override
                                        public void accept(Integer integer) throws Exception {
                                            if (integer == 0) {
                                                YoYo.with(Techniques.SlideOutRight).duration(500).playOn(binding.includeHehe.getRoot());
                                            }
                                        }
                                    });
                                }
                            }).playOn(binding.includeHehe.getRoot());
                        }
                    });
                } else if (o instanceof VideoChatEvent.UPDATE_VIDEO_SERVICE_TIME_ENENT) {
                    serviceTime = ((VideoChatEvent.UPDATE_VIDEO_SERVICE_TIME_ENENT) o).getServiceTime();
                } else if (o instanceof TurntableEvent.TurntableRefuseEvent) {
                    ToastUtils.showToast("对方拒绝了你的邀请");
                    if (luckpanDialog != null && luckpanDialog.isShowing()) {
                        luckpanDialog.dismiss();
                    }
                } else if (o instanceof VideoChatEvent.UPDATE_VIDEO_SERVICE_ENENT) {
                    if (((VideoChatEvent.UPDATE_VIDEO_SERVICE_ENENT) o).getSystemNotification().getValue().getUser_service_id().equals(userServiceId)) {
                        serviceTime = ((VideoChatEvent.UPDATE_VIDEO_SERVICE_ENENT) o).getSystemNotification().getValue().getService_time();
                    }

                } else if (o instanceof TurntableEvent.TurntableInviteEvent) {

                    TurntableIntiveTipDialog turntableIntiveTipDialog = new TurntableIntiveTipDialog.Builder(MAPP.mapp.getCurrentActivity(), ((TurntableEvent.TurntableInviteEvent) o).getUserServiceId(), ((TurntableEvent.TurntableInviteEvent) o).getPanId(), ((TurntableEvent.TurntableInviteEvent) o).getAmount(), ((TurntableEvent.TurntableInviteEvent) o).getServerTime(), ((TurntableEvent.TurntableInviteEvent) o).getApplyTime(), ((TurntableEvent.TurntableInviteEvent) o).getRole()).create();

                    turntableIntiveTipDialog.show();

                } else if (o instanceof TurntableEvent.PlayTurntableResultEvent) {
                    MValue.turntableResultBean = ((TurntableEvent.PlayTurntableResultEvent) o).getTurntableResultBean();
                    if (MValue.luckpanDialog != null) {
                        MValue.luckpanDialog.setResultindex(((TurntableEvent.PlayTurntableResultEvent) o).getTurntableResultBean().getIndex());
                        MValue.luckpanDialog.startResult();
                    } else if (turntableConfigBean != null) {

                        Observable.just("Amit")
                                //延时两秒，第一个参数是数值，第二个参数是事件单位
                                .delay(1000, TimeUnit.MILLISECONDS)
                                // Run on a background thread
                                .subscribeOn(AndroidSchedulers.mainThread())
                                // Be notified on the main thread
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        //播放送礼物动画
                                        if (MValue.turntableResultBean != null && MValue.turntableResultBean.getType().equals("gift")) {
                                            GiftBean giftBean = new GiftRepository().getGiftById(turntableConfigBean.getTurntable_config().getItems().get(MValue.turntableResultBean.getIndex()).getGoods_id() + "");
                                            if (giftBean != null) {
                                                giftBean.setIncome_msg("");
                                                giftBean.setTip_msg(MValue.turntableResultBean.getTip_msg());
                                            }
                                            RxBus.getInstance().post(new GiftEvent.SendGiftEventByTurntable(giftBean));
                                        } else if (MValue.turntableResultBean != null && MValue.turntableResultBean.getType().equals("action")) {
                                            RxBus.getInstance().post(new TurntableEvent.ShowTurntableActionMSGEvent(MValue.turntableResultBean.getTip_msg()));
                                        }
                                    }
                                });//这里的观察者依然不重要
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

        binding.ivApply.setOnClickListener((v) -> {
//            AudioUtil.stop();
            AudioUtil2.getInstance(this).stopRinging();
            if (!permissionUtils.hasCameraPermission(MAPP.mapp)) {
                permissionUtils.getCameraPermission(this);
                return;
            }
            MAPP.mapp.getM_agoraAPI().channelInviteAccept(postVideoChatBean.getChannel_name(), String.valueOf(postVideoChatBean.getUser().getId()), 0, "");
            joinChannel();
            setupLocalVideo();
        });
        binding.ivReject.setOnClickListener((v) -> {
//            AudioUtil.stop();
            AudioUtil2.getInstance(this).stopRinging();
            MAPP.mapp.getM_agoraAPI().channelInviteRefuse(postVideoChatBean.getChannel_name(), String.valueOf(postVideoChatBean.getUser().getId()), 0, "");
            completeOrder();
        });

        binding.ivIngCancel.setOnClickListener((v) -> {
            tipCustomDialog = new TipCustomDialog.Builder(this, new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    if (integer == 1) {
                        completeOrder();
                    }
                }
            }, "确定要结束通话吗？", getString(R.string.go_on_next), "挂断").create();
            tipCustomDialog.show();
        });

        binding.ivPublishCancel.setOnClickListener((v) -> {
            MAPP.mapp.getM_agoraAPI().channelInviteEnd(postVideoChatBean.getChannel_name(), String.valueOf(postVideoChatBean.getUser().getId()), 0);
            orderService.cancelOrder(userServiceId);
        });

        binding.ivGift.getRoot().setOnClickListener((v) -> {
//            BottomVideoGiftSheetDialog2 bottomVideoGiftSheetDialog = new BottomVideoGiftSheetDialog2.Builder(this, getSupportFragmentManager(), new WeakReference<>(this)).create();
//            bottomVideoGiftSheetDialog.show();
            BottomVideoGiftSheetDialogHehe bottomVideoGiftSheetDialogHehe = new BottomVideoGiftSheetDialogHehe(String.valueOf(postVideoChatBean.getUser().getId()), userServiceId);
            bottomVideoGiftSheetDialogHehe.show(((FragmentActivity) MAPP.mapp.getCurrentActivity()).getSupportFragmentManager(), "");
        });


        RxView.clicks(binding.ivEnableLocalvideo).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (localVideoEnable) {
//                mRtcEngine.disableVideo();
                    localVideoEnable = false;
//                    binding.localVideoViewContainer.removeAllViews();
                    binding.localVideoViewContainer.setVisibility(View.GONE);
                    binding.mGlview.setVisibility(View.INVISIBLE);
                    NimChatService.getInstance().updateUserCameraStatus(localVideoEnable, postVideoChatBean.getUser().getId(), userServiceId);
                    binding.ivEnableLocalvideo.setBackgroundResource(R.drawable.icon_camera_disable);
                } else {
                    binding.localVideoViewContainer.setVisibility(View.VISIBLE);
                    binding.mGlview.setVisibility(View.VISIBLE);
//                mRtcEngine.enableVideo();
                    setupLocalVideo();
                    localVideoEnable = true;
                    NimChatService.getInstance().updateUserCameraStatus(localVideoEnable, postVideoChatBean.getUser().getId(), userServiceId);
                    binding.ivEnableLocalvideo.setBackgroundResource(R.drawable.icon_camera_enable);
                }
            }
        });

        RxView.clicks(binding.ivPan.getRoot()).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (luckpanDialog == null || !luckpanDialog.isShowing()) {
                    initTurntable();
                }
            }
        });


    }

    private LuckpanDialog luckpanDialog;

    private void initTurntable() {
        //初始化
        params = SignUtils.getNormalParams();
        params.put(MKey.USER_SERVICE_ID, userServiceId);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().init_turntable(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<TurntableConfigBean>() {
            @Override
            public void onSuccess(BaseBean<TurntableConfigBean> baseBean) {
                turntableConfigBean = baseBean.getData();
                luckpanDialog = new LuckpanDialog.Builder(VideoChatActivity.this, userServiceId, baseBean.getData().getTurntable_config(), false, -1, videoChatFrom == VideoChatFrom.USER ? MValue.USER_ROLE_NORMAL : MValue.USER_ROLE_SERVICER).create();
                luckpanDialog.show();
            }

            @Override
            public void onError(BaseBean<TurntableConfigBean> baseBean) {

            }
        });
    }

    private void loadAnimation(String s) {
        binding.svgaview.setLoops(1);
        SVGAParser parser = new SVGAParser(this);
        resetDownloader(parser);
        try {
            URL url = null;
            try {
                url = new URL(s);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            parser.parse(url, new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    binding.ivGiftBg.setVisibility(View.VISIBLE);
                    binding.svgaview.setVisibility(View.VISIBLE);
                    SVGADrawable drawable = new SVGADrawable(videoItem);
                    binding.svgaview.setImageDrawable(drawable);
                    binding.svgaview.setCallback(new SVGACallback() {
                        @Override
                        public void onPause() {

                        }

                        @Override
                        public void onFinished() {
                            binding.svgaview.stopAnimation();
                            binding.svgaview.setVisibility(View.GONE);
                            binding.ivGiftBg.setVisibility(View.GONE);
                        }

                        @Override
                        public void onRepeat() {

                        }

                        @Override
                        public void onStep(int i, double v) {

                        }
                    });
                    binding.svgaview.startAnimation();

                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }
    }

    /**
     * 设置下载器，这是一个可选的配置项。
     *
     * @param parser
     */
    private void resetDownloader(SVGAParser parser) {
        parser.setFileDownloader(new SVGAParser.FileDownloader() {
            @Override
            public void resume(final URL url, final Function1<? super InputStream, Unit> complete, final Function1<? super Exception, Unit> failure) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(url).get().build();
                        try {
                            Response response = client.newCall(request).execute();
                            complete.invoke(response.body().byteStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                            failure.invoke(e);
                        }
                    }
                }).start();
            }
        });
    }


    private void startOrder() {
        params = SignUtils.getNormalParams();

        params.put(MKey.USER_SERVICE_ID, userServiceId);
        String sign = SignUtils.doSign(params);
        params.put(MKey.SIGN, sign);
        HttpClient.Builder.getGuodongServer().video_chart_start(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<VideoChatStratEndBean>() {
            @Override
            public void onSuccess(BaseBean<VideoChatStratEndBean> baseBean) {
                timeCancelObserable = ObserableUtils.countdownBySECONDS(150);
                timeCancelObserable.subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        cancelTime = aLong;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                serviceTime = baseBean.getData().getService_time();
                timeHandler.sendEmptyMessageDelayed(0, 1000);
            }

            @Override
            public void onError(BaseBean<VideoChatStratEndBean> baseBean) {

            }
        });
    }

    private void completeOrder() {
        videoChatService.completeVideoChatOrder(userServiceId, new HttpObserver<VideoChatStratEndBean>() {
            @Override
            public void onSuccess(BaseBean<VideoChatStratEndBean> baseBean) {
                if (videoChatFrom == VideoChatFrom.USER) {
                    mRtcEngine.leaveChannel();
                    if (disposable != null && !disposable.isDisposed()) {
                        disposable.dispose();
                    }

                    destoryTimeHandler();
                    doComment();
                } else {
                    finish();
                }
            }

            @Override
            public void onError(BaseBean<VideoChatStratEndBean> baseBean) {
                mRtcEngine.leaveChannel();
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }

                destoryTimeHandler();
                finish();
            }
        });

    }

    private void doComment() {

        //请求评价的标签
        orderService.doCommentOrder(VideoChatActivity.this, userServiceId, new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }


    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();     // Tutorial Step 1
        setupVideoProfile();         // Tutorial Step 2
        if (videoChatFrom == VideoChatFrom.SERVICER) {
            hideUserVideoView();
        }
    }


    // Tutorial Step 1
    public void initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(MAPP.mapp, MAPP.mapp.getString(R.string.agora_appid), mRtcEventHandler);
//            mRtcEngine.setSpeakerphoneVolume(1);
//            mRtcEngine.setEnableSpeakerphone(true);
//            mRtcEngine.enableInEarMonitoring(true);
        } catch (Exception e) {
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }


    // Tutorial Step 2
    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        mRtcEngine.setExternalVideoSource(true, true, true);
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_720P, false);
    }


    /**
     * 设置本地视频视图
     */
    private void setupLocalVideo() {
        FrameLayout container = binding.localVideoViewContainer;
        container.setVisibility(View.VISIBLE);
    }

    // Tutorial Step 4
    private void joinChannel() {
        mRtcEngine.joinChannel(postVideoChatBean.getChannel_key(), postVideoChatBean.getChannel_name(), "Extra Optional Data", UserService.getInstance().getUser().getId()); // if you do not specify the uid, we will generate the uid for you
    }

    // Tutorial Step 5
    private void setupRemoteVideo(int uid) {
        remoteContainer = (FrameLayout) findViewById(R.id.remote_video_view_container);

        if (remoteContainer.getChildCount() >= 1) {
//            remoteContainer.removeAllViews();
            binding.mGlview.onResume();
            return;
        }
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        remoteContainer.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));

        surfaceView.setTag(uid); // for mark purpose

//        if (videoChatFrom == VideoChatFrom.SERVICER) {
//            setLocalVideoToShow();
////            showUserVideoView();
//        } else if (videoChatFrom == VideoChatFrom.SERVICER && !localVideoEnable) {
////            hideUserVideoView();
//        }


    }

    /**
     * 初始化glsurfaceview
     */
    private void initGLSurfaceView() {
//        binding.mGlview.setEGLContextClientVersion(2);
        mCustomizedCameraRenderer = new FURenderer.Builder(this).maxFaces(4)
                .inputTextureType(FURenderer.FU_ADM_FLAG_EXTERNAL_OES_TEXTURE)
                .createEGLContext(true)
                .needReadBackImage(false)
                .defaultEffect(null)
                .setOnTrackingStatusChangedListener(new MOnTrackingStatusChangedListener()).build();

        binding.mGlview.setZOrderMediaOverlay(true);
        mCameraRenderer = new CameraRenderer(this, binding.mGlview, new MOnRendererStatusListener(new WeakReference<>(this)));
//        binding.mGlview.setRenderer(mCameraRenderer);
//        binding.mGlview.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//        mCameraRenderer.onCreate();
//        mCameraRenderer.onResume();
    }


    /**
     * 用户打开了摄像头
     */
    private void showUserVideoView() {
        binding.ivAvatarBg.setVisibility(View.INVISIBLE);
        if (binding.remoteVideoViewContainer != null) {
//            setRemoteVideoShow();
            binding.remoteVideoViewContainer.setVisibility(View.VISIBLE);
        }
    }

    private void hideUserVideoView() {
        binding.ivAvatarBg.setVisibility(View.VISIBLE);
        if (binding.remoteVideoViewContainer != null) {
            binding.remoteVideoViewContainer.setVisibility(View.INVISIBLE);
//            setRemoteVideoHide();
        }
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) { // Tutorial Step 5
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(int uid, int reason) { // Tutorial Step 7
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    onRemoteUserLeft();
                    if (videoChatFrom == VideoChatFrom.USER) {
                        if (reason == 0) {
                            if (disposable != null && !disposable.isDisposed()) {
                                disposable.dispose();
                            }
                        }
                        completeOrder();
                    } else {
                        if (reason == 0) {
                            tipDialog = DialogUtils.getInfolDialog(VideoChatActivity.this, "对方已取消", true);
                            tipDialog.setOnDismissListener(onDismissListener);
                            tipDialog.show();
                        }
                        finish();
                    }

                }
            });
        }

        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) { // Tutorial Step 10

        }

        @Override
        public void onJoinChannelSuccess(final String channel, final int uid, final int elapsed) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.ivReject.setVisibility(View.GONE);
                    binding.tvReject.setVisibility(View.GONE);
                    binding.ivApply.setVisibility(View.GONE);
                    binding.tvApply.setVisibility(View.GONE);
                    if (videoChatFrom == VideoChatFrom.USER) {
                        userJoined = true;
//                        AudioUtil.playSound(R.raw.voip_calling_ring, 1, -1);
                    } else {
                        currentState = VideoCatState.ing;
                        binding.ivPublishCancel.setVisibility(View.GONE);
                        binding.ivIngCancel.setVisibility(View.VISIBLE);
                        binding.ivPan.getRoot().setVisibility(View.VISIBLE);
                        binding.bgb.setVisibility(View.GONE);
                        binding.tvWait.setVisibility(View.GONE);
                        timeHandler.sendEmptyMessageDelayed(0, 1000);

                    }
                }
            });

            super.onJoinChannelSuccess(channel, uid, elapsed);

        }

        @Override
        public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onRejoinChannelSuccess(channel, uid, elapsed);
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            if (uid != UserService.getInstance().getUser().getId().intValue()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateState(VideoCatState.ing);
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                        binding.ivPublishCancel.setVisibility(View.GONE);
                        binding.ivIngCancel.setVisibility(View.VISIBLE);
                        binding.tvWaitInfo.setVisibility(View.GONE);
                        if (videoChatFrom == VideoChatFrom.USER) {
                            binding.ivPan.getRoot().setVisibility(View.VISIBLE);
                            binding.ivGift.getRoot().setVisibility(View.VISIBLE);
                            binding.bgb.setVisibility(View.GONE);
                            binding.ivAvatarBg.setVisibility(View.INVISIBLE);
                            binding.ivEnableLocalvideo.setVisibility(View.VISIBLE);
                            binding.remoteVideoViewContainer.setVisibility(View.VISIBLE);
//                            setRemoteVideoShow();
                            hideTeseTip();
                        }

                        //隐藏掉头像和昵称
                        binding.ivAvatar.setVisibility(View.INVISIBLE);
                        binding.tvName.setVisibility(View.INVISIBLE);
                        binding.tvWait.setVisibility(View.INVISIBLE);
//                        AudioUtil.stop();
                        AudioUtil2.getInstance(VideoChatActivity.this).stopRinging();
                        if (!timeFlag) {

                            if (videoChatFrom == VideoChatFrom.USER) {
                                startOrder();
                            }
                            timeFlag = true;
                        }
                    }
                });

//


            }
            super.onUserJoined(uid, elapsed);
        }

        @Override
        public void onAudioRouteChanged(int routing) {
            super.onAudioRouteChanged(routing);
        }
    };


    private void ing2shutdown() {
        if (currentState == VideoCatState.ing) {
            currentState = VideoCatState.shutdown;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRtcEngine.leaveChannel();

                    if (videoChatFrom == VideoChatFrom.USER) {
//                        if (reason == 0) {
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                        tipDialog = DialogUtils.getInfolDialog(VideoChatActivity.this, "对方已取消", true);
//                        tipDialog.setOnDismissListener(onDismissListener);
                        tipDialog.show();
                        destoryTimeHandler();
                        doComment();
                    } else {
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                        tipDialog = DialogUtils.getInfolDialog(VideoChatActivity.this, "对方已取消", true);
                        tipDialog.setOnDismissListener(onDismissListener);
                        tipDialog.show();
                    }
                }
            });
        }
    }

    private void destoryTimeHandler() {
        timeHandler.removeMessages(0);
        timeHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 服务者拒绝了视频邀约
     * 用户收到后 提示并关掉页面
     */
    private void before2refuse() {
//        AudioUtil.stop();
        AudioUtil2.getInstance(VideoChatActivity.this).stopRinging();
        if (currentState == VideoCatState.before) {
            currentState = VideoCatState.refuse;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tipDialog = DialogUtils.getInfolDialog(MAPP.mapp.getCurrentActivity(), "对方已拒绝", true);
                    tipDialog.setOnDismissListener(onDismissListener);
                    if (!isDestroyed()) {
                        tipDialog.show();
                    }

                }
            });
        }
    }

    /**
     * 用户取消了视频邀约
     * 服务者收到后 提示并关掉页面
     */
    private void before2cancel() {
//        AudioUtil.stop();
        AudioUtil2.getInstance(VideoChatActivity.this).stopRinging();
        if (currentState == VideoCatState.before) {
            currentState = VideoCatState.cancel;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tipDialog = DialogUtils.getInfolDialog(MAPP.mapp.getCurrentActivity(), "对方已取消", true);
                    tipDialog.setOnDismissListener(onDismissListener);
                    if (!isDestroyed()) {
                        tipDialog.show();
                    }

                }
            });
        }
    }

    public void setCancelEnable(boolean enable) {
        if (enable) {
            binding.ivPublishCancel.setBackgroundResource(R.drawable.icon_video_chat_over);
        } else {
            binding.ivPublishCancel.setBackgroundResource(R.drawable.icon_video_chat_over_gray);
        }
        binding.ivPublishCancel.setEnabled(enable);
    }

    private void updateState(VideoCatState state) {
        currentState = state;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (Math.abs(x) > 3 || Math.abs(y) > 3) {
                if (Math.abs(x) > Math.abs(y)) {
//                    onSensorChanged(x > 0 ? 0 : 180);
                    mCustomizedCameraRenderer.setTrackOrientation(x > 0 ? 0 : 180);
                } else {
                    mCustomizedCameraRenderer.setTrackOrientation(y > 0 ? 90 : 270);
//                    onSensorChanged(y > 0 ? 90 : 270);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private static class MHandler extends Handler {

        private WeakReference<VideoChatActivity> weakReference;

        private long time = 0;

        public MHandler(WeakReference<VideoChatActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference.get().binding.tvTime.getVisibility() == View.GONE) {
                weakReference.get().binding.tvTime.setVisibility(View.VISIBLE);
            }

            if (!StringUtils.isEmpty(weakReference.get().serviceTime) && (Integer.parseInt(weakReference.get().serviceTime) * 60) - time <= 30) {//120
                if (!weakReference.get().balanceTipflag) {
                    RxBus.getInstance().post(VideoChatEvent.VIDEO_CHAT_BALANCE_TIP_EVENT);
                    weakReference.get().balanceTipflag = true;
                }
            }
            if (!StringUtils.isEmpty(weakReference.get().serviceTime) && (Integer.parseInt(weakReference.get().serviceTime) * 60) - time <= 0) {
                RxBus.getInstance().post(VideoChatEvent.VIDEO_CHAT_END_EVENT);
                return;
            }


            time = time + 1;
            MValue.currentVideoTime = (int) time;
            weakReference.get().binding.tvTime.setText(ThemeService.getMiL(time) + ":" + ThemeService.getMiR(time));
            sendEmptyMessageDelayed(1, 1000);
        }
    }

    private static class BeforeCancelHandler extends Handler {
        private WeakReference<VideoChatActivity> weakReference;

        private int time = 0;

        public BeforeCancelHandler(WeakReference<VideoChatActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            time = time + 1;
            if (time >= 10) {
                weakReference.get().setCancelEnable(true);
                removeMessages(400001);
            } else {
                sendEmptyMessageDelayed(1, 1000);
            }

        }
    }

    private static class MOnTrackingStatusChangedListener implements FURenderer.OnTrackingStatusChangedListener {

        @Override
        public void onTrackingStatusChanged(int status) {

        }
    }

    private static class MOnRendererStatusListener implements CameraRenderer.OnRendererStatusListener {


        private WeakReference<VideoChatActivity> weakReference;

        public MOnRendererStatusListener(WeakReference<VideoChatActivity> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            try {
                weakReference.get().mCustomizedCameraRenderer.onSurfaceCreated();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
        }

        @Override
        public int onDrawFrame(byte[] cameraNV21Byte, int cameraTextureId, int cameraWidth, int cameraHeight, float[] mtx, long timeStamp) {
            int fuTextureId;
            byte[] newByte = null;
            if (newByte == null) {
                newByte = new byte[cameraNV21Byte.length];
            }


            fuTextureId = weakReference.get().mCustomizedCameraRenderer.onDrawFrame(cameraNV21Byte, cameraTextureId, cameraWidth, cameraHeight, newByte, cameraWidth, cameraHeight);


            if (weakReference.get().videoChatFrom == VideoChatFrom.SERVICER || (weakReference.get().videoChatFrom == VideoChatFrom.USER)) {// && weakReference.get().localVideoEnable
                if (weakReference.get().currentState == VideoCatState.ing) {
                    if (newByte == null) {
                    } else {
                        AgoraVideoFrame vf = new AgoraVideoFrame();
//                        vf.format = AgoraVideoFrame.FORMAT_TEXTURE_2D;
//                        vf.timeStamp = System.currentTimeMillis();
//                        vf.stride = 1080;
//                        vf.height = 1920;
//                        vf.syncMode = true;
//                        vf.textureID = fuTextureId;
//                        vf.eglContext11 = weakReference.get().mCameraRenderer.getmEGLCurrentContext();
//                        vf.transform = UNIQUE_MAT;


                        vf.format = AgoraVideoFrame.FORMAT_NV21;
                        vf.timeStamp = System.currentTimeMillis();
                        vf.stride = cameraWidth;
                        vf.height = cameraHeight;
                        vf.buf = newByte;
                        vf.rotation = 270;
                        vf.syncMode = true;
                        boolean result = weakReference.get().mRtcEngine.pushExternalVideoFrame(vf);
                    }


                }
            }
            return fuTextureId;
        }

        @Override
        public void onSurfaceDestroy() {
            weakReference.get().mCustomizedCameraRenderer.onSurfaceDestroyed();
        }

        @Override
        public void onCameraChange(int currentCameraType, int cameraOrientation) {
            if (weakReference.get() != null && !weakReference.get().userJoined) {
                if (weakReference.get().videoChatFrom == VideoChatFrom.USER) {
                    weakReference.get().joinChannel();
                }
            }
            weakReference.get().mCustomizedCameraRenderer.onCameraChange(currentCameraType, cameraOrientation);
        }
    }

    private final static float[] UNIQUE_MAT = {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraRenderer != null) {
            mCameraRenderer.onPause();
            mCameraRenderer.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCameraRenderer != null) {
            mCameraRenderer.onCreate();
            mCameraRenderer.onResume();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
        }
        return true;
    }


    private void showTeseTip() {
        if (postVideoChatBean.getUser().getVideo_tags() != null && postVideoChatBean.getUser().getVideo_tags().size() > 0) {
            binding.includeTipTese.setInfo1("视频特色");
            binding.includeTipTese.setInfo2(UserInfoService.getTagsString(postVideoChatBean.getUser().getVideo_tags()));
            YoYo.with(Techniques.SlideInLeft).duration(500).playOn(binding.includeTipTese.getRoot());
        }
    }

    private void hideTeseTip() {
        YoYo.with(Techniques.SlideOutRight).duration(500).playOn(binding.includeTipTese.getRoot());
    }

    private void hideTipView() {
        if (binding.includeHehe.getRoot().getVisibility() == View.VISIBLE) {
            YoYo.with(Techniques.SlideOutRight).duration(500).playOn(binding.includeHehe.getRoot());
        }
    }

    private void showTipView(String info1, String info2) {
        binding.includeHehe.setInfo1(info1);
        binding.includeHehe.setInfo2(info2);
        YoYo.with(Techniques.SlideInLeft).duration(500).withListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObserableUtils.countdown(3).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer == 0) {
                            hideTipView();
                        }
                    }
                });
            }
        }).playOn(binding.includeHehe.getRoot());
    }


    @Override
    protected void onDestroy() {
        // TODO: 2018/7/16  不再进行悬浮窗权限申请
//        closePopView();
        MValue.currentVideoTime = 0;
        ObserableUtils.initValue = 0;
        MValue.currentVideoCoinAmount = 0;
//        AudioUtil.stop();
        AudioUtil2.getInstance(VideoChatActivity.this).stopRinging();
        mRtcEngine.leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
        timeHandler.removeCallbacksAndMessages(null);
        MValue.SEND_VIDEO_INVITE = false;
        MAPP.mapp.getM_agoraAPI().channelLeave(postVideoChatBean.getChannel_name());
        super.onDestroy();
    }

    private static class MyAgoraCallback extends AgoraAPI.CallBack {

        private WeakReference<VideoChatActivity> weakReference;

        public MyAgoraCallback(WeakReference<VideoChatActivity> weakReference) {
            this.weakReference = weakReference;
        }


        @Override
        public void onChannelJoined(String channelID) {
            if (weakReference.get().videoChatFrom == VideoChatFrom.USER) {
                //邀请对方进入
                MAPP.mapp.getM_agoraAPI().channelInviteUser(weakReference.get().postVideoChatBean.getChannel_name(), String.valueOf(weakReference.get().postVideoChatBean.getUser().getId()), 0);
            }
            super.onChannelJoined(channelID);
        }

        @Override
        public void onInviteRefusedByPeer(String channelID, String account, int uid, String extra) {
            if (weakReference.get() != null) {
                weakReference.get().before2refuse();
            }
            super.onInviteRefusedByPeer(channelID, account, uid, extra);
        }

        @Override
        public void onInviteAcceptedByPeer(String channelID, String account, int uid, String extra) {
            super.onInviteAcceptedByPeer(channelID, account, uid, extra);
        }


        @Override
        public void onChannelUserLeaved(String account, int uid) {
            super.onChannelUserLeaved(account, uid);
        }

        @Override
        public void onInviteFailed(String channelID, String account, int uid, int ecode, String extra) {

            //发送邀请失败 对方可能不在线等
            super.onInviteFailed(channelID, account, uid, ecode, extra);
            if (MValue.SEND_VIDEO_INVITE) {
                if (channelID.equals(weakReference.get().postVideoChatBean.getChannel_name())) {
                    MAPP.mapp.getM_agoraAPI().channelInviteUser(weakReference.get().postVideoChatBean.getChannel_name(), String.valueOf(weakReference.get().postVideoChatBean.getUser().getId()), 0);
                }
            }
        }

        @Override
        public void onInviteEndByPeer(String channelID, String account, int uid, String extra) {
            super.onInviteEndByPeer(channelID, account, uid, extra);

            if (weakReference.get() != null && !weakReference.get().isDestroyed()) {
//                AudioUtil.stop();
                AudioUtil2.getInstance(weakReference.get()).stopRinging();
                weakReference.get().before2cancel();
            } else {
                AudioUtil2.getInstance(MAPP.mapp).stopRinging();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            permissionUtils.re(this);
//
//        }
//        TipCustomOnePermissionDialog tipCustomOnePermissionDialog = null;
//
//        if (requestCode == 4002) {
//            if (tipCustomOnePermissionDialog == null || !tipCustomOnePermissionDialog.isShowing()) {
//                tipCustomOnePermissionDialog = new TipCustomOnePermissionDialog.Builder(this, "需要摄像头权限才能正常视频", getString(R.string.goto_setting), new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                        intent.setData(uri);
//
//                        // Start for result
//                        startActivityForResult(intent, MCode.REQUEST_CODE.GET_PERMISSION_LOC);
//                    }
//                }).create();
//                tipCustomOnePermissionDialog.show();
//            }
//        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_chat;
    }
}
