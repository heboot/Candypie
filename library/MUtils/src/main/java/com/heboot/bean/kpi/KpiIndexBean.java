package com.heboot.bean.kpi;

import com.heboot.base.BaseBeanEntity;

public class KpiIndexBean extends BaseBeanEntity {

    private float kpi_rate;

    private float income;

    private float invite_income;

    private int level;

    private String week_kpi;

    public float getKpi_rate() {
        return kpi_rate;
    }

    public void setKpi_rate(float kpi_rate) {
        this.kpi_rate = kpi_rate;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getInvite_income() {
        return invite_income;
    }

    public void setInvite_income(float invite_income) {
        this.invite_income = invite_income;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getWeek_kpi() {
        return week_kpi;
    }

    public void setWeek_kpi(String week_kpi) {
        this.week_kpi = week_kpi;
    }
}
