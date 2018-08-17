package com.heboot.bean.index;

import com.heboot.base.BaseBeanEntity;
import com.heboot.entity.User;

import java.util.List;

/**
 * Created by heboot on 2018/2/8.
 */

public class IndexV5Bean extends BaseBeanEntity {
    /**
     * top_ad_list : [{"id":2,"title":"选择服务","url":"http://chuansong.me/n/1758211652411","img":"","params_config":"","type":"h5"}]
     * message_tip : {"im_tip":1}
     * tt_message_list : ["张媛媛 充值了100元"]
     */

    private List<IndexPopTipBean> top_ad_list;
    private IndexPopTipBean popup_tip;
    private List<User> list;
    private IndexRecommendConfigBean recommend_config;
    private int service_auth_tip;


    public int getService_auth_tip() {
        return service_auth_tip;
    }

    public void setService_auth_tip(int service_auth_tip) {
        this.service_auth_tip = service_auth_tip;
    }

    public IndexRecommendConfigBean getRecommend_config() {
        return recommend_config;
    }

    public void setRecommend_config(IndexRecommendConfigBean recommend_config) {
        this.recommend_config = recommend_config;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public IndexPopTipBean getPopup_tip() {
        return popup_tip;
    }

    public void setPopup_tip(IndexPopTipBean popup_tip) {
        this.popup_tip = popup_tip;
    }

    public List<IndexPopTipBean> getTop_ad_list() {
        return top_ad_list;
    }

    public void setTop_ad_list(List<IndexPopTipBean> top_ad_list) {
        this.top_ad_list = top_ad_list;
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
