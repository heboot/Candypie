package com.gdlife.candypie.adapter.message;

import android.view.ViewGroup;

import com.gdlife.candypie.R;
import com.gdlife.candypie.serivce.MessageService;
import com.heboot.bean.message.SystemMessage;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewAdapter;
import com.heboot.recyclerview.baseadapter.BaseRecyclerViewHolder;
import com.heboot.utils.DateUtil;
import com.netease.nim.uikit.databinding.LayoutMessageSystemNotificationBinding;

import java.util.List;

public class SystemMessageAdapter extends BaseRecyclerViewAdapter {


    private MessageService messageService;

    public SystemMessageAdapter(List<SystemMessage> datas) {
        messageService = new MessageService();
        this.data.addAll(datas);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new SystemMessageAdapter.ViewHolder(parent, R.layout.layout_message_system_notification);
    }


    private class ViewHolder extends BaseRecyclerViewHolder<SystemMessage, LayoutMessageSystemNotificationBinding> {
        //
        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final SystemMessage s, int position) {

            binding.tvTitle.setText(s.getTitle());

            binding.tvTime.setText(DateUtil.getMessageInterval(s.getCreate_time() * 1000));

            binding.tvContent.setText(s.getMsg());

            binding.getRoot().setOnClickListener((v) -> {
                messageService.doSystemMessageAction(s.getTo_action(), s);
            });

        }


    }
}
