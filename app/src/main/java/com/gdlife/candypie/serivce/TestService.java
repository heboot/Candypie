package com.gdlife.candypie.serivce;

import com.heboot.utils.LogUtil;

public class TestService {

    private UIService uiService;

    private TestService() {
    }

    public TestService(UIService uis) {
        uiService = uis;
    }

    private void doPrint() {
        LogUtil.e("daggertest", "printF");
    }

}
