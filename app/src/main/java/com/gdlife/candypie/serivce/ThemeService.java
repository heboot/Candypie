package com.gdlife.candypie.serivce;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.gdlife.candypie.MAPP;
import com.gdlife.candypie.R;
import com.gdlife.candypie.common.MValue;
import com.gdlife.candypie.utils.ObserableUtils;
import com.gdlife.candypie.utils.StringUtils;
import com.gdlife.candypie.widget.CenterAlignImageSpan;
import com.heboot.bean.config.ConfigBean;
import com.heboot.bean.theme.MeetPoiDataBean;
import com.heboot.bean.user.UserSkillBean;
import com.heboot.entity.User;
import com.heboot.utils.DateUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by heboot on 2018/2/8.
 */

public class ThemeService {

    /**
     * 获取热门服务
     *
     * @return
     */
    public static List<ConfigBean.ServiceItemsConfigBean.ListBean> getHotThemeList() {

        List<ConfigBean.ServiceItemsConfigBean.ListBean> allServices = MAPP.mapp.getConfigBean().getService_items_config().getList();

        List<ConfigBean.ServiceItemsConfigBean.ListBean> returnServices = new ArrayList<>();

        String hotIds = MAPP.mapp.getConfigBean().getService_items_config().getHot_list();

        String[] ids = hotIds.split(",");


        for (String s : ids) {
            for (ConfigBean.ServiceItemsConfigBean.ListBean bean : allServices) {
                if (s.equals(bean.getId())) {
                    returnServices.add(bean);
                }
            }
        }


        return returnServices;

    }

    /**
     * 获取全部服务
     *
     * @return
     */
    public static List<ConfigBean.ServiceItemsConfigBean.ListBean> getAllThemeList() {
        List<ConfigBean.ServiceItemsConfigBean.ListBean> allServices = MAPP.mapp.getConfigBean().getService_items_config().getList();
//        for (ConfigBean.ServiceItemsConfigBean.ListBean listBean : allServices) {
//            if (listBean.getId().equals("0")) {
//                allServices.remove(listBean);
//                return allServices;
//            }
//        }
        return allServices;
    }

    public static List<ConfigBean.ServiceItemsConfigBean.ListBean> getAllThemeListNoMore() {
        List<ConfigBean.ServiceItemsConfigBean.ListBean> allServices = getAllThemeList();

        List<ConfigBean.ServiceItemsConfigBean.ListBean> result = new ArrayList<>();

        result.addAll(allServices);

        List<ConfigBean.ServiceItemsConfigBean.ListBean> returnResult = new ArrayList<>();

        for (ConfigBean.ServiceItemsConfigBean.ListBean listBean : result) {
            if (Integer.parseInt(listBean.getId()) > 0) {
                returnResult.add(listBean);
            }
        }

//        for (int i = 0; i < result.size(); i++) {
//            if (result.get(i).getId().equals("0") || result.get(i).getId().equals("-1")) {
//                result.remove(i);
//            }
//        }
        return returnResult;
    }

    public static List<ConfigBean.ServiceItemsConfigBean.ListBean> getAllThemeListNoMoreNoVideo() {
        List<ConfigBean.ServiceItemsConfigBean.ListBean> allServices = getAllThemeList();

        List<ConfigBean.ServiceItemsConfigBean.ListBean> result = new ArrayList<>();

        result.addAll(allServices);

        List<ConfigBean.ServiceItemsConfigBean.ListBean> returnResult = new ArrayList<>();

        for (ConfigBean.ServiceItemsConfigBean.ListBean listBean : result) {
            if (Integer.parseInt(listBean.getId()) > 0 && Integer.parseInt(listBean.getId()) != 1) {
                returnResult.add(listBean);
            }
        }

//        for (int i = 0; i < result.size(); i++) {
//            if (result.get(i).getId().equals("0") || result.get(i).getId().equals("-1")) {
//                result.remove(i);
//            }
//        }
        return returnResult;
    }

    /**
     * 获取全部需要认证的服务
     *
     * @return
     */
    public static List<ConfigBean.ServiceItemsConfigBean.ListBean> getAllAuthThemeList() {
        List<ConfigBean.ServiceItemsConfigBean.ListBean> result = new ArrayList<>();
        List<ConfigBean.ServiceItemsConfigBean.ListBean> allServices = MAPP.mapp.getConfigBean().getService_items_config().getList();

        for (ConfigBean.ServiceItemsConfigBean.ListBean listBean : allServices) {
            if (!StringUtils.isEmpty(listBean.getNeed_auth()) && !listBean.getNeed_auth().equals("0") && !listBean.getNeed_auth().equals("-1")) {
                result.add(listBean);
            }
        }

        return result;
    }

    /**
     * 获取全部服务
     *
     * @return
     */
    public static List<String> getRewardConfig(int duIndex, ConfigBean.ServiceItemsConfigBean.ListBean listBean) {
        List<String> reward = new ArrayList<>();
        int minPrice = Integer.parseInt(listBean.getTime_price().get(duIndex).getPrice());
        int maxPrice = Integer.parseInt(listBean.getPrice_limit().split(",")[1]);
        while (minPrice <= maxPrice) {
            reward.add("" + minPrice);
            minPrice = minPrice + 100;
        }
        return reward;
    }

