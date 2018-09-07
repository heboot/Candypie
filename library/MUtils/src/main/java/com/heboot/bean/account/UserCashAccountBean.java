package com.heboot.bean.account;

import com.heboot.base.BaseBeanEntity;

public class UserCashAccountBean extends BaseBeanEntity {

    private UserCashAccountChildBean user_account;

    public UserCashAccountChildBean getUser_account() {
        return user_account;
    }

    public void setUser_account(UserCashAccountChildBean user_account) {
        this.user_account = user_account;
    }
}
