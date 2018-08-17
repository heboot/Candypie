package com.gdlife.candypie.adapter.index;

import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.activitys.common.MainActivity;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.LauoutRecommendVideoUsersItemBinding;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.entity.User;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

public class FreevideoRecommendUserAdapter extends BaseRecyclerViewAdapter {

    private WeakReference<MainActivity> weakReference;

    public FreevideoRecommendUserAdapter(WeakReference<MainActivity> weakReference, List<User> users) {
        this.weakReference = weakReference;
        data.addAll(users);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.lauout_recommend_video_users_item);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<User, LauoutRecommendVideoUsersItemBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final User s, int position) {

            binding.setUser(s);

            binding.v1.setOnClickListener((v) -> {
                weakReference.get().postVideoService(s);
            });

            binding.ivAvatar.setOnClickListener((v) -> {
                IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_OTHER, s, null, null);
            });


        }


    }


}
