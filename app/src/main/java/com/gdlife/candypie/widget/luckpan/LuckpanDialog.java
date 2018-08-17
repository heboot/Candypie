package com.gdlife.candypie.widget.luckpan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.databinding.LayoutLuckPanBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.repository.GiftRepository;
import com.gdlife.candypie.serivce.PayService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.widget.common.TipDialog;
import com.heboot.base.BaseBean;
import com.heboot.bean.gift.GiftBean;
import com.heboot.bean.luckypan.InvitePlayBean;
import com.heboot.bean.luckypan.TurntableConfigBean;
import com.heboot.bean.luckypan.TurntableConfigChildBean;
import com.heboot.bean.video.VideoChatStratEndBean;
import com.heboot.event.GiftEvent;
import com.heboot.event.TurntableEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yalantis.dialog.TipCustomDialog;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/2.
 */

public class LuckpanDialog extends Dialog {

    // 动画时长
    private final static int mAnimationDuration = 200;
    // 持有 ContentView，为了做动画

    private View mContentView;

    private boolean mIsAnimating = false;

    private RotateListener rotateListener;

    private View rotateView;

    private TextView textView;

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public View getRotateView() {
        return rotateView;
    }

    public void setRotateView(View rotateView) {
        this.rotateView = rotateView;
    }

    public LuckpanDialog(Context context) {
        super(context);
    }

    public LuckpanDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private LuckpanDialog dialog;


        private List<TurntableConfigChildBean.ItemsBean> itemsBeans;

        private TurntableConfigChildBean turntableConfigBean;

        private int currentStatus = 0;

        private Map<String, Object> params;

        private String userServiceId;

        private boolean autoResult;

        private MHandler handler;

        private int resultPosition = -1;

        protected Observable<Object> rxObservable;

        private GiftRepository giftRepository;

        private TipCustomDialog tipDialog;

        private int role;

        public Builder(Context context, String userServiceId, TurntableConfigChildBean turntableConfigBean, boolean auto, int resultPosition, int role) {
            this.context = context;
            this.userServiceId = userServiceId;
            this.turntableConfigBean = turntableConfigBean;
            this.itemsBeans = turntableConfigBean.getItems();
            this.autoResult = auto;
            this.resultPosition = resultPosition;
            this.role = role;
        }


        public LuckpanDialog create() {

            giftRepository = new GiftRepository();

            dialog = new LuckpanDialog(context, R.style.QMUI_BottomSheet);

            LayoutLuckPanBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_luck_pan, null, false);

            binding.includeContent1.setFrom(role);
            binding.includeContent1.setType(itemsBeans.get(0).getType());
            binding.includeContent1.setGoodsData(itemsBeans.get(0).getGoods());


            float startAng = 360 / itemsBeans.size();
            binding.includeContent2.getRoot().setRotationX(0.5f);
            binding.includeContent2.getRoot().setRotationY(0.5f);
            binding.includeContent2.getRoot().setRotation(startAng);
            binding.includeContent2.setFrom(role);
            binding.includeContent2.setType(itemsBeans.get(1).getType());
            binding.includeContent2.setGoodsData(itemsBeans.get(1).getGoods());


            startAng = startAng + 360 / itemsBeans.size();
            binding.includeContent3.getRoot().setRotationX(0.5f);
            binding.includeContent3.getRoot().setRotationY(0.5f);
            binding.includeContent3.getRoot().setRotation(startAng);
            binding.includeContent3.setFrom(role);
            binding.includeContent3.setType(itemsBeans.get(2).getType());
            binding.includeContent3.setGoodsData(itemsBeans.get(2).getGoods());

            startAng = startAng + 360 / itemsBeans.size();
            binding.includeContent4.getRoot().setRotationX(0.5f);
            binding.includeContent4.getRoot().setRotationY(0.5f);
            binding.includeContent4.getRoot().setRotation(startAng);
            binding.includeContent4.setFrom(role);
            binding.includeContent4.setType(itemsBeans.get(3).getType());
            binding.includeContent4.setGoodsData(itemsBeans.get(3).getGoods());

            startAng = startAng + 360 / itemsBeans.size();
            binding.includeContent5.getRoot().setRotationX(0.5f);
            binding.includeContent5.getRoot().setRotationY(0.5f);
            binding.includeContent5.getRoot().setRotation(startAng);
            binding.includeContent5.setFrom(role);
            binding.includeContent5.setType(itemsBeans.get(4).getType());
            binding.includeContent5.setGoodsData(itemsBeans.get(4).getGoods());

