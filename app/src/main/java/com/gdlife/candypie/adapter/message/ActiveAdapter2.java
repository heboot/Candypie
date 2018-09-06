package com.gdlife.candypie.adapter.message;

import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.databinding.LayoutActiveUserBinding;
import com.gdlife.candypie.serivce.UserService;
import com.gdlife.candypie.utils.IntentUtils;
import com.heboot.entity.User;
import com.heboot.event.IMEvent;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.heboot.rxbus.RxBus;

import java.util.List;

public class ActiveAdapter2 extends BaseQuickAdapter<User, BaseViewHolder> {


    //layout_active_user
    public ActiveAdapter2(int layoutResId, List<User> list) {
        super(layoutResId, list);
    }


    @Override
    protected void convert(BaseViewHolder helper, User s) {
        LayoutActiveUserBinding binding = DataBindingUtil.bind(helper.itemView);
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
//            if (UserService.getInstance().isServicer()) {
//                IntentUtils.toUserInfoActivity(v.getContext(), MValue.USER_INFO_TYPE_NORMAL, MValue.USER_INFO_TYPE_NORMAL, s, null, null);
//            } else {
//                IntentUtils.toHomepageActivity(v.getContext(), MValue.FROM_OTHER, s, null, null);
//            }
            IntentUtils.toUserPageActivity(v.getContext(), String.valueOf(s.getId()));
        });

        binding.getRoot().setOnClickListener((v) -> {
            IntentUtils.toUserPageActivity(v.getContext(), String.valueOf(s.getId()));
        });
    }


}
