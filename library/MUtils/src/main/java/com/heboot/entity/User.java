package com.heboot.entity;

import com.heboot.bean.im.IMTagsBean;
import com.heboot.bean.index.IndexPopTipBean;
import com.heboot.bean.index.VisitListBean;
import com.heboot.bean.user.OnLineStatus;
import com.heboot.bean.user.PreEditMeetTagsBean;
import com.heboot.bean.user.TagsChildBean;
import com.heboot.bean.user.UserEvalBean;
import com.heboot.bean.user.UserSkillBean;
import com.heboot.bean.video.HomepageVideoBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by heboot on 2018/2/6.
 */

public class User implements Serializable {

    /**
     * id : 10
     * nickname : 习大大
     * mobile : 15652339916
     * avatar : http://127.0.0.1/candypie/public/static/admin/img/avatar.jpg
     * role : 2
     * sex : 1
     * birthday : 1986-04-16
     * constellation : 白羊座
     * user_auth_status : 0
     * status : 1
     * last_lng : null
     * last_lat : null
     */

    private Integer id;
    private String nickname;

    private String mobile;
    private String tel;
    private String avatar;
    private Integer role;
    private int sex;
    private String birthday;
    private String constellation;
    private Integer user_auth_status;
    private Integer service_auth_status;
    private Integer status;
    private Double last_lng;
    private Double last_lat;
    private String city;
    private String abst;

    private String height;
    private String weight;

    private String coupons_nums;
    private String level;
    private Integer vip_level;


    private long update_time;

    private int is_favs;
    private String balance;
    private String cash_balance;
    private String coin;

    private int is_black;

    private List<HomepageVideoBean> main_video_list;

    private List<HomepageVideoBean> video_list;
    private List<UserSkillBean> skill_list;
    private UserEvalBean user_eval;


    private CoverVideo cover_video;


    //设置加的字段
    private int sound, display_content, enable_notice;

    //认证流程加的字段
    private String id_face;
    private String hand_id_face;

    //注册流程单独加的字段
    private String pwd;
    private String smscode;

    //发现
    private int is_online;

    //新发现
    private String cover_img;

    //视频聊天状态
    private int video_chat_status;

    private String city_id;

    private int free_video_chat;

    private int video_nums;

    private String video_chat_price;

    private OnLineStatus online_status;

    private PreEditMeetTagsBean meet_tags;

    private VisitListBean visit_list;

    //视频特色标签
    private List<TagsChildBean> video_tags;

    //本地新增字段

    private String im_report_tip;

    private String im_danger_tip;

    private IMTagsBean local_video_tags;

    //排行榜相关

    private int rank;

    private String rank_value;

    //我的页面
    private List<IndexPopTipBean> ad_list;

    public List<IndexPopTipBean> getAd_list() {
        return ad_list;
    }