            startAng = startAng + 360 / itemsBeans.size();
            binding.includeContent6.getRoot().setRotationX(0.5f);
            binding.includeContent6.getRoot().setRotationY(0.5f);
            binding.includeContent6.getRoot().setRotation(startAng);
            binding.includeContent6.setFrom(role);
            binding.includeContent6.setType(itemsBeans.get(5).getType());
            binding.includeContent6.setGoodsData(itemsBeans.get(5).getGoods());

            startAng = startAng + 360 / itemsBeans.size();

            binding.rotatePan.setItemsBeans(itemsBeans);

            binding.tvCoinBalance.setText(UserService.getInstance().getUser().getCoin());

            binding.qrbRecharge.setOnClickListener((v) -> {
                IntentUtils.toRechargeActivity(context, RechargeType.COIN);
            });

            binding.vClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });


            RotateListener rotateListener = new RotateListener() {
                @Override
                public void rotateEnd(int position, String des) {

                    LogUtil.e("转盘旋转后>", position + "");

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
                                    dialog.dismiss();
                                    //播放送礼物动画
                                    if (MValue.turntableResultBean != null && MValue.turntableResultBean.getType().equals("gift")) {
                                        GiftBean giftBean = giftRepository.getGiftById(turntableConfigBean.getItems().get(MValue.turntableResultBean.getIndex()).getGoods_id() + "");
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

                @Override
                public void rotating(ValueAnimator valueAnimator) {

                }

                @Override
                public void rotateBefore(ImageView goImg) {

                }
            };

            dialog.setRotateListener(rotateListener);

            if (!autoResult) {
                binding.tvNode.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAPP.mapp.getResources().getDimensionPixelOffset(R.dimen.x14));
                binding.tvNode.setText(turntableConfigBean.getStatus_message());
            } else {
                binding.tvNode.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAPP.mapp.getResources().getDimensionPixelOffset(R.dimen.x36));
                binding.tvNode.setText(3 + "");
            }
