package com.heboot.bean.video;

/**
 * Created by heboot on 2018/1/17.
 */

public class VideoPlayerURLBeanChild {

    private String RequestId;

    private VideoPlayerURLBeanVideoBase VideoBase;

    private VideoPlayerURLBeanPlayInfoList PlayInfoList;

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public VideoPlayerURLBeanVideoBase getVideoBase() {
        return VideoBase;
    }

    public void setVideoBase(VideoPlayerURLBeanVideoBase videoBase) {
        VideoBase = videoBase;
    }

    public VideoPlayerURLBeanPlayInfoList getPlayInfoList() {
        return PlayInfoList;
    }

    public void setPlayInfoList(VideoPlayerURLBeanPlayInfoList playInfoList) {
        PlayInfoList = playInfoList;
    }
}
