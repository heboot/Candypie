package com.heboot.entity.user;

import com.heboot.bean.index.IndexPopTipBean;
import com.heboot.entity.User;

import java.util.List;

public class MeUser extends User {

    private List<IndexPopTipBean> ad_list;

    public List<IndexPopTipBean> getAd_list() {
        return ad_list;
    }

    public void setAd_list(List<IndexPopTipBean> ad_list) {
        this.ad_list = ad_list;
    }
}
