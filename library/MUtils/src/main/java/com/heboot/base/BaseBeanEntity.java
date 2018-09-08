package com.heboot.base;

import com.heboot.bean.order.RunServiceTipBean;
import com.heboot.entity.User;

/**
 * Created by heboot on 2018/2/6.
 */

public class BaseBeanEntity {

    private String token;

    private int total;

    private int pageSize;

    private int totalPages;

    private int sp;

    private User user;

    private RunServiceTipBean run_service_tip;

    private int service_cancel_result;

    private String sync_login_id;

    public String getSync_login_id() {
        return sync_login_id;
    }

    public void setSync_login_id(String sync_login_id) {
        this.sync_login_id = sync_login_id;
    }

    public int getService_cancel_result() {
        return service_cancel_result;
    }

    public void setService_cancel_result(int service_cancel_result) {
        this.service_cancel_result = service_cancel_result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RunServiceTipBean getRun_service_tip() {
        return run_service_tip;
    }

    public void setRun_service_tip(RunServiceTipBean run_service_tip) {
        this.run_service_tip = run_service_tip;
    }
}
