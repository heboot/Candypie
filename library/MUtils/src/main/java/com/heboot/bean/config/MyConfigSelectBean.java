package com.heboot.bean.config;

import com.heboot.bean.theme.ServiceReqParamsBean;

/**
 * Created by heboot on 2018/2/8.
 */

public class MyConfigSelectBean {

    private String rv1SelectedText;

    private String rv2SelectedText;

    private String rv3SelectedText;

    private String rv4SelectedText;

    private String selectedText;

    private int rv1SelectedIndex;

    private int rv2SelectedIndex;

    private int rv3SelectedIndex;

    private int rv4SelectedIndex;

    private long start_time;

    private ConfigBean.RequireIdsBean requireIdsBean;

    private ServiceReqParamsBean serviceReqParamsBean;

    public String getRv1SelectedText() {
        return rv1SelectedText;
    }

    public void setRv1SelectedText(String rv1SelectedText) {
        this.rv1SelectedText = rv1SelectedText;
    }

    public String getRv2SelectedText() {
        return rv2SelectedText;
    }

    public void setRv2SelectedText(String rv2SelectedText) {
        this.rv2SelectedText = rv2SelectedText;
    }

    public String getRv3SelectedText() {
        return rv3SelectedText;
    }

    public void setRv3SelectedText(String rv3SelectedText) {
        this.rv3SelectedText = rv3SelectedText;
    }

    public String getRv4SelectedText() {
        return rv4SelectedText;
    }

    public void setRv4SelectedText(String rv4SelectedText) {
        this.rv4SelectedText = rv4SelectedText;
    }

    public int getRv1SelectedIndex() {
        return rv1SelectedIndex;
    }

    public void setRv1SelectedIndex(int rv1SelectedIndex) {
        this.rv1SelectedIndex = rv1SelectedIndex;
    }

    public int getRv2SelectedIndex() {
        return rv2SelectedIndex;
    }

    public void setRv2SelectedIndex(int rv2SelectedIndex) {
        this.rv2SelectedIndex = rv2SelectedIndex;
    }

    public int getRv3SelectedIndex() {
        return rv3SelectedIndex;
    }

    public void setRv3SelectedIndex(int rv3SelectedIndex) {
        this.rv3SelectedIndex = rv3SelectedIndex;
    }

    public int getRv4SelectedIndex() {
        return rv4SelectedIndex;
    }

    public void setRv4SelectedIndex(int rv4SelectedIndex) {
        this.rv4SelectedIndex = rv4SelectedIndex;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public ConfigBean.RequireIdsBean getRequireIdsBean() {
        return requireIdsBean;
    }

    public void setRequireIdsBean(ConfigBean.RequireIdsBean requireIdsBean) {
        this.requireIdsBean = requireIdsBean;
    }

    public ServiceReqParamsBean getServiceReqParamsBean() {
        return serviceReqParamsBean;
    }

    public void setServiceReqParamsBean(ServiceReqParamsBean serviceReqParamsBean) {
        this.serviceReqParamsBean = serviceReqParamsBean;
    }
}
