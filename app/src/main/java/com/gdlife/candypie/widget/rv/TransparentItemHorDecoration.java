package com.gdlife.candypie.widget.rv;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gdlife.candypie.R;

/**
 * Created by Heboot on 2017/9/27.
 */

public class TransparentItemHorDecoration extends RecyclerView.ItemDecoration {


    private Paint dividerPaint;

    public TransparentItemHorDecoration() {
        dividerPaint = new Paint();
        dividerPaint.setColor(Color.TRANSPARENT);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = parent.getContext().getResources().getDimensionPixelSize(R.dimen.x7);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
//        int left = parent.getPaddingLeft();
//        int right = parent.getWidth() - parent.getPaddingRight();

        int tt = parent.getPaddingTop();
        int bb = parent.getHeight() - parent.getPaddingBottom();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);

            float ll = view.getLeft();

            float rr = view.getRight() + parent.getContext().getResources().getDimensionPixelSize(R.dimen.x7);

            c.drawRect(ll, tt, rr, bb, dividerPaint);
        }


//        for (int i = 0; i < childCount - 1; i++) {
//            View view = parent.getChildAt(i);
//            float top = view.getBottom();
//            float bottom = view.getBottom() + dividerHeight;
//            c.drawRect(left, top, right, bottom, dividerPaint);
//        }
    }


}
