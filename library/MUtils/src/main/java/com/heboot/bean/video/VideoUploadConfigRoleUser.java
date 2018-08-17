package com.heboot.bean.video;

/**
 * Created by heboot on 2018/1/17.
 */

public class VideoUploadConfigRoleUser {

    private String AssumedRoleId;

    private String Arn;

    public String getAssumedRoleId() {
        return AssumedRoleId;
    }

    public void setAssumedRoleId(String assumedRoleId) {
        AssumedRoleId = assumedRoleId;
    }

    public String getArn() {
        return Arn;
    }

    public void setArn(String arn) {
        Arn = arn;
    }
}
