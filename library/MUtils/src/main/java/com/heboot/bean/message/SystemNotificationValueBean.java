package com.heboot.bean.message;

import com.heboot.bean.gift.GiftBean;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.bean.theme.PostVideoChatBean;
import com.heboot.entity.User;

import java.util.List;

public class SystemNotificationValueBean {

    private String user_service_id;

    private String service_time;

    private String msg;

    private int uid;

    private User user;

    private String to_action;

    private int service_auth_status;

    private User apply_user;

    private OrderListBean.ListBean push_service;

    private PostVideoChatBean chat_room_config;

    private String status;

    private int is_tip;

    private GiftBean gift;

    private List<User> push_users;

    private TurntableResultBean result;

    private String user_turntable_id;

    private String turntable_amount;

    private long turntable_apply_time;

    private int role;

    private long time;

    private int camera_status;

    public int getCamera_status() {
        return camera_status;
    }

    public void setCamera_status(int camera_status) {
        this.camera_status = camera_status;
    }

    public TurntableResultBean getResult() {
        return result;
    }

    public void setResult(TurntableResultBean result) {
        this.result = result;
    }

    public long getTurntable_apply_time() {
        return turntable_apply_time;
    }

    public void setTurntable_apply_time(long turntable_apply_time) {
        this.turntable_apply_time = turntable_apply_time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getTurntable_amount() {
        return turntable_amount;
    }

    public void setTurntable_amount(String turntable_amount) {
        this.turntable_amount = turntable_amount;
    }


    public String getUser_turntable_id() {
        return user_turntable_id;
    }

    public void setUser_turntable_id(String user_turntable_id) {
        this.user_turntable_id = user_turntable_id;
    }

    public List<User> getPush_users() {
        return push_users;
    }

    public void setPush_users(List<User> push_users) {
        this.push_users = push_users;
    }

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
    }

    public GiftBean getGift() {
        return gift;
    }

    public void setGift(GiftBean gift) {
        this.gift = gift;
    }

    public int getIs_tip() {
        return is_tip;
    }

    public void setIs_tip(int is_tip) {
        this.is_tip = is_tip;
    }

    public String getUser_service_id() {
        return user_service_id;
    }

    public void setUser_service_id(String user_service_id) {
        this.user_service_id = user_service_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTo_action() {
        return to_action;
    }

    public void setTo_action(String to_action) {
        this.to_action = to_action;
    }

    public User getApply_user() {
        return apply_user;
    }

    public void setApply_user(User apply_user) {
        this.apply_user = apply_user;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderListBean.ListBean getPush_service() {
        return push_service;
    }

    public void setPush_service(OrderListBean.ListBean push_service) {
        this.push_service = push_service;
    }

    public PostVideoChatBean getChat_room_config() {
        return chat_room_config;
    }

    public void setChat_room_config(PostVideoChatBean chat_room_config) {
        this.chat_room_config = chat_room_config;
    }

    public int getService_auth_status() {
        return service_auth_status;
    }

    public void setService_auth_status(int service_auth_status) {
        this.service_auth_status = service_auth_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
