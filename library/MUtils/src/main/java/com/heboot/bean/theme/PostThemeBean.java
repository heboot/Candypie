package com.heboot.bean.theme;

import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.pay.CouponsBeanModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heboot on 2018/2/27.
 */

public class PostThemeBean extends BaseBeanEntity implements Serializable {

    private String user_service_id;
    private String amount;//
    private List<String> payment_config;
    private int used_coupons;
    private int is_first_order;
    private int used_balance;
    private List<CouponsBeanModel> coupons_list;
    private String payable_amount;
    private String expire_time;
    private PostVideoChatBean chat_room_config;

    private String first_order_rate;

    //自己加的属性
    private int selectedUserNum;
    private String selectedUserId;
    private String serviceId;

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUser_service_id() {
        return user_service_id;
    }

    public void setUser_service_id(String user_service_id) {
        this.user_service_id = user_service_id;
    }

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

    public int getIs_first_order() {
        return is_first_order;
    }

    public void setIs_first_order(int is_first_order) {
        this.is_first_order = is_first_order;
    }

    public int getUsed_balance() {
        return used_balance;
    }

    public void setUsed_balance(int used_balance) {
        this.used_balance = used_balance;
    }

    public List<CouponsBeanModel> getCoupons_list() {
        return coupons_list;
    }

    public void setCoupons_list(List<CouponsBeanModel> coupons_list) {
        this.coupons_list = coupons_list;
    }

    public PostVideoChatBean getChat_room_config() {
        return chat_room_config;
    }

    public void setChat_room_config(PostVideoChatBean chat_room_config) {
        this.chat_room_config = chat_room_config;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public int getSelectedUserNum() {
        return selectedUserNum;
    }

    public void setSelectedUserNum(int selectedUserNum) {
        this.selectedUserNum = selectedUserNum;
    }

    public String getFirst_order_rate() {
        return first_order_rate;
    }

    public void setFirst_order_rate(String first_order_rate) {
        this.first_order_rate = first_order_rate;
    }
}
