package com.heboot.bean.config;

import com.heboot.bean.gift.GiftBean;
import com.heboot.bean.pay.ServiceLevelBean;
import com.heboot.bean.pay.VipLevelBean;
import com.heboot.entity.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heboot on 2018/2/8.
 */

public class ConfigBean implements Serializable {


    /**
     * v : 201802081731307
     * city_config : [{"id":"131","name":"北京"},{"id":"289","name":"上海"},{"id":"257","name":"广州"},{"id":"340","name":"深圳"},{"id":"132","name":"重庆"},{"id":"332","name":"天津"},{"id":"150","name":"石家庄"},{"id":"179","name":"杭州"},{"id":"218","name":"武汉"},{"id":"315","name":"南京"},{"id":"224","name":"苏州"},{"id":"75","name":"成都"},{"id":"300","name":"福州"},{"id":"125","name":"海口"},{"id":"104","name":"昆明"},{"id":"158","name":"长沙"},{"id":"288","name":"济南"},{"id":"268","name":"郑州"},{"id":"167","name":"大连"},{"id":"236","name":"青岛"},{"id":"178","name":"温州"},{"id":"180","name":"宁波"},{"id":"346","name":"扬州"},{"id":"111","name":"大理"},{"id":"194","name":"厦门"},{"id":"140","name":"珠海"},{"id":"134","name":"泉州"},{"id":"355","name":"大同"},{"id":"121","name":"三亚"},{"id":"138","name":"佛山"},{"id":"119","name":"东莞"},{"id":"326","name":"烟台"},{"id":"287","name":"潍坊"},{"id":"172","name":"枣庄"},{"id":"153","name":"洛阳"},{"id":"191","name":"廊坊"},{"id":"307","name":"保定"},{"id":"317","name":"无锡"},{"id":"127","name":"合肥"},{"id":"176","name":"太原"},{"id":"261","name":"南宁"},{"id":"146","name":"贵阳"},{"id":"360","name":"银川"},{"id":"233","name":"西安"},{"id":"66","name":"西宁"},{"id":"163","name":"南昌"},{"id":"36","name":"兰州"},{"id":"48","name":"哈尔滨"},{"id":"53","name":"长春"},{"id":"58","name":"沈阳"},{"id":"321","name":"呼和浩特"},{"id":"92","name":"乌鲁木齐"}]
     * service_items_config : {"list":[{"id":"2","type":"meet","title":"美食","img":"http://img.guodonglife.cn/public/uploads/images/20180208/4daa1490dbf6667e762deaff9b420706.png","need_auth":"0","auth_abst":"","min_time":"90","max_time":"360","sort":"1","is_recommend":"1","time_price":[{"time":90,"price":"300"},{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"300"},{"time":210,"price":"400"},{"time":240,"price":"400"},{"time":270,"price":"500"},{"time":300,"price":"500"},{"time":330,"price":"600"},{"time":360,"price":"600"}],"unit":"30","dp_tags":"美食","content":"边吃边聊","time_limit":"10,23","price_limit":"200,1000","cover_img":"1915"},{"id":"5","type":"meet","title":"看电影","img":"http://img.guodonglife.cn/public/uploads/images/20180208/ed89ffff50f66693eda876453ca2cb08.png","need_auth":"0","auth_abst":"","min_time":"120","max_time":"240","sort":"2","is_recommend":"1","time_price":[{"time":120,"price":"200"},{"time":150,"price":"200"},{"time":180,"price":"300"},{"time":210,"price":"300"},{"time":240,"price":"300"}],"unit":"30","dp_tags":"电影院,私人影院","content":"一起安静看场电影","require_ids":null,"time_limit":"8,23","price_limit":"200,1000","cover_img":"0"},{"id":"4","type":"meet","title":"唱歌","img":"http://img.guodonglife.cn/public/uploads/images/20180208/8006d0e81d01b283bea7458a5e0e7091.png","need_auth":"1","auth_abst":"唱一段","min_time":"120","max_time":"360","sort":"3","is_recommend":"1","time_price":[{"time":90,"price":"300"},{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"400"},{"time":210,"price":"400"},{"time":240,"price":"500"},{"time":270,"price":"500"},{"time":300,"price":"500"},{"time":330,"price":"600"},{"time":360,"price":"600"}],"unit":"30","dp_tags":"KTV,K歌","content":"以歌会友","time_limit":"10,23","price_limit":"300,1000","cover_img":"0"},{"id":"1","type":"video","title":"视频聊天","img":"http://img.guodonglife.cn/public/uploads/images/20180208/476ca04da2c5a9fd654b124fc4949d7f.png","need_auth":"0","auth_abst":"","min_time":"3","max_time":"0","sort":"4","is_recommend":"1","time_price":null,"unit":"1","dp_tags":"","content":"视频聊天","require_ids":null,"time_limit":"0,23","price_limit":"2,10","cover_img":"0"},{"id":"3","type":"meet","title":"喝一杯","img":"http://img.guodonglife.cn/public/uploads/images/20180208/80ac4888d4e1cf8e17117fa992b3c1b9.png","need_auth":"0","auth_abst":"","min_time":"90","max_time":"360","sort":"5","is_recommend":"1","time_price":[{"time":90,"price":"300"},{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"400"},{"time":210,"price":"400"},{"time":240,"price":"500"},{"time":270,"price":"500"},{"time":300,"price":"600"},{"time":330,"price":"600"},{"time":360,"price":"600"}],"unit":"30","dp_tags":"酒吧,茶馆,咖啡厅,夜店,精品酒店","content":"一切尽在不言中","time_limit":"10,23","price_limit":"300,1000","cover_img":"0"},{"id":"6","type":"meet","title":"同城陪游","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"1","auth_abst":"介绍一个景点","min_time":"240","max_time":"480","sort":"6","is_recommend":"0","time_price":[{"time":120,"price":"300"},{"time":180,"price":"300"},{"time":240,"price":"400"},{"time":300,"price":"500"},{"time":360,"price":"600"},{"time":420,"price":"600"},{"time":480,"price":"800"},{"time":540,"price":"1000"},{"time":600,"price":"1000"},{"time":660,"price":"1200"},{"time":720,"price":"1200"}],"unit":"60","dp_tags":"周边游","content":"不可错过的精彩","require_ids":null,"time_limit":"8,22","price_limit":"300,1000","cover_img":"0"},{"id":"7","type":"meet","title":"打游戏","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"1","auth_abst":"介绍自己擅长的游戏","min_time":"120","max_time":"720","sort":"7","is_recommend":"0","time_price":[{"time":120,"price":"200"},{"time":150,"price":"200"},{"time":180,"price":"300"},{"time":210,"price":"300"},{"time":240,"price":"400"},{"time":270,"price":"400"},{"time":300,"price":"500"},{"time":330,"price":"500"},{"time":360,"price":"600"},{"time":390,"price":"600"},{"time":420,"price":"800"},{"time":450,"price":"800"},{"time":480,"price":"800"},{"time":510,"price":"800"},{"time":540,"price":"1000"},{"time":570,"price":"1000"},{"time":600,"price":"1000"},{"time":630,"price":"1000"},{"time":660,"price":"1200"},{"time":690,"price":"1200"},{"time":720,"price":"1200"}],"unit":"30","dp_tags":"网吧","content":"为了胜利一起战斗","require_ids":null,"time_limit":"10,23","price_limit":"200,1000","cover_img":"0"},{"id":"8","type":"meet","title":"羽毛球","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"0","auth_abst":"","min_time":"60","max_time":"240","sort":"8","is_recommend":"0","time_price":[{"time":60,"price":"200"},{"time":90,"price":"200"},{"time":120,"price":"200"},{"time":150,"price":"200"},{"time":180,"price":"300"},{"time":210,"price":"300"},{"time":240,"price":"400"}],"unit":"30","dp_tags":"羽毛球馆","content":"一起运动运动","require_ids":null,"time_limit":"10,20","price_limit":"200,1000","cover_img":"0"},{"id":"9","type":"meet","title":"台球","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"0","auth_abst":"","min_time":"120","max_time":"360","sort":"9","is_recommend":"0","time_price":[{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"300"},{"time":210,"price":"300"},{"time":240,"price":"400"},{"time":270,"price":"400"},{"time":300,"price":"500"},{"time":330,"price":"500"},{"time":360,"price":"600"}],"unit":"30","dp_tags":"台球馆","content":"球杆前分出你我","require_ids":null,"time_limit":"10,23","price_limit":"300,1000","cover_img":"0"},{"id":"10","type":"meet","title":"高尔夫","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"1","auth_abst":"介绍自己高尔夫经验","min_time":"120","max_time":"360","sort":"10","is_recommend":"0","time_price":[{"time":120,"price":"400"},{"time":150,"price":"400"},{"time":180,"price":"500"},{"time":210,"price":"500"},{"time":240,"price":"600"},{"time":270,"price":"600"},{"time":300,"price":"600"},{"time":330,"price":"800"},{"time":360,"price":"800"}],"unit":"30","dp_tags":"高尔夫场","content":"以球会友","require_ids":null,"time_limit":"6,22","price_limit":"400,1000","cover_img":"0"},{"id":"11","type":"meet","title":"跑步","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"1","auth_abst":"介绍自己跑步健身经验","min_time":"90","max_time":"240","sort":"11","is_recommend":"0","time_price":[{"time":90,"price":"200"},{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"300"},{"time":210,"price":"400"},{"time":240,"price":"400"}],"unit":"30","dp_tags":"健身会所","content":"从此不再一人跑步","require_ids":null,"time_limit":"6,22","price_limit":"200,1000","cover_img":"0"},{"id":"12","type":"meet","title":"会展演出","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"1","auth_abst":"介绍自己","min_time":"240","max_time":"480","sort":"12","is_recommend":"0","time_price":[{"time":240,"price":"400"},{"time":270,"price":"400"},{"time":300,"price":"500"},{"time":330,"price":"500"},{"time":360,"price":"600"},{"time":390,"price":"600"},{"time":420,"price":"800"},{"time":450,"price":"800"},{"time":480,"price":"1000"}],"unit":"30","dp_tags":"商圈","content":"寻找形象气质俱佳者","require_ids":null,"time_limit":"8,22","price_limit":"400,1000","cover_img":"0"},{"id":"13","type":"meet","title":"下午茶","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"0","auth_abst":"","min_time":"90","max_time":"240","sort":"13","is_recommend":"0","time_price":[{"time":90,"price":"300"},{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"400"},{"time":210,"price":"400"},{"time":240,"price":"400"}],"unit":"30","dp_tags":"茶餐厅,精品酒店,茶馆,咖啡厅,星级酒店","content":"找个安静的地方聊聊天","require_ids":null,"time_limit":"14,18","price_limit":"300,1000","cover_img":"0"}],"hot_list":"2,5,4,1,3"}
     * static_url_config : {"coupons_rule":"www.guodonglife.cn/public/static/help/coupons.html?v=1","about":"www.guodonglife.cn/public/static/help/about.html?v=1","help":"www.guodonglife.cn/public/static/help/index.html?v=3","reg_protocol":"www.guodonglife.cn/public/static/help/reg_protocol.html?v=1","credit_score_rule":"www.guodonglife.cn/public/static/help/xyzgz.html?v=1","auth_rule":"www.guodonglife.cn/public/static/help/auth.html","subscribe_rule":"www.guodonglife.cn/public/static/help/sub_line.html","cash_rule":"www.guodonglife.cn/public/static/help/cash_rule.html","user_auth":"www.guodonglife.cn/public/static/help/user_auth.html"}
     * kf_config : {"kf_uid":"21","kf_name":"1号菌","kf_tel":"400-886-6952"}
     * input_limit_config : {"password":["6","12"],"nickname":["4","20"],"age":["16","50"]}
     */

