package com.heboot.event;

/**
 * Created by heboot on 2018/2/7.
 */

public class NormalEvent {

    public static final String FINISH_PAGE = "FINISH_PAGE";

    public static final String FINISH_INDEX_PAGE = "FINISH_INDEX_PAGE";

    public static final String FINISH_PAGE_BY_SELECT_USER = "FINISH_PAGE_BY_SELECT_USER";

    public static final String FINISH_AUTH_INDEX_PAGE = "FINISH_AUTH_INDEX_PAGE";

    public static final String FINISH_REC_PAGE = "FINISH_REC_PAGE";

    public static final String FINISH_NEW_THEME_PAGE = "FINISH_NEW_THEME_PAGE";


    public static final String FINISH_AUTH_WELCOME_PAGE = "FINISH_AUTH_WELCOME_PAGE";

    public static final String FINISH_USER_VIDEO_PAGE = "FINISH_USER_VIDEO_PAGE";

    public static final String FINISH_SELECT_USER_PAGE = "FINISH_SELECT_USER_PAGE";

    public static final String TEST_NOTI_EVENT = "testnotieve";

    public static class BottomSheetSelectEvent {
        private int position;

        private BottomSheetSelectEvent() {
        }

        public BottomSheetSelectEvent(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

}
