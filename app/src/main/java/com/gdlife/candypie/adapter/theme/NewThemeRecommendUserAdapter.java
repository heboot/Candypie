package com.gdlife.candypie.adapter.theme;

import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ItemRecommendUserBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.bean.config.ConfigBean;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by heboot on 2018/2/1.
 */

public class NewThemeRecommendUserAdapter extends BaseRecyclerViewAdapter {


    private ConfigBean.ServiceItemsConfigBean.ListBean listBean;

    public NewThemeRecommendUserAdapter(List<User> mdata, ConfigBean.ServiceItemsConfigBean.ListBean listBean) {
        data = mdata;
        this.listBean = listBean;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewThemeRecommendUserAdapter.ViewHolder(parent, R.layout.item_recommend_user);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<User, ItemRecommendUserBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final User s, int position) {
            binding.setUser(s);
            binding.getRoot().setOnClickListener((v) -> {
                IntentUtils.toHomepageActivity(v.getContext(), UserService.getInstance().getUser() != null && UserService.getInstance().getUser().getId() != null && s.getId() == UserService.getInstance().getUser().getId() ? MValue.FROM_MY : MValue.FROM_OTHER, s, MValue.HOMEPAG_FROM.RECOMMEND_USER, listBean);
            });
        }
    }


}