    /**
     * @param listBean
     * @return
     */
    public static List<String> getVideoRewardConfig(ConfigBean.ServiceItemsConfigBean.ListBean listBean) {
        String[] result = listBean.getPrice_limit().split(",");

        List<String> config = new ArrayList<>();

        int min = Integer.parseInt(result[0]);

        int max = Integer.parseInt(result[1]);

        while (min <= max) {
            config.add(min + "");
            min = min + 100;
        }

        return config;

    }


    /**
     * 获取某个服务可选的时长
     *
     * @param listBean
     * @return
     */
    public static List<String> getServiceDurations(ConfigBean.ServiceItemsConfigBean.ListBean listBean) {

        List<String> durations = new ArrayList<>();

        for (ConfigBean.ServiceItemsConfigBean.ListBean.TimePriceBean timePriceBean : listBean.getTime_price()) {
            durations.add(DateUtil.getHoursByDuration(timePriceBean.getTime()));
        }

        return durations;
    }


    public static String getServiceDurationsByListBean(ConfigBean.ServiceItemsConfigBean.ListBean listBean, int position) {
        return String.valueOf(listBean.getTime_price().get(position).getTime());
    }

    /**
     * 获取某个服务可选的时间范围
     *
     * @return
     */
    public static List<String> getServiceDays(boolean outDay) {

        List<String> days = new ArrayList<>();

        if (!outDay) {
            days.add(MAPP.mapp.getString(R.string.today));
        }
        days.add(MAPP.mapp.getString(R.string.today2));
        days.add(MAPP.mapp.getString(R.string.today3));

        return days;
    }


    /**
     * 获取某个服务可选的时间范围
     *
     * @param listBean
     * @return
     */
    public static List<String> getServiceHours(ConfigBean.ServiceItemsConfigBean.ListBean listBean) {

        Calendar selectedDate = Calendar.getInstance();

        List<String> hours = new ArrayList<>();

        String time = listBean.getTime_limit();

        int min = Integer.parseInt(time.split(",")[0]);
        int max = Integer.parseInt(time.split(",")[1]);


        while (min <= max) {
//            hours.add(min + MAPP.mapp.getString(R.string.hour));
            hours.add(String.valueOf(min));
            min = min + 1;
        }

        return hours;
    }

    /**
     * 获取某个服务可选的时间范围
     *
     * @param listBean
     * @return
     */
    public static List<String> getServiceTodayHours(ConfigBean.ServiceItemsConfigBean.ListBean listBean) {

        Calendar selectedDate = Calendar.getInstance();

        int currentHour = selectedDate.get(Calendar.HOUR_OF_DAY);

        int currentSencond = selectedDate.get(Calendar.MINUTE);

        List<String> hours = new ArrayList<>();

        String time = listBean.getTime_limit();

        int min = Integer.parseInt(time.split(",")[0]);
        int max = Integer.parseInt(time.split(",")[1]);

        if (currentHour > max) {
            return null;
        } else if (currentHour == max && currentSencond > 30) {
            return null;
        } else if (currentSencond > 50) {
            currentHour = currentHour + 1;
        }

        currentHour = currentHour + 1;

        min = currentHour;

        while (min <= max) {
//            hours.add(min + " " + MAPP.mapp.getString(R.string.hour_shi));
            hours.add(String.valueOf(min));
            min = min + 1;
        }

        return hours;
    }


    /**
     * 获取某个服务可选的时间范围
     *
     * @return
     */
    public static List<String> getServiceMins() {

        List<String> mins = new ArrayList<>();

//        mins.add("00" + MAPP.mapp.getString(R.string.minute));
//
//        mins.add("10" + MAPP.mapp.getString(R.string.minute));
//
//        mins.add("20" + MAPP.mapp.getString(R.string.minute));
//
//        mins.add("30" + MAPP.mapp.getString(R.string.minute));
//
//        mins.add("40" + MAPP.mapp.getString(R.string.minute));
//
//        mins.add("50" + MAPP.mapp.getString(R.string.minute));

        mins.add("00");

        mins.add("10");

        mins.add("20");

        mins.add("30");

        mins.add("40");

        mins.add("50");

        return mins;
    }


    /**
     * 获取某个服务可选的时间范围
     *
     * @return
     */
    public static List<String> getServiceTodayMins() {

        Calendar selectedDate = Calendar.getInstance();

        int currentSencond = selectedDate.get(Calendar.MINUTE);

        List<String> mins = new ArrayList<>();

        int startSencond = (int) (Math.ceil(currentSencond / 10.0) * 10);

//        int startSencond = 0;

        while (startSencond <= 50) {

//            mins.add(startSencond + MAPP.mapp.getString(R.string.minute));

            mins.add(String.valueOf(startSencond));

            startSencond = startSencond + 10;
        }


        return mins;
    }


