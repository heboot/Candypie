package com.heboot.recyclerview.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heboot on 2017/6/30.
 */

public abstract class BaseViewAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater inflater;
    protected List<T> data = new ArrayList<>();


    public BaseViewAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }


}
