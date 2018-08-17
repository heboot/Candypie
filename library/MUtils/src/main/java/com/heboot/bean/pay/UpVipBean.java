package com.heboot.bean.pay;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

public class UpVipBean extends BaseBeanEntity {

    private String amount;//
    private List<String> payment_config;
    private int used_coupons;
    private int used_balance;
    private String payable_amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<String> getPayment_config() {
        return payment_config;
    }

    public void setPayment_config(List<String> payment_config) {
        this.payment_config = payment_config;
    }

    public int getUsed_coupons() {
        return used_coupons;
    }

    public void setUsed_coupons(int used_coupons) {
        this.used_coupons = used_coupons;
    }

    public int getUsed_balance() {
        return used_balance;
    }

    public void setUsed_balance(int used_balance) {
        this.used_balance = used_balance;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }
}
