package com.heboot.event;

import com.heboot.bean.account.UserCashAccountChildBean;

public class AccountEvent {
    public static class SET_CASH_ACCOUNT_EVENT {

        private UserCashAccountChildBean userCashAccountChildBean;

        public SET_CASH_ACCOUNT_EVENT(UserCashAccountChildBean userCashAccountChildBean) {
            this.userCashAccountChildBean = userCashAccountChildBean;
        }

        public UserCashAccountChildBean getUserCashAccountChildBean() {
            return userCashAccountChildBean;
        }
    }
}
