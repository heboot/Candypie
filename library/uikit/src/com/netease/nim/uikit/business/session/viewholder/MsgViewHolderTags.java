package com.netease.nim.uikit.business.session.viewholder;

import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.heboot.bean.message.CustomMessageModel;
import com.heboot.entity.CustomMessageData;
import com.heboot.message.MessageType;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;


public class MsgViewHolderTags extends MsgViewHolderBase {

    protected TextView tvContent;

    public MsgViewHolderTags(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.layout_message_user_tags;
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
            if (model.getType().equals(MessageType.MESSAGE_TYPE_TAGS)) {
                CustomMessageData customMessageData = JSON.parseObject(model.getData(), CustomMessageData.class);
                if (customMessageData != null) {
                    handleTextNotification(customMessageData.getTitle(), customMessageData.getTags());
                }


            }
        }

    }

    private void handleTextNotification(String title, String tags) {

        tvContent.setText(title);

        SpannableString timeSpan = new SpannableString(tags);
        timeSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(tvContent.getContext(), R.color.color_FF5252)), 0, tags.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvContent.append(timeSpan);


    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
