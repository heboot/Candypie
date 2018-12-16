package com.gdlife.candypie.adapter.user;

import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.databinding.LayoutIndexVisitItemBinding;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.util.List;

/**
 * 服务者首页 活跃页面 上面看到的来访者 适配器
 */
public class UserFollowAdapter extends BaseRecyclerViewAdapter {

    private User toUser;

    public UserFollowAdapter(List<User> list) {
        this.data = list;
    }

    public UserFollowAdapter(List<User> list, User user) {
        this.data = list;
        this.toUser = user;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserFollowAdapter.ViewHolder(parent, R.layout.layout_index_visit_item);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<User, LayoutIndexVisitItemBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final User s, int position) {
            binding.setUser(s);
            binding.getRoot().setOnClickListener((v) -> {
                IntentUtils.toUserFollowActivity(binding.getRoot().getContext(), s,s.getFollow_love_list().getNums());
            });
        }


    }
}
