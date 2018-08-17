package com.heboot.bean.config;

import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.login2register.UpdateVersionBean;

/**
 * Created by heboot on 2018/2/8.
 */

public class VConfigBean extends BaseBeanEntity {

    private VConfigEntity v_config;

    private UpdateVersionBean app_version_update;

    public UpdateVersionBean getApp_version_update() {
        return app_version_update;
    }

    public void setApp_version_update(UpdateVersionBean app_version_update) {
        this.app_version_update = app_version_update;
    }

    public VConfigEntity getV_config() {
        return v_config;
    }

    public void setV_config(VConfigEntity v_config) {
        this.v_config = v_config;
    }
}
