package com.gdlife.candypie.adapter.card;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.ItemBalanceBinding;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by heboot on 2017/10/12.
 */

public class CardAdapter extends BaseRecyclerViewAdapter {

    private boolean isShow = true;

    private Set<ImageView> sets;

    public CardAdapter(List<String> userProfileModelList) {
        sets = new HashSet<>();
        data = userProfileModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardAdapter.ViewHolder(parent, R.layout.item_balance);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<String, ItemBalanceBinding> {

        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
//            this.setIsRecyclable(false);
        }

        @Override
        public void onBindViewHolder(final String s, int position) {
        }
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
