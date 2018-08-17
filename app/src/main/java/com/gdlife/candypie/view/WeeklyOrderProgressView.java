package com.gdlife.candypie.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.gdlife.candypie.R;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.utils.LogUtil;

public class WeeklyOrderProgressView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float meetOrder, videoOrder = 0f;

    public WeeklyOrderProgressView(Context context, String meetOrderString, String videoOrderString) {

        super(context);
        LogUtil.e("画圈1", "WeeklyOrderProgressView" + meetOrderString + ">>>>>" + videoOrderString + ">>>");
        if (!StringUtils.isEmpty(meetOrderString)) {
            meetOrder = Float.parseFloat(meetOrderString);
        }
        if (!StringUtils.isEmpty(videoOrderString)) {
            videoOrder = Float.parseFloat(videoOrderString);
        }
    }

    public WeeklyOrderProgressView(Context context) {
        super(context);
        LogUtil.e("画圈2", "WeeklyOrderProgressView");
    }

    public WeeklyOrderProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LogUtil.e("画圈3", "WeeklyOrderProgressView");
    }

    public WeeklyOrderProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LogUtil.e("画圈4", "WeeklyOrderProgressView");
//        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.JoyProgressFramelayout, defStyleAttr, 0);
//        if (a != null) {
////            meetOrder = a.getResourceId(R.styleable.WeeklyorderProgressStyle_meet_order, 0);
////            videoOrder = a.getResourceId(R.stayleable.WeeklyorderProgressStyle_video_order, 0);
//            String meetOrderString = a.getString(R.styleable.WeeklyorderProgressStyle_meet_order);
//            String videoOrderString = a.getString(R.styleable.WeeklyorderProgressStyle_video_order);
//            if (!StringUtils.isEmpty(meetOrderString)) {
//                meetOrder = Float.parseFloat(meetOrderString);
//            }
//            if (!StringUtils.isEmpty(videoOrderString)) {
//                videoOrder = Float.parseFloat(videoOrderString);
//            }
//
//            a.recycle();
//        }


    }

    public WeeklyOrderProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        LogUtil.e("画圈5", "WeeklyOrderProgressView");

    }

    @Override
    protected void onDraw(Canvas canvas) {

        LogUtil.e("画圈6", "WeeklyOrderProgressView");

        float result = meetOrder + videoOrder;

        float meetResult, videoResult;

        if (videoOrder <= 0) {
            meetResult = 360 * (meetOrder / result);
        } else {
            meetResult = 360 * (meetOrder / result)  ;//- 10
        }

        if(meetOrder <=0){
            videoResult = 360 * (videoOrder / result);
        }else{
            videoResult = 360 * (videoOrder / result) - 10;
        }



        //修改为画线模式
        paint.setStyle(Paint.Style.STROKE);

        //线条宽度
        paint.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.x10));

        paint.setColor(ContextCompat.getColor(getContext(), R.color.white));


        float x = 0f, y = 0f;

        RectF rectF = new RectF(x + 20, y + 20, getWidth() - 20, getHeight() - 20);

        canvas.drawArc(rectF, -90, 360, false, paint);

        LogUtil.e("画圈", meetResult + ">>>" + videoResult + ">>>>>" + meetOrder + ">>>>" + videoOrder);


        if (meetOrder > 0 && meetOrder > videoOrder) {
            //设置画笔颜色
            paint.setColor(ContextCompat.getColor(getContext(), R.color.theme_color));

            canvas.drawArc(rectF, -90, meetResult, false, paint);

            paint.setColor(ContextCompat.getColor(getContext(), R.color.color_FF5252));

            canvas.drawArc(rectF, meetResult - 90 + 5, videoResult, false, paint);

        } else if (videoOrder > 0 && videoOrder > meetOrder) {
            //设置画笔颜色
            paint.setColor(ContextCompat.getColor(getContext(), R.color.color_FF5252));

            canvas.drawArc(rectF, -90, videoResult, false, paint);

            paint.setColor(ContextCompat.getColor(getContext(), R.color.theme_color));

            canvas.drawArc(rectF, videoResult - 90 + 5, meetResult, false, paint);

        } else if (videoOrder > 0 && videoOrder == meetOrder) {

            //设置画笔颜色
            paint.setColor(ContextCompat.getColor(getContext(), R.color.color_FF5252));

            canvas.drawArc(rectF, -90, 90, false, paint);

            paint.setColor(ContextCompat.getColor(getContext(), R.color.theme_color));

            canvas.drawArc(rectF, 90, -90, false, paint);

        } else if (videoOrder == 0 && meetOrder == 0) {//&& meetResult == 0 && videoResult == 0
            paint.setColor(ContextCompat.getColor(getContext(), R.color.color_898A9E));
            canvas.drawArc(rectF, -90, 360, false, paint);
        }


        super.onDraw(canvas);
    }
}
