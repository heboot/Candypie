package com.heboot.bean.order;

import com.heboot.bean.user.TagsChildBean;

import java.util.List;

public class ServiceEvalTagsBean {

    private String eval_time;

    private String title;

    private List<TagsChildBean> tags;

    public String getEval_time() {
        return eval_time;
    }

    public void setEval_time(String eval_time) {
        this.eval_time = eval_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TagsChildBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsChildBean> tags) {
        this.tags = tags;
    }
}
