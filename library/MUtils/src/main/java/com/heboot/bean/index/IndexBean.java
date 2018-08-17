package com.heboot.bean.index;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

/**
 * Created by heboot on 2018/2/8.
 */

public class IndexBean extends BaseBeanEntity {
    /**
     * top_ad_list : [{"id":2,"title":"选择服务","url":"http://chuansong.me/n/1758211652411","img":"","params_config":"","type":"h5"}]
     * message_tip : {"im_tip":1}
     * tt_message_list : ["张媛媛 充值了100元"]
     */

    private MessageTipBean message_tip;
    private List<IndexPopTipBean> top_ad_list;
    private List<String> tt_message_list;
    private IndexPopTipBean popup_tip;
    private List<String> video_chat_avatar_list;

    public List<String> getVideo_chat_avatar_list() {
        return video_chat_avatar_list;
    }

    public void setVideo_chat_avatar_list(List<String> video_chat_avatar_list) {
        this.video_chat_avatar_list = video_chat_avatar_list;
    }

    public IndexPopTipBean getPopup_tip() {
        return popup_tip;
    }

    public void setPopup_tip(IndexPopTipBean popup_tip) {
        this.popup_tip = popup_tip;
    }

    public MessageTipBean getMessage_tip() {
        return message_tip;
    }

    public void setMessage_tip(MessageTipBean message_tip) {
        this.message_tip = message_tip;
    }

    public List<IndexPopTipBean> getTop_ad_list() {
        return top_ad_list;
    }

    public void setTop_ad_list(List<IndexPopTipBean> top_ad_list) {
        this.top_ad_list = top_ad_list;
    }

    public List<String> getTt_message_list() {
        return tt_message_list;
    }

    public void setTt_message_list(List<String> tt_message_list) {
        this.tt_message_list = tt_message_list;
    }

    public static class MessageTipBean {
        /**
         * im_tip : 1
         */

        private int im_tip;

        public int getIm_tip() {
            return im_tip;
        }

        public void setIm_tip(int im_tip) {
            this.im_tip = im_tip;
        }
    }

}
