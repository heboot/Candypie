package com.gdlife.candypie.http;

import com.example.http.HttpUtils;
import com.heboot.base.BaseBean;
import com.heboot.base.BaseBeanEntity;
import com.heboot.bean.auth.ServiceAuthQBean;
import com.heboot.bean.auth.SubmitAuthBean;
import com.heboot.bean.auth.UserAuthIDBean;
import com.heboot.bean.auth.UserAuthQBean;
import com.heboot.bean.config.StartPageConfigBean;
import com.heboot.bean.config.VConfigBean;
import com.heboot.bean.im.ImChatStatusBean;
import com.heboot.bean.index.ActiveUserListBean;
import com.heboot.bean.index.IndexBean;
import com.heboot.bean.index.IndexV5Bean;
import com.heboot.bean.kpi.KpiIndexBean;
import com.heboot.bean.kpi.KpiLevelBean;
import com.heboot.bean.kpi.KpiLogBean;
import com.heboot.bean.kpi.KpiWeeklyBean;
import com.heboot.bean.login2register.RegisterBean;
import com.heboot.bean.login2register.SendSMSBean;
import com.heboot.bean.luckypan.InvitePlayBean;
import com.heboot.bean.luckypan.TurntableConfigBean;
import com.heboot.bean.luckypan.TurntableConfigChildBean;
import com.heboot.bean.me.MeDataBean;
import com.heboot.bean.message.SystemMessageBean;
import com.heboot.bean.order.OrderDetailBean;
import com.heboot.bean.order.PreServiceEvalBean;
import com.heboot.bean.order.ServiceEvalBean;
import com.heboot.bean.pay.CouponsBean;
import com.heboot.bean.pay.InComeBean;
import com.heboot.bean.pay.RechargeConfigBean;
import com.heboot.bean.pay.ServicePaymentBean;
import com.heboot.bean.pay.UpVipBean;
import com.heboot.bean.pay.UpdatePaymentStatusBean;
import com.heboot.bean.rank.RankBean;
import com.heboot.bean.search.SearchUserBean;
import com.heboot.bean.theme.ApplyOrderBean;
import com.heboot.bean.theme.CancelCauseBean;
import com.heboot.bean.theme.CancelServiceBean;
import com.heboot.bean.theme.MeetPoiDataBean;
import com.heboot.bean.theme.MeetPoiListData;
import com.heboot.bean.theme.OrderListBean;
import com.heboot.bean.theme.PostThemeBean;
import com.heboot.bean.theme.PushUserListBean;
import com.heboot.bean.theme.UserSkillListBean;
import com.heboot.bean.user.BlackBean;
import com.heboot.bean.user.PreEditMeetTagsBean;
import com.heboot.bean.user.RecommendServicerListBean;
import com.heboot.bean.user.SetPriceInitBean;
import com.heboot.bean.user.UserFavsListBean;
import com.heboot.bean.user.UserInfoBean;
import com.heboot.bean.user.UserInfoEditBean;
import com.heboot.bean.user.UserVisitListBean;
import com.heboot.bean.video.StsUploadCredentialsBean;
import com.heboot.bean.video.UserVideosBean;
import com.heboot.bean.video.VideoChatStratEndBean;
import com.heboot.bean.video.VideoPlayerURLBean;
import com.heboot.bean.video.VideoUploadConfigBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by jingbin on 16/11/21.
 * 网络请求类（一个接口一个方法）
 */
public interface HttpClient {

    class Builder {

        public static HttpClient getGuodongServer() {
            return HttpUtils.getInstance().getGuodongServer(HttpClient.class);
        }

        public static HttpClient getOtherServer() {
            return HttpUtils.getInstance().getGuodongServer(HttpClient.class);
        }
    }

    @GET("aliyun_api/get_video_info?video_id=2ed7267d38474d518fee2f6d53fac060")
    Observable<BaseBean<VideoPlayerURLBean>> getVideo();

