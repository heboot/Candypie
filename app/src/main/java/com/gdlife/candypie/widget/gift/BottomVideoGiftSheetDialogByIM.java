package com.gdlife.candypie.widget.gift;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
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

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.video.VideoChatActivity;
import com.gdlife.candypie.adapter.gift.GiftAdapter;
import com.gdlife.candypie.base.HttpObserver;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.databinding.FragmentGiftBinding;
import com.gdlife.candypie.databinding.LayoutVideoGift2Binding;
import com.gdlife.candypie.databinding.LayoutVideoGiftByImBinding;
import com.gdlife.candypie.fragments.gift.GiftFragment2;
import com.gdlife.candypie.http.HttpClient;
import com.gdlife.candypie.repository.GiftRepository;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.SignUtils;
import com.gdlife.candypie.view.MHV;
import com.gdlife.candypie.view.ScrollViewListener;
import com.heboot.base.BaseBean;
import com.heboot.bean.gift.GiftBean;
import com.heboot.bean.video.VideoChatStratEndBean;
import com.heboot.event.IMEvent;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.LogUtil;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.yalantis.dialog.TipCustomDialog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by heboot on 2018/2/2.
 */

public class BottomVideoGiftSheetDialogByIM extends Dialog {

    // 动画时长
    private final static int mAnimationDuration = 200;
    // 持有 ContentView，为了做动画

    private View mContentView;

    private boolean mIsAnimating = false;

    public BottomVideoGiftSheetDialogByIM(Context context) {
        super(context);
    }

    public BottomVideoGiftSheetDialogByIM(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private BottomVideoGiftSheetDialogByIM dialog;

        GiftRepository giftRepository;


        private Consumer<GiftBean> consumer;

        private String toUid;


        private GiftAdapter giftAdapter1, giftAdapter2;

        private LayoutVideoGiftByImBinding binding;
        GiftBean giftBean = null;
        boolean scFlag = false;


        public Builder(Context context, Consumer<GiftBean> consumer, String toUid) {
            this.context = context;
            this.consumer = consumer;
            this.toUid = toUid;
        }


        public BottomVideoGiftSheetDialogByIM create() {
            giftRepository = new GiftRepository();


            dialog = new BottomVideoGiftSheetDialogByIM(context, R.style.QMUI_Dialog_Wrapper_FullScreen);

            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_video_gift_by_im, null, false);

            dialog.setmContentView(binding.getRoot());


            binding.tvCoinBalance.setText(UserService.getInstance().getUser().getCoin());

            binding.qrbRecharge.setOnClickListener((v) -> {
                IntentUtils.toRechargeActivity(context, RechargeType.COIN);
            });


            binding.container.setOnClickListener((v) -> {
                dialog.dismiss();
            });
            binding.qrbSend.setOnClickListener((v) -> {
                if (giftAdapter1.getSelectedGiftBean() == null && giftAdapter2 != null && giftAdapter2.getSelectedGiftBean() == null) {
                    return;
                }

                if (giftRepository.getAllGifts().size() > 8) {

                    if (giftAdapter1.getSelectedGiftBean() != null) {
                        giftBean = giftAdapter1.getSelectedGiftBean();
                    } else if (giftAdapter2 != null && giftAdapter2.getSelectedGiftBean() != null) {
                        giftBean = giftAdapter2.getSelectedGiftBean();
                    }
                } else {
                    giftBean = giftAdapter1.getSelectedGiftBean();
                }
                binding.ivGiftBg.setVisibility(View.VISIBLE);
                binding.clytGift.setVisibility(View.INVISIBLE);
                binding.svgaview.setVisibility(View.VISIBLE);


                if (Integer.parseInt(UserService.getInstance().getUser().getCoin()) < Integer.parseInt(giftBean.getPrice())) {
                    dialog.dismiss();
                    TipCustomDialog coinDialog = new TipCustomDialog.Builder(context, new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            if (integer == 1) {
                                IntentUtils.toRechargeActivity(context, RechargeType.COIN);
                            }

                        }
                    }, "你的钻石余额不足\n请充值", "取消", "充值"

                    ).create();
                    coinDialog.show();
                    return;
                }

                Map<String, Object> params = new HashMap<>();

                params = SignUtils.getNormalParams();

//                params.put(MKey.USER_SERVICE_ID, userServiceId);
                params.put(MKey.GIFT_ID, giftBean.getId());
                params.put(MKey.NUMS, 1);
                params.put(MKey.TO_UID, toUid.replace(MValue.CHAT_PRIEX, ""));
                String sign = SignUtils.doSign(params);
                params.put(MKey.SIGN, sign);
                HttpClient.Builder.getGuodongServer().send_gift(params).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new HttpObserver<VideoChatStratEndBean>() {
                    @Override
                    public void onSuccess(BaseBean<VideoChatStratEndBean> baseBean) {
//                        RxBus.getInstance().post(new IMEvent.UPDATE_IM_STAUTS_EVENT(context, toUid, baseBean.getData().getIs_im_chat()));
                        loadAnimation(context, giftBean.getPlay_url());
                    }

                    @Override
                    public void onError(BaseBean<VideoChatStratEndBean> baseBean) {

                    }
                });

//                loadAnimation(context, giftBean.getPlay_url());

//                videoChatActivityWeakReference.get().sendGift(giftBean);
//                try {
//                    consumer.accept(giftBean);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            });

            FragmentGiftBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(MAPP.mapp), R.layout.fragment_gift, null, false);
            binding1.rvList.setLayoutManager(new GridLayoutManager(context, 4));
            giftAdapter1 = new GiftAdapter(giftRepository.getVideoGiftsByPage1());
            binding1.rvList.setAdapter(giftAdapter1);


            binding.rvList.addView(binding1.getRoot());


            if (giftRepository.getAllGifts().size() > 8) {

                binding.llytPoint.setVisibility(View.VISIBLE);

                FragmentGiftBinding binding2 = DataBindingUtil.inflate(LayoutInflater.from(MAPP.mapp), R.layout.fragment_gift, null, false);
                binding2.rvList.setLayoutManager(new GridLayoutManager(context, 4));
                giftAdapter2 = new GiftAdapter(giftRepository.getVideoGiftsByPage2());
                binding2.rvList.setAdapter(giftAdapter2);

                binding.rvList.addView(binding2.getRoot());

                binding.sv.setScrollViewListener(new ScrollViewListener() {
                    @Override
                    public void onScrollChanged(MHV scrollView, int x, int y, int oldx, int oldy) {
                        LogUtil.e("wocaohuadong", x + ">>>>>" + oldx);
                        if (x > 50) {
                            if (!scFlag) {
                                binding.sv.arrowScroll(View.FOCUS_RIGHT);

                                scFlag = true;
                                binding.cb1.setChecked(false);
                                binding.cb1.setSelected(false);
                                binding.cb2.setChecked(true);
                                binding.cb2.setSelected(true);
                            }
                        } else {
                            if (!scFlag) {
                                binding.sv.arrowScroll(View.FOCUS_LEFT);
                                scFlag = true;
                                binding.cb1.setChecked(true);
                                binding.cb1.setSelected(true);
                                binding.cb2.setChecked(false);
                                binding.cb2.setSelected(false);
                            }
                        }
                    }
                });

            }


//

            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setAttributes(layoutParams);
            return dialog;
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

        private void loadAnimation(Context context, String s) {
            binding.svgaview.setLoops(1);
            SVGAParser parser = new SVGAParser(context);
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
                        binding.svgaview.setVisibility(View.VISIBLE);
                        SVGADrawable drawable = new SVGADrawable(videoItem);
                        binding.svgaview.setImageDrawable(drawable);
                        binding.svgaview.setCallback(new SVGACallback() {
                            @Override
                            public void onPause() {

                            }

                            @Override
                            public void onFinished() {
                                try {
                                    binding.svgaview.stopAnimation();
//                                binding.svgaview.setVisibility(View.GONE);
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    LogUtil.e("播放礼物错误", e.getMessage());
                                    e.printStackTrace();
                                }

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
                        LogUtil.e("播放礼物错误", "error");
                    }
                });
            } catch (Exception e) {
                System.out.print(true);
                LogUtil.e("播放礼物错误>>", e.getMessage());
            }
        }

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
                            BottomVideoGiftSheetDialogByIM.super.dismiss();
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
        animateUp();

    }

    @Override
    public void dismiss() {
        LogUtil.e("播放动画", "dimiss" + mIsAnimating);
        if (mIsAnimating) {
            return;
        }
        animateDown();
    }
}