    private String v;
    private ServiceItemsConfigBean service_items_config;
    private StaticUrlConfigBean static_url_config;
    private KFConfigBean kf_config;
    private InputLimitConfigBean input_limit_config;
    private List<CityConfigBean> city_config;
    private List<ServiceLevelBean> service_level;
    private List<VipLevelBean> vip_level;
    private ServicerAuthConfigBean servicer_auth_config;
    private UserCashConfigBean user_cash_config;
    private List<KpiConfigBean> kpi_config;
    private ShareConfigBean share_config;
    private InviteUrlConfigBean invite_url_config;
    private List<IncomeFilterConfigBean> income_filter_config;
    private List<GiftBean> gift_config;
    private List<String> kf_uids;
    private IMChatConfig im_chat_config;
    private List<User> recommend_user_list;
    private FunctionModuleSwitchBean function_module_switch;
    private int is_review_status;
    private List<TopMenuBean> index_top_menu;
    private List<TopMenuBean> rank_config;

    public List<TopMenuBean> getRank_config() {
        return rank_config;
    }

    public void setRank_config(List<TopMenuBean> rank_config) {
        this.rank_config = rank_config;
    }

    public List<TopMenuBean> getIndex_top_menu() {
        return index_top_menu;
    }

    public void setIndex_top_menu(List<TopMenuBean> index_top_menu) {
        this.index_top_menu = index_top_menu;
    }

