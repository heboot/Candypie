package com.heboot.bean.index;

import com.heboot.base.BaseBeanEntity;
import com.heboot.entity.User;

import java.util.List;

public class ActiveUserListBean extends BaseBeanEntity {


    private List<User> list;

    private VisitListBean visit_list;


    private List<IndexPopTipBean> top_ad_list;

    public List<IndexPopTipBean> getTop_ad_list() {
        return top_ad_list;
    }

    public void setTop_ad_list(List<IndexPopTipBean> top_ad_list) {
        this.top_ad_list = top_ad_list;
    }

    public VisitListBean getVisit_list() {
        return visit_list;
    }

    public void setVisit_list(VisitListBean visit_list) {
        this.visit_list = visit_list;
    }


    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
