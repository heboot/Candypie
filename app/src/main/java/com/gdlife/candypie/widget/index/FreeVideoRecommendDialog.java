package com.gdlife.candypie.widget.index;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
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

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.common.MainActivity;
import com.gdlife.candypie.adapter.index.FreevideoRecommendUserAdapter;
import com.gdlife.candypie.databinding.LayoutRecommendVideoUsersBinding;
import com.heboot.bean.index.IndexRecommendConfigBean;

import java.lang.ref.WeakReference;

/**
 * Created by heboot on 2018/2/2.
 */

public class FreeVideoRecommendDialog extends Dialog {
    // 动画时长
    private final static int mAnimationDuration = 200;
    // 持有 ContentView，为了做动画

    private View mContentView;

    private boolean mIsAnimating = false;


    public FreeVideoRecommendDialog(Context context) {
        super(context);
    }

    public FreeVideoRecommendDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;


        private FreeVideoRecommendDialog dialog;

        private IndexRecommendConfigBean indexRecommendConfigBean;

        private WeakReference<MainActivity> weakReference;

        public Builder(Context context, IndexRecommendConfigBean indexPopTipBean, WeakReference<MainActivity> weakReference) {
            this.context = context;
            this.indexRecommendConfigBean = indexPopTipBean;
            this.weakReference = weakReference;
        }

        public FreeVideoRecommendDialog create() {


            dialog = new FreeVideoRecommendDialog(context, R.style.QMUI_BottomSheet);

            LayoutRecommendVideoUsersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_recommend_video_users, null, false);

            binding.vClose.setOnClickListener((v) -> {
                dialog.dismiss();
            });
            dialog.setmContentView(binding.getRoot());
            binding.setRecommendData(indexRecommendConfigBean);

            binding.rvList.setLayoutManager(new GridLayoutManager(context, 2));

            binding.rvList.setAdapter(new FreevideoRecommendUserAdapter(weakReference, indexRecommendConfigBean.getList()));


            dialog.addContentView(binding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                            FreeVideoRecommendDialog.super.dismiss();
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
