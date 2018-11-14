package com.gdlife.candypie.adapter.message;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.databinding.ItemOrderRobBinding;
import com.gdlife.candypie.databinding.ItemVideoOrderBinding;
import com.gdlife.candypie.fragments.message.MessageOrderFragment;
import com.gdlife.candypie.serivce.ThemeService;
import com.gdlife.candypie.utils.ImageUtils;
import com.gdlife.candypie.utils.IntentUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.videochat.VideoChatOrderChildBean;
import com.heboot.utils.DateUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by heboot on 2018/3/6.
 */

public class MessageVideoOrderListAdapter extends BaseQuickAdapter<VideoChatOrderChildBean, BaseViewHolder> {


    private WeakReference<MessageOrderFragment> weakReference;

    public MessageVideoOrderListAdapter(int layoutResId, List<VideoChatOrderChildBean> list) {
        super(layoutResId, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoChatOrderChildBean s) {
        ItemVideoOrderBinding binding = DataBindingUtil.bind(helper.itemView);

        binding.tvServeName.setText(s.getUser().getNickname());

        ImageUtils.showAvatar(binding.ivAvatar, s.getUser().getAvatar());

        binding.includeSexage.setUser(s.getUser());

//        binding.btnRegister.setText(s.getAction_config().get(0).getValue());
        binding.tvStatus.setText(s.getStatus().getValue());
        if (s.getStatus() != null && !StringUtils.isEmpty(s.getStatus().getFont_color())) {
            binding.tvStatus.setTextColor(Color.parseColor(s.getStatus().getFont_color()));
        }

        binding.tvTime.setText(DateUtil.getRecommendUserInterval(s.getPost_time() * 1000l));

        binding.tvServeAddr.setText(s.getUser().getCity());


        binding.ivAvatar.setOnClickListener((v) -> {
            IntentUtils.toUserPageActivity(MAPP.mapp.getCurrentActivity(), String.valueOf(s.getUser().getId()));
        });

        binding.getRoot().setOnClickListener((v) -> {
            IntentUtils.toOrderDetailActivity(v.getContext(), s.getId(), true);
        });


    }


}
