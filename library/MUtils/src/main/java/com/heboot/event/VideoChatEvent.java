package com.heboot.event;

import com.heboot.bean.message.SystemNotification;
import com.heboot.common.VideoCatState;

public class VideoChatEvent {

    public static final String VIDEO_CHAT_BALANCE_TIP_EVENT = "VIDEO_CHAT_BALANCE_TIP";

    public static final String VIDEO_CHAT_END_EVENT = "VIDEO_CHAT_END";

    public static class UPDATE_VIDEO_STATE_EVENT {

        private VideoCatState state;
        private String user_service_id;

        public UPDATE_VIDEO_STATE_EVENT(VideoCatState state, String user_service_id) {
            this.state = state;
            this.user_service_id = user_service_id;
        }

        public VideoCatState getState() {
            return state;
        }

        public String getUser_service_id() {
            return user_service_id;
        }
    }

    public static class UPDATE_VIDEO_SERVICE_ENENT {

        private SystemNotification systemNotification;

        public UPDATE_VIDEO_SERVICE_ENENT(SystemNotification systemNotification) {
            this.systemNotification = systemNotification;
        }

        public SystemNotification getSystemNotification() {
            return systemNotification;
        }
    }

    public static class UPDATE_CAMERA_STATUS_EVENT {
        private SystemNotification systemNotification;

        public UPDATE_CAMERA_STATUS_EVENT(SystemNotification systemNotification) {
            this.systemNotification = systemNotification;
        }

        public SystemNotification getSystemNotification() {
            return systemNotification;
        }


    }


}
