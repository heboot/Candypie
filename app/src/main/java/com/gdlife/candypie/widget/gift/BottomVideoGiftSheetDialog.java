package com.gdlife.candypie.widget.gift;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.gdlife.candypie.adapter.gift.GiftFragmentAdapter;
import com.gdlife.candypie.adapter.pay.CouponFragmentAdapter;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.common.RechargeType;
import com.gdlife.candypie.databinding.FragmentGiftBinding;
import com.gdlife.candypie.databinding.LayoutVideoGift2Binding;
import com.gdlife.candypie.databinding.LayoutVideoGiftPagerBinding;
import com.gdlife.candypie.fragments.gift.GiftFragment2;
import com.gdlife.candypie.fragments.gift.GiftRVFragment;
import com.gdlife.candypie.fragments.pay.CouponFragment;
import com.gdlife.candypie.repository.GiftRepository;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.ToastUtils;
import com.gdlife.candypie.view.MHV;
import com.gdlife.candypie.view.ScrollViewListener;
import com.heboot.bean.gift.GiftBean;
import com.heboot.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heboot on 2018/2/2.
 */

public class BottomVideoGiftSheetDialog extends Dialog {

    // 动画时长
    private final static int mAnimationDuration = 200;
    // 持有 ContentView，为了做动画

    private View mContentView;

    private boolean mIsAnimating = false;

    public BottomVideoGiftSheetDialog(Context context) {
        super(context);
    }

    public BottomVideoGiftSheetDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private BottomVideoGiftSheetDialog dialog;

        private GiftRepository giftRepository;

        private List<GiftRVFragment> giftRVFragments = new ArrayList<>();

        private FragmentManager fragmentManager;

        public Builder(Context context, FragmentManager f) {
            this.context = context;
            this.fragmentManager = f;
        }


        public BottomVideoGiftSheetDialog create() {

            giftRepository = new GiftRepository();

            dialog = new BottomVideoGiftSheetDialog(context, R.style.QMUI_BottomSheet);

            LayoutVideoGiftPagerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_video_gift_pager, null, false);

            binding.tvCoinBalance.setText(UserService.getInstance().getUser().getCoin());

            binding.qrbRecharge.setOnClickListener((v) -> {
                IntentUtils.toRechargeActivity(context, RechargeType.COIN);
            });

            List<GiftBean> allGifts = giftRepository.getAllGifts();


            for (int i = 0; i < allGifts.size(); i++) {
                List<GiftBean> giftBeans = new ArrayList<>();
                giftBeans.add(allGifts.get(i));
                if (i > 0 && (giftBeans.size() == 8 || i % 7 == 0 || i == allGifts.size() - 1)) {
//                    GiftRVFragment giftRVFragment = GiftRVFragment.newInstance(giftBeans);
                    GiftRVFragment giftRVFragment = new GiftRVFragment(giftBeans);
                    giftRVFragments.add(giftRVFragment);
//                    giftBeans.clear();
                }
            }


            binding.viewPager.setAdapter(new GiftFragmentAdapter(fragmentManager, giftRVFragments));


            binding.qrbSend.setOnClickListener((v) -> {
                if (MValue.currentSelectedGiftBean != null) {
                    ToastUtils.showToast(MValue.currentSelectedGiftBean.getName());
                }
                dialog.dismiss();
            });


            dialog.setmContentView(binding.getRoot());

//            FragmentGiftBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(MAPP.mapp), R.layout.fragment_gift, null, false);
//            binding1.rvList.setLayoutManager(new GridLayoutManager(context, 4));
//            giftAdapter1 = new GiftAdapter(giftRepository.getVideoGiftsByPage1());
//            binding1.rvList.setAdapter(giftAdapter1);
//
//
//            binding.rvList.addView(binding1.getRoot());
//
//
//            if (giftRepository.getAllGifts().size() > 8) {
//
//                binding.llytPoint.setVisibility(View.VISIBLE);
//
//                FragmentGiftBinding binding2 = DataBindingUtil.inflate(LayoutInflater.from(MAPP.mapp), R.layout.fragment_gift, null, false);
//                binding2.rvList.setLayoutManager(new GridLayoutManager(context, 4));
//                giftAdapter2 = new GiftAdapter(giftRepository.getVideoGiftsByPage2());
//                binding2.rvList.setAdapter(giftAdapter2);
//
//                binding.rvList.addView(binding2.getRoot());
//
//                binding.sv.setScrollViewListener(new ScrollViewListener() {
//                    @Override
//                    public void onScrollChanged(MHV scrollView, int x, int y, int oldx, int oldy) {
//                        LogUtil.e("wocaohuadong", x + ">>>>>" + oldx);
//                        if (x > 50) {
//                            if (!scFlag) {
//                                binding.sv.arrowScroll(View.FOCUS_RIGHT);
//
//                                scFlag = true;
//                                binding.cb1.setChecked(false);
//                                binding.cb1.setSelected(false);
//                                binding.cb2.setChecked(true);
//                                binding.cb2.setSelected(true);
//                            }
//                        } else {
//                            if (!scFlag) {
//                                binding.sv.arrowScroll(View.FOCUS_LEFT);
//                                scFlag = true;
//                                binding.cb1.setChecked(true);
//                                binding.cb1.setSelected(true);
//                                binding.cb2.setChecked(false);
//                                binding.cb2.setSelected(false);
//                            }
//                        }
//                    }
//                });

//            }


//

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
                            BottomVideoGiftSheetDialog.super.dismiss();
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
        if (mIsAnimating) {
            return;
        }
        animateDown();
    }
}
