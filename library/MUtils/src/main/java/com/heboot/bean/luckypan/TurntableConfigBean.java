package com.heboot.bean.luckypan;

import com.heboot.base.BaseBeanEntity;

public class TurntableConfigBean extends BaseBeanEntity {

    private TurntableConfigChildBean turntable_config;

    public TurntableConfigChildBean getTurntable_config() {
        return turntable_config;
    }

    public void setTurntable_config(TurntableConfigChildBean turntable_config) {
        this.turntable_config = turntable_config;
    }
}
