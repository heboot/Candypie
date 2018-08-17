package com.heboot.bean.kpi;

import com.heboot.base.BaseBeanEntity;

public class KpiWeeklyBean extends BaseBeanEntity {

    private KpiWeeklyBeanModel week_api;

    public KpiWeeklyBeanModel getWeek_api() {
        return week_api;
    }

    public void setWeek_api(KpiWeeklyBeanModel week_api) {
        this.week_api = week_api;
    }
}