    @FormUrlEncoded
    @POST("app/aliyun_api/sts_upload_credentials")
    Observable<BaseBean<StsUploadCredentialsBean>> sts_upload_credentials(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_video/upload_video")
    Observable<BaseBean<BaseBeanEntity>> upload_video(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/register")
    Observable<BaseBean<VideoUploadConfigBean>> register(@FieldMap Map<String, Object> params);

    //    index.php?g=Web&c=Mock&o=simple&projectID=5&uri=app/
    @FormUrlEncoded
    @POST("app/sms_code/send")
    Observable<BaseBean<SendSMSBean>> send_sms(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/sms_code/check")
    Observable<BaseBean<SendSMSBean>> send_sms_check(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/register")
    Observable<BaseBean<RegisterBean>> user_register(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/check_nickname")
    Observable<BaseBean> user_check_nick(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/find_password")
    Observable<BaseBean> user_find_password(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/login")
    Observable<BaseBean<RegisterBean>> user_login(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/index")
    Observable<BaseBean<IndexBean>> index(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/config/start_page")
    Observable<BaseBean<StartPageConfigBean>> start_page(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/config/check_config")
    Observable<BaseBean<VConfigBean>> check_config(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_service/video_chat")
    Observable<BaseBean<PostThemeBean>> video_chat(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_service/post_meet_service")
    Observable<BaseBean<PostThemeBean>> post_meet_service(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/meet_poi/init")
    Observable<BaseBean<MeetPoiDataBean>> poi_init(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/meet_poi/query_poi")
    Observable<BaseBean<MeetPoiListData>> query_poi(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/edit_profile")
    Observable<BaseBean<UserInfoEditBean>> edit_profile(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/profile")
    Observable<BaseBean<UserInfoBean>> profile(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user/profile")
    Observable<BaseBean<UserInfoBean>> user_profile(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/servicer_profile")
    Observable<BaseBean<UserInfoBean>> servicer_profile(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/service_user/recommend_servicer_list")
    Observable<BaseBean<RecommendServicerListBean>> recommend_servicer_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/upload_avatar")
    Observable<BaseBean<UserInfoEditBean>> upload_avatar(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_favs/favs")
    Observable<BaseBean<BaseBeanEntity>> favs(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_video/set_cover_video")
    Observable<BaseBean<UserInfoEditBean>> set_cover_video(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_favs/unfavs")
    Observable<BaseBean<BaseBeanEntity>> unfavs(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_video/video_list")
    Observable<BaseBean<UserVideosBean>> video_list(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("user/user_auth/user_auth")
    Observable<BaseBean<UserAuthIDBean>> user_auth(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_auth/q_user_auth")
    Observable<BaseBean<UserAuthQBean>> q_user_auth(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/servicer_auth_profile")
    Observable<BaseBean<ServiceAuthQBean>> servicer_auth_profile(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/user_coupons/coupons_list")
    Observable<BaseBean<CouponsBean>> coupons_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/recharge/recharge_config")
    Observable<BaseBean<RechargeConfigBean>> recharge_config(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/recharge/recharge_coin_config")
    Observable<BaseBean<RechargeConfigBean>> recharge_coin_config(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/order/service_payment")
    Observable<BaseBean<ServicePaymentBean>> service_payment(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/recharge/payment")
    Observable<BaseBean<ServicePaymentBean>> recharge_payment(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("service/user_service/push_user_list")
    Observable<BaseBean<PushUserListBean>> push_user_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/order/order_list")
    Observable<BaseBean<OrderListBean>> order_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_service_apply/push_list")
    Observable<BaseBean<OrderListBean>> push_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/order/details")
    Observable<BaseBean<OrderDetailBean>> details(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_service_apply/apply")
    Observable<BaseBean<ApplyOrderBean>> apply(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/skill/user_skill_list")
    Observable<BaseBean<UserSkillListBean>> user_skill_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_service/select_apply_user")
    Observable<BaseBean<PushUserListBean>> select_apply_user(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/order/cancel_tip")
    Observable<BaseBean<CancelServiceBean>> cancel_tip(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/order/cancel")
    Observable<BaseBean<BaseBeanEntity>> cancel_order(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/order/complete")
    Observable<BaseBean<BaseBeanEntity>> complete_order(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_auth/submit_auth")
    Observable<BaseBean<SubmitAuthBean>> submit_auth(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_service/video_chart_start")
    Observable<BaseBean<VideoChatStratEndBean>> video_chart_start(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_service/video_chart_over")
    Observable<BaseBean<VideoChatStratEndBean>> video_chart_over(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/income_log/income_list")
    Observable<BaseBean<InComeBean>> income_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/skill/auth")
    Observable<BaseBean<BaseBeanEntity>> skill_auth(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_eval/is_delay")
    Observable<BaseBean<BaseBeanEntity>> is_delay(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_eval/service_eval")
    Observable<BaseBean<BaseBeanEntity>> service_eval(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user_message/message_list")
    Observable<BaseBean<SystemMessageBean>> message_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/order/update_payment_status")
    Observable<BaseBean<UpdatePaymentStatusBean>> update_payment_status(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/recharge/update_payment_status")
    Observable<BaseBean<UpdatePaymentStatusBean>> update_recharge_payment_status(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/recharge/cancel_payment")
    Observable<BaseBean<BaseBeanEntity>> cancel_payment(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_favs/favs_list")
    Observable<BaseBean<UserFavsListBean>> favs_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/black_user/black_list")
    Observable<BaseBean<UserFavsListBean>> black_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/black_user/black_user")
    Observable<BaseBean<BaseBeanEntity>> black_user(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/black_user/un_black")
    Observable<BaseBean<BaseBeanEntity>> un_black(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_report/post")
    Observable<BaseBean<BaseBeanEntity>> post(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user_suggest/post")
    Observable<BaseBean<BaseBeanEntity>> user_suggest_post(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("order/user_cash/apply_cash")
    Observable<BaseBean<BaseBeanEntity>> apply_cash(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("user/black_user/black_user_status")
    Observable<BaseBean<BlackBean>> black_user_status(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("service/user_kpi/kpi_index")
    Observable<BaseBean<KpiIndexBean>> kpi_index(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_kpi/week_kpi")
    Observable<BaseBean<KpiWeeklyBean>> week_kpi(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_kpi/level")
    Observable<BaseBean<KpiLevelBean>> level(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("service/user_score_log/score_list")
    Observable<BaseBean<KpiLogBean>> score_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("order/recharge/vip_config")
    Observable<BaseBean<UpVipBean>> vip_config(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("app/user/setting")
    Observable<BaseBean<RegisterBean>> setting(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("stats/action_log/log")
    Observable<BaseBean<BaseBeanEntity>> log(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_service_cancel_result/pre_cancel_result")
    Observable<BaseBean<CancelCauseBean>> pre_cancel_result(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_service_cancel_result/cancel_result")
    Observable<BaseBean<BaseBeanEntity>> cancel_result(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("app/gift/send_gift")
    Observable<BaseBean<VideoChatStratEndBean>> send_gift(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_video_recommend/index")
    Observable<BaseBean<RecommendServicerListBean>> user_video_recommend_index(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/im_chat/status")
    Observable<BaseBean<ImChatStatusBean>> imchat_status(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/turntable/invite_play")
    Observable<BaseBean<InvitePlayBean>> invite_play(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/turntable/init_turntable")
    Observable<BaseBean<TurntableConfigBean>> init_turntable(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/turntable/refuse")
    Observable<BaseBean<BaseBeanEntity>> turntable_refuse(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/turntable/play")
    Observable<BaseBean<BaseBeanEntity>> play(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("app/index/index_v5")
    Observable<BaseBean<IndexV5Bean>> index_v5(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/index/index_v8")
    Observable<BaseBean<IndexV5Bean>> index_v8(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("app/user/pre_edit_meet_tags")
    Observable<BaseBean<PreEditMeetTagsBean>> pre_edit_meet_tags(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/edit_meet_tags")
    Observable<BaseBean<PreEditMeetTagsBean>> edit_meet_tags(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("user/user/active_user_list")
    Observable<BaseBean<ActiveUserListBean>> active_user_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_visit_log/log")
    Observable<BaseBean<BaseBeanEntity>> visit_log(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_visit_log/visit_list")
    Observable<BaseBean<UserVisitListBean>> visit_list(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user/init_setting")
    Observable<BaseBean<SetPriceInitBean>> init_setting(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("user/user/set_service_price")
    Observable<BaseBean<BaseBeanEntity>> set_service_price(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user/edit_video_tags")
    Observable<BaseBean<BaseBeanEntity>> edit_video_tags(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("service/user_eval/pre_service_eval")
    Observable<BaseBean<PreServiceEvalBean>> pre_service_eval(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("service/user_eval/service_tags_eval")
    Observable<BaseBean<ServiceEvalBean>> service_tags_eval(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user_setting/switch_video_chat_status")
    Observable<BaseBean<BaseBeanEntity>> switch_video_chat_status(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user/query_user")
    Observable<BaseBean<SearchUserBean>> query_user(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("stats/rank/rank")
    Observable<BaseBean<RankBean>> rank(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/user/me")
    Observable<BaseBean<MeDataBean>> me(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/user/update_push_token")
    Observable<BaseBean<BaseBeanEntity>> update_push_token(@FieldMap Map<String, Object> params);
//    /**
//     * 首页轮播图
//     */
//    @GET("ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14")
//    Observable<FrontpageBean> getFrontpage();
//
//    /**
//     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
//     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
//     * 请求个数： 数字，大于0
//     * 第几页：数字，大于0
//     * eg: http://gank.io/api/data/Android/10/1
//     */
//    @GET("data/{type}/{pre_page}/{page}")
//    Observable<GankIoDataBean> getGankIoData(@Path("type") String id, @Path("page") int page, @Path("pre_page") int pre_page);
//
//    /**
//     * 每日数据： http://gank.io/api/day/年/月/日
//     * eg:http://gank.io/api/day/2015/08/06
//     */
//    @GET("day/{year}/{month}/{day}")
//    Observable<GankIoDayBean> getGankIoDay(@Path("year") String year, @Path("month") String month, @Path("day") String day);
//
//    /**
//     * 豆瓣热映电影，每日更新
//     */
//    @GET("v2/movie/in_theaters")
//    Observable<HotMovieBean> getHotMovie();
//
//    /**
//     * 获取电影详情
//     *
//     * @param id 电影bean里的id
//     */
//    @GET("v2/movie/subject/{id}")
//    Observable<MovieDetailBean> getMovieDetail(@Path("id") String id);
//
//    /**
//     * 获取豆瓣电影top250
//     *
//     * @param start 从多少开始，如从"0"开始
//     * @param count 一次请求的数目，如"10"条，最多100
//     */
//    @GET("v2/movie/top250")
//    Observable<HotMovieBean> getMovieTop250(@Query("start") int start, @Query("count") int count);
//
//    /**
//     * 根据tag获取图书
//     *
//     * @param tag   搜索关键字
//     * @param count 一次请求的数目 最多100
//     */
//
//    @GET("v2/book/search")
//    Observable<BookBean> getBook(@Query("tag") String tag, @Query("start") int start, @Query("count") int count);
//
//    @GET("v2/book/{id}")
//    Observable<BookDetailBean> getBookDetail(@Path("id") String id);

    /**
     * 根据tag获取music
     * @param tag
     * @return
     */

//    @GET("v2/music/search")
//    Observable<MusicRoot> searchMusicByTag(@Query("tag")String tag);

//    @GET("v2/music/{id}")
//    Observable<Musics> getMusicDetail(@Path("id") String id);
}