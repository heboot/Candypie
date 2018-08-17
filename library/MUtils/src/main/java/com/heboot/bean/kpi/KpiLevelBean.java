package com.heboot.bean.kpi;

import com.heboot.base.BaseBeanEntity;

public class KpiLevelBean extends BaseBeanEntity {

    private int level;

    private int up_order_nums;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getUp_order_nums() {
        return up_order_nums;
    }

    public void setUp_order_nums(int up_order_nums) {
        this.up_order_nums = up_order_nums;
    }
}