    public static long getServiceStartTime(int dayIndex, String hourStr, String seStr, int daySize) {


        String result = "";


        int hour = Integer.parseInt(hourStr.replace(MAPP.mapp.getString(R.string.hour_shi), "").trim());

        int sencond = Integer.parseInt(seStr.replace(MAPP.mapp.getString(R.string.minute), ""));

        //只有明天和后天
        if (daySize == 2) {
            dayIndex = dayIndex + 1;
        }
        //只有后天
        else if (daySize == 1) {
            dayIndex = dayIndex + 2;
        }


        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, dayIndex);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, sencond);

        long selectTime = calendar.getTime().getTime();


        return selectTime;
    }


    public static String getServiceEndText3(ConfigBean.ServiceItemsConfigBean.ListBean listBean, int dayIndex, String hourStr, String seStr, String duStr) {


        String result = "";


//        int hour = Integer.parseInt(hourStr.replace(MAPP.mapp.getString(R.string.hour_shi), "").trim());
//
//        int sencond = Integer.parseInt(seStr.replace(MAPP.mapp.getString(R.string.minute), "").trim());

        int hour = Integer.parseInt(hourStr.trim());

        int sencond = Integer.parseInt(seStr.trim());


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, dayIndex);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, sencond);

        long selectTime = calendar.getTime().getTime();

        long currentTime = System.currentTimeMillis();

        long cha = Math.abs(selectTime - currentTime);

        double du = Double.parseDouble(duStr.replace(MAPP.mapp.getString(R.string.hours), "").trim());

        int dus = (int) (du * 60 * 60 * 1000);

        long totalss = cha + dus + System.currentTimeMillis();


        result = getDu(totalss) + MAPP.mapp.getString(R.string.finish);


        return result;
    }

    public static String getDu(long time) {
        String result = "";
        int count = DateUtil.getServiceDurationDays(time);
        count = Math.abs(count);
        switch (count) {
            case 0:
                result = MAPP.mapp.getString(R.string.today) + DateUtil.date2Str(new Date(time), DateUtil.FORMAT_HHMM_CN);
                break;
            case 1:
                result = MAPP.mapp.getString(R.string.today2) + DateUtil.date2Str(new Date(time), DateUtil.FORMAT_HHMM_CN);
                break;
            case 2:
                result = MAPP.mapp.getString(R.string.today3) + DateUtil.date2Str(new Date(time), DateUtil.FORMAT_HHMM_CN);
                break;
            case 3:
                result = DateUtil.date2Str(new Date(time), DateUtil.FORMAT_YMDHM_CN);
                break;
        }
        return result;
    }


    /**
     * 获取所有支持的城市
     *
     * @return
     */
    public static List<ConfigBean.CityConfigBean> getAllCitys() {
        return MAPP.mapp.getConfigBean().getCity_config();
    }

    public static boolean supportCity(String cityId) {
        List<ConfigBean.CityConfigBean> list = getAllCitys();
        for (ConfigBean.CityConfigBean cityConfigBean : list) {
            if (cityConfigBean.getId().equals(cityId)) {
                return true;
            }
        }
        return false;
    }

    public static ConfigBean.CityConfigBean getCityByID(String cityId) {
        List<ConfigBean.CityConfigBean> list = getAllCitys();
        for (ConfigBean.CityConfigBean cityConfigBean : list) {
            if (cityConfigBean.getId().equals(cityId)) {
                return cityConfigBean;
            }
        }
        return null;
    }

    /**
     * 获取所有支持的城市
     *
     * @return
     */
    public static List<String> getAllCitysByString(boolean isVideo) {
        List<String> result = new ArrayList<>();
        if (isVideo) {
            result.add(MAPP.mapp.getString(R.string.theme_choose_address_unlimited));
        }

        List<ConfigBean.CityConfigBean> cityConfigBeans = getAllCitys();
        for (ConfigBean.CityConfigBean bean : cityConfigBeans) {
            result.add(bean.getName());
        }
        return result;
    }


    /**
     * 根据用户当前的位置查询城市
     *
     * @return
     */
    public static ConfigBean.CityConfigBean getCurrentCity() {
        if (MValue.currentLocation != null && !StringUtils.isEmpty(MValue.currentLocation.getCityCode())) {
            List<ConfigBean.CityConfigBean> cityConfigBeans = MAPP.mapp.getConfigBean().getCity_config();
            for (ConfigBean.CityConfigBean bean : cityConfigBeans) {
                if (MValue.currentLocation.getCityCode().equals(bean.getId())) {
                    return bean;
                }
            }
        }
        return null;
    }

    /**
     * 区域数据
     *
     * @return
     */
    public static List<MeetPoiDataBean.ZoneConfigBean.ItemsBeanX> getZoneDatas(MeetPoiDataBean meetPoiDataBean) {
        return meetPoiDataBean.getZone_config().getItems();
    }

    /**
     * 区域子数据
     *
     * @return
     */
    public static List<MeetPoiDataBean.ZoneConfigBean.ItemsBeanX.ItemsBean> getZoneChildDatas(MeetPoiDataBean meetPoiDataBean, int position) {
        List<MeetPoiDataBean.ZoneConfigBean.ItemsBeanX> items = getZoneDatas(meetPoiDataBean);
        List<MeetPoiDataBean.ZoneConfigBean.ItemsBeanX.ItemsBean> result = items.get(position).getItems();
        return result;
    }

    public static ConfigBean.RequireIdsBean getServiceReqIdsBean(ConfigBean.ServiceItemsConfigBean.ListBean listBean) {
        if (listBean.getRequire_ids() != null && listBean.getRequire_ids().size() >= 1) {
            return listBean.getRequire_ids().get(0);
        }
        return null;
    }


    /**
     * 获取视频发布最小的钻石数
     *
     * @param coinBySecond
     * @return
     */
    public static int getVideoMinReqCoin(int coinBySecond) {
        return coinBySecond;
    }

    /**
     * 组装选择要求后要显示的字符串
     *
     * @param nums
     * @param sex
     * @param privacy
     * @param extend
     * @return
     */
    public static String getRequireStr(int nums, int sex, int privacy, String extend) {
        String sexStr = "";

        if (sex == 0) {
            sexStr = MAPP.mapp.getString(R.string.woman_unit);
        } else if (sex == 1) {
            sexStr = MAPP.mapp.getString(R.string.man_unit);
        } else if (sex == 2) {
            sexStr = MAPP.mapp.getString(R.string.sex_other_unit);
        }
        String result = null;

        if (sex == 2) {
            result = nums + MAPP.mapp.getString(R.string.persons_unit) + " " + (privacy == 1 ? MAPP.mapp.getString(R.string.privacy) : "") + extend;
        } else {
            result = nums + MAPP.mapp.getString(R.string.person_unit) + sexStr + (privacy == 1 ? MAPP.mapp.getString(R.string.privacy) : "") + extend;
        }


        return result;
    }

    /**
     * 根据用户的技能列表 去服务列表里去匹配
     */
    public static List<ConfigBean.ServiceItemsConfigBean.ListBean> getUserSkillList(User user) {
        List<UserSkillBean> skillBeans = user.getSkill_list();
        List<ConfigBean.ServiceItemsConfigBean.ListBean> alllistBeans = getAllThemeList();
        List<ConfigBean.ServiceItemsConfigBean.ListBean> listBeans = new ArrayList<>();
        if (skillBeans != null && skillBeans.size() > 0) {
            for (ConfigBean.ServiceItemsConfigBean.ListBean bean : alllistBeans) {
                for (UserSkillBean skillBean : skillBeans) {
                    if (bean.getId().equals(skillBean.getId())) {
                        listBeans.add(bean);
                    }
                }
            }
            return listBeans;
        }
        return null;
    }

    /**
     * 获取一个模拟的加号技能
     */
    public static List<ConfigBean.ServiceItemsConfigBean.ListBean> getUserSkillListByUserNull() {
        List<ConfigBean.ServiceItemsConfigBean.ListBean> listBeans = new ArrayList<>();
        return listBeans;
    }


    /**
     * 获取视频聊天的最低价格
     *
     * @return
     */
    public static int getVideoServiceMinPrice() {
        List<ConfigBean.ServiceItemsConfigBean.ListBean> alllistBeans = getAllThemeList();
        for (ConfigBean.ServiceItemsConfigBean.ListBean bean : alllistBeans) {
            if (bean.getType().equals(MValue.ORDER_TYPE_VIDEO)) {
                return Integer.parseInt(bean.getPrice_limit().split(",")[0]);
            }
        }
        return 3;
    }

    /**
     * 获取商家地址的URL
     */
    public static String getAddressMapURL(double poiLat, double poiLng) {
        String params = "?";
        params = params + "lat=" + MValue.currentLocation.getLatitude() + "&lng=" + MValue.currentLocation.getLongitude() + "&poi_lat=" + poiLat + "&poi_lng=" + poiLng;
        return MValue.MAP_URL + params;
    }


    /**
     * 根据服务ID获取某一个服务
     *
     * @param id
     * @return
     */
    public static ConfigBean.ServiceItemsConfigBean.ListBean getServiceBeanById(String id) {
        List<ConfigBean.ServiceItemsConfigBean.ListBean> alllistBeans = getAllThemeList();
        for (ConfigBean.ServiceItemsConfigBean.ListBean bean : alllistBeans) {
            if (bean.getId().equals(id)) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 根据服务IDS获取服务列表
     *
     * @param ids
     * @return
     */
    public static List<ConfigBean.ServiceItemsConfigBean.ListBean> getServiceBeanByIds(List<String> ids) {
        List<ConfigBean.ServiceItemsConfigBean.ListBean> result = new ArrayList<>();
        for (String id : ids) {
            if (getServiceBeanById(id) != null) {
                result.add(getServiceBeanById(id));
            }
        }
        return result;
    }

    /**
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param du        持续时间
     * @return
     */
    public static String getServiceOrderTime(long startTime, long endTime, float du) {

        //几月几日
        String day = DateUtil.date2Str(new Date(startTime * 1000), DateUtil.FORMAT_MD_CN);

        String start = DateUtil.date2Str(new Date(startTime * 1000), DateUtil.FORMAT_HM);


        String isDay = DateUtil.date2Str(new Date(endTime * 1000), DateUtil.FORMAT_MD_CN);

        String end = "";

        if (!isDay.equals(day)) {
            end = DateUtil.date2Str(new Date(endTime * 1000), DateUtil.FORMAT_MD_CN_SERVICE);
        } else {
            end = DateUtil.date2Str(new Date(endTime * 1000), DateUtil.FORMAT_HM);
        }

        String time = du % 60.0 == 0 ? String.valueOf(du / 60) : String.valueOf(du / 60.0);

        String duStr = time + MAPP.mapp.getString(R.string.hours);

        String sss = DateUtil.getForDay(startTime * 1000);

        if (StringUtils.isEmpty(sss)) {
            return day + " " + start + "-" + end + "  " + duStr;
        }

        return sss + " " + start + " -" + end + "  " + duStr;

    }

    public static String getServiceOrderTimeByVideo(long startTime, float du) {

        //几月几日
        String day = DateUtil.date2Str(new Date(startTime * 1000), DateUtil.FORMAT_MD_CN);

        String start = DateUtil.date2Str(new Date(startTime * 1000), DateUtil.FORMAT_HM);


        String time = du % 60.0 == 0 ? String.valueOf(du / 60) : String.valueOf(du / 60.0);

        String duStr = time + MAPP.mapp.getString(R.string.unit_minute);

        String sss = DateUtil.getForDay(startTime * 1000);

        if (StringUtils.isEmpty(sss)) {
            return day + " " + start + " " + duStr;
        }

        return sss + " " + start + " " + duStr;

    }


    /**
     * 根据弹窗的类型获取对应的文字content
     */
    public static String getServiceTipDialogText(MValue.TIP_DIALOG_TYPE type) {
        if (type == MValue.TIP_DIALOG_TYPE.CONFINE) {
            return MAPP.mapp.getString(R.string.cancel_server_more_tip_title);
        } else if (type == MValue.TIP_DIALOG_TYPE.ONE) {
            return MAPP.mapp.getString(R.string.v1_tip);
        } else if (type == MValue.TIP_DIALOG_TYPE.NO_USER) {
            return "很抱歉没有服务者抢单\n" +
                    "本次订单已自动取消";
        } else if (type == MValue.TIP_DIALOG_TYPE.TIMEOUT) {
            return "抱歉由于您选人超过时限\n" +
                    "本次订单自动取消";
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST) {
            return "为了吸引更多服务者抢单\n" +
                    "蜜糖派采取预支付方式";
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST_ROB) {
            return MAPP.mapp.getString(R.string.first_rob_title);
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST_VIDEO_ORDER) {
            return MAPP.mapp.getString(R.string.first_video_order_tip);
        } else if (type == MValue.TIP_DIALOG_TYPE.UPDATE_GLOD_VIP) {
            return "充值成功\n感谢您升级为金卡会员";
        }
        return "";
    }

    /**
     * 根据弹窗的类型获取对应的文字content2
     */
    public static void getServiceTipDialogText2(TextView textView, Long time, MValue.TIP_DIALOG_TYPE type, String radio, String rechargePrice) {
        if (type == MValue.TIP_DIALOG_TYPE.CONFINE) {
            if (time == null) {
                return;
            }
            textView.append(MAPP.mapp.getString(R.string.cancel_server_more_tip_content1) + "\t");
            //时间红色
            String expire_time = DateUtil.getForDay(time * 1000) + DateUtil.date2Str(new Date(time * 1000), DateUtil.FORMAT_HM);
            SpannableString timeSpan = new SpannableString(expire_time);
            timeSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MAPP.mapp, R.color.color_FF5252)), 0, expire_time.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.append(timeSpan);
            textView.append("\n");


            if (!StringUtils.isEmpty(VipService.getNextVipLevelName(UserService.getInstance().getUser().getLevel()))) {

                String updateLevle = MAPP.mapp.getString(R.string.update_level);
                updateLevle = updateLevle + VipService.getNextVipLevelName(UserService.getInstance().getUser().getLevel());
                SpannableString levelSpan = new SpannableString(updateLevle);
                levelSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MAPP.mapp, R.color.color_007AFF)), 0, updateLevle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.append(levelSpan);
                textView.append(MAPP.mapp.getString(R.string.cancel_server_more_tip_content2));
            }


        } else if (type == MValue.TIP_DIALOG_TYPE.ONE) {
            textView.append(MAPP.mapp.getString(R.string.v1_tip2) + "\t");
            //时间红色
            String expire_time = "1小时";
            SpannableString timeSpan = new SpannableString(expire_time);
            timeSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MAPP.mapp, R.color.color_FF5252)), 0, expire_time.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.append(timeSpan);
            textView.append(MAPP.mapp.getString(R.string.v1_tip3));
        } else if (type == MValue.TIP_DIALOG_TYPE.NO_USER) {
            textView.setText(MAPP.mapp.getString(R.string.order_nouser_tip));
        } else if (type == MValue.TIP_DIALOG_TYPE.TIMEOUT) {
            textView.setText(MAPP.mapp.getString(R.string.order_timeout_tip));
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST) {


            textView.append(MAPP.mapp.getString(R.string.first_order_pay_tip1));

            radio = radio + "%";
            //时间红色
            SpannableString timeSpan = new SpannableString(radio);
            timeSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MAPP.mapp, R.color.color_FF5252)), 0, radio.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.append(timeSpan);

            textView.append(MAPP.mapp.getString(R.string.first_order_pay_tip2));