    public void setAd_list(List<IndexPopTipBean> ad_list) {
        this.ad_list = ad_list;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getRank_value() {
        return rank_value;
    }

    public void setRank_value(String rank_value) {
        this.rank_value = rank_value;
    }

    public int getVideo_chat_status() {
        return video_chat_status;
    }

    public void setVideo_chat_status(int video_chat_status) {
        this.video_chat_status = video_chat_status;
    }

    public VisitListBean getVisit_list() {
        return visit_list;
    }

    public List<TagsChildBean> getVideo_tags() {
        return video_tags;
    }

    public void setVideo_tags(List<TagsChildBean> video_tags) {
        this.video_tags = video_tags;
    }

    public void setVisit_list(VisitListBean visit_list) {
        this.visit_list = visit_list;
    }

    public IMTagsBean getLocal_video_tags() {
        return local_video_tags;
    }

    public void setLocal_video_tags(IMTagsBean local_video_tags) {
        this.local_video_tags = local_video_tags;
    }

    public String getIm_report_tip() {
        return im_report_tip;
    }

    public void setIm_report_tip(String im_report_tip) {
        this.im_report_tip = im_report_tip;
    }

    public String getIm_danger_tip() {
        return im_danger_tip;
    }

    public void setIm_danger_tip(String im_danger_tip) {
        this.im_danger_tip = im_danger_tip;
    }

    public OnLineStatus getOnline_status() {
        return online_status;
    }

    public void setOnline_status(OnLineStatus online_status) {
        this.online_status = online_status;
    }

    public String getVideo_chat_price() {
        return video_chat_price;
    }

    public void setVideo_chat_price(String video_chat_price) {
        this.video_chat_price = video_chat_price;
    }

    public UserEvalBean getUser_eval() {
        return user_eval;
    }

    public void setUser_eval(UserEvalBean user_eval) {
        this.user_eval = user_eval;
    }

    public PreEditMeetTagsBean getMeet_tags() {
        return meet_tags;
    }

    public void setMeet_tags(PreEditMeetTagsBean meet_tags) {
        this.meet_tags = meet_tags;
    }

    public int getFree_video_chat() {
        return free_video_chat;
    }

    public void setFree_video_chat(int free_video_chat) {
        this.free_video_chat = free_video_chat;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public int getVideo_nums() {
        return video_nums;
    }

    public void setVideo_nums(int video_nums) {
        this.video_nums = video_nums;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }


    public int getIs_online() {
        return is_online;
    }

    public void setIs_online(int is_online) {
        this.is_online = is_online;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getConstellation() {
        if (constellation == null) {
            return "";
        }
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }


    public List<HomepageVideoBean> getVideo_list() {
        return video_list;
    }

    public void setVideo_list(List<HomepageVideoBean> video_list) {
        this.video_list = video_list;
    }


    public Double getLast_lng() {
        return last_lng;
    }

    public void setLast_lng(Double last_lng) {
        this.last_lng = last_lng;
    }

    public Double getLast_lat() {
        return last_lat;
    }

    public void setLast_lat(Double last_lat) {
        this.last_lat = last_lat;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getAbst() {
        return abst;
    }

    public void setAbst(String abst) {
        this.abst = abst;
    }


    public List<UserSkillBean> getSkill_list() {
        return skill_list;
    }

    public void setSkill_list(List<UserSkillBean> skill_list) {
        this.skill_list = skill_list;
    }


    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public int getIs_favs() {
        return is_favs;
    }

    public void setIs_favs(int is_favs) {
        this.is_favs = is_favs;
    }

    public CoverVideo getCover_video() {
        return cover_video;
    }

    public void setCover_video(CoverVideo cover_video) {
        this.cover_video = cover_video;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getId_face() {
        return id_face;
    }

    public void setId_face(String id_face) {
        this.id_face = id_face;
    }

    public String getHand_id_face() {
        return hand_id_face;
    }

    public void setHand_id_face(String hand_id_face) {
        this.hand_id_face = hand_id_face;
    }

    public List<HomepageVideoBean> getMain_video_list() {
        return main_video_list;
    }

    public void setMain_video_list(List<HomepageVideoBean> main_video_list) {
        this.main_video_list = main_video_list;
    }

    public String getCash_balance() {
        return cash_balance;
    }

    public void setCash_balance(String cash_balance) {
        this.cash_balance = cash_balance;
    }

    public String getCoin() {
        return coin;
    }

    public String getCoupons_nums() {
        return coupons_nums;
    }

    public void setCoupons_nums(String coupons_nums) {
        this.coupons_nums = coupons_nums;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getVip_level() {
        return vip_level;
    }

    public void setVip_level(Integer vip_level) {
        this.vip_level = vip_level;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getIs_black() {
        return is_black;
    }

    public void setIs_black(int is_black) {
        this.is_black = is_black;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getUser_auth_status() {
//        if (user_auth_status == null) {
//            user_auth_status = 0;
//        }
        return user_auth_status;
    }

    public void setUser_auth_status(Integer user_auth_status) {
        this.user_auth_status = user_auth_status;
    }

    public Integer getService_auth_status() {
//        if (service_auth_status == null) {
//            service_auth_status = 0;
//        }
        return service_auth_status;
    }

    public void setService_auth_status(Integer service_auth_status) {
        this.service_auth_status = service_auth_status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getDisplay_content() {
        return display_content;
    }

    public void setDisplay_content(int display_content) {
        this.display_content = display_content;
    }

    public int getEnable_notice() {
        return enable_notice;
    }

    public void setEnable_notice(int enable_notice) {
        this.enable_notice = enable_notice;
    }
}
