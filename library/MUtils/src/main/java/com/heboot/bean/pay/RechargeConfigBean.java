package com.heboot.bean.pay;

import com.heboot.base.BaseBeanEntity;

import java.util.List;

/**
 * Created by heboot on 2018/3/14.
 */

public class RechargeConfigBean extends BaseBeanEntity {


    /**
     * config : [{"product_id":"100001","amount":10},{"product_id":"100002","amount":30},{"product_id":"100003","amount":50},{"product_id":"100004","amount":100},{"product_id":"100005","amount":200},{"product_id":"100006","amount":500}]
     * payment_config : ["weixin","alipay"]
     * used_coupons : 0
     * used_balance : 0
     */

    private int used_coupons;
    private int used_balance;
    private List<ConfigBean> config;
    private List<String> payment_config;

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

    public List<ConfigBean> getConfig() {
        return config;
    }

    public void setConfig(List<ConfigBean> config) {
        this.config = config;
    }

    public List<String> getPayment_config() {
        return payment_config;
    }

    public void setPayment_config(List<String> payment_config) {
        this.payment_config = payment_config;
    }

    public static class ConfigBean {
        /**
         * product_id : 100001
         * amount : 10
         */

        private String product_id;
        private int amount;
        private String coin;

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }
    }
}
