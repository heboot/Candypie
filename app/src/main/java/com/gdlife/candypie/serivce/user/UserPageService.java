package com.gdlife.candypie.serivce.user;

import com.gdlife.candypie.MAPP;
import com.heboot.utils.PreferencesUtils;

public class UserPageService {

    public boolean isFirst() {
        return PreferencesUtils.getBoolean(MAPP.mapp, "userPageFirst", true);
    }

    public void setFirst() {
        PreferencesUtils.putBoolean(MAPP.mapp, "userPageFirst", false);
    }

}
