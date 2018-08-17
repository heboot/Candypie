package com.netease.nim.uikit.custom;

import com.alibaba.fastjson.JSON;
import com.heboot.message.MessageType;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

/**
 * Created by heboot on 2017/11/17.
 */

public class CustomAttachParser implements MsgAttachmentParser {
    // 根据解析到的消息类型，确定附件对象类型
    @Override
    public MsgAttachment parse(String json) {
        CustomAttachment attachment = JSON.parseObject(json, CustomAttachment.class);
        if (attachment.getType().equals(MessageType.MESSAGE_TYPE_ORDER)) {
            CustomOrderAttachment customOrderAttachment = JSON.parseObject(json, CustomOrderAttachment.class);
            return customOrderAttachment;
        } else if (attachment.getType().equals(MessageType.MESSAGE_TYPE_TIP)) {
            CustomAttachmentByTip customOrderAttachment = JSON.parseObject(json, CustomAttachmentByTip.class);
            return customOrderAttachment;
        } else if (attachment.getType().equals(MessageType.MESSAGE_TYPE_TAGS)) {
            CustomAttachmentByTags customOrderAttachment = JSON.parseObject(json, CustomAttachmentByTags.class);
            return customOrderAttachment;
        }
        return attachment;
    }

//    public static String packData(int type, JSONObject data) {
//        JSONObject object = new JSONObject();
//        object.put(KEY_TYPE, type);
//        if (data != null) {
//            object.put(KEY_DATA, data);
//        }
//
//        return object.toJSONString();
//    }
}
