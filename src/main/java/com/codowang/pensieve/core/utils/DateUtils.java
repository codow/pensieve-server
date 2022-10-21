package com.codowang.pensieve.core.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Elastic 系列之字符串工具类。
 *
 * @author lvhonglun
 */
public abstract class DateUtils {

    /**
     * 格式化年月日
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 格式化年月日时分秒
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取系统当前时间
     *
     * @return java.sql.Timestamp
     */
    public static Timestamp getNow() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取日期（yyyy-MM-dd）字符串
     *
     * @param time Long
     * @return String
     */
    public static String getString(Long time) {
        return getString(new Date(time));
    }

    /**
     * 获取日期（yyyy-MM-dd）字符串
     *
     * @param date java.util.Date
     * @return String
     */
    public static String getString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取日期（yyyy-MM-dd）字符串
     *
     * @param date java.util.Date
     * @return String
     */
    public static String getString(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return date.format(dateTimeFormatter);
    }

    /**
     * 获取日期（yyyy-MM-dd）字符串
     *
     * @param date java.util.Date
     * @return String
     */
    public static String getString(LocalDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        return date.format(dateTimeFormatter);
    }

    /**
     * 获取日期（yyyy-MM-dd）字符串
     *
     * @param timestamp java.util.Date
     * @return String
     */
    public static String getString(Timestamp timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(timestamp);
    }

    /**
     * 获取指定日期格式字符串
     *
     * @param date    java.util.Date
     * @param pattern
     * @return String
     */
    public static String getString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取指定日期格式字符串
     *
     * @param timestamp java.sql.Timestamp
     * @param pattern
     * @return String
     */
    public static String getString(Timestamp timestamp, String pattern) {
        if (timestamp == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(timestamp);
    }

    /**
     * 获取指定日期格式字符串
     *
     * @param time    java.lang.long
     * @param pattern
     * @return String
     */
    public static String getString(long time, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date(time));
    }

    public static String getString(String dateStr, String parsePattern, String formatPattern) throws ParseException {
        SimpleDateFormat sdfParse = new SimpleDateFormat(parsePattern);
        return getString(sdfParse.parse(dateStr), formatPattern);
    }

    /**
     * 获取指定字符串的
     *
     * @param string
     * @return java.util.Date
     * @throws ParseException 日期字符串解析异常
     */
    public static Date getDate(String string) throws ParseException {
        return getDate(string, DATETIME_FORMAT);
    }

