package com.heboot.bean.im;

import com.heboot.base.BaseBeanEntity;

public class ImChatStatusBean extends BaseBeanEntity {

    private int is_im_chat;

    private int is_black;

    private String city_id;

    private String im_price;

    private String im_report_tip;

    private String im_danger_tip;

    private IMTagsBean video_tags;

    public IMTagsBean getVideo_tags() {
        return video_tags;
    }

    public void setVideo_tags(IMTagsBean video_tags) {
        this.video_tags = video_tags;
    }

    public String getIm_report_tip() {
        return im_report_tip;
    }

    public void setIm_report_tip(String im_report_tip) {
        this.im_report_tip = im_report_tip;
    }

    public String getIm_danger_tip() {
        return im_danger_tip;
    }

    public void setIm_danger_tip(String im_danger_tip) {
        this.im_danger_tip = im_danger_tip;
    }

    public String getIm_price() {
        return im_price;
    }

    public void setIm_price(String im_price) {
        this.im_price = im_price;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public int getIs_black() {
        return is_black;
    }

    public void setIs_black(int is_black) {
        this.is_black = is_black;
    }

    public int getIs_im_chat() {
        return is_im_chat;
    }

    public void setIs_im_chat(int is_im_chat) {
        this.is_im_chat = is_im_chat;
    }
}
