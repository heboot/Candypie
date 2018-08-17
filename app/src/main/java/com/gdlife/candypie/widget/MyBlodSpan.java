package com.gdlife.candypie.widget;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by heboot on 2018/3/19.
 */

public class MyBlodSpan extends ClickableSpan {

    private int color;


    private boolean bold;

    public MyBlodSpan(int color, boolean isBold) {
        this.color = color;
        this.bold = isBold;

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setFakeBoldText(bold);
        ds.setColor(color);
    }
}