    public int getIs_review_status() {
        return is_review_status;
    }

    public void setIs_review_status(int is_review_status) {
        this.is_review_status = is_review_status;
    }

    public FunctionModuleSwitchBean getFunction_module_switch() {
        return function_module_switch;
    }

    public void setFunction_module_switch(FunctionModuleSwitchBean function_module_switch) {
        this.function_module_switch = function_module_switch;
    }

    public List<User> getRecommend_user_list() {
        return recommend_user_list;
    }

    public void setRecommend_user_list(List<User> recommend_user_list) {
        this.recommend_user_list = recommend_user_list;
    }

    public IMChatConfig getIm_chat_config() {
        return im_chat_config;
    }

    public void setIm_chat_config(IMChatConfig im_chat_config) {
        this.im_chat_config = im_chat_config;
    }

    public List<String> getKf_uids() {
        return kf_uids;
    }

    public void setKf_uids(List<String> kf_uids) {
        this.kf_uids = kf_uids;
    }

    public List<GiftBean> getGift_config() {
        return gift_config;
    }

    public void setGift_config(List<GiftBean> gift_config) {
        this.gift_config = gift_config;
    }

    public InviteUrlConfigBean getInvite_url_config() {
        return invite_url_config;
    }

    public List<IncomeFilterConfigBean> getIncome_filter_config() {
        return income_filter_config;
    }

