package com.gdlife.candypie.adapter.index;

import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.common.CustomEvent;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.ItemLevelRateBinding;
import com.gdlife.candypie.databinding.LayoutActiveUserBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.CustomEventUtil;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.bean.pay.ServiceLevelBean;
import com.heboot.entity.User;
import com.heboot.event.IMEvent;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.heboot.rxbus.RxBus;
import com.heboot.utils.DateUtil;

import java.util.List;

import static com.gdlife.candypie.common.CustomEvent.TO_USERPAGE_BY_SEARCH;
import static com.gdlife.candypie.common.CustomEvent.TO_USERPAGE_BY_VISIT;

public class ActiveAdapter extends BaseRecyclerViewAdapter {

    private boolean isSearch;

    public ActiveAdapter(List<User> list, boolean isSearch) {
        this.data = list;
        this.isSearch = isSearch;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActiveAdapter.ViewHolder(parent, R.layout.layout_active_user);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<User, LayoutActiveUserBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final User s, int position) {
            binding.setUser(s);
            binding.includeSexage.setUser(s);
            binding.tvInfo.setText(s.getCity());//+ "·" + DateUtil.getRecommendUserInterval(s.getUpdate_time() * 1000l) + "登录"

            binding.ivMsg.setOnClickListener((v) -> {
                if (UserService.getInstance().checkTourist()) {
                    return;
                }
                if (UserService.getInstance().getUser() != null && s.getId() == UserService.getInstance().getUser().getId()) {
                    return;
                }
                RxBus.getInstance().post(new IMEvent.QUERY_IM_STAUTS_EVENT(binding.getRoot().getContext(), String.valueOf(s.getId())));
            });

            binding.ivAvatar.setOnClickListener((v) -> {
//                if (UserService.getInstance().isServicer()) {
//                    IntentUtils.toUserInfoActivity(v.getContext(), MValue.USER_INFO_TYPE_NORMAL, MValue.USER_INFO_TYPE_NORMAL, s, null, null);
//                } else {
//                    IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_OTHER, s, null, null);
//                }
                if (isSearch) {
                    CustomEventUtil.onEvent(TO_USERPAGE_BY_SEARCH);
                } else {
                    CustomEventUtil.onEvent(TO_USERPAGE_BY_VISIT);
                }
                if (s.getService_auth_status() != null && s.getService_auth_status() == MValue.AUTH_STATUS_SUC) {
                    IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_OTHER, s, null, null);
                } else {
                    IntentUtils.toUserInfoActivity(v.getContext(), MValue.USER_INFO_TYPE_NORMAL, MValue.USER_INFO_TYPE_NORMAL, s, null, null);
                }

            });

            binding.getRoot().setOnClickListener((v) -> {
                if (isSearch) {
                    CustomEventUtil.onEvent(TO_USERPAGE_BY_SEARCH);
                } else {
                    CustomEventUtil.onEvent(TO_USERPAGE_BY_VISIT);
                }
                if (s.getService_auth_status() != null && s.getService_auth_status() == MValue.AUTH_STATUS_SUC) {
                    IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_OTHER, s, null, null);
                } else {
                    IntentUtils.toUserInfoActivity(v.getContext(), MValue.USER_INFO_TYPE_NORMAL, MValue.USER_INFO_TYPE_NORMAL, s, null, null);
                }
            });
        }


    }

}
