package com.gdlife.candypie.adapter.homepage;

import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.FragmentHomepageVideoBinding;
import com.gdlife.candypie.databinding.ItemHomepageSkillBinding;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heboot on 2018/2/28.
 */

public class HomepageVideoRVAdapter extends BaseRecyclerViewAdapter {

    public HomepageVideoRVAdapter() {
        List<String> datas = new ArrayList<>();
        datas.add("111");
        datas.add("22");
        datas.add("33");
        data = datas;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomepageVideoRVAdapter.ViewHolder(parent, R.layout.fragment_homepage_video);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<String, FragmentHomepageVideoBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final String s, int position) {

        }

    }
}
