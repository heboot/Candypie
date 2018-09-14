package com.gdlife.candypie.widget.rv;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SearchUserWhite3ItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;//是否有头部

    private Drawable mDriver;

    public SearchUserWhite3ItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
        mDriver = new ColorDrawable(Color.WHITE);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        if (includeEdge) {
            if (position % 2 == 0) {
                outRect.left = spacing;
                outRect.right = spacing;
            } else {
                outRect.right = spacing;
            }
        } else {
            if (position % 2 == 0) {
                outRect.right = spacing;
            } else {
                outRect.left = spacing;
                outRect.right = spacing;
            }
        }
    }

    //
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//
//        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
//        int childCount = parent.getChildCount();
//
//
//        if (includeEdge) {
//            for (int i = 1; i < childCount - 1; i++) {
////            if (i % 2 == 0) {
////                final View child = parent.getChildAt(i);
////                //将带有颜色的分割线处于中间位置
////                final float centerLeft = ((float) (layoutManager.getLeftDecorationWidth(child) + layoutManager.getRightDecorationWidth(child))
////                        * spanCount / (spanCount + 1) + 1 - spacing) / 2;
////                final float centerTop = (layoutManager.getBottomDecorationHeight(child) + 1 - spacing) / 2;
////                //得到它在总数里面的位置
////                final int position = parent.getChildAdapterPosition(child);
////                //判断是否为第一排
////                boolean isFirst = layoutManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount) == 0;
////                //画上边的，第一排不需要上边的,只需要在最左边的那项的时候画一次就好
////                int left = layoutManager.getLeftDecorationWidth(child);
////                int right = parent.getWidth() - layoutManager.getLeftDecorationWidth(child);
////                int top = child.getTop();
////                int bottom = child.getBottom();
////                mDriver.setBounds(left, top, right, bottom);
////                mDriver.draw(c);
////            } else {
//                final View child = parent.getChildAt(i);
//                //画上边的，第一排不需要上边的,只需要在最左边的那项的时候画一次就好
//                int left = layoutManager.getLeftDecorationWidth(child);
//                int right = parent.getWidth() - layoutManager.getLeftDecorationWidth(child);
//                int top = child.getTop();
//                int bottom = child.getBottom();
//                mDriver.setBounds(left, top, right, bottom);
//                mDriver.draw(c);
////            }
//
//            }
//        } else {
//            for (int i = 0; i < childCount - 1; i++) {
////            if (i % 2 == 0) {
////                final View child = parent.getChildAt(i);
////                //将带有颜色的分割线处于中间位置
////                final float centerLeft = ((float) (layoutManager.getLeftDecorationWidth(child) + layoutManager.getRightDecorationWidth(child))
////                        * spanCount / (spanCount + 1) + 1 - spacing) / 2;
////                final float centerTop = (layoutManager.getBottomDecorationHeight(child) + 1 - spacing) / 2;
////                //得到它在总数里面的位置
////                final int position = parent.getChildAdapterPosition(child);
////                //判断是否为第一排
////                boolean isFirst = layoutManager.getSpanSizeLookup().getSpanGroupIndex(position, spanCount) == 0;
////                //画上边的，第一排不需要上边的,只需要在最左边的那项的时候画一次就好
////                int left = layoutManager.getLeftDecorationWidth(child);
////                int right = parent.getWidth() - layoutManager.getLeftDecorationWidth(child);
////                int top = child.getTop();
////                int bottom = child.getBottom();
////                mDriver.setBounds(left, top, right, bottom);
////                mDriver.draw(c);
////            } else {
//                final View child = parent.getChildAt(i);
//                //画上边的，第一排不需要上边的,只需要在最左边的那项的时候画一次就好
//                int left = layoutManager.getLeftDecorationWidth(child);
//                int right = parent.getWidth() - layoutManager.getLeftDecorationWidth(child);
//                int top = child.getTop();
//                int bottom = child.getBottom();
//                mDriver.setBounds(left, top, right, bottom);
//                mDriver.draw(c);
////            }
//
//            }
//        }

    }
}
