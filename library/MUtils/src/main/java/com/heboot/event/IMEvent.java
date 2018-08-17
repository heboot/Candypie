package com.heboot.event;

import android.content.Context;

public class IMEvent {


    public static class IMStatusResultEvent {

        private int status;

        public IMStatusResultEvent(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    public static class QUERY_IM_STAUTS_EVENT {

        private Context context;

        private String toUid;

        public String getToUid() {
            return toUid;
        }

        public Context getContext() {
            return context;
        }

        public QUERY_IM_STAUTS_EVENT(Context context, String toUid) {
            this.context = context;
            this.toUid = toUid;
        }
    }

    public static class UPDATE_IM_STAUTS_EVENT {

        private Context context;

        private String toUid;

        private int status;

        public UPDATE_IM_STAUTS_EVENT(Context context, String toUid, int status) {
            this.context = context;
            this.toUid = toUid;
            this.status = status;
        }

        public String getToUid() {
            return toUid;
        }

        public Context getContext() {
            return context;
        }

        public int getStatus() {
            return status;
        }
    }

    public static final String SHOW_IM_GIFT_LAYOUT_EVENT = "SHOW_IM_GIFT_LAYOUT_EVENT";

    public static final String HIDE_IM_GIFT_LAYOUT_EVENT = "HIDE_IM_GIFT_LAYOUT_EVENT";

    public static final String TO_RECHARGE_BY_IM = "TO_RECHARGE_BY_IM";

    public static class SHOW_IM_GIFT_EVENT {

        private Context context;

        private String toUid;

        public String getToUid() {
            return toUid;
        }

        public SHOW_IM_GIFT_EVENT(Context context, String toUid) {
            this.context = context;
            this.toUid = toUid;
        }

        public Context getContext() {
            return context;
        }
    }


    public static class UPDATE_COIN_BALANCE_EVENT {

        private Integer coinAmount;


        public UPDATE_COIN_BALANCE_EVENT(Integer coinAmount) {
            this.coinAmount = coinAmount;
        }

        public Integer getCoinAmount() {
            return coinAmount;
        }

    }

}
