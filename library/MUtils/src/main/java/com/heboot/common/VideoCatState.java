package com.heboot.common;

public enum VideoCatState {

    /**
     * 用户取消
     */
    cancel,

    /**
     * 服务者点击拒绝
     * 发起者收到的这个消息
     */
    refuse,

    /**
     * 服务者点击接电话
     * 发起者收到的这个消息
     */
    accept,

    /**
     * 挂断
     */
    shutdown,

    //---------自己加的

    /**
     * 进入通话之前
     */
    before,

    /**
     * 通话中
     */
    ing,


}
