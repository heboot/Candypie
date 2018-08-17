package com.heboot.bean.order;

import com.heboot.bean.user.UserEvalBean;

import java.util.List;

public class OrderServiceEvalBean {
    /**
     * title : 对方已评价
     * eval_time : 1523255415
     * tags : [{"title":"真人比视频美","icon":"http://img.guodonglife.cn/public/static/icon/video_eval.png"},{"title":"满意度","icon":"http://img.guodonglife.cn/public/static/icon/service_eval.png","value":"4"},{"title":"准时"}]
     */

    private String title;
    private String eval_time;
    private List<UserEvalBean> tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEval_time() {
        return eval_time;
    }

    public void setEval_time(String eval_time) {
        this.eval_time = eval_time;
    }

    public List<UserEvalBean> getTags() {
        return tags;
    }

    public void setTags(List<UserEvalBean> tags) {
        this.tags = tags;
    }


}
