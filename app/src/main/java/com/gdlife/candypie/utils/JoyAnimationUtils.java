package com.gdlife.candypie.utils;

import android.animation.Animator;
import android.view.View;

import com.gdlife.candypie.listener.AnimatorListener;


public class JoyAnimationUtils {
    private static final int ANIMATION_TIME = 300;

    public static void makeProgressDismiss(final View fromView, final View toView) {
        makeProgressDismissWithListener(fromView, toView, null);
    }

    public static void makeProgressDismissWithListener(final View fromView, final View toView, final AnimatorListener animatorListener) {
        if (null != fromView) {
            if (fromView.getVisibility() != View.VISIBLE) {
                fromView.setVisibility(View.VISIBLE);
            }
            fromView.animate().alpha(0f).setListener(new AnimatorListener() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    fromView.setVisibility(View.GONE);
                }

            }).setDuration(ANIMATION_TIME).start();
        }
        toView.setAlpha(0f);
        toView.setVisibility(View.GONE);
        toView.animate().alpha(1f).setListener(new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (null != animatorListener) {
                    animatorListener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                toView.setVisibility(View.VISIBLE);
                if (null != animatorListener) {
                    animatorListener.onAnimationStart(animation);
                }
            }
        }).setDuration(ANIMATION_TIME).start();
    }
}
