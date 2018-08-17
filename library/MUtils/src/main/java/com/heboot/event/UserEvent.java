package com.heboot.event;

import com.heboot.bean.user.TagsChildBean;
import com.heboot.entity.User;

import java.util.List;

/**
 * Created by heboot on 2018/2/8.
 */

public class UserEvent {

    public static final String LOGIN_SUC = "LOGIN_SUC";

    public static final String LOGOUT = "LOGOUT";

    public static final String UPDATE_PROFILE = "UPDATE_PROFILE";

    public static final String OTHER_LOGIN = "OTHER_LOGIN";

    public static final String AUTH_SERVICE_SUC_EVENT = "AUTH_SERVICE_SUC_EVENT";

    public static final String UPDATE_USER_INFO_BY_MEET_TAG = "UPDATE_USER_INFO_BY_MEET_TAG";

    public static class UserBlackEvent {

        public int is_black;

        public UserBlackEvent(int is_black) {
            this.is_black = is_black;
        }

        public int getIs_black() {
            return is_black;
        }
    }

    public static class UPDATE_USER_BY_IM {
        private User user;

        public UPDATE_USER_BY_IM(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }


    public static class UserInfoInputEvent {

        private String content;

        private UserInfoInputEvent() {
        }

        public UserInfoInputEvent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

    public static class UpdateUserMeetTagEvent {

        private List<TagsChildBean> list;

        public UpdateUserMeetTagEvent(List<TagsChildBean> list) {
            this.list = list;
        }

        public List<TagsChildBean> getList() {
            return list;
        }


    }




}