//            else {
//                //倒计时
//                binding.tvNode.setText("3");
//                handler = new MHandler(binding.tvNode, new Consumer() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        dialog.startRotate(binding.clytContainer, resultPosition);
//                    }
//                });
//                handler.sendEmptyMessageDelayed(1, 1000);
//            }


            if (!autoResult) {
                binding.ivNode.setOnClickListener((v) -> {
                    if (currentStatus == 0) {

                        if (role == MValue.USER_ROLE_NORMAL) {
                            if (Integer.parseInt(UserService.getInstance().getUser().getCoin()) <= PayService.getTurntableNeedCoin(MValue.currentVideoCoinAmount, Integer.parseInt(MAPP.mapp.getConfigBean().getIm_chat_config().getTurntable_amount()))) {
                                tipDialog = new TipCustomDialog.Builder(context, new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer integer) throws Exception {
                                        if (integer == 0) {
                                            tipDialog.dismiss();
                                        } else {
                                            IntentUtils.toRechargeActivity(context, RechargeType.COIN);
                                        }
                                    }
                                }, "你的钻石余额不足\n" +
                                        "请充值", "取消", "充值").create();
                                tipDialog.show();
                            } else {
                                params = SignUtils.getNormalParams();

                                params.put(MKey.USER_SERVICE_ID, userServiceId);
                                params.put(MKey.USER_TURNTABLE_ID, turntableConfigBean.getUser_turntable_id());
                                String sign = SignUtils.doSign(params);
                                params.put(MKey.SIGN, sign);
                                HttpClient.Builder.getGuodongServer().invite_play(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<InvitePlayBean>() {
                                    @Override
                                    public void onSuccess(BaseBean<InvitePlayBean> baseBean) {
                                        currentStatus = baseBean.getData().getStatus();
                                        binding.tvNode.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAPP.mapp.getResources().getDimensionPixelOffset(R.dimen.x14));
                                        binding.tvNode.setText(baseBean.getData().getStatus_message());
                                    }

                                    @Override
                                    public void onError(BaseBean<InvitePlayBean> baseBean) {

                                    }
                                });
                            }

                        } else {
                            //发起
                            params = SignUtils.getNormalParams();

                            params.put(MKey.USER_SERVICE_ID, userServiceId);
                            params.put(MKey.USER_TURNTABLE_ID, turntableConfigBean.getUser_turntable_id());
                            String sign = SignUtils.doSign(params);
                            params.put(MKey.SIGN, sign);
                            HttpClient.Builder.getGuodongServer().invite_play(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<InvitePlayBean>() {
                                @Override
                                public void onSuccess(BaseBean<InvitePlayBean> baseBean) {
                                    currentStatus = baseBean.getData().getStatus();
                                    binding.tvNode.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAPP.mapp.getResources().getDimensionPixelOffset(R.dimen.x14));
                                    binding.tvNode.setText(baseBean.getData().getStatus_message());
                                }

                                @Override
                                public void onError(BaseBean<InvitePlayBean> baseBean) {

                                }
                            });
                        }
                    }

                });
            }


            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    MValue.PAN_SHOWING = false;
                    MValue.luckpanDialog = null;
                }
            });

            dialog.setRotateView(binding.clytContainer);

            dialog.setTextView(binding.tvNode);

            dialog.setmContentView(binding.getRoot());


            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(layoutParams);
            return dialog;
        }


    }


    //目前的角度
    private float currAngle = 0;
    //记录上次的位置
    private int lastPosition;

    //每个扇形旋转的时间
    private int mVarTime = 175;//init 75

    private MHandler handler;

    private int resultindex = 0;

    public int getResultindex() {
        return resultindex;
    }

    public void setResultindex(int resultindex) {
        this.resultindex = resultindex;
    }

    public void startResult() {
        getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, MAPP.mapp.getResources().getDimensionPixelOffset(R.dimen.x36));
        //倒计时
        textView.setText("3");
        handler = new MHandler(textView, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                startRotate(getRotateView(), resultindex);
            }
        });
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    static class MHandler extends Handler {


        private int time = 3;

        private TextView textView;

        private Consumer<String> consumer;

        public MHandler(TextView textView, Consumer consumer) {
            this.textView = textView;
            this.consumer = consumer;
        }

        @Override
        public void handleMessage(Message msg) {

            if (time > 0) {
                time = time - 1;
                textView.setText(time + "");
                sendEmptyMessageDelayed(1, 1000);
            } else {
                try {
                    consumer.accept("");
                    removeMessages(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }


    private int ppos;

    /**
     * 开始转动
     * pos 位置 1 开始 这里的位置上是按照逆时针递增的 比如当前指的那个选项是第一个  那么他左边的那个是第二个 以此类推
     */
    public void startRotate(View v, final int ps) {
        switch (ps) {
            case 0:
                ppos = 1;
                break;

            case 1:
                ppos = 6;
                break;
            case 2:
                ppos = 5;
                break;
            case 3:
                ppos = 4;
                break;

            case 4:
                ppos = 3;
                break;
            case 5:
                ppos = 2;
                break;
        }

        LogUtil.e("转盘旋转前>", ppos + "");
        lastPosition = 0;
        int mMinTimes = 3;


        float mAngle = (float) (360.0 / 6);
        //最低圈数是mMinTimes圈
        int newAngle = (int) (360 * mMinTimes + (ppos - 1) * mAngle + currAngle - (lastPosition == 0 ? 0 : ((lastPosition - 1) * mAngle)));
        //计算目前的角度划过的扇形份数
        int num = (int) ((newAngle - currAngle) / mAngle);
        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "rotation", currAngle, newAngle);
        currAngle = newAngle;
        lastPosition = ppos;
        // 动画的持续时间，执行多久？
        anim.setDuration(num * mVarTime);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //将动画的过程态回调给调用者
                if (rotateListener != null)
                    rotateListener.rotating(animation);
            }
        });
        final float[] f = {0};
        anim.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float t) {
                float f1 = (float) (Math.cos((t + 1) * Math.PI) / 2.0f) + 0.5f;
                Log.e("HHHHHHHh", "" + t + "     " + (f[0] - f1));
                f[0] = (float) (Math.cos((t + 1) * Math.PI) / 2.0f) + 0.5f;
                return f[0];
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //当旋转结束的时候回调给调用者当前所选择的内容
                if (rotateListener != null) {

                    rotateListener.rotateEnd(ppos, "");
                }
            }
        });
        // 正式开始启动执行动画
        anim.start();
    }

    public RotateListener getRotateListener() {
        return rotateListener;
    }

    public void setRotateListener(RotateListener rotateListener) {
        this.rotateListener = rotateListener;
    }

    /**
     * BottomSheet升起动画
     */
    private void animateUp() {
        if (mContentView == null) {
            return;
        }
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
        );
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        mContentView.startAnimation(set);
    }

    /**
     * BottomSheet降下动画
     */
    private void animateDown() {
        if (mContentView == null || !isShowing()) {
            return;
        }
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
        );
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsAnimating = false;
                /**
                 * Bugfix： Attempting to destroy the window while drawing!
                 */
                mContentView.post(new Runnable() {
                    @Override
                    public void run() {
                        // java.lang.IllegalArgumentException: View=com.android.internal.policy.PhoneWindow$DecorView{22dbf5b V.E...... R......D 0,0-1080,1083} not attached to window manager
                        // 在dismiss的时候可能已经detach了，简单try-catch一下
                        try {
                            LuckpanDialog.super.dismiss();
                        } catch (Exception e) {
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContentView.startAnimation(set);
    }


    public void setmContentView(View mContentView) {
        this.mContentView = mContentView;
    }

    @Override
    public void show() {
        super.show();
        MValue.PAN_SHOWING = true;
        MValue.luckpanDialog = this;
        animateUp();

    }

    @Override
    public void dismiss() {
        if (mIsAnimating) {
            return;
        }
        animateDown();
    }
//    TurntableConfigChildBean.ItemsBean itemsBean1 = new TurntableConfigChildBean.ItemsBean();
//            itemsBean1.setType("gift");
//    TurntableConfigChildBean.ItemsBean.GoodsBean goodsBean1 = new TurntableConfigChildBean.ItemsBean.GoodsBean();
//            goodsBean1.setImg("wocao");
//            itemsBean1.setGoods(goodsBean1);
//
//    TurntableConfigChildBean.ItemsBean itemsBean2 = new TurntableConfigChildBean.ItemsBean();
//            itemsBean2.setType("action");
//    TurntableConfigChildBean.ItemsBean.GoodsBean goodsBean2 = new TurntableConfigChildBean.ItemsBean.GoodsBean();
//            goodsBean2.setImg("wocao");
//            itemsBean2.setGoods(goodsBean2);
//
//    TurntableConfigChildBean.ItemsBean itemsBean3 = new TurntableConfigChildBean.ItemsBean();
//            itemsBean3.setType("gift");
//    TurntableConfigChildBean.ItemsBean.GoodsBean goodsBean3 = new TurntableConfigChildBean.ItemsBean.GoodsBean();
//            goodsBean3.setImg("wocao");
//            itemsBean3.setGoods(goodsBean3);
//
//    TurntableConfigChildBean.ItemsBean itemsBean4 = new TurntableConfigChildBean.ItemsBean();
//            itemsBean4.setType("action");
//    TurntableConfigChildBean.ItemsBean.GoodsBean goodsBean4 = new TurntableConfigChildBean.ItemsBean.GoodsBean();
//            itemsBean4.setGoods(goodsBean4);
//
//    TurntableConfigChildBean.ItemsBean itemsBean5 = new TurntableConfigChildBean.ItemsBean();
//            itemsBean5.setType("gift");
//    TurntableConfigChildBean.ItemsBean.GoodsBean goodsBean5 = new TurntableConfigChildBean.ItemsBean.GoodsBean();
//            goodsBean5.setImg("wocao");
//            itemsBean5.setGoods(goodsBean5);
//
//    TurntableConfigChildBean.ItemsBean itemsBean6 = new TurntableConfigChildBean.ItemsBean();
//            itemsBean6.setType("action");
//    TurntableConfigChildBean.ItemsBean.GoodsBean goodsBean6 = new TurntableConfigChildBean.ItemsBean.GoodsBean();
//            itemsBean6.setGoods(goodsBean6);
//
//    List<TurntableConfigChildBean.ItemsBean> itemsBeans = new ArrayList<>();
//
//            itemsBeans.add(itemsBean1);
//            itemsBeans.add(itemsBean2);
//            itemsBeans.add(itemsBean3);
//            itemsBeans.add(itemsBean4);
//            itemsBeans.add(itemsBean5);
//            itemsBeans.add(itemsBean6);
}
