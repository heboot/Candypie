package com.heboot.bean.theme;

import com.google.gson.annotations.SerializedName;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.pay.CouponsBeanModel;
import com.heboot.entity.User;

import java.util.List;

/**
 * Created by heboot on 2018/3/16.
 */

public class PushUserListBean extends BaseBeanEntity {
    /**
     * chat_room_config : {"token":"1:657f3a61e84b4771b568983b2faf09c6:1520505750:4a490a49a1822885e2e8016d0e9d8f0c","channel_name":"cdp_10_11","channel_key":"005AQAoADQ4RUJCNzdBMEQ1MjkyMDZEQjUwNjdGQjRFQTIxRTQxQzYxRkZBNDcQAGV/OmHoS0dxtWiYOy+vCcYWwp9aKNihIQAAAAAAAA=="}
     * push_nums : 1
     * push_time : 300
     * select_time : 300
     */

    private PostVideoChatBean chat_room_config;
    private int push_nums;
    private int push_time;
    private int select_time_interval;
    private String push_time_interval;
    private String payable_amount;
    private int is_first_order;
    private String start_push_time;
    private int max_select_nums;
    private List<User> list;
    private String service_type;
    private String user_service_id;
    private String amount;
    private List<String> payment_config;
    private int used_coupons;
    private int used_balance;
    private String service_id;
    private List<CouponsBeanModel> coupons_list;


    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getPush_time_interval() {
        return push_time_interval;
    }

    public void setPush_time_interval(String push_time_interval) {
        this.push_time_interval = push_time_interval;
    }

    public PostVideoChatBean getChat_room_config() {
        return chat_room_config;
    }

    public void setChat_room_config(PostVideoChatBean chat_room_config) {
        this.chat_room_config = chat_room_config;
    }

    public int getPush_nums() {
        return push_nums;
    }

    public void setPush_nums(int push_nums) {
        this.push_nums = push_nums;
    }

    public int getPush_time() {
        return push_time;
    }

    public void setPush_time(int push_time) {
        this.push_time = push_time;
    }

    public int getSelect_time_interval() {
        return select_time_interval;
    }

    public void setSelect_time_interval(int select_time_interval) {
        this.select_time_interval = select_time_interval;
    }


    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public int getIs_first_order() {
        return is_first_order;
    }

    public void setIs_first_order(int is_first_order) {
        this.is_first_order = is_first_order;
    }

    public String getStart_push_time() {
        return start_push_time;
    }

    public void setStart_push_time(String start_push_time) {
        this.start_push_time = start_push_time;
    }

    public int getMax_select_nums() {
        return max_select_nums;
    }

    public void setMax_select_nums(int max_select_nums) {
        this.max_select_nums = max_select_nums;
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
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
}
