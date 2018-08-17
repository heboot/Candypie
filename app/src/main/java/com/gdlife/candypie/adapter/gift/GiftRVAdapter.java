package com.gdlife.candypie.adapter.gift;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MKey;
import com.gdlife.candypie.databinding.FragmentGiftBinding;
import com.heboot.bean.gift.GiftBean;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class GiftRVAdapter extends BaseRecyclerViewAdapter {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GiftRVAdapter.ViewHolder(parent, R.layout.fragment_gift);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    private class ViewHolder extends BaseRecyclerViewHolder<User, FragmentGiftBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final User s, int position) {

        }


    }


    @Override
    public int getItemCount() {
        return data.size() % 8 == 0 ? data.size() / 8 : data.size() / 8 + 1;
    }
}
