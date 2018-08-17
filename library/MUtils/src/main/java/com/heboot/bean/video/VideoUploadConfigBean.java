package com.heboot.bean.video;

import com.heboot.base.BaseBeanEntity;

/**
 * Created by heboot on 2018/1/17.
 */

public class VideoUploadConfigBean extends BaseBeanEntity {

    private VideoUploadConfigChildBean credentials_config;

    public VideoUploadConfigChildBean getCredentials_config() {
        return credentials_config;
    }

    public void setCredentials_config(VideoUploadConfigChildBean credentials_config) {
        this.credentials_config = credentials_config;
    }
}
