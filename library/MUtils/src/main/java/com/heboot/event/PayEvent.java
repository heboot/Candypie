package com.heboot.event;

import com.heboot.bean.pay.CouponsBeanModel;

/**
 * Created by heboot on 2018/3/13.
 */

public class PayEvent {

    public static String PaySUCEvent = "PaySUCEvent";

    public static String RechargeSUCEvent = "RechargeSUCEvent";

    public static String RechargeGoldVipSUCEvent = "RechargeGoldVipSUCEvent";

    public static class SelectCouponEvent {

        private CouponsBeanModel couponsBeanModel;

        private SelectCouponEvent() {
        }

        public SelectCouponEvent(CouponsBeanModel listBean) {
            this.couponsBeanModel = listBean;
        }

        public CouponsBeanModel getCouponsBeanModel() {
            return couponsBeanModel;
        }
    }


}
