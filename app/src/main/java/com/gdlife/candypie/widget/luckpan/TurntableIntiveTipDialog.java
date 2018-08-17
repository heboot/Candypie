package com.gdlife.candypie.widget.luckpan;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.video.VideoChatActivity;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.databinding.DialogTipBinding;
import com.gdlife.candypie.databinding.DialogTurntableBinding;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.serivce.PayService;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.luckypan.TurntableConfigBean;
import com.heboot.event.VideoChatEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by heboot on 2018/2/2.
 */

public class TurntableIntiveTipDialog extends Dialog {


    public TurntableIntiveTipDialog(Context context) {
        super(context);
    }

    public TurntableIntiveTipDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private String amount;

        private TurntableIntiveTipDialog dialog;

        private long serverTime;

        private long applyTime;

        private DialogTurntableBinding binding;

        private MHandler timeHandler;

        private String userServiceId;

        private int role;

        private String panId;

        public Builder(Context context, String userServiceId, String panId, String amount, long serverTime, long applyTime, int role) {
            this.context = context;
            this.amount = amount;
            this.serverTime = serverTime;
            this.applyTime = applyTime;
            this.userServiceId = userServiceId;
            this.role = role;
            this.panId = panId;

        }

        public TurntableIntiveTipDialog create() {


            dialog = new TurntableIntiveTipDialog(context, R.style.QMUI_TipDialog);

            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_turntable, null, false);

            LogUtil.e("转盘过期时间测试", applyTime + ">>>>>" + serverTime);

            timeHandler = new MHandler((int) ((applyTime - serverTime)), binding.tvTime, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    dialog.dismiss();
                    turntable_refuse();
                }
            });
            timeHandler.sendEmptyMessageDelayed(1, 1000);


            if (role == MValue.USER_ROLE_NORMAL) {
                String result = MAPP.mapp.getString(R.string.turntable_invite_tip3);
                String result2 = String.format(result, amount);
                binding.tipBalance.setText(result2);
                if (Integer.parseInt(UserService.getInstance().getUser().getCoin()) <= PayService.getTurntableNeedCoin(MValue.currentVideoCoinAmount, Integer.parseInt(MAPP.mapp.getConfigBean().getIm_chat_config().getTurntable_amount()))) {
                    binding.tvApply.setText(MAPP.mapp.getString(R.string.to_recharge));
                    binding.tvApply.setOnClickListener((v) -> {
                        IntentUtils.toRechargeActivity(context, RechargeType.COIN);
                    });


                    RxView.clicks(binding.tvRefuse).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            timeHandler.removeMessages(1);
                            dialog.dismiss();
                            turntable_refuse();
                        }
                    });

                } else {
                    RxView.clicks(binding.tvRefuse).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            timeHandler.removeMessages(1);
                            dialog.dismiss();
                            turntable_refuse();
                        }
                    });

                    RxView.clicks(binding.tvApply).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            timeHandler.removeMessages(1);
                            initTurntable(context);
                        }
                    });
                }

            } else {
                RxView.clicks(binding.tvRefuse).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        timeHandler.removeMessages(1);
                        dialog.dismiss();
                        turntable_refuse();
                    }
                });
                RxView.clicks(binding.tvApply).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        timeHandler.removeMessages(1);
                        initTurntable(context);
                    }
                });

            }


            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(layoutParams);
            return dialog;
        }

        private void turntable_refuse() {
            //初始化
            Map<String, Object> params = new HashMap<>();
            params = SignUtils.getNormalParams();
            params.put(MKey.USER_TURNTABLE_ID, panId);
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);
            HttpClient.Builder.getGuodongServer().turntable_refuse(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                @Override
                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {

                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {

                }
            });
        }

        private void initTurntable(Context context) {
            //初始化
            Map<String, Object> params = new HashMap<>();
            params = SignUtils.getNormalParams();
            if (!StringUtils.isEmpty(userServiceId)) {
                params.put(MKey.USER_SERVICE_ID, userServiceId);
            }
            params.put(MKey.USER_TURNTABLE_ID, panId);
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);
            HttpClient.Builder.getGuodongServer().init_turntable(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<TurntableConfigBean>() {
                @Override
                public void onSuccess(BaseBean<TurntableConfigBean> baseBean) {
                    dialog.dismiss();
                    MAPP.mapp.setCurrentTurnableConfigBean(baseBean.getData());
//                    play(context);
                    LuckpanDialog luckpanDialog = new LuckpanDialog.Builder(context, userServiceId, baseBean.getData().getTurntable_config(), true, -1, role).create();
                    luckpanDialog.show();


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
                                    play(context);
                                }
                            });//这里的观察者依然不重要


                }

                @Override
                public void onError(BaseBean<TurntableConfigBean> baseBean) {

                }
            });
        }

        private void play(Context context) {
            //初始化
            Map<String, Object> params = new HashMap<>();
            params = SignUtils.getNormalParams();
            if (!StringUtils.isEmpty(userServiceId)) {
                params.put(MKey.USER_SERVICE_ID, userServiceId);
            }
            params.put(MKey.USER_TURNTABLE_ID, panId);
            String sign = SignUtils.doSign(params);
            params.put(MKey.SIGN, sign);
            HttpClient.Builder.getGuodongServer().play(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<BaseBeanEntity>() {
                @Override
                public void onSuccess(BaseBean<BaseBeanEntity> baseBean) {
                    dialog.dismiss();
//                    LuckpanDialog luckpanDialog = new LuckpanDialog.Builder(context,userServiceId,baseBean.getData().getTurntable_config(),true,0).create();
//                    luckpanDialog.show();
                }

                @Override
                public void onError(BaseBean<BaseBeanEntity> baseBean) {

                }
            });
        }

        static class MHandler extends Handler {


            private int time = 0;


            private TextView textView;

            private Consumer consumer;

            public MHandler(int time, TextView textView, Consumer consumer) {
                this.time = time;
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    removeMessages(1);
                }


            }
        }

    }


}
