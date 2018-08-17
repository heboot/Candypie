package com.heboot.event;

import com.heboot.bean.message.SystemMessage;
import com.heboot.bean.message.SystemNotification;
import com.heboot.bean.message.SystemNotificationValueBean;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.entity.User;

public class MessageEvent {


    public static final String TO_SYSTEM_MESSAGE_PAGE_EVENT = "TO_SYSTEM_MESSAGE_PAGE_EVENT";

    public static final String REFRESH_UNREAD_NUM_ENENT = "REFRESH_UNREAD_NUM_ENENT";


    public static class ToOrderDetailByMessageEvent {
        private String userServiceId;

        public ToOrderDetailByMessageEvent(String userServiceId) {
            this.userServiceId = userServiceId;
        }

        public String getUserServiceId() {
            return userServiceId;
        }
    }

    public static class DoSystemMessageActionEvent {

        private String toAction;

        private String data;

        private SystemMessage systemMessage;

        public DoSystemMessageActionEvent(String toAction, String data, SystemMessage systemMessage) {
            this.toAction = toAction;
            this.data = data;
            this.systemMessage = systemMessage;
        }

        public SystemMessage getSystemMessage() {
            return systemMessage;
        }

        public String getToAction() {
            return toAction;
        }

        public String getData() {
            return data;
        }
    }

    public static class NewApplyUserEvent {
        private String userServiceId;

        private User user;

        public NewApplyUserEvent(String userServiceId, User user) {
            this.userServiceId = userServiceId;
            this.user = user;
        }

        public String getUserServiceId() {
            return userServiceId;
        }

        public User getUser() {
            return user;
        }
    }

    public static class NewOrderEvent {

        private String userServiceId;

        private OrderListBean.ListBean push_service;

        public NewOrderEvent(String userServiceId, OrderListBean.ListBean push_service) {
            this.userServiceId = userServiceId;
            this.push_service = push_service;
        }

        public String getUserServiceId() {
            return userServiceId;
        }


        public OrderListBean.ListBean getPush_service() {
            return push_service;
        }

    }


    public static class ShowMessageAlertWindowEvent {

        private SystemNotificationValueBean systemNotificationValueBean;

        public ShowMessageAlertWindowEvent(SystemNotificationValueBean systemNotification) {
            this.systemNotificationValueBean = systemNotification;
        }

        public SystemNotificationValueBean getSystemNotificationValueBean() {
            return systemNotificationValueBean;
        }
    }


    public static class ShowMessageNotiEvent {
        private SystemNotification systemNotification;

        public ShowMessageNotiEvent(SystemNotification systemNotification) {
            this.systemNotification = systemNotification;
        }

        public SystemNotification getSystemNotification() {
            return systemNotification;
        }

    }

    public static class TO_AGAIN_ORDER_EVENT {

        private User user;

        public User getUser() {
            return user;
        }

        public TO_AGAIN_ORDER_EVENT(User user) {
            this.user = user;
        }
    }

}