//            textView.setText("首次发单只需支付 10%订金\n" +
//                    "订金可抵扣最终费用");
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST_ROB) {
            textView.setText(MAPP.mapp.getString(R.string.first_rob_content));
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST_VIDEO_ORDER) {

            textView.append("你的账户钻石不足\n充值");

            rechargePrice = rechargePrice + MAPP.mapp.getString(R.string.price_unit);

            //时间红色
            SpannableString timeSpan = new SpannableString(rechargePrice);
            timeSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MAPP.mapp, R.color.color_FF5252)), 0, rechargePrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.append(timeSpan);

            textView.append("立即视频");


//            textView.setText("首次发单只需支付 10%订金\n" +
//                    "订金可抵扣最终费用");
        } else if (type == MValue.TIP_DIALOG_TYPE.UPDATE_GLOD_VIP) {

            textView.append("现在您将享受更多的特权");

        }
    }

    /**
     * 根据弹窗的类型获取对应的文字content2
     */
    public static String getServiceTipDialogLeftText(MValue.TIP_DIALOG_TYPE type) {
        if (type == MValue.TIP_DIALOG_TYPE.CONFINE) {
            return "知道了";
        } else if (type == MValue.TIP_DIALOG_TYPE.ONE) {
            return "知道了";
        } else if (type == MValue.TIP_DIALOG_TYPE.NO_USER) {
            return "知道了";
        } else if (type == MValue.TIP_DIALOG_TYPE.TIMEOUT) {
            return "知道了";
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST) {
            return "再等等";
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST_VIDEO_ORDER) {
            return "再等等";
        } else if (type == MValue.TIP_DIALOG_TYPE.UPDATE_GLOD_VIP) {
            return "知道了";

        }
        return "";
    }

    /**
     * @param textView
     * @param type
     * @param prePrice 首单弹框的话 需要知道预支付金额
     * @return
     */
    public static void getServiceTipRightText(TextView textView, MValue.TIP_DIALOG_TYPE type, String prePrice, String rechargePrice) {
        if (type == MValue.TIP_DIALOG_TYPE.CONFINE) {
            if (!StringUtils.isEmpty(VipService.getNextVipLevelName(UserService.getInstance().getUser().getLevel()))) {
                String updateLevle = MAPP.mapp.getString(R.string.update_level);

                if (StringUtils.isEmpty(VipService.getNextVipLevelName(UserService.getInstance().getUser().getLevel()))) {
                    textView.setText(MAPP.mapp.getString(R.string.confirm));
                } else {
                    updateLevle = updateLevle + VipService.getNextVipLevelName(UserService.getInstance().getUser().getLevel());
                    textView.setText(updateLevle);
                }


            }
        } else if (type == MValue.TIP_DIALOG_TYPE.ONE) {
            textView.setText(MAPP.mapp.getString(R.string.iknow));
        } else if (type == MValue.TIP_DIALOG_TYPE.NO_USER) {
            textView.setText(MAPP.mapp.getString(R.string.resend_service));
        } else if (type == MValue.TIP_DIALOG_TYPE.TIMEOUT) {
            textView.setText(MAPP.mapp.getString(R.string.resend_service));
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST) {
            textView.setText(MAPP.mapp.getString(R.string.pre_to_pay) + prePrice + MAPP.mapp.getString(R.string.price_unit));
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST_ROB) {
            textView.setText(MAPP.mapp.getString(R.string.resend_service));
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST_VIDEO_ORDER) {
            textView.setText(MAPP.mapp.getString(R.string.account_recharge) + rechargePrice + MAPP.mapp.getString(R.string.price_unit));
        } else if (type == MValue.TIP_DIALOG_TYPE.UPDATE_GLOD_VIP) {
            textView.setText(MAPP.mapp.getString(R.string.account_recharge) + rechargePrice + MAPP.mapp.getString(R.string.price_unit));
        }
    }

    public static int getServiceTipDialogBG(MValue.TIP_DIALOG_TYPE type) {
        if (type == MValue.TIP_DIALOG_TYPE.CONFINE) {
            return R.drawable.bg_dialogservice_confine;
        } else if (type == MValue.TIP_DIALOG_TYPE.ONE) {
            return R.drawable.bg_dialogservice_one;
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST_VIDEO_ORDER) {
            return R.drawable.bg_dialogservice_one;
        } else if (type == MValue.TIP_DIALOG_TYPE.NO_USER) {
            return R.drawable.bg_dialogservice_nouser;
        } else if (type == MValue.TIP_DIALOG_TYPE.TIMEOUT) {
            return R.drawable.bg_dialogservice_timeout;
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST) {
            return R.drawable.bg_dialogservice_first;
        } else if (type == MValue.TIP_DIALOG_TYPE.FIRST_ROB) {
            return R.drawable.bg_dialogservice_first_rob;
        } else if (type == MValue.TIP_DIALOG_TYPE.UPDATE_GLOD_VIP) {
            return R.drawable.bg_dialogservice_one;
        }
        return R.drawable.bg_dialogservice_nouser;
    }


    /**
     * 获取取消订单提示的文本
     *
     * @return
     */
    public static void setCancelDialogText(TextView textView, String rate) {

        String cancelTitle = MAPP.mapp.getString(R.string.cancel_order_tip_title);

        SpannableString titleSpan = new SpannableString(cancelTitle);
        titleSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, cancelTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleSpan.setSpan(new AbsoluteSizeSpan(QMUIDisplayHelper.dpToPx(17)), 0, cancelTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.append(cancelTitle);

        String result = MAPP.mapp.getString(R.string.cancel_order_tip_content);
        String result2 = String.format(result, rate + "%");
        SpannableString timeSpan = new SpannableString(result2);
        timeSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MAPP.mapp, R.color.color_58586C)), 0, result2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        timeSpan.setSpan(new AbsoluteSizeSpan(QMUIDisplayHelper.dpToPx(13)), 0, result2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.append(timeSpan);

    }

    /**
     * 获取取消订单页面的文本
     *
     * @param textView
     */
    public static void setCancelPageText(TextView textView, String hour) {


        String str1 = MAPP.mapp.getString(R.string.cancel_order_tip_page_content);

        textView.append(str1);

        String count = VipService.getCurrentVipLevel(UserService.getInstance().getUser().getVip_level()).getCancel_nums();


        SpannableString countSpan = new SpannableString(count);
        countSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MAPP.mapp, R.color.color_FF5252)), 0, count.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        countSpan.setSpan(new AbsoluteSizeSpan(QMUIDisplayHelper.dpToPx(20)), 0, count.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.append(countSpan);

        textView.append(MAPP.mapp.getString(R.string.cancel_order_tip_page_content3));


        String time = hour;


        SpannableString timeSpan = new SpannableString(time);
        timeSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MAPP.mapp, R.color.color_FF5252)), 0, time.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        timeSpan.setSpan(new AbsoluteSizeSpan(QMUIDisplayHelper.dpToPx(28)), 0, time.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.append(timeSpan);

        textView.append(MAPP.mapp.getString(R.string.cancel_order_tip_page_content2));


        if (!StringUtils.isEmpty(VipService.getNextVipLevelName(UserService.getInstance().getUser().getLevel()))) {

            String updateLevle = MAPP.mapp.getString(R.string.update_level);

            updateLevle = updateLevle + VipService.getNextVipLevelName(UserService.getInstance().getUser().getLevel());

            SpannableString updateLevel = new SpannableString(updateLevle);
            updateLevel.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MAPP.mapp, R.color.color_007AFF)), 0, updateLevle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.append(updateLevel);

            textView.append(MAPP.mapp.getString(R.string.update_level_count_str));
        }


    }

    /**
     * 获取首页底部喇叭提示
     *
     * @param datas
     * @return
     */
    public static List<String> getIndexBottomTip(List<String> datas) {
        int index = 0;
        List<String> result = new ArrayList<>();

        String temp = "";

        for (String s : datas) {
            if (index <= 1) {
                if (index == 0) {
                    temp = temp + s + "\n";
                }
                if (index == 1) {
                    temp = temp + s;
                    index = -1;
                    result.add(temp);
                    temp = "";
                }

            }
            index = index + 1;
        }
        return result;
    }

    public static String newThemeSelectedText(long startTime, long endTime) {

        String str1 = "";

        String str2 = "";

        String day1 = DateUtil.getForDay(startTime);

        String hour1 = DateUtil.date2Str(new Date(startTime), DateUtil.FORMAT_HM);

        String day2 = DateUtil.getForDay(endTime);

        if (StringUtils.isEmpty(day2)) {
            day2 = DateUtil.date2Str(new Date(endTime), DateUtil.FORMAT_MD_CN) + " ";
        }

        if (day2.equals("今天")) {
            day2 = "";
        }


        String hour2 = DateUtil.date2Str(new Date(endTime), DateUtil.FORMAT_HM);

        str1 = day1 + hour1;

        str2 = day2 + hour2;

        return str1 + " - " + str2;


    }


    /**
     * @param start_push_time    开始推送时间
     * @param push_time_interval 推送时间
     * @return
     */
    public static long getServiceUserListPushTime(String start_push_time, String push_time_interval, String serverCurrentTime) {

        long startPushTime = Long.parseLong(start_push_time);
        long pushTimeInterval = Integer.parseInt(push_time_interval) * 60;
        long currentTime = Long.parseLong(serverCurrentTime);

        long result = (startPushTime - currentTime) + pushTimeInterval;

        return result;


//        long startPushTime = Long.parseLong(start_push_time);
////        long currentTime = System.currentTimeMillis() / 1000;
//
//        long currentTime = Long.parseLong(serverCurrentTime);
//
//        long pushTimeInterval = Integer.parseInt(push_time_interval) * 60;
//
//        long resultPushTime = currentTime - startPushTime;
//
//        if (resultPushTime >= pushTimeInterval) {
//            return null;
//        }
//        if (resultPushTime <= 0) {
//            resultPushTime = 0;
//        }
//
//        Long r = null;
//
//        if (resultPushTime <= 0) {
//            r = pushTimeInterval - resultPushTime <= 0 ? 0 : pushTimeInterval + resultPushTime;
//        } else {
//            r = pushTimeInterval - resultPushTime <= 0 ? 0 : pushTimeInterval - resultPushTime;
//        }
//
//        if (r <= 0) {
//            return null;
//        }
//
//        return r;


//        return (pushTimeInterval - resultPushTime);


//        if ((int) resultPushTime > pushTimeInterval) {
//            //推送已经超时
//            return "选人时间开始";
//        } else {
//            ObserableUtils.countdownByMILLISECONDS()
//        }

    }

    public static Long getServiceUserListSelectTime(boolean isPushEnd, Long pushTime, String select_time_interval) {

        long selectTimeInterval = Integer.parseInt(select_time_interval) * 60;

        if (isPushEnd) {
            return selectTimeInterval;
        }

        return pushTime + selectTimeInterval;


//        long startPushTime = Long.parseLong(start_push_time);
//        long pushTimeInterval = Integer.parseInt(push_time_interval) * 60;
//        long selectTime = Integer.parseInt(select_time_interval) * 60;
////        long currentTime = System.currentTimeMillis() / 1000;
//        long currentTime = Long.parseLong(serverCurrentTime);
////        long resultPushTime = currentTime - (startPushTime + pushTimeInterval + selectTime);
//        long resultPushTime = currentTime - (startPushTime + pushTimeInterval + selectTime);
//
//        //选人已经结束
//        if (resultPushTime >= selectTime) {
//            return null;
//        }
//
//        Long r = null;
//
//
//        //180  -360
//        if (resultPushTime <= 0) {
////            r = selectTime - resultPushTime <= 0 ? 0 : selectTime + resultPushTime;
//            r = selectTime;
//        } else {
//            r = selectTime - resultPushTime <= 0 ? 0 : selectTime - resultPushTime;
//        }
//
//        if (r <= 0) {
//            return null;
//        }
//
//        return r;


    }

    public static Long getServiceRobListSelectTime(String expire_time) {


        long expireTtime = Long.parseLong(expire_time);

//        long currentTime = System.currentTimeMillis() / 1000;

//        long currentTime = Long.parseLong(serverCurrentTime);

//        long resultTime = expireTtime - currentTime;

        //选人已经结束
        if (expireTtime <= 0) {
            return null;
        }

        return expireTtime;


    }

    public static String getMi(long time) {
        String result = "";
        int left = (int) (time / 60);
        if (left < 10) {
            result = result + "0" + left;
        } else {
            result = result + left;
        }
        int right = (int) (time % 60);
        if (right < 10) {
            result = result + ":0" + right;
        } else {
            result = result + ":" + right;
        }
        return result;
    }

    public static String getMiL(long time) {
        String result = "";
        int left = (int) (time / 60);
        if (left < 10) {
            result = result + "0" + left;
        } else {
            result = result + left;
        }
        return result;
    }

    public static String getMiR(long time) {
        String result = "";
        int right = (int) (time % 60);
        if (right < 10) {
            result = result + "0" + right;
        } else {
            result = result + right;
        }
        return result;
    }


    /**
     * 获取订单地址后面的红色导航标志
     *
     * @param context
     * @param text
     * @return
     */
    public static SpannableString getOrderAddressSpannable(Context context, String text) {
        SpannableString sp = new SpannableString(text);
        //获取一张图片
        Drawable drawable = context.getDrawable(R.drawable.icon_order_addr_nav);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());


        //居中对齐imageSpan
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable);
        sp.setSpan(imageSpan, text.length() - 1, text.length(), ImageSpan.ALIGN_BASELINE);


        return sp;

    }


    public static String getVideoOrderTimeText(int status, long startTime, long duTime) {
        if (status != MValue.ORDER_LIST_STATUS_YIWANCHENG) {
//            ConfigBean.ServiceItemsConfigBean.ListBean listBean = getServiceBeanById("1");
//            return listBean.getMin_time() + "分钟起聊";
            String s = DateUtil.date2Str(new Date(startTime * 1000L), DateUtil.FORMAT_MDHM) + "  ";
            return s;
        } else {
            String s = DateUtil.date2Str(new Date(startTime * 1000L), DateUtil.FORMAT_MDHM) + "  ";

            long du = duTime;

            if (du / 60 / 60 > 0) {
                s = s + du / 60 / 60 + "小时";
            } else if (du / 60 > 0) {
                s = s + du / 60 + "分";
            } else if (du % 60 > 0) {
                s = s + du % 60 + "秒";
            }
            return s;

        }
    }

    /**
     * 不存在的认证技能 过滤掉
     */
    public static List<UserSkillBean> filterUserSkill(List<UserSkillBean> userSkillBeans) {
        if (userSkillBeans == null) {
            return null;
        }
        List<UserSkillBean> result = new ArrayList<>();

        for (UserSkillBean userSkillBean : userSkillBeans) {
            if (ThemeService.getServiceBeanById(userSkillBean.getId()) != null) {
                result.add(userSkillBean);
            }
        }

        return result;


    }

}
