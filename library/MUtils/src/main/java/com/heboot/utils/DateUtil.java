package com.heboot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by heboot on 2018/2/7.
 */

public class DateUtil {
    /**
     * 英文简写如：2010
     */
    public static String FORMAT_Y = "yyyy";

    /**
     * 英文简写如：12:01
     */
    public static String FORMAT_HM = "HH:mm";

    /**
     * 英文简写如：1-12 12:01
     */
    public static String FORMAT_MDHM = "MM-dd HH:mm";

    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_YMD = "yyyy-MM-dd";

    public static String FORMAT_YMD_POINT = "yyyy.MM.dd";

    /**
     * 英文全称  如：2010-12-01 23:15
     */
    public static String FORMAT_YMDHM = "yyyy-MM-dd HH:mm";

    /**
     * 英文全称  如：2010-12-01 23:15:06
     */
    public static String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL_SN = "yyyyMMddHHmmssS";

    /**
     * 中文简写  如：2010年12月01日
     */
    public static String FORMAT_YMD_CN = "yyyy年MM月dd日";

    /**
     * 中文简写  如：2010年12月01日
     */
    public static String FORMAT_MD_CN = "MM月dd日";

    /**
     * 中文简写  如：2010年12月01日  12时
     */
    public static String FORMAT_YMDH_CN = "yyyy年MM月dd日 HH时";

    /**
     * 中文简写  如：2010年12月01日  12时12分
     */
    public static String FORMAT_YMDHM_CN = "yyyy年MM月dd日 HH时mm分";

    public static String FORMAT_HHMM_CN = "HH时mm分";

    /**
     * 中文简写  如：2010年12月01日
     */
    public static String FORMAT_MD_CN_SERVICE = "MM月dd日 HH:mm";

    /**
     * 中文全称  如：2010年12月01日  23时15分06秒
     */
    public static String FORMAT_YMDHMS_CN = "yyyy年MM月dd日  HH时mm分ss秒";

    public static String UPLOAD = "yyyyMMddHHmmss";

    /**
     * 精确到毫秒的完整中文时间
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";

    public static Calendar calendar = null;

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static Date str2Date(String str) {
        return str2Date(str, null);
    }


    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static Calendar str2Calendar(String str) {
        return str2Calendar(str, null);
    }


    public static Calendar str2Calendar(String str, String format) {

        Date date = str2Date(str, format);
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c;
    }


    public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
        return date2Str(c, null);
    }


    public static String date2Str(Calendar c, String format) {
        if (c == null) {
            return null;
        }
        return date2Str(c.getTime(), format);
    }


    public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
        return date2Str(d, null);
    }


    public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }


    public static String getCurDateStr() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" +
                c.get(Calendar.DAY_OF_MONTH) + "-" +
                c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) +
                ":" + c.get(Calendar.SECOND);
    }


    /**
     * 获得当前日期的字符串格式
     *
     * @param format 格式化的类型
     * @return 返回格式化之后的事件
     */
    public static String getCurDateStr(String format) {
        Calendar c = Calendar.getInstance();
        return date2Str(c, format);

    }