    public void setIncome_filter_config(List<IncomeFilterConfigBean> income_filter_config) {
        this.income_filter_config = income_filter_config;
    }

    public void setInvite_url_config(InviteUrlConfigBean invite_url_config) {
        this.invite_url_config = invite_url_config;
    }

    public ShareConfigBean getShare_config() {
        return share_config;
    }

    public void setShare_config(ShareConfigBean share_config) {
        this.share_config = share_config;
    }

    public List<KpiConfigBean> getKpi_config() {
        return kpi_config;
    }

    public void setKpi_config(List<KpiConfigBean> kpi_config) {
        this.kpi_config = kpi_config;
    }

    public UserCashConfigBean getUser_cash_config() {
        return user_cash_config;
    }

    public void setUser_cash_config(UserCashConfigBean user_cash_config) {
        this.user_cash_config = user_cash_config;
    }

    public ServicerAuthConfigBean getServicer_auth_config() {
        return servicer_auth_config;
    }

    public void setServicer_auth_config(ServicerAuthConfigBean servicer_auth_config) {
        this.servicer_auth_config = servicer_auth_config;
    }

    public KFConfigBean getKf_config() {
        return kf_config;
    }

    public void setKf_config(KFConfigBean kf_config) {
        this.kf_config = kf_config;
    }

    public List<ServiceLevelBean> getService_level() {
        return service_level;
    }

    public void setService_level(List<ServiceLevelBean> service_level) {
        this.service_level = service_level;
    }

    public List<VipLevelBean> getVip_level() {
        return vip_level;
    }

