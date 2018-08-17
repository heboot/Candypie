package com.heboot.event;

import com.heboot.bean.theme.MeetPoiDataBean;
import com.heboot.bean.theme.PushUserTipBean;

/**
 * Created by heboot on 2018/2/27.
 */

public class ThemeEvent {

    public static final String CLOSE_NEW_SERVICE_PAGE_EVENT = "CLOSE_NEW_SERVICE_PAGE_EVENT";

    public static class ThemeChooseAddressEvent {

        private MeetPoiDataBean.ListBean listBean;

        private ThemeChooseAddressEvent() {
        }

        public ThemeChooseAddressEvent(MeetPoiDataBean.ListBean listBean) {
            this.listBean = listBean;
        }

        public MeetPoiDataBean.ListBean getListBean() {
            return listBean;
        }
    }

    public static class ChooseUserByHomepageEvent {

        private int uid;

        public ChooseUserByHomepageEvent(int uid) {
            this.uid = uid;
        }

        public int getUid() {
            return uid;
        }
    }


    public static class PushUsersEvent{
        private PushUserTipBean pushUserTipBean;

        public PushUserTipBean getPushUserTipBean() {
            return pushUserTipBean;
        }

        public PushUsersEvent(PushUserTipBean pushUserTipBean) {
            this.pushUserTipBean = pushUserTipBean;
        }
    }


}