    /**
     * @param time 当前的时间
     * @return 格式到秒
     */
    //
    public static String getMillon(long time) {

        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);

    }


    /**
     * @param time 当前的时间
     * @return 当前的天
     */
    public static String getDay(long time) {

        return new SimpleDateFormat("yyyy-MM-dd").format(time);

    }


    /**
     * @param time 时间
     * @return 返回一个毫秒
     */
    // 格式到毫秒
    public static String getSMillon(long time) {

        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);

    }


    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return 增加数个整月
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();

    }


    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return 增加之后的天数
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();

    }


    /**
     * 获取距现在某一小时的时刻
     *
     * @param format 格式化时间的格式
     * @param h      距现在的小时 例如：h=-1为上一个小时，h=1为下一个小时
     * @return 获取距现在某一小时的时刻
     */
    public static String getNextHour(String format, int h) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        date.setTime(date.getTime() + h * 60 * 60 * 1000);
        return sdf.format(date);

    }


    /**
     * 获取时间戳
     *
     * @return 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());

    }


    /**
     * 功能描述：返回月
     *
     * @param date Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }


    /**
     * 功能描述：返回日
     *
     * @param date Date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 功能描述：返回小
     *
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }


    /**
     * 获得默认的 date pattern
     *
     * @return 默认的格式
     */
    public static String getDatePattern() {

        return FORMAT_YMDHMS;
    }


    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        calendar = Calendar.getInstance();

        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }


    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return 提取字符串的日期
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());

    }


    /**
     * 功能描述：返回毫
     *
     * @param date 日期
     * @return 返回毫
     */
    public static long getMillis(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }


    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return 按默认格式的字符串距离今天的天数
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;

    }

    public static int countDays(long date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;

    }


    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return 提取字符串日期
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return 按用户格式字符串距离今天的天数
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;

    }

    //===============

    //判断某个时间是否是昨天：
    public static int isDay(long time) {
        //// -2:前天             -1：昨天            0：今天             1：明天             2：后天
//        if (time >= getDayMinTimeMillis(yesterday().getTime()) && time <= getDayMaxTimeMillis
//                (yesterday().getTime())) {
//            return 1;//昨天
//        } else if (time >= getDayMinTimeMillis(tomorrow().getTime()) && time <= getDayMaxTimeMillis
//                (tomorrow().getTime())) {
//            return 2;//明天
//        } else if (time >= getDayMinTimeMillis(System.currentTimeMillis()) && time <= getDayMaxTimeMillis
//                (System.currentTimeMillis())) {
//            return 0;//今天
//        }
//        return -1;
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis() + offSet) / 86400000;
        long start = (time + offSet) / 86400000;
        return (int) (start - today);
    }

    public static String getForDay(long time) {
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis() + offSet) / 86400000;
        long start = (time + offSet) / 86400000;
        switch ((int) (start - today)) {
            case 0:
                return "今天";
            case 1:
                return "明天";
            case 2:
                return "后天";
            case -1:
                return "昨天";
            case -2:
                return "前天";
        }
        return null;
    }


    /**
     * 根据日期获取年龄
     *
     * @param brithday
     * @return
     * @throws ParseException
     * @throws Exception
     */
    public static int getCurrentAgeByBirthdate(Date brithday) {
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatDate = new SimpleDateFormat(FORMAT_YMD_CN);
            String currentTime = formatDate.format(calendar.getTime());
            Date today = formatDate.parse(currentTime);
            Date brithDay = brithday;

            return today.getYear() - brithDay.getYear();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 根据时长获取小时
     *
     * @param time
     * @return
     */
    public static String getHoursByDuration(int time) {
        if (time % 60.0 == 0) {
            return time / 60 + "";
        }
        return time / 60.0 + "";
    }


    public static int getServiceDurationDays(long time) {
        return isDay(time);
    }

    public static String getUserLastUpdateTime(long time) {

        return getRecommendUserInterval(time * 1000l);

//        return (System.currentTimeMillis() / 1000 - time) / 60 + "分钟前";
    }

    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getRecommendUserInterval(long time) {
        Date createAt = new Date(time);
        // 定义最终返回的结果字符串。
        String interval = null;

        long millisecond = new Date().getTime() - createAt.getTime();

        long second = millisecond / 1000;

        if (second <= 0) {
            second = 0;
        }

//*--------------微博体（标准）
        if (second == 0) {
            interval = "刚刚";
        } else if (second < 30) {
            interval = "刚刚";
        } else if (second >= 30 && second < 60) {
            interval = "刚刚";
        } else if (second >= 60 && second < 60 * 60) {//大于1分钟 小于1小时
            long minute = second / 60;
            interval = minute + "分钟前";
        } else if (isDay(createAt.getTime()) == 0) {//大于1小时 小于24小时//second >= 60 * 60 && second < 60 * 60 * 24
            long hour = (second / 60) / 60;
//            if (hour <= 3) {
            interval = hour + "小时前";
//            } else {
//            interval = "今天" + format(createAt, "HH:mm");
//            }
        } else if (isDay(createAt.getTime()) == -1) {//大于1D 小于2D second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2
            interval = "昨天";
        } else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 7) {//大于2D小时 小于 7天
            interval = format(createAt, "MM月dd日");
        } else if (second <= 60 * 60 * 24 * 365 && second >= 60 * 60 * 24 * 7) {//大于7天小于365天
            interval = format(createAt, "MM月dd日");
        } else if (second >= 60 * 60 * 24 * 365) {//大于365天
            interval = format(createAt, "MM月dd日");
        } else {
            interval = format(createAt, "MM月dd日");

        }
        return interval;
    }


    public static String getMessageInterval(long time) {
        Date createAt = new Date(time);
        // 定义最终返回的结果字符串。
        String interval = null;

        long millisecond = new Date().getTime() - createAt.getTime();

        long second = millisecond / 1000;

        if (second <= 0) {
            second = 0;
        }

//*--------------微博体（标准）
        if (second == 0) {
            interval = "刚刚";
        } else if (second < 30) {
            interval = "刚刚";
        } else if (second >= 30 && second < 60) {
            interval = "刚刚";
        } else if (second >= 60 && second < 60 * 60) {//大于1分钟 小于1小时
            long minute = second / 60;
            interval = minute + "分钟前";
        } else if (isDay(createAt.getTime()) == 0) {//大于1小时 小于24小时//second >= 60 * 60 && second < 60 * 60 * 24
//            long hour = (second / 60) / 60;
//            if (hour <= 3) {
//                interval = hour + "小时前";
//            } else {
            interval = "今天" + format(createAt, "HH:mm");
//            }
        } else if (isDay(createAt.getTime()) == -1) {//大于1D 小于2D second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2
            interval = "昨天" + format(createAt, "HH:mm");
        } else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 7) {//大于2D小时 小于 7天
            interval = format(createAt, "MM月dd日");
        } else if (second <= 60 * 60 * 24 * 365 && second >= 60 * 60 * 24 * 7) {//大于7天小于365天
            interval = format(createAt, "MM月dd日");
        } else if (second >= 60 * 60 * 24 * 365) {//大于365天
            interval = format(createAt, "MM月dd日");
        } else {
            interval = format(createAt, "MM月dd日");

        }
        return interval;
    }


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

        String duStr = time + "小时";

        String sss = DateUtil.getForDay(startTime * 1000);

        if (sss == null || sss.equals("")) {
            return day + " " + start + "-" + end + "  " + duStr;
        }

        return sss + " " + start + " -" + end + "  " + duStr;

    }

}
