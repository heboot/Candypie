package com.heboot.event;

/**
 * Created by heboot on 2018/3/19.
 */

public class OrderEvent {

    public static final String CANCEL_ORDER_ENENT = "CANCEL_ORDER_ENENT";

    public static final String COMPLETE_ORDER_ENENT = "COMPLETE_ORDER_ENENT";

    public static final String REFRESH_ORDER_ENENT = "REFRESH_ORDER_ENENT";


    public static class DO_COMMENT_VIDEO_EVENT {
        private String userServiceId;

        public DO_COMMENT_VIDEO_EVENT(String userServiceId) {
            this.userServiceId = userServiceId;
        }

        public String getUserServiceId() {
            return userServiceId;
        }
    }

}
