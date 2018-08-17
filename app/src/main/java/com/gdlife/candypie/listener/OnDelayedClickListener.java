package com.gdlife.candypie.listener;

import android.view.View;

/**
 * Created by xicheng on 15/4/18.
 */
public abstract class OnDelayedClickListener implements View.OnClickListener {
    private static final int DELAY_TIME = 2000;
    private int delayed;
    private final Object lock = new Object();
    private long currentTime = 0;

    public OnDelayedClickListener(int delayed) {
        this.delayed = delayed;
    }

    public OnDelayedClickListener() {
        this.delayed = DELAY_TIME;
    }

    @Override
    public final void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - currentTime > delayed) {
            synchronized (lock) {
                if (nowTime - currentTime > delayed) {
                    currentTime = nowTime;
                    onDelayClick(v);
                }
            }
        }
    }

    public abstract void onDelayClick(View view);

}
