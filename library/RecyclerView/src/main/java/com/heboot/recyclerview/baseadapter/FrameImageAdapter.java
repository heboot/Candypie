package com.heboot.recyclerview.baseadapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by heboot on 2018/1/29.
 */

public class FrameImageAdapter extends BaseRecyclerViewAdapter {

    public FrameImageAdapter(List<String> datas) {
        this.data = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }


}