    public void setVip_level(List<VipLevelBean> vip_level) {
        this.vip_level = vip_level;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public ServiceItemsConfigBean getService_items_config() {
        return service_items_config;
    }

    public void setService_items_config(ServiceItemsConfigBean service_items_config) {
        this.service_items_config = service_items_config;
    }

    public StaticUrlConfigBean getStatic_url_config() {
        return static_url_config;
    }

    public void setStatic_url_config(StaticUrlConfigBean static_url_config) {
        this.static_url_config = static_url_config;
    }


    public InputLimitConfigBean getInput_limit_config() {
        return input_limit_config;
    }

    public void setInput_limit_config(InputLimitConfigBean input_limit_config) {
        this.input_limit_config = input_limit_config;
    }

    public List<CityConfigBean> getCity_config() {
        return city_config;
    }

    public void setCity_config(List<CityConfigBean> city_config) {
        this.city_config = city_config;
    }

    public static class ServiceItemsConfigBean implements Serializable {
        /**
         * list : [{"id":"2","type":"meet","title":"美食","img":"http://img.guodonglife.cn/public/uploads/images/20180208/4daa1490dbf6667e762deaff9b420706.png","need_auth":"0","auth_abst":"","min_time":"90","max_time":"360","sort":"1","is_recommend":"1","time_price":[{"time":90,"price":"300"},{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"300"},{"time":210,"price":"400"},{"time":240,"price":"400"},{"time":270,"price":"500"},{"time":300,"price":"500"},{"time":330,"price":"600"},{"time":360,"price":"600"}],"unit":"30","dp_tags":"美食","content":"边吃边聊","time_limit":"10,23","price_limit":"200,1000","cover_img":"1915"},{"id":"5","type":"meet","title":"看电影","img":"http://img.guodonglife.cn/public/uploads/images/20180208/ed89ffff50f66693eda876453ca2cb08.png","need_auth":"0","auth_abst":"","min_time":"120","max_time":"240","sort":"2","is_recommend":"1","time_price":[{"time":120,"price":"200"},{"time":150,"price":"200"},{"time":180,"price":"300"},{"time":210,"price":"300"},{"time":240,"price":"300"}],"unit":"30","dp_tags":"电影院,私人影院","content":"一起安静看场电影","require_ids":null,"time_limit":"8,23","price_limit":"200,1000","cover_img":"0"},{"id":"4","type":"meet","title":"唱歌","img":"http://img.guodonglife.cn/public/uploads/images/20180208/8006d0e81d01b283bea7458a5e0e7091.png","need_auth":"1","auth_abst":"唱一段","min_time":"120","max_time":"360","sort":"3","is_recommend":"1","time_price":[{"time":90,"price":"300"},{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"400"},{"time":210,"price":"400"},{"time":240,"price":"500"},{"time":270,"price":"500"},{"time":300,"price":"500"},{"time":330,"price":"600"},{"time":360,"price":"600"}],"unit":"30","dp_tags":"KTV,K歌","content":"以歌会友","time_limit":"10,23","price_limit":"300,1000","cover_img":"0"},{"id":"1","type":"video","title":"视频聊天","img":"http://img.guodonglife.cn/public/uploads/images/20180208/476ca04da2c5a9fd654b124fc4949d7f.png","need_auth":"0","auth_abst":"","min_time":"3","max_time":"0","sort":"4","is_recommend":"1","time_price":null,"unit":"1","dp_tags":"","content":"视频聊天","require_ids":null,"time_limit":"0,23","price_limit":"2,10","cover_img":"0"},{"id":"3","type":"meet","title":"喝一杯","img":"http://img.guodonglife.cn/public/uploads/images/20180208/80ac4888d4e1cf8e17117fa992b3c1b9.png","need_auth":"0","auth_abst":"","min_time":"90","max_time":"360","sort":"5","is_recommend":"1","time_price":[{"time":90,"price":"300"},{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"400"},{"time":210,"price":"400"},{"time":240,"price":"500"},{"time":270,"price":"500"},{"time":300,"price":"600"},{"time":330,"price":"600"},{"time":360,"price":"600"}],"unit":"30","dp_tags":"酒吧,茶馆,咖啡厅,夜店,精品酒店","content":"一切尽在不言中","time_limit":"10,23","price_limit":"300,1000","cover_img":"0"},{"id":"6","type":"meet","title":"同城陪游","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"1","auth_abst":"介绍一个景点","min_time":"240","max_time":"480","sort":"6","is_recommend":"0","time_price":[{"time":120,"price":"300"},{"time":180,"price":"300"},{"time":240,"price":"400"},{"time":300,"price":"500"},{"time":360,"price":"600"},{"time":420,"price":"600"},{"time":480,"price":"800"},{"time":540,"price":"1000"},{"time":600,"price":"1000"},{"time":660,"price":"1200"},{"time":720,"price":"1200"}],"unit":"60","dp_tags":"周边游","content":"不可错过的精彩","require_ids":null,"time_limit":"8,22","price_limit":"300,1000","cover_img":"0"},{"id":"7","type":"meet","title":"打游戏","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"1","auth_abst":"介绍自己擅长的游戏","min_time":"120","max_time":"720","sort":"7","is_recommend":"0","time_price":[{"time":120,"price":"200"},{"time":150,"price":"200"},{"time":180,"price":"300"},{"time":210,"price":"300"},{"time":240,"price":"400"},{"time":270,"price":"400"},{"time":300,"price":"500"},{"time":330,"price":"500"},{"time":360,"price":"600"},{"time":390,"price":"600"},{"time":420,"price":"800"},{"time":450,"price":"800"},{"time":480,"price":"800"},{"time":510,"price":"800"},{"time":540,"price":"1000"},{"time":570,"price":"1000"},{"time":600,"price":"1000"},{"time":630,"price":"1000"},{"time":660,"price":"1200"},{"time":690,"price":"1200"},{"time":720,"price":"1200"}],"unit":"30","dp_tags":"网吧","content":"为了胜利一起战斗","require_ids":null,"time_limit":"10,23","price_limit":"200,1000","cover_img":"0"},{"id":"8","type":"meet","title":"羽毛球","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"0","auth_abst":"","min_time":"60","max_time":"240","sort":"8","is_recommend":"0","time_price":[{"time":60,"price":"200"},{"time":90,"price":"200"},{"time":120,"price":"200"},{"time":150,"price":"200"},{"time":180,"price":"300"},{"time":210,"price":"300"},{"time":240,"price":"400"}],"unit":"30","dp_tags":"羽毛球馆","content":"一起运动运动","require_ids":null,"time_limit":"10,20","price_limit":"200,1000","cover_img":"0"},{"id":"9","type":"meet","title":"台球","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"0","auth_abst":"","min_time":"120","max_time":"360","sort":"9","is_recommend":"0","time_price":[{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"300"},{"time":210,"price":"300"},{"time":240,"price":"400"},{"time":270,"price":"400"},{"time":300,"price":"500"},{"time":330,"price":"500"},{"time":360,"price":"600"}],"unit":"30","dp_tags":"台球馆","content":"球杆前分出你我","require_ids":null,"time_limit":"10,23","price_limit":"300,1000","cover_img":"0"},{"id":"10","type":"meet","title":"高尔夫","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"1","auth_abst":"介绍自己高尔夫经验","min_time":"120","max_time":"360","sort":"10","is_recommend":"0","time_price":[{"time":120,"price":"400"},{"time":150,"price":"400"},{"time":180,"price":"500"},{"time":210,"price":"500"},{"time":240,"price":"600"},{"time":270,"price":"600"},{"time":300,"price":"600"},{"time":330,"price":"800"},{"time":360,"price":"800"}],"unit":"30","dp_tags":"高尔夫场","content":"以球会友","require_ids":null,"time_limit":"6,22","price_limit":"400,1000","cover_img":"0"},{"id":"11","type":"meet","title":"跑步","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"1","auth_abst":"介绍自己跑步健身经验","min_time":"90","max_time":"240","sort":"11","is_recommend":"0","time_price":[{"time":90,"price":"200"},{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"300"},{"time":210,"price":"400"},{"time":240,"price":"400"}],"unit":"30","dp_tags":"健身会所","content":"从此不再一人跑步","require_ids":null,"time_limit":"6,22","price_limit":"200,1000","cover_img":"0"},{"id":"12","type":"meet","title":"会展演出","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"1","auth_abst":"介绍自己","min_time":"240","max_time":"480","sort":"12","is_recommend":"0","time_price":[{"time":240,"price":"400"},{"time":270,"price":"400"},{"time":300,"price":"500"},{"time":330,"price":"500"},{"time":360,"price":"600"},{"time":390,"price":"600"},{"time":420,"price":"800"},{"time":450,"price":"800"},{"time":480,"price":"1000"}],"unit":"30","dp_tags":"商圈","content":"寻找形象气质俱佳者","require_ids":null,"time_limit":"8,22","price_limit":"400,1000","cover_img":"0"},{"id":"13","type":"meet","title":"下午茶","img":"http://img.guodonglife.cn/public/static/admin/img/none.png","need_auth":"0","auth_abst":"","min_time":"90","max_time":"240","sort":"13","is_recommend":"0","time_price":[{"time":90,"price":"300"},{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"400"},{"time":210,"price":"400"},{"time":240,"price":"400"}],"unit":"30","dp_tags":"茶餐厅,精品酒店,茶馆,咖啡厅,星级酒店","content":"找个安静的地方聊聊天","require_ids":null,"time_limit":"14,18","price_limit":"300,1000","cover_img":"0"}]
         * hot_list : 2,5,4,1,3
         */

        private String hot_list;
        private List<ListBean> list;

        public String getHot_list() {
            return hot_list;
        }

        public void setHot_list(String hot_list) {
            this.hot_list = hot_list;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable {
            /**
             * id : 2
             * type : meet
             * title : 美食
             * img : http://img.guodonglife.cn/public/uploads/images/20180208/4daa1490dbf6667e762deaff9b420706.png
             * need_auth : 0
             * auth_abst :
             * min_time : 90
             * max_time : 360
             * sort : 1
             * is_recommend : 1
             * time_price : [{"time":90,"price":"300"},{"time":120,"price":"300"},{"time":150,"price":"300"},{"time":180,"price":"300"},{"time":210,"price":"400"},{"time":240,"price":"400"},{"time":270,"price":"500"},{"time":300,"price":"500"},{"time":330,"price":"600"},{"time":360,"price":"600"}]
             * unit : 30
             * dp_tags : 美食
             * content : 边吃边聊
             * time_limit : 10,23
             * price_limit : 200,1000
             * cover_img : 1915
             * require_ids : null
             */

            private String id;
            private String type = "";
            private String title;
            private String img;
            private String need_auth;
            private String auth_abst;
            private String min_time;
            private String max_time;
            private String sort;
            private String is_recommend;
            private String unit;
            private String dp_tags;
            private String content;
            private String time_limit;
            private String bg_color;
            private String price_limit;
            private String cover_img;
            private List<RequireIdsBean> require_ids;
            private List<TimePriceBean> time_price;
            private String icon;
            private String disable_icon;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getNeed_auth() {
                return need_auth;
            }

            public void setNeed_auth(String need_auth) {
                this.need_auth = need_auth;
            }

            public String getAuth_abst() {
                return auth_abst;
            }

            public void setAuth_abst(String auth_abst) {
                this.auth_abst = auth_abst;
            }

            public String getMin_time() {
                return min_time;
            }

            public void setMin_time(String min_time) {
                this.min_time = min_time;
            }

            public String getMax_time() {
                return max_time;
            }

            public void setMax_time(String max_time) {
                this.max_time = max_time;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public String getIs_recommend() {
                return is_recommend;
            }

            public void setIs_recommend(String is_recommend) {
                this.is_recommend = is_recommend;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getDp_tags() {
                return dp_tags;
            }

            public void setDp_tags(String dp_tags) {
                this.dp_tags = dp_tags;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTime_limit() {
                return time_limit;
            }

            public void setTime_limit(String time_limit) {
                this.time_limit = time_limit;
            }

            public String getPrice_limit() {
                return price_limit;
            }

            public void setPrice_limit(String price_limit) {
                this.price_limit = price_limit;
            }

            public String getCover_img() {
                return cover_img;
            }

            public void setCover_img(String cover_img) {
                this.cover_img = cover_img;
            }

            public List<RequireIdsBean> getRequire_ids() {
                return require_ids;
            }

            public void setRequire_ids(List<RequireIdsBean> require_ids) {
                this.require_ids = require_ids;
            }

            public List<TimePriceBean> getTime_price() {
                return time_price;
            }

            public void setTime_price(List<TimePriceBean> time_price) {
                this.time_price = time_price;
            }

            public String getBg_color() {
                return bg_color;
            }

            public void setBg_color(String bg_color) {
                this.bg_color = bg_color;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getDisable_icon() {
                return disable_icon;
            }

            public void setDisable_icon(String disable_icon) {
                this.disable_icon = disable_icon;
            }

            public static class TimePriceBean implements Serializable {
                /**
                 * time : 90
                 * price : 300
                 */

                private int time;
                private String price;

                public int getTime() {
                    return time;
                }

                public void setTime(int time) {
                    this.time = time;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }
            }
        }
    }

    public static class StaticUrlConfigBean implements Serializable {
        /**
         * coupons_rule : www.guodonglife.cn/public/static/help/coupons.html?v=1
         * about : www.guodonglife.cn/public/static/help/about.html?v=1
         * help : www.guodonglife.cn/public/static/help/index.html?v=3
         * reg_protocol : www.guodonglife.cn/public/static/help/reg_protocol.html?v=1
         * credit_score_rule : www.guodonglife.cn/public/static/help/xyzgz.html?v=1
         * auth_rule : www.guodonglife.cn/public/static/help/auth.html
         * subscribe_rule : www.guodonglife.cn/public/static/help/sub_line.html
         * cash_rule : www.guodonglife.cn/public/static/help/cash_rule.html
         * user_auth : www.guodonglife.cn/public/static/help/user_auth.html
         */

        private String coupons_rule;
        private String about;
        private String user_help;
        private String reg_protocol;
        private String credit_score_rule;
        private String auth_rule;
        private String subscribe_rule;
        private String cash_rule;
        private String user_auth;
        private String servicer_help;
        private String cancel_rule;
        private String kpi;
        private String user_qa;
        private String servicer_qa;

        public String getServicer_qa() {
            return servicer_qa;
        }

        public void setServicer_qa(String servicer_qa) {
            this.servicer_qa = servicer_qa;
        }

        public String getUser_qa() {
            return user_qa;
        }

        public void setUser_qa(String user_qa) {
            this.user_qa = user_qa;
        }

        public String getKpi() {
            return kpi;
        }

        public void setKpi(String kpi) {
            this.kpi = kpi;
        }

        public String getCancel_rule() {
            return cancel_rule;
        }

        public void setCancel_rule(String cancel_rule) {
            this.cancel_rule = cancel_rule;
        }

        public String getServicer_help() {
            return servicer_help;
        }

        public void setServicer_help(String servicer_help) {
            this.servicer_help = servicer_help;
        }

        public String getCoupons_rule() {
            return coupons_rule;
        }

        public void setCoupons_rule(String coupons_rule) {
            this.coupons_rule = coupons_rule;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getUser_help() {
            return user_help;
        }

        public void setUser_help(String user_help) {
            this.user_help = user_help;
        }

        public String getReg_protocol() {
            return reg_protocol;
        }

        public void setReg_protocol(String reg_protocol) {
            this.reg_protocol = reg_protocol;
        }

        public String getCredit_score_rule() {
            return credit_score_rule;
        }

        public void setCredit_score_rule(String credit_score_rule) {
            this.credit_score_rule = credit_score_rule;
        }

        public String getAuth_rule() {
            return auth_rule;
        }

        public void setAuth_rule(String auth_rule) {
            this.auth_rule = auth_rule;
        }

        public String getSubscribe_rule() {
            return subscribe_rule;
        }

        public void setSubscribe_rule(String subscribe_rule) {
            this.subscribe_rule = subscribe_rule;
        }

        public String getCash_rule() {
            return cash_rule;
        }

        public void setCash_rule(String cash_rule) {
            this.cash_rule = cash_rule;
        }

        public String getUser_auth() {
            return user_auth;
        }

        public void setUser_auth(String user_auth) {
            this.user_auth = user_auth;
        }
    }


    public static class InputLimitConfigBean implements Serializable {
        private List<String> password;
        private List<String> nickname;
        private List<String> age;
        private List<String> abst;
        private List<String> height;
        private List<String> weight;
        private List<String> service_remark;

        public List<String> getAbst() {
            return abst;
        }

        public void setAbst(List<String> abst) {
            this.abst = abst;
        }

        public List<String> getPassword() {
            return password;
        }

        public void setPassword(List<String> password) {
            this.password = password;
        }

        public List<String> getNickname() {
            return nickname;
        }

        public void setNickname(List<String> nickname) {
            this.nickname = nickname;
        }

        public List<String> getAge() {
            return age;
        }

        public void setAge(List<String> age) {
            this.age = age;
        }

        public List<String> getHeight() {
            return height;
        }

        public void setHeight(List<String> height) {
            this.height = height;
        }

        public List<String> getWeight() {
            return weight;
        }

        public void setWeight(List<String> weight) {
            this.weight = weight;
        }

        public List<String> getService_remark() {
            return service_remark;
        }

        public void setService_remark(List<String> service_remark) {
            this.service_remark = service_remark;
        }
    }

    public static class CityConfigBean implements Serializable {
        /**
         * id : 131
         * name : 北京
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class RequireIdsBean implements Serializable {
        private String id;
        private String content;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class KpiConfigBean implements Serializable {
        private int id;
        private String title;
        private String img;
        private String score_limit;
        private int rate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getScore_limit() {
            return score_limit;
        }

        public void setScore_limit(String score_limit) {
            this.score_limit = score_limit;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }
    }


    public static class UserCashConfigBean implements Serializable {

        /**
         * tax : 0.006
         * cash_week : ["4"]
         * min_balance : 100
         * min_amount : 100
         */

        private String tax;
        private String min_balance;
        private String min_amount;
        private List<String> cash_week;

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getMin_balance() {
            return min_balance;
        }

        public void setMin_balance(String min_balance) {
            this.min_balance = min_balance;
        }

        public String getMin_amount() {
            return min_amount;
        }

        public void setMin_amount(String min_amount) {
            this.min_amount = min_amount;
        }

        public List<String> getCash_week() {
            return cash_week;
        }

        public void setCash_week(List<String> cash_week) {
            this.cash_week = cash_week;
        }
    }

    public static class ShareConfigBean implements Serializable {
        private ShareConfigBeanModel profile_share_config;
        private ShareConfigBeanModel video_share_config;

        public ShareConfigBeanModel getVideo_share_config() {
            return video_share_config;
        }

        public void setVideo_share_config(ShareConfigBeanModel video_share_config) {
            this.video_share_config = video_share_config;
        }

        public ShareConfigBeanModel getProfile_share_config() {
            return profile_share_config;
        }

        public void setProfile_share_config(ShareConfigBeanModel profile_share_config) {
            this.profile_share_config = profile_share_config;
        }
    }

    public static class InviteUrlConfigBean implements Serializable {
        private String u_u;
        private String s_u;

        public String getU_u() {
            return u_u;
        }

        public void setU_u(String u_u) {
            this.u_u = u_u;
        }

        public String getS_u() {
            return s_u;
        }

        public void setS_u(String s_u) {
            this.s_u = s_u;
        }
    }

    public static class ShareConfigBeanWebModel implements Serializable {

        /**
         * title :  好友推荐了{#nickname}，快上蜜糖派约Ta吧
         * content : {#nickname}是一个不错的朋友
         * img : {#avatar}
         * link : a.app.qq.com/o/simple.jsp?pkgname=com.gdlife.candypie&uid={#uid}&h5sign={#uidmd5}
         * is_share : 1
         * type : ["wx_pf","wx_zone","weibo"]
         */

        private String title;
        private String content;
        private String img;
        private String link;
        private String is_share;
        private String type;
        private String action_id;

        public String getAction_id() {
            return action_id;
        }

        public void setAction_id(String action_id) {
            this.action_id = action_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getIs_share() {
            return is_share;
        }

        public void setIs_share(String is_share) {
            this.is_share = is_share;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class ShareConfigBeanModel implements Serializable {

        /**
         * title :  好友推荐了{#nickname}，快上蜜糖派约Ta吧
         * content : {#nickname}是一个不错的朋友
         * img : {#avatar}
         * link : a.app.qq.com/o/simple.jsp?pkgname=com.gdlife.candypie&uid={#uid}&h5sign={#uidmd5}
         * is_share : 1
         * type : ["wx_pf","wx_zone","weibo"]
         */

        private String title;
        private String content;
        private String img;
        private String link;
        private String is_share;
        private List<String> type;
        private String action_id;
        private String tip;

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public String getAction_id() {
            return action_id;
        }

        public void setAction_id(String action_id) {
            this.action_id = action_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getIs_share() {
            return is_share;
        }

        public void setIs_share(String is_share) {
            this.is_share = is_share;
        }

        public List<String> getType() {
            return type;
        }

        public void setType(List<String> type) {
            this.type = type;
        }
    }

}
