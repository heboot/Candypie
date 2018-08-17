package com.gdlife.candypie.widget.rv;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Heboot on 2017/9/27.
 */

public class TransparentItemDecoration extends RecyclerView.ItemDecoration {

    private int dividerHeight;

    private Paint dividerPaint;

    public TransparentItemDecoration(int height, int color) {
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
//        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.divider_height);
        dividerHeight = height;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }


}
