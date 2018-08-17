package com.gdlife.candypie.view.luckpan;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.gdlife.candypie.R;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.List;

public class MPan extends View {

    /**
     * 扇形尺寸
     */
    private float[] sizes = new float[]{30f, 30f, 100f, 80f, 50f, 35f};

    private int panNum;

    private String[] strs;

    private Integer[] images;

    private Paint dPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private List<Bitmap> bitmaps = new ArrayList<>();

    public MPan(Context context) {
        super(context);
    }

    public MPan(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MPan(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MPan(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        checkPanState(context,attrs);
        dPaint.setColor(Color.rgb(255, 133, 132));
        sPaint.setColor(Color.rgb(254, 104, 105));
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(QMUIDisplayHelper.dp2px(context, 16));

        for (int i = 0; i < panNum; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), images[i]);
            bitmaps.add(bitmap);
        }
    }

    private void checkPanState(Context context, AttributeSet attrs) {
        LogUtil.e("luckpan", "start load luckpan resources ...");
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.luckpan);
        panNum = 6;// typedArray.getInteger(R.styleable.luckpan_pannum,0);
        if (360 % panNum != 0)
            throw new RuntimeException("can't split pan for all icon.");
        int nameArray = typedArray.getResourceId(R.styleable.luckpan_names, -1);
        if (nameArray == -1) throw new RuntimeException("Can't find pan name.");
        strs = context.getResources().getStringArray(nameArray);
        int iconArray = typedArray.getResourceId(R.styleable.luckpan_icons, -1);
        if (iconArray == -1) throw new RuntimeException("Can't find pan icon.");

        String[] iconStrs = context.getResources().getStringArray(iconArray);
        List<Integer> iconLists = new ArrayList<>();
        for (int i = 0; i < iconStrs.length; i++) {
            iconLists.add(context.getResources().getIdentifier(iconStrs[i], "mipmap", context.getPackageName()));
        }

        images = iconLists.toArray(new Integer[iconLists.size()]);
//        Logger.getLogger().d(A/zrrays.toString(images));
        typedArray.recycle();
        if (strs == null || images == null)
            throw new RuntimeException("Can't find string or icon resources.");
        if (strs.length != panNum || images.length != panNum)
            throw new RuntimeException("The string length or icon length  isn't equals panNum.");
//        Logger.getLogger().d("l/oad luckpan resources successfully -_- ~");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
