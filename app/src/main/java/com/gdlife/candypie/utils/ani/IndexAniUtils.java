package com.gdlife.candypie.utils.ani;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gdlife.candypie.adapter.index.IndexPopMenuAdapter;
import com.gdlife.candypie.serivce.ThemeService;
import com.heboot.bean.config.ConfigBean;
import com.heboot.utils.LogUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by heboot on 2018/2/5.
 */

public class IndexAniUtils {

    private ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 1f);


    /**
     * 左侧菜单的动画
     */
    private int leftAniIndex;
    private ValueAnimator.AnimatorUpdateListener leftAnimatorUpdateListener;
    private Animator.AnimatorListener leftAnimatorListener;

    /**
     * 大球点击的动画
     */
    private int bigBallDuration = 400;

    private ObjectAnimator bigBallAnimator1;
    private ObjectAnimator bigBallAnimator2;
    private ObjectAnimator bigBallAnimator3;

    private AnimatorSet bigBallAnimatorSet;
    private Animator.AnimatorListener bigBallAnimatorListener;


    /**
     * 弹出菜单的动画
     */
    private ValueAnimator.AnimatorUpdateListener popmenuAnimatorUpdateListener;
    private ValueAnimator valueAnimator2 = ObjectAnimator.ofFloat(0, 1f);


    /**
     * 停止左侧菜单的动画
     */
    public void stopLeftAni(View v1, View v2, View v3, View v4, View v5, View v6) {//, View v6
        if (valueAnimator != null) {
            valueAnimator.removeUpdateListener(leftAnimatorUpdateListener);
            valueAnimator.cancel();
        }
//        v1.setAlpha(0);
//        v2.setAlpha(0);
//        v3.setAlpha(0);
//        v4.setAlpha(0);
//        v5.setAlpha(0);
//        v6.setAlpha(0);


//        YoYo.with(Techniques.FadeOutLeft).duration(600).playOn(v1);
//        YoYo.with(Techniques.FadeOutLeft).withListener(leftAnimatorListener).duration(600).delay(100).playOn(v2);
//        YoYo.with(Techniques.FadeOutLeft).withListener(leftAnimatorListener).duration(600).delay(150).playOn(v3);
//        YoYo.with(Techniques.FadeOutLeft).withListener(leftAnimatorListener).duration(600).delay(200).playOn(v4);
//        YoYo.with(Techniques.FadeOutLeft).withListener(leftAnimatorListener).duration(600).delay(250).playOn(v5);
//        YoYo.with(Techniques.FadeOutLeft).withListener(leftAnimatorListener).duration(600).delay(300).playOn(v6);

        v1.animate().alpha(0).translationX(20).setDuration(600).start();
        v2.animate().alpha(0).translationX(20).setDuration(600).start();
        v3.animate().alpha(0).translationX(20).setDuration(600).start();
        v4.animate().alpha(0).translationX(20).setDuration(600).start();
        v5.animate().alpha(0).translationX(20).setDuration(600).start();
        v6.animate().alpha(0).translationX(20).setDuration(600).start();

    }

    /**
     * 播放左侧菜单的动画
     *
     * @param v1
     * @param v2
     * @param v3
     * @param v4
     * @param v5
     * @param v6
     */
    public void showLeftAni(View v1, View v2, View v3, View v4, View v5, View v6) {
        leftAniIndex = 1;
        valueAnimator.setDuration(200);
        if (leftAnimatorUpdateListener == null) {
            leftAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
//                    float ff = (float) animation.getAnimatedValue();
//                    switch (leftAniIndex) {
//                        case 1:
//                            v1.setAlpha(ff);
//                            break;
//                        case 2:
//                            v2.setAlpha(ff);
//                            break;
//                        case 3:
//                            v3.setAlpha(ff);
//                            break;
//                        case 4:
//                            v4.setAlpha(ff);
//                            break;
//                        case 5:
//                            v5.setAlpha(ff);
//                            break;
//                        case 6:
//                            v6.setAlpha(ff);
//                            break;
//                    }
                }
            };
        }
        if (leftAnimatorListener == null) {
            leftAnimatorListener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    leftAniIndex = leftAniIndex + 1;
                    valueAnimator.start();
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            };
        }
        YoYo.with(Techniques.FadeInLeft).duration(600).playOn(v1);
        YoYo.with(Techniques.FadeInLeft).withListener(leftAnimatorListener).duration(600).delay(100).playOn(v2);
//        YoYo.with(Techniques.FadeInLeft).withListener(leftAnimatorListener).duration(600).delay(150).playOn(v3);
//        YoYo.with(Techniques.FadeInLeft).withListener(leftAnimatorListener).duration(600).delay(200).playOn(v4);
//        YoYo.with(Techniques.FadeInLeft).withListener(leftAnimatorListener).duration(600).delay(250).playOn(v5);
//        YoYo.with(Techniques.FadeInLeft).withListener(leftAnimatorListener).duration(600).delay(300).playOn(v6);
    }


    public void showLeftAni2(View v1, View v2, View v3, View v4, View v5, View setPriceView, View enableView) {//, View v6
        int x = QMUIDisplayHelper.dpToPx(25);
        v4.animate().alpha(1f).translationX(x).setDuration(200).start();
        v3.animate().alpha(1f).translationX(x).setDuration(200).setStartDelay(80).start();
        v1.animate().alpha(1f).translationX(x).setDuration(200).setStartDelay(100).start();
        v2.animate().alpha(1f).translationX(x).setDuration(200).setStartDelay(120).start();
        v5.animate().alpha(1f).translationX(x).setDuration(200).setStartDelay(140).start();
        setPriceView.animate().alpha(1f).translationX(x).setDuration(200).setStartDelay(160).start();
        enableView.animate().alpha(1f).translationX(x).setDuration(200).setStartDelay(180).start();
    }
}
