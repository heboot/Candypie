package com.heboot.bean.theme;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

/**
 * Created by heboot on 2018/2/26.
 */

public class MeetPoiListData extends BaseBeanEntity {

    private List<MeetPoiDataBean.ListBean> list;

    public List<MeetPoiDataBean.ListBean> getList() {
        return list;
    }

    public void setList(List<MeetPoiDataBean.ListBean> list) {
        this.list = list;
    }
}
