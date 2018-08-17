package com.gdlife.candypie.adapter.video;

import android.content.Context;
import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.LayoutIndexUserBinding;
import com.heboot.bean.video.HomepageVideoBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.List;

public class UserVideosRVAdapter extends BaseRecyclerViewAdapter {

    private List<HomepageVideoBean> userList;

    public UserVideosRVAdapter(Context context, List<HomepageVideoBean> s) {
        this.userList = s;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserVideosRVAdapter.ViewHolder(parent, R.layout.activity_player2);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<String, LayoutIndexUserBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final String s, int position) {



        }


    }
}