    /**
     * 获取指定字符串的
     *
     * @param string
     * @param datePattern
     * @return java.util.Date
     * @throws ParseException 日期字符串解析异常
     */
    public static Date getDate(String string, String datePattern) throws ParseException {
        if (StringUtils.isBlank(string)) {
            return null;
        }
        if (string.matches("\\d+")) {
            return new Date(Long.parseLong(string));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        return simpleDateFormat.parse(string);
    }

    public static LocalDate getLocalDate(Object obj) {
        return getLocalDate(obj, null);
    }

    public static LocalDate getLocalDate(Object obj, LocalDate defaultVal) {
        if (obj == null) {
            return defaultVal;
        }
        if (obj instanceof LocalDate) {
            return (LocalDate) obj;
        }
        String str = obj.toString();
        if (StringUtils.isBlank(str)) {
            return defaultVal;
        }
        return LocalDate.parse(str, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static LocalDateTime getLocalDateTime(Object obj) {
        return getLocalDateTime(obj, null);
    }

    public static LocalDateTime getLocalDateTime(Object obj, LocalDateTime defaultVal) {
        if (obj == null) {
            return defaultVal;
        }
        if (obj instanceof LocalDateTime) {
            return (LocalDateTime) obj;
        }
        String str = obj.toString();
        if (StringUtils.isBlank(str)) {
            return defaultVal;
        }
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(DATETIME_FORMAT));
    }

    /**
     * 计算时间的间隔，根据类型来判断计算哪个字段的间隔
     *
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @param type      类型，year|month|day|hour|minute|second
     * @return 时间间隔
     */
    public static long diff(Date startDate, Date endDate, String type) {
        switch (type) {
            case "year":
                return diffYear(startDate, endDate);
            case "month":
                return diffMonth(startDate, endDate);
            case "day":
                return diffDay(startDate, endDate);
            case "hour":
                return diffHour(startDate, endDate);
            case "minute":
                return diffMinute(startDate, endDate);
            case "second":
                return diffSecond(startDate, endDate);
            default:
                return diffMillisecond(startDate, endDate);
        }
    }

    /**
     * 计算两个时间相差的年份
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 年份差值
     */
    public static long diffYear(Date startDate, Date endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        return endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
    }

    /**
     * 计算两个时间相差的月份
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 月份差值
     */
    public static long diffMonth(Date startDate, Date endDate) {
        long diffYear = diffYear(startDate, endDate);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        // 相差年份
        return diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    }

    /**
     * 计算两个时间相差的天数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 天数差值
     */
    public static long diffDay(Date startDate, Date endDate) {
        long baseTime = 24 * 60 * 60 * 1000L;
        return diffTime(startDate, endDate, baseTime);
    }

    /**
     * 计算两个时间相差的小时数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 小时差值
     */
    public static long diffHour(Date startDate, Date endDate) {
        long baseTime = 60 * 60 * 1000L;
        return diffTime(startDate, endDate, baseTime);
    }

    /**
     * 计算两个时间相差的分钟数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 分钟差值
     */
    public static long diffMinute(Date startDate, Date endDate) {
        long baseTime = 60 * 1000L;
        return diffTime(startDate, endDate, baseTime);
    }

    /**
     * 计算两个时间相差的秒数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 秒数差值
     */
    public static long diffSecond(Date startDate, Date endDate) {
        long baseTime = 1000L;
        return diffTime(startDate, endDate, baseTime);
    }

    /**
     * 计算两个时间相差的毫秒数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 毫秒差值
     */
    public static long diffMillisecond(Date startDate, Date endDate) {
        long baseTime = 1L;
        return diffTime(startDate, endDate, baseTime);
    }

    /**
     * 计算两个时间相差的时间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param baseTime  单位的时长
     * @return 时间差值
     */
    public static long diffTime(Date startDate, Date endDate, long baseTime) {
        return (endDate.getTime() - startDate.getTime()) / baseTime;
    }

    /**
     * 根据类型获取开始日期
     *
     * @param dateStr       日期字符串
     * @param field         支持日历类中YEAR|MONTH|DAY_OF_WEEK
     * @param parsePattern  解析格式
     * @param formatPattern 转换格式
     * @return 获取到的开始日期字符串
     * @throws ParseException 日期字符串解析异常
     */
    public static String getStartDate(String dateStr, int field, String parsePattern, String formatPattern) throws ParseException {
        SimpleDateFormat sourceSdf = new SimpleDateFormat(parsePattern);
        Date sourceDate = sourceSdf.parse(dateStr);
        Date targetDate = getStartDate(sourceDate, field);

        return getString(targetDate, formatPattern);
    }

    /**
     * 根据类型获取开始日期
     *
     * @param date  日期
     * @param field 支持日历类中YEAR|MONTH|DAY_OF_WEEK
     * @return 获取到的开始日期
     */
    public static Date getStartDate(Date date, int field) {
        Calendar calendar = Calendar.getInstance();

        switch (field) {
            case Calendar.YEAR:
                calendar.set(Calendar.YEAR, getValue(date, Calendar.YEAR));
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DATE, 1);
                break;
            case Calendar.DAY_OF_WEEK:
                calendar.add(Calendar.WEEK_OF_MONTH, 0);
                calendar.set(Calendar.DAY_OF_WEEK, 2);
                break;
            default:
                calendar.set(Calendar.YEAR, getValue(date, Calendar.YEAR));
                calendar.set(Calendar.MONTH, getValue(date, Calendar.MONTH));
                calendar.set(Calendar.DATE, 1);
                break;
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 根据年、月获取开始日期
     *
     * @param year  年
     * @param month 月
     * @return 获取到的开始日期
     */
    public static Date getStartDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 根据年、月获取开始日期
     *
     * @param year          年
     * @param month         月
     * @param formatPattern 转换格式
     * @return 获取到的开始日期字符串
     */
    public static String getStartDate(int year, int month, String formatPattern) {
        return getString(getStartDate(year, month), formatPattern);
    }

    /**
     * 根据类型获取结束日期
     *
     * @param dateStr       日期字符串
     * @param field         支持日历类中YEAR|MONTH|DAY_OF_WEEK
     * @param parsePattern  解析格式
     * @param formatPattern 转换格式
     * @return 获取到的结束日期字符串
     * @throws ParseException 日期字符串解析异常
     */
    public static String getEndDate(String dateStr, int field, String parsePattern, String formatPattern) throws ParseException {
        SimpleDateFormat sourceSdf = new SimpleDateFormat(parsePattern);
        Date sourceDate = sourceSdf.parse(dateStr);
        Date targetDate = getEndDate(sourceDate, field);

        return getString(targetDate, formatPattern);
    }

    /**
     * 根据类型获取结束日期
     *
     * @param date  日期
     * @param field 支持日历类中YEAR|MONTH|DAY_OF_WEEK
     * @return 获取到的结束日期
     */
    public static Date getEndDate(Date date, int field) {
        Calendar calendar = Calendar.getInstance();

        switch (field) {
            case Calendar.YEAR:
                calendar.set(Calendar.YEAR, getValue(date, Calendar.YEAR));
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case Calendar.DAY_OF_WEEK:
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                break;
            default:
                calendar.set(Calendar.YEAR, getValue(date, Calendar.YEAR));
                calendar.set(Calendar.MONTH, getValue(date, Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
        }
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }

    /**
     * 根据类型获取结束日期
     *
     * @param year  年
     * @param month 月
     * @return 获取到的结束日期
     */
    public static Date getEndDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }

    /**
     * 根据类型获取结束日期
     *
     * @param year          年
     * @param month         月
     * @param formatPattern 转换格式
     * @return 获取到的结束日期字符串
     */
    public static String getEndDate(int year, int month, String formatPattern) {
        return getString(getEndDate(year, month), formatPattern);
    }

    /**
     * 根据单位和类型获取日期之后单位的日期
     *
     * @param dateStr       日期字符串
     * @param unit          单位
     * @param field         支持日历类中类型
     * @param parsePattern  解析格式
     * @param formatPattern 转换格式
     * @return 获取到的之后单位的日期字符串
     * @throws ParseException 日期字符串解析异常
     */
    public static String getDateAfter(String dateStr, int unit, int field, String parsePattern, String formatPattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(parsePattern);
        Date date = sdf.parse(dateStr);
        return getString(getDateAfter(date, unit, field), formatPattern);
    }

    /**
     * 根据单位和类型获取日期之后单位的日期
     *
     * @param date  日期
     * @param unit  单位
     * @param field 支持日历类中类型
     * @return 获取到的之后单位的日期字符串
     */
    public static Date getDateAfter(Date date, int unit, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, unit);
        return calendar.getTime();
    }

    /**
     * 获取指定类型的值
     *
     * @param dateStr      日期字符串
     * @param field        持日历类中类型
     * @param parsePattern 解析格式
     * @return 获取到的对应类型的值
     * @throws ParseException 日期字符串解析异常
     */
    public static int getValue(String dateStr, int field, String parsePattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(parsePattern);
        Date date = sdf.parse(dateStr);
        return getValue(date, field);
    }

    /**
     * 获取指定类型的值
     *
     * @param date  日期
     * @param field 持日历类中类型
     * @return 获取到的对应类型的值
     */
    public static int getValue(Date date, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(field);
    }

    /**
     * 比较两个日期的大小
     *
     * @param dateStr      被比较的日期字符串
     * @param compDateStr  比较的日期字符串
     * @param parsePattern 解析格式
     * @return >:1   =:0  <:-1
     * @throws ParseException 日期字符串解析异常
     */
    public static int compareTo(String dateStr, String compDateStr, String parsePattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(parsePattern);
        Date date = sdf.parse(dateStr);
        Date compDate = sdf.parse(compDateStr);
        return compareTo(date, compDate);
    }

    /**
     * 比较两个日期的大小
     *
     * @param date     被比较的日期
     * @param compDate 比较的日期
     * @return >:1   =:0  <:-1
     */
    public static int compareTo(Date date, Date compDate) {
        long dateTime = date.getTime();
        long compDateTime = compDate.getTime();

        if (dateTime - compDateTime > 0) {
            return 1;
        } else if (dateTime == compDateTime) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 根据日期获取日期属于周几
     *
     * @param dateStr      日期字符串
     * @param parsePattern 解析格式
     * @return 获取的周几
     * @throws ParseException 日期字符串解析异常
     */
    public static String getDayOfWeek(String dateStr, String parsePattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(parsePattern);
        Date date = sdf.parse(dateStr);
        return getDayOfWeek(date);
    }

    /**
     * 根据日期获取日期属于周几
     *
     * @param date 日期
     * @return 获取的周几
     */
    public static String getDayOfWeek(Date date) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     * 给日期加一个指定类型的值，返回新的日期
     *
     * @param date 日期对象
     * @param value 加的值，可以为负数
     * @param type 类型，使用Calendar.YEAR等常量
     * @return 新的日期对象
     */
    public static Date add(Date date, int value, int type) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, value);
        return calendar.getTime();
    }

    public static void main(String[] args) throws Exception {
        // 测试时间差计算
//        Date startDate = getDate("2000-1-10 22:12:50");
//        Date endDate = getDate("1999-10-10 22:13:12");
//        System.out.println("查询年数：" + diff(startDate, endDate, "year"));
//        System.out.println("查询月数：" + diff(startDate, endDate, "month"));
//        System.out.println("查询天数：" + diff(startDate, endDate, "day"));
//        System.out.println("查询小时：" + diff(startDate, endDate, "hour"));
//        System.out.println("查询分钟：" + diff(startDate, endDate, "minute"));
//        System.out.println("查询秒钟：" + diff(startDate, endDate, "second"));
//        System.out.println("查询毫秒：" + diff(startDate, endDate, "millisecond"));

        System.out.println(getLocalDateTime("2022-01-01 22:00:00"));
    }
}
