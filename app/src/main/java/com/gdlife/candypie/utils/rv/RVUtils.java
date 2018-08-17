package com.gdlife.candypie.utils.rv;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * Created by heboot on 2018/3/13.
 */

public class RVUtils {

    public void initXRecyclerViewDisablePull(XRecyclerView xRecyclerViewDisablePull) {
        xRecyclerViewDisablePull.setPullRefreshEnabled(false);
    }

    public static void initSwiperefreshlayout(SwipeRefreshLayout srl, XRecyclerView xrecyclerView) {
        if (xrecyclerView != null) {
            xrecyclerView.setPullRefreshEnabled(false);
        }
        srl.setEnabled(true);
        setSwiperefreshColor(srl, ContextCompat.getColor(srl.getContext(), R.color.theme_color));
        setSwiperefreshDistance(srl, 500);
        setSwiperefreshBG(srl, Color.WHITE);
        setSwiperefreshSize(srl, SwipeRefreshLayout.DEFAULT);
    }


    /**
     * 设置圆圈的大小
     *
     * @param srl
     * @param size
     */
    private static void setSwiperefreshSize(SwipeRefreshLayout srl, int size) {
        srl.setSize(size);
    }


    /**
     * 设置刷新圆圈颜色
     *
     * @param srl
     * @param color
     */
    private static void setSwiperefreshColor(SwipeRefreshLayout srl, int color) {
        srl.setColorSchemeColors(color);
    }

    /**
     * 设定下拉圆圈的背景
     *
     * @param srl
     * @param bg
     */
    private static void setSwiperefreshBG(SwipeRefreshLayout srl, int bg) {
        srl.setProgressBackgroundColorSchemeColor(bg);
    }

    /**
     * 设置手指在屏幕下拉多少距离会触发下拉刷新
     *
     * @param srl
     * @param distance
     */
    private static void setSwiperefreshDistance(SwipeRefreshLayout srl, int distance) {
        srl.setDistanceToTriggerSync(distance);
    }

}
