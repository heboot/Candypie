package com.heboot.bean.config;

import java.io.Serializable;

public class FunctionModuleSwitchBean implements Serializable {

    private int servicer_rechage;
    private int servicer_kpi;

    public int getServicer_rechage() {
        return servicer_rechage;
    }

    public void setServicer_rechage(int servicer_rechage) {
        this.servicer_rechage = servicer_rechage;
    }

    public int getServicer_kpi() {
        return servicer_kpi;
    }

    public void setServicer_kpi(int servicer_kpi) {
        this.servicer_kpi = servicer_kpi;
    }
}
