package com.heboot.bean.kpi;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

public class KpiLogBean extends BaseBeanEntity {

    private List<KpiLogBeanModel> list;

    public List<KpiLogBeanModel> getList() {
        return list;
    }

    public void setList(List<KpiLogBeanModel> list) {
        this.list = list;
    }
}
