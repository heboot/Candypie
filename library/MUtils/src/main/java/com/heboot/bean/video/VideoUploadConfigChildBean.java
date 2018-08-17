package com.heboot.bean.video;

/**
 * Created by heboot on 2018/1/17.
 */

public class VideoUploadConfigChildBean {


    private String RequestId;
    private VideoUploadConfigRoleUser AssumedRoleUser;
    private VideoUploadConfigCredentials Credentials;

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public VideoUploadConfigRoleUser getAssumedRoleUser() {
        return AssumedRoleUser;
    }

    public void setAssumedRoleUser(VideoUploadConfigRoleUser assumedRoleUser) {
        AssumedRoleUser = assumedRoleUser;
    }

    public VideoUploadConfigCredentials getCredentials() {
        return Credentials;
    }

    public void setCredentials(VideoUploadConfigCredentials credentials) {
        Credentials = credentials;
    }
}
