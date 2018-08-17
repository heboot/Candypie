package com.netease.nim.uikit.business.session.viewholder;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.heboot.bean.im.IMGiftBean;
import com.heboot.bean.message.CustomMessageModel;
import com.heboot.bean.message.SystemMessage;
import com.heboot.event.MessageEvent;
import com.heboot.message.MessageType;
import com.heboot.rxbus.RxBus;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;


public class MsgViewHolderGift extends MsgViewHolderBase {

    protected TextView bodyTextView;


    private CustomMessageModel model = null;

//    private HeadImageView leftHeadView, rightHeadView;

    private boolean isMe;

    private IMGiftBean systemMessage;

    public MsgViewHolderGift(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
//        binding = DataBindingUtil.inflate(LayoutInflater.from(NimUIKit.getContext()), R.layout.layout_message_order, null, false);
        return R.layout.nim_message_item_text;
    }

    @Override
    protected void inflateContentView() {
        bodyTextView = view.findViewById(R.id.nim_message_item_text_body);
//        leftHeadView = view.findViewById(R.id.message_item_portrait_left);
//        rightHeadView = view.findViewById(R.id.message_item_portrait_right);
    }

    @Override
    protected void bindContentView() {
        model = null;
        try {
            model = JSON.parseObject(message.getAttachment().toJson(false), CustomMessageModel.class);
            isMe = message.getFromAccount().equals(NimUIKit.getAccount()) ? true : false;
        } catch (Exception e) {

        }

        if (model != null) {
            if (model.getType().equals(MessageType.MESSAGE_TYPE_GIFT_MSG)) {
                  systemMessage = JSON.parseObject(model.getData(), IMGiftBean.class);
                if (systemMessage != null) {
                    handleTextNotification(systemMessage);
                }


            }
        }
        MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, systemMessage.getMsg() + "\n" + systemMessage.getAmount(), ImageSpan.ALIGN_BOTTOM);

    }

    private void handleTextNotification(IMGiftBean systemMessage) {
//        if (isMe) {
////            leftHeadView.setVisibility(View.GONE);
////            rightHeadView.setVisibility(View.VISIBLE);
//            tvContent.setBackgroundResource(R.drawable.icon_bg_gift_right);
////            rightHeadView.loadBuddyAvatar(message);
//        } else {
////            leftHeadView.setVisibility(View.VISIBLE);
////            rightHeadView.setVisibility(View.GONE);
//            tvContent.setBackgroundResource(R.drawable.icon_bg_gift_left);
////            leftHeadView.loadBuddyAvatar(message);
//        }

        if (isReceivedMessage()) {
            bodyTextView.setBackgroundResource(NimUIKitImpl.getOptions().messageLeftGiftBackground);
            bodyTextView.setTextColor(Color.WHITE);
            bodyTextView.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil.dip2px(10), ScreenUtil.dip2px(8));
        } else {
            bodyTextView.setBackgroundResource(NimUIKitImpl.getOptions().messageRightGiftBackground);
            bodyTextView.setTextColor(Color.WHITE);
            bodyTextView.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil.dip2px(15), ScreenUtil.dip2px(8));
        }

        bodyTextView.setText(systemMessage.getMsg() + "\n" + systemMessage.getAmount());

    }
    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }


}
