package com.gdlife.candypie.adapter.auth;

import android.view.ViewGroup;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.ItemHomepageSkill2Binding;
import com.gdlife.candypie.databinding.ItemHomepageSkillBinding;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.bean.config.ConfigBean;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by heboot on 2018/2/28.
 */

public class AuthSkillsAdapter extends BaseRecyclerViewAdapter {


    public AuthSkillsAdapter(List<ConfigBean.ServiceItemsConfigBean.ListBean> mdata) {
        data.addAll(mdata);

    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AuthSkillsAdapter.ViewHolder(parent, R.layout.item_homepage_skill2);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<ConfigBean.ServiceItemsConfigBean.ListBean, ItemHomepageSkill2Binding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final ConfigBean.ServiceItemsConfigBean.ListBean s, int position) {
            binding.setListBean(s);
            binding.getRoot().setOnClickListener((v) -> {
                IntentUtils.toAuthSkillActivity(binding.getRoot().getContext(), s);
            });

        }

    }
}
