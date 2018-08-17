package com.netease.nim.uikit.utils;

import com.heboot.entity.CustomMessageData;
import com.heboot.message.MessageType;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.custom.CustomAttachment;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

public class ChatService {

    public boolean hasMySendMessage(List<IMMessage> messageList) {
        if (messageList == null || messageList.size() <= 0) {
            return false;
        }
        for (IMMessage message : messageList) {
            if (message.getDirect() == MsgDirectionEnum.Out) {
                return true;
            }
        }
        return false;
    }

    public IMMessage getTipLocalMessage(String title) {
        CustomAttachment customAttachment = new CustomAttachment();
        CustomMessageData customMessageData = new CustomMessageData();
        customMessageData.setTitle(StringUtil.isEmpty(title) ? "温馨提示：请尊重他人，绿色聊天，文明聊天，展现自己的礼貌与素质，共建良好的交友环境。" : title);
        customAttachment.setType(MessageType.MESSAGE_TYPE_TIP);
        customAttachment.setData(customMessageData);
        IMMessage message = MessageBuilder.createCustomMessage("", SessionTypeEnum.P2P, customAttachment);
        message.setStatus(MsgStatusEnum.read);
        return message;
    }

    public IMMessage getTagsLocalMessage(String title, String tags) {
        CustomAttachment customAttachment = new CustomAttachment();
        customAttachment.setType(MessageType.MESSAGE_TYPE_TAGS);

        CustomMessageData customMessageData = new CustomMessageData();
        customMessageData.setTitle(title);
        customMessageData.setTags(tags);

        customAttachment.setData(customMessageData);

        IMMessage message = MessageBuilder.createCustomMessage("", SessionTypeEnum.P2P, customAttachment);
        message.setStatus(MsgStatusEnum.read);
        return message;
    }

}
