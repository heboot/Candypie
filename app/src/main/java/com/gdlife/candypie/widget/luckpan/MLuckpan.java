package com.gdlife.candypie.widget.luckpan;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.heboot.bean.luckypan.TurntableConfigChildBean;

import java.util.List;

public class MLuckpan extends View {

    private List<TurntableConfigChildBean.ItemsBean> itemsBeans;

    private int panNum;

    public MLuckpan(Context context, List<TurntableConfigChildBean.ItemsBean> itemsBeans) {
        super(context);
        this.itemsBeans = itemsBeans;
        init(context);
    }

    public MLuckpan(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MLuckpan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        panNum = itemsBeans.size();
    }

}
