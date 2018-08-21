package com.heboot.bean.theme;

import com.google.gson.annotations.SerializedName;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.index.IndexPopTipBean;
import com.heboot.bean.order.OrderServiceEvalBean;
import com.heboot.bean.order.ServiceEvalTagsBean;
import com.heboot.entity.User;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by heboot on 2018/3/16.
 */

public class OrderListBean extends BaseBeanEntity {
    /**
     * tip : {"1":8,"2":1}
     * list : [{"id":10017,"uid":10,"service_id":2,"select_uid":0,"price":300,"start_time":1520507400,"end_time":1520507520,"service_time":120,"status":4,"status_config":{"value":"待处理","font_color":"#898A9E"},"action_config":[{"value":"取消约会","color":"#7136FB","action":"cancel"}],"poi":{"name":"温野菜日式火锅","business_id":"20842827","address":"新中街甲3号蜂巢剧场B1层","lng":"116.432630","lat":"39.938920"}},{"id":10016,"uid":10,"service_id":2,"select_uid":0,"price":2,"start_time":null,"end_time":null,"service_time":null,"status":5,"users":[{"id":1,"nickname":"admin","sex":0,"avatar":0}],"status_config":{"value":"待完成","font_color":"#FF5252"},"action_config":[{"value":"打电话","action":"tel"},{"value":"聊一聊","action":"im"},{"value":"完成约会","color":"#7136FB","action":"complete"}]}]
     */

//    private Map<String,String> tip;
    private List<ListBean> list;

    private List<IndexPopTipBean> ad_list;

    private List<IndexPopTipBean> top_ad_list;

    private int init_setting;

    public List<IndexPopTipBean> getTop_ad_list() {
        return top_ad_list;
    }

    public void setTop_ad_list(List<IndexPopTipBean> top_ad_list) {
        this.top_ad_list = top_ad_list;
    }

    public int getInit_setting() {
        return init_setting;
    }

    public void setInit_setting(int init_setting) {
        this.init_setting = init_setting;
    }

    public List<IndexPopTipBean> getAd_list() {
        return ad_list;
    }

    public void setAd_list(List<IndexPopTipBean> ad_list) {
        this.ad_list = ad_list;
    }

    //    public Map<String, String> getTip() {
//        return tip;
//    }
//
//    public void setTip(Map<String, String> tip) {
//        this.tip = tip;
//    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }


    public static class ListBean {
        /**
         * id : 10017
         * uid : 10
         * service_id : 2
         * select_uid : 0
         * price : 300
         * start_time : 1520507400
         * end_time : 1520507520
         * service_time : 120
         * status : 4
         * status_config : {"value":"待处理","font_color":"#898A9E"}
         * action_config : [{"value":"取消约会","color":"#7136FB","action":"cancel"}]
         * poi : {"name":"温野菜日式火锅","business_id":"20842827","address":"新中街甲3号蜂巢剧场B1层","lng":"116.432630","lat":"39.938920"}
         * users : [{"id":1,"nickname":"admin","sex":0,"avatar":0}]
         */

        private String id;
        private int uid;
        private String service_id;
        private int select_uid;
        private String price;
        private int start_time;
        private int end_time;
        private int service_time;
        private int status;
        private long create_time;
        private ExtendBean extend;
        private StatusConfigBean status_config;
        private MeetPoiDataBean.ListBean poi;
        private List<ActionConfigBean> action_config;
        private List<User> users;
        private User user;
        private String expire_time;
        private String service_name;
        private String tip;
        private String currency;
        //        private OrderServiceEvalBean service_eval;
        private String service_type;
        private String amount;
        private ServiceEvalTagsBean service_eval_tags;

        public ServiceEvalTagsBean getService_eval_tags() {
            return service_eval_tags;
        }

        public void setService_eval_tags(ServiceEvalTagsBean service_eval_tags) {
            this.service_eval_tags = service_eval_tags;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getService_type() {
            return service_type;
        }

        public void setService_type(String service_type) {
            this.service_type = service_type;
        }

//        public OrderServiceEvalBean getService_eval() {
//            return service_eval;
//        }
//
//        public void setService_eval(OrderServiceEvalBean service_eval) {
//            this.service_eval = service_eval;
//        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public ExtendBean getExtend() {
            return extend;
        }

        public void setExtend(ExtendBean extend) {
            this.extend = extend;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        public int getSelect_uid() {
            return select_uid;
        }

        public void setSelect_uid(int select_uid) {
            this.select_uid = select_uid;
        }


        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }

        public int getService_time() {
            return service_time;
        }

        public void setService_time(int service_time) {
            this.service_time = service_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public StatusConfigBean getStatus_config() {
            return status_config;
        }

        public void setStatus_config(StatusConfigBean status_config) {
            this.status_config = status_config;
        }

        public MeetPoiDataBean.ListBean getPoi() {
            return poi;
        }

        public void setPoi(MeetPoiDataBean.ListBean poi) {
            this.poi = poi;
        }

        public List<ActionConfigBean> getAction_config() {
            return action_config;
        }

        public void setAction_config(List<ActionConfigBean> action_config) {
            this.action_config = action_config;
        }

        public String getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        public static class StatusConfigBean {
            /**
             * value : 待处理
             * font_color : #898A9E
             */

            private String value;
            private String font_color;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getFont_color() {
                return font_color;
            }

            public void setFont_color(String font_color) {
                this.font_color = font_color;
            }
        }


        public static class ActionConfigBean implements Serializable {
            /**
             * value : 取消约会
             * color : #7136FB
             * action : cancel
             */

            private String value;
            private String color;
            private String action;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }
        }

        public static class ExtendBean {
            private List<String> require;
            private String remark;

            public List<String> getRequire() {
                return require;
            }

            public void setRequire(List<String> require) {
                this.require = require;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }


    }
}
