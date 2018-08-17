package com.netease.nim.uikit.business.session.viewholder;

import com.alibaba.fastjson.JSON;
import com.aliyun.common.utils.StringUtils;
import com.heboot.bean.message.CustomMessageModel;
import com.heboot.message.MessageType;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.custom.CustomAttachment;
import com.netease.nim.uikit.custom.CustomAttachmentByTags;
import com.netease.nim.uikit.custom.CustomAttachmentByTip;
import com.netease.nim.uikit.custom.CustomOrderAttachment;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.LocationAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 消息项展示ViewHolder工厂类。
 */
public class MsgViewHolderFactory {

    private static HashMap<Class<? extends MsgAttachment>, Class<? extends MsgViewHolderBase>> viewHolders = new HashMap<>();

    private static Class<? extends MsgViewHolderBase> tipMsgViewHolder;

    static {
        // built in
        register(ImageAttachment.class, MsgViewHolderPicture.class);
        register(AudioAttachment.class, MsgViewHolderAudio.class);
        register(VideoAttachment.class, MsgViewHolderVideo.class);
        register(LocationAttachment.class, MsgViewHolderLocation.class);
//        register(NotificationAttachment.class, MsgViewHolderNotification.class);
        register(RobotAttachment.class, MsgViewHolderRobot.class);
        register(CustomOrderAttachment.class, MsgViewHolderOrder.class);
        register(CustomAttachment.class, MsgViewHolderSystemMSG.class);
        register(CustomAttachment.class, MsgViewHolderGift.class);
        register(CustomAttachmentByTip.class, MsgViewHolderTip.class);
        register(CustomAttachmentByTags.class, MsgViewHolderTags.class);
        registerTipMsgViewHolder(MsgViewHolderNotification.class);
    }

    public static void register(Class<? extends MsgAttachment> attach, Class<? extends MsgViewHolderBase> viewHolder) {
        viewHolders.put(attach, viewHolder);
    }

    public static void registerTipMsgViewHolder(Class<? extends MsgViewHolderBase> viewHolder) {
        tipMsgViewHolder = viewHolder;
    }

    public static Class<? extends MsgViewHolderBase> getViewHolderByType(IMMessage message) {
        if (message.getMsgType() == MsgTypeEnum.text) {
            return MsgViewHolderText.class;
        } else if (message.getMsgType() == MsgTypeEnum.tip) {
            return tipMsgViewHolder == null ? MsgViewHolderUnknown.class : tipMsgViewHolder;
        } else if (message.getMsgType() == MsgTypeEnum.custom) {

            LogUtil.e("自定义消息", JSON.toJSONString(message.getAttachment()));

            CustomMessageModel model = null;
            try {

                model = JSON.parseObject(message.getAttachment().toJson(false), CustomMessageModel.class);
//                model = (CustomAttachment) message.getAttachment();
            } catch (Exception e) {
                LogUtil.e("自定义消息", e.getMessage());

            }
            if (model != null) {
                if (!StringUtil.isEmpty(model.getType()) && model.getType().equals(MessageType.MESSAGE_TYPE_ORDER)) {
                    return MsgViewHolderOrder.class;
                } else if (!StringUtil.isEmpty(model.getType()) && model.getType().equals(MessageType.MESSAGE_TYPE_SYSTEM_MSG)) {
                    return MsgViewHolderSystemMSG.class;
                } else if (!StringUtil.isEmpty(model.getType()) && model.getType().equals(MessageType.MESSAGE_TYPE_GIFT_MSG)) {
                    return MsgViewHolderGift.class;
                } else if (!StringUtil.isEmpty(model.getType()) && model.getType().equals(MessageType.MESSAGE_TYPE_TIP)) {
                    return MsgViewHolderTip.class;
                } else if (!StringUtil.isEmpty(model.getType()) && model.getType().equals(MessageType.MESSAGE_TYPE_TAGS)) {
                    return MsgViewHolderTags.class;
                }
            }
            return MsgViewHolderUnknown.class;
        } else {
            Class<? extends MsgViewHolderBase> viewHolder = null;
            if (message.getAttachment() != null) {
                Class<? extends MsgAttachment> clazz = message.getAttachment().getClass();
                while (viewHolder == null && clazz != null) {
                    viewHolder = viewHolders.get(clazz);
                    if (viewHolder == null) {
                        clazz = getSuperClass(clazz);
                    }
                }
            }
            return viewHolder == null ? MsgViewHolderUnknown.class : viewHolder;
        }
    }

    private static Class<? extends MsgAttachment> getSuperClass(Class<? extends MsgAttachment> derived) {
        Class sup = derived.getSuperclass();
        if (sup != null && MsgAttachment.class.isAssignableFrom(sup)) {
            return sup;
        } else {
            for (Class itf : derived.getInterfaces()) {
                if (MsgAttachment.class.isAssignableFrom(itf)) {
                    return itf;
                }
            }
        }
        return null;
    }

    public static List<Class<? extends MsgViewHolderBase>> getAllViewHolders() {
        List<Class<? extends MsgViewHolderBase>> list = new ArrayList<>();
        list.addAll(viewHolders.values());
        if (tipMsgViewHolder != null) {
            list.add(tipMsgViewHolder);
        }
        list.add(MsgViewHolderUnknown.class);
        list.add(MsgViewHolderText.class);

        return list;
    }
}
