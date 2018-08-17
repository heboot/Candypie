package com.netease.nim.uikit.business.session.viewholder;

import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.heboot.bean.message.CustomMessageModel;
import com.heboot.entity.CustomMessageData;
import com.heboot.message.MessageType;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;


public class MsgViewHolderTip extends MsgViewHolderBase {

    protected TextView tvContent;

    public MsgViewHolderTip(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.layout_message_tip;
    }

    @Override
    protected void inflateContentView() {
        tvContent = view.findViewById(R.id.tv_content);
    }

    @Override
    protected void bindContentView() {
        CustomMessageModel model = null;
        try {
            model = JSON.parseObject(message.getAttachment().toJson(false), CustomMessageModel.class);
        } catch (Exception e) {

        }

        if (model != null) {
            if (model.getType().equals(MessageType.MESSAGE_TYPE_TIP)) {
                CustomMessageData customMessageData = JSON.parseObject(model.getData(), CustomMessageData.class);
                if (customMessageData != null) {
                    handleTextNotification(customMessageData.getTitle());
                }


            }
        }

    }

    private void handleTextNotification(String tip) {

        tvContent.setText(tip);


    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
