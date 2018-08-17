package com.heboot.event;

import com.heboot.bean.message.TurntableResultBean;

public class TurntableEvent {

    public static class PlayTurntableResultEvent {

        private TurntableResultBean turntableResultBean;

        public PlayTurntableResultEvent(TurntableResultBean turntableResultBean) {
            this.turntableResultBean = turntableResultBean;

        }

        public TurntableResultBean getTurntableResultBean() {
            return turntableResultBean;
        }
    }

    public static class TurntableInviteEvent {

        private String userServiceId;

        private String amount;

        private String panId;

        private int role;

        private long serverTime;

        private long applyTime;

        public long getServerTime() {
            return serverTime;
        }

        public long getApplyTime() {
            return applyTime;
        }

        public TurntableInviteEvent(String userServiceId, String amount, int role, long serverTime, long applyTime, String panId) {
            this.userServiceId = userServiceId;
            this.amount = amount;
            this.role = role;
            this.serverTime = serverTime;
            this.applyTime = applyTime;
            this.panId = panId;
        }

        public String getPanId() {
            return panId;
        }

        public int getRole() {
            return role;
        }

        public String getUserServiceId() {
            return userServiceId;
        }

        public String getAmount() {
            return amount;
        }
    }

    public static class ShowTurntableActionMSGEvent{
        private String msg;

        public ShowTurntableActionMSGEvent(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }


    }

    public static class TurntableRefuseEvent {

        private String userServiceId;

        public TurntableRefuseEvent(String userServiceId) {
            this.userServiceId = userServiceId;
        }

        public String getUserServiceId() {
            return userServiceId;
        }
    }

}
