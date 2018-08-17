package com.heboot.event;

/**
 * Created by heboot on 2018/3/12.
 */

public class VideoEvent {

    public static final String MUTE_ON = "MUTE_ON";

    public static final String MUTE_OFF = "MUTE_OFF";

    public static final String VIDEO_UPLOAD_SUC_EVENT_BY_SERVICE_AUTH = "VIDEO_UPLOAD_SUC_EVENT_BY_SERVICE_AUTH";

    public static final String VIDEO_UPLOAD_SUC_EVENT_BY_SKILL = "VIDEO_UPLOAD_SUC_EVENT_BY_SKILL";

    public static final String VIDEO_UPLOAD_SUC_EVENT_BY_USERVIDEOS = "VIDEO_UPLOAD_SUC_EVENT_BY_USERVIDEOS";

    public static final String VIDEO_UPLOAD_SUC_EVENT_TO_USERINFO = "VIDEO_UPLOAD_SUC_EVENT_TO_USERINFO";

    public static final String VIDEO_UPLOAD_ERROR_EVENT = "VIDEO_UPLOAD_ERROR_EVENT";

    public static final String UPDATE_AVATAR_SUC_EVENT = "UPDATE_AVATAR_SUC_EVENT";

    public static final String REPLACE_SUC_EVENT = "REPLACE_SUC_EVENT";


    public static class VideoUploadSkillEvent {
        private String videoId;

        public VideoUploadSkillEvent(String videoId) {
            this.videoId = videoId;
        }

        public String getVideoId() {
            return videoId;
        }
    }

    public static class VideoUploadEvent {

        private String videoPath;

        private String avatarPath;

        private String imagePath;

        public VideoUploadEvent(String path, String imgPath, String avatarPath) {
            this.videoPath = path;
            this.imagePath = imgPath;
            this.avatarPath = avatarPath;
        }

        public String getAvatarPath() {
            return avatarPath;
        }

        public String getVideoPath() {
            return videoPath;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

    public static class VideoUploadProgressEvent {

        private int progress;

        public int getProgress() {
            return progress;
        }

        public VideoUploadProgressEvent(int progress) {
            this.progress = progress;
        }
    }


    public static class UPDATE_USER_AVATAR_EVENT {

        private String avatarPath;

        public String getAvatarPath() {
            return avatarPath;
        }

        public UPDATE_USER_AVATAR_EVENT(String avatarPath) {
            this.avatarPath = avatarPath;
        }
    }

}
