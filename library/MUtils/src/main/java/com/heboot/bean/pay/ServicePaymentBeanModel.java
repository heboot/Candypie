package com.heboot.bean.pay;

import com.heboot.base.BaseBeanEntity;

/**
 * Created by heboot on 2018/3/15.
 */

public class ServicePaymentBeanModel extends BaseBeanEntity {

    /**
     * order_id : 18
     * payment_config : {"payment_params":"alipay_sdk=alipay-sdk-php-20161101&app_id=2017081108147086&biz_content=%7B%22body%22%3A%22%E6%94%AF%E4%BB%98%E6%9C%8D%E5%8A%A1%E8%B4%B9%22%2C%22subject%22%3A%22%E6%94%AF%E4%BB%98%E6%9C%8D%E5%8A%A1%E8%B4%B9%22%2C%22out_trade_no%22%3A%2220180309165214S10022S7%22%2C%22timeout_express%22%3A%222h%22%2C%22total_amount%22%3A280%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22goods_type%22%3A%220%22%2C%22passback_params%22%3A%22%7B%5C%22type%5C%22%3A%5C%22service%5C%22%7D%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2Fwww.guodonglife.cn%2Findex%2Fplugin%2Fexecute%2F_plugin%2FAlipay%2F_controller%2FAlipay%2F_action%2Fpay_notify.html&sign_type=RSA2×tamp=2018-03-09+16%3A52%3A14&version=1.0&sign=GW%2B4MTpqHsgYP%2FeLzAfMJMl7OY%2F7OYdX8bfuAx6jKCm0W%2BHhaFwUgfLkLaxJaUSHWgdmfNf8fjFD%2BsBtqBlESukrTlUmNQS33yqfeljYSpgg5Wu6ecAtgKSovPAswRJWTKtaE2i7RQd492y4knB3EnCB%2BsXS1yeN7G7mOYk1m7%2FSOvzuApklSz06L8xaJjEGOJZaAH8RRz10oSy1aPwMeqS%2FZat%2FUWIZQvzmLuYxyUfAlPJ6Z5LHB6vgWjAZ4jFqtp7dH0orIrgsWVmxWRdBgvySFqLF7STclOUrJPFmWvWhlC3OEVqSzRSIcD2%2B9GhF9jBBghHvD%2FAYJLt9W3rfnw%3D%3D"}
     */

    private String order_id;
    private PaymentConfigBean payment_config;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public PaymentConfigBean getPayment_config() {
        return payment_config;
    }

    public void setPayment_config(PaymentConfigBean payment_config) {
        this.payment_config = payment_config;
    }

    public static class PaymentConfigBean {
        /**
         * appid : wx9ba6461e58638026
         * partnerid : 1485236472
         * prepayid : wx2018031515281125720bdd180963734352
         * noncestr : 059d9e01176ab2f0892fe2215835bf19
         * timestamp : 1521098891
         * package : Sign=WXPay
         * sign : 553CEFA7D65421466142D3EC9ED68675
         * signstr : appid=wx9ba6461e58638026&noncestr=059d9e01176ab2f0892fe2215835bf19&package=Sign=WXPay&partnerid=1485236472&prepayid=wx2018031515281125720bdd180963734352&sign=553CEFA7D65421466142D3EC9ED68675&timestamp=1521098891&key=adsssssss33454d312222dfre121efdf
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String timestamp;
        @com.google.gson.annotations.SerializedName("package")
        private String packageData;
        private String sign;
        private String signstr;
        private String payment_params;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getPackageData() {
            return packageData;
        }

        public void setPackageData(String packageData) {
            this.packageData = packageData;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSignstr() {
            return signstr;
        }

        public void setSignstr(String signstr) {
            this.signstr = signstr;
        }

        public String getPayment_params() {
            return payment_params;
        }

        public void setPayment_params(String payment_params) {
            this.payment_params = payment_params;
        }

/**
 * appid : wx9ba6461e58638026
 * partnerid : 1485236472
 * prepayid : wx2018031515281125720bdd180963734352
 * noncestr : 059d9e01176ab2f0892fe2215835bf19
 * timestamp : 1521098891
 * package : Sign=WXPay
 * sign : 553CEFA7D65421466142D3EC9ED68675
 * signstr : appid=wx9ba6461e58638026&noncestr=059d9e01176ab2f0892fe2215835bf19&package=Sign=WXPay&partnerid=1485236472&prepayid=wx2018031515281125720bdd180963734352&sign=553CEFA7D65421466142D3EC9ED68675&timestamp=1521098891&key=adsssssss33454d312222dfre121efdf
 */


    }


}
