package com.netease.nim.uikit.custom;

import com.alibaba.fastjson.JSON;
import com.heboot.entity.CustomMessageData;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import java.io.Serializable;

/**
 * Created by heboot on 2017/11/17.
 */

public class CustomAttachmentByTip implements MsgAttachment, Serializable {

    //    private String data;
    private CustomMessageData data;
    private String type;
    private String to_action;
    private String link;

    @Override
    public String toJson(boolean b) {
        if (data == null) {
            return "kong";
        }
        return JSON.toJSONString(this);
    }

    public CustomMessageData getData() {
        return data;
    }

    public void setData(CustomMessageData data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTo_action() {
        return to_action;
    }

    public void setTo_action(String to_action) {
        this.to_action = to_action;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
