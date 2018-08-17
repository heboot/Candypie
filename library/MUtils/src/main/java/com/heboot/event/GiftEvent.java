package com.heboot.event;

import com.heboot.bean.gift.GiftBean;
import com.heboot.bean.theme.MeetPoiDataBean;

public class GiftEvent {

    public static class SendGiftEvent {

        private GiftBean giftBean;

        public SendGiftEvent(GiftBean giftBean) {
            this.giftBean = giftBean;
        }

        public GiftBean getGiftBean() {
            return giftBean;
        }
    }

    public static class SendGiftEventByTurntable {

        private GiftBean giftBean;

        public SendGiftEventByTurntable(GiftBean giftBean) {
            this.giftBean = giftBean;
        }

        public GiftBean getGiftBean() {
            return giftBean;
        }
    }
}
