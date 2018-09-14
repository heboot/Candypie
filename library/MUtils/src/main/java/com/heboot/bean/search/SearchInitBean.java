package com.heboot.bean.search;

import com.heboot.base.BaseBeanEntity;

public class SearchInitBean extends BaseBeanEntity {

    private SearchInitChildBean hot_tags;

    private SearchHotRecmmendChildBean hot_recommend;

    public SearchInitChildBean getHot_tags() {
        return hot_tags;
    }

    public void setHot_tags(SearchInitChildBean hot_tags) {
        this.hot_tags = hot_tags;
    }

    public SearchHotRecmmendChildBean getHot_recommend() {
        return hot_recommend;
    }

    public void setHot_recommend(SearchHotRecmmendChildBean hot_recommend) {
        this.hot_recommend = hot_recommend;
    }
}
