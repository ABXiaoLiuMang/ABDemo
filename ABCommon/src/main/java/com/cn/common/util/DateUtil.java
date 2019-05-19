package com.cn.common.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 方法描述: DateUtl，这里是补充方法
 **/

public class DateUtil {

    private DateUtil() {
    }

    /**
     * 将年月日的int转成date
     *
     * @param year  年
     * @param month 月 1-12
     * @param day   日
     *              注：月表示Calendar的月，比实际小1
     */
    @SuppressWarnings("unused")
    public static Date getDate(int year, int month, int day) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month - 1, day);
        return mCalendar.getTime();
    }

    /**
     * 求两个日期相差天数
     *
     * @param strat 起始日期，格式yyyy/MM/dd
     * @param end   终止日期，格式yyyy/MM/dd
     * @return 两个日期相差天数
     */
    @SuppressWarnings("unused")
    public static int getIntervalDays2(Date strat, Date end) {
        return (int) ((strat.getTime() - end.getTime()) / (3600 * 24 * 1000));
    }

    /**
     * desc: 获取当月的第几个月 如上个月 amount 传-1
     *
     * @author Jeff created on 2018/10/7 11:46
     */
    @SuppressWarnings("unused")
    public static Date getMonth(int amount) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, amount);
        return c.getTime();
    }

    /**
     * desc: 一个月的第几天
     *
     * @author Jeff created on 2018/10/7 18:52
     */
    @SuppressWarnings({"unused","WeakerAccess"})
    public static Date getMonthDay(int day) {
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        //设置为1号,当前日期既为本月第day天
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * desc: 获取当月的第一天
     */
    @SuppressWarnings("unused")
    public static Date getMonthFirst() {
        return getMonthDay(1);
    }

    /**
     * 日期转相应模板格式的字符串
     * @param date   Date
     * @param format 模板
     *               y 年
     *               Y 周年
     *               M 月
     *               w 一年中的第几周
     *               W 一个月中的第几周
     *               d 一年中的第几天
     *               D 一个月中的第几天
     *               F 所在周是当月的第几周
     *               E 星期的英文名称
     *               u 一星期中的第几天 星期一是1
     *               a 上午还是下午的标记 AM是上午 PM是下午
     *               H 一天中的小时 0 - 23
     *               k 一天中的小时 1 - 24
     *               K 一天中的小时 0 - 11
     *               h 一天中的小时 1 - 12
     *               m 一小时中的分钟
     *               s 一分钟的秒
     *               S 毫秒
     *               z 时区 GMT-08:00
     *               Z 时区号码 -0800
     *               X 时区 -08;-0800;-08:00
     * @return 时间模板
     * 例子 :
     * Date and Time Pattern	               Result
     * "yyyy.MM.dd G 'at' HH:mm:ss z"	2001.07.04 AD at 12:08:56 PDT
     * "EEE, MMM d, ''yy"	              Wed, Jul 4, '01
     * "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"	2001-07-04T12:08:56.235-07:00
     */
    @SuppressWarnings("unused")
    public static String toString(Date date, String format) {
        if (date == null) {
            return "";
        }
        if (TextUtils.isEmpty(format)) {
            return "";
        }
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(format);

            return dateFormat.format(date);
        }catch (Exception e){
        }

        return null;
    }

    /**
     * String模板格式 的时间转成Date实例
     * @param strDate 时间字符串
     * @param format 时间字符串的格式
     * @return Date实例
     */
    @SuppressWarnings("unused")
    public static Date toDate(String strDate, String format){
        if (TextUtils.isEmpty(strDate)) {
            return null;
        }

        if (TextUtils.isEmpty(format)){
            return null;
        }


        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(strDate);
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 获得今天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    @SuppressWarnings("unused")
    public static Date getToday() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.getTime();
    }

    /**
     * 获得昨天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    @SuppressWarnings("unused")
    public static Date getYesterday() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, -1);
        return mCalendar.getTime();
    }

    /**
     * 获得前天的日期(格式：yyyy-MM-dd)
     *
     * @return yyyy-MM-dd
     */
    @SuppressWarnings("unused")
    public static Date getBeforeYesterday() {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, -2);
        return mCalendar.getTime();
    }

    /**
     * 获得几天之前或者几天之后的日期
     *
     * @param diff 差值：正的往后推，负的往前推
     * @return date实例
     */
    @SuppressWarnings("unused")
    public static Date getOtherDay(int diff) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, diff);
        return mCalendar.getTime();
    }

    /**
     * 获得几天之前或者几天之后的日期
     *
     * @param diff 差值：正的往后推，负的往前推
     * @return date实例
     */
    @SuppressWarnings("unused")
    public static Date getOtherDate(int diff) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DATE, diff);
        return mCalendar.getTime();
    }


    /**
     * 取得给定日期加上一定天数后的日期对象.
     *
     * @param date   给定的日期对象
     * @param amount 需要添加的天数，如果是向前的天数，使用负数就可以.
     * @return Date 加上一定天数以后的Date对象.
     */
    @SuppressWarnings("unused")
    public static Date getCalcDate(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
    }

    /**
     * 获得一个计算十分秒之后的日期对象
     *
     * @param date date实例
     * @param hOffset 时偏移量，可为负
     * @param mOffset 分偏移量，可为负
     * @param sOffset 秒偏移量，可为负
     * @return date实例
     */
    @SuppressWarnings("unused")
    public static Date getCalcTime(Date date, int hOffset, int mOffset, int sOffset) {
        Calendar cal = Calendar.getInstance();
        if (date != null){
            cal.setTime(date);
        }
        cal.add(Calendar.HOUR_OF_DAY, hOffset);
        cal.add(Calendar.MINUTE, mOffset);
        cal.add(Calendar.SECOND, sOffset);
        return cal.getTime();
    }

    /**
     * 根据指定的年月日小时分秒，返回一个java.Util.Date对象。
     *
     * @param year      年
     * @param month     月 0-11
     * @param date      日
     * @param hourOfDay 小时 0-23
     * @param minute    分 0-59
     * @param second    秒 0-59
     * @return 一个Date对象
     */
    @SuppressWarnings("unused")
    public static Date getDate(int year, int month, int date, int hourOfDay,
                               int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date, hourOfDay, minute, second);
        return cal.getTime();
    }


    /**
     * 获得年月日数据
     *
     * @return arr[0]:年， arr[1]:月 0-11 , arr[2]日
     */
    @SuppressWarnings("unused")
    public static int[] getYearMonthAndDayFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int[] arr = new int[3];
        arr[0] = calendar.get(Calendar.YEAR);
        arr[1] = calendar.get(Calendar.MONTH);
        arr[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return arr;
    }

    /**
     * 获取http返回的请求头中的日期
     * @param dateString 请求头中的日期字符串
     * @return Date实例
     */
    @SuppressWarnings("unused")
    public static Date parseHttpHeaderDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        Date date;
        try {
            date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
        }
        return null;
    }

}
