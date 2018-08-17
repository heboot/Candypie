package com.gdlife.candypie.common;

/**
 * Created by heboot on 2018/1/17.
 */

public class MCode {

    public final static class HTTP_CODE {
        public final static int SUCCESS = 0;
    }

    public final static class ERROR_CODE {
        //取消次数太多不能发布
        public final static int SERVICE_CANCEL_MORE = 501;

        public final static int SERVICE_HAS_RUNING = 110;

        public final static int PLASE_LOGIN = 401;
    }

    public final static class REQUEST_CODE {

        public final static int GET_PERMISSION_CAMERA = 4001;

        public final static int GET_PERMISSION_STORAGE = 4002;

        public final static int GET_PERMISSION_LOC = 4003;

        public final static int GET_PERMISSION_ALERTWINDOW = 4003;

        public final static int RECORD_VIDEO_CODE = 1001;

        public final static int CHOOSE_VIDEO_CODE = 1002;
    }

}
