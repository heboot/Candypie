package com.netease.nim.uikit.business.session.viewholder;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.heboot.bean.message.CustomMessageModel;
import com.heboot.bean.message.SystemMessage;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.event.MessageEvent;
import com.heboot.message.MessageType;
import com.heboot.rxbus.RxBus;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.databinding.LayoutMessageOrderBinding;


public class MsgViewHolderSystemMSG extends MsgViewHolderBase {

    protected TextView tvContent;

    private TextView tvtitle;

    private CustomMessageModel model = null;

    private ConstraintLayout container;

    public MsgViewHolderSystemMSG(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
//        binding = DataBindingUtil.inflate(LayoutInflater.from(NimUIKit.getContext()), R.layout.layout_message_order, null, false);
        return R.layout.layout_message_system_notification;
    }

    @Override
    protected void inflateContentView() {
        container = (ConstraintLayout) view.findViewById(R.id.clyt_container);
        tvContent = view.findViewById(R.id.tv_content);
        tvtitle = view.findViewById(R.id.tv_title);
    }

    @Override
    protected void bindContentView() {
        model = null;
        try {
            model = JSON.parseObject(message.getAttachment().toJson(false), CustomMessageModel.class);
        } catch (Exception e) {

        }

        if (model != null) {
            if (model.getType().equals(MessageType.MESSAGE_TYPE_SYSTEM_MSG)) {
                SystemMessage systemMessage = JSON.parseObject(model.getData(), SystemMessage.class);
                if (systemMessage != null) {
                    handleTextNotification(systemMessage);
                }


            }
        }

    }

    private void handleTextNotification(SystemMessage systemMessage) {

        tvContent.setText(systemMessage.getMsg());
        tvtitle.setText(systemMessage.getTitle());

        view.setOnClickListener((v) -> {
            RxBus.getInstance().post(new MessageEvent.DoSystemMessageActionEvent(model.getTo_action(), model.getData(), systemMessage));
        });
        container.setOnClickListener((v) -> {
            RxBus.getInstance().post(new MessageEvent.DoSystemMessageActionEvent(model.getTo_action(), model.getData(), systemMessage));
        });
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
