package com.acooly.core.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 简单 Date 相关静态工具
 *
 * @author zhangpu
 */
public class Dates {

    public static final String CHINESE_DATE_FORMAT_LINE = "yyyy-MM-dd";
    public static final String CHINESE_DATETIME_FORMAT_LINE = "yyyy-MM-dd HH:mm:ss";
    public static final String CHINESE_DATE_FORMAT_SLASH = "yyyy/MM/dd";
    public static final String CHINESE_DATETIME_FORMAT_SLASH = "yyyy/MM/dd HH:mm:ss";
    public static final String DATETIME_NOT_SEPARATOR = "yyyyMMddHHmmssSSS";
    /**
     * 中国周一是一周的第一天
     */
    public static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY;
    private static final String DEFAULT_DATE_FORMAT = CHINESE_DATETIME_FORMAT_LINE;

    /**
     * 根据模式长度倒叙排列
     */
    private static final List<String> PATTERNS =
            Lists.newArrayList(
                    new String[]{
                            CHINESE_DATETIME_FORMAT_LINE,
                            CHINESE_DATETIME_FORMAT_SLASH,
                            DATETIME_NOT_SEPARATOR,
                            CHINESE_DATE_FORMAT_LINE,
                            CHINESE_DATE_FORMAT_SLASH
                    });

    public static void registerPattern(String pattern) {
        PATTERNS.add(pattern);
        sort();
    }

    public static void unRegisterPattern(String pattern) {
        PATTERNS.remove(pattern);
        sort();
    }

    private static void sort() {
        Collections.sort(
                PATTERNS,
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        if (o1 == null || o2 == null) {
                            return 0;
                        }
                        if (o1.length() > o2.length()) {
                            return -1;
                        } else if (o1.length() < o2.length()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
    }

    /**
     * 获取日期部分
     * <p>
     * 例如：2019-11-11 12:12:12 --getDate()--> 2019-11-11 00:00:00
     *
     * @param date
     * @return {@link Date}
     */
    public static Date getDate(Date date) {
        return parse(format(date, Dates.CHINESE_DATE_FORMAT_LINE));
    }

    /**
     * 格式化输出
     *
     * @param date
     * @param pattern
     * @return 指定格式的日期字符串
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String format(Date date) {
        return format(date, DEFAULT_DATE_FORMAT);
    }

    public static String format(String pattern) {
        return format(new Date(), pattern);
    }

    public static Date parse(String dateString, String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        try {
            return sdf.parse(dateString);
        } catch (Exception e) {
            throw new RuntimeException(
                    "parse String[" + dateString + "] to Date faulure with pattern[" + pattern + "].");
        }
    }

    public static Date parse(String dateString, String[] patterns) {
        for (String pattern : patterns) {
            if (StringUtils.isBlank(pattern)) {
                continue;
            }
            SimpleDateFormat sdf = getSimpleDateFormat(pattern);
            try {
                return sdf.parse(dateString);
            } catch (Exception e) {
                // ignore exception
            }
        }
        throw new UnsupportedOperationException(
                "Parse String["
                        + dateString
                        + "] to Date faulure with PATTERNS["
                        + Arrays.toString(patterns)
                        + "]");
    }

    public static Date parse(String dateString) {
        return parse(dateString, PATTERNS.toArray(new String[0]));
    }

    public static Date addYear(Date date, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    public static Date addMonth(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    public static Date addWeek(Date date, int weeks) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.WEEK_OF_YEAR, weeks);
        return c.getTime();
    }

    public static Date addDay(Date date) {
        return addDay(date, 1);
    }

    public static Date addDay(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }

    public static Date addWorkDay(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int totalDays = 0;
        while (true) {
            c.add(Calendar.DAY_OF_YEAR, 1);
            int d = c.get(Calendar.DAY_OF_WEEK) - 1;
            // 0代表是周末 6代表是周六
            if (d != 0 && d != 6) {
                totalDays++;
            }
            if (totalDays >= days) {
                break;
            }
        }
        return c.getTime();
    }

    /**
     * 减去一天
     *
     * @param date
     * @return 日期
     */
    @Deprecated
    public static Date minusDay(Date date) {
        //24 * 60 * 60 * 1000= 86400 000
        long oneDayMillisecond = 86400000;
        return addDate(date, -oneDayMillisecond);
    }

    /**
     * 减法运算
     */
    public static Date subDate(Date date, long size, TimeUnit timeUnit) {
        return opt(date, -size, timeUnit);
    }

    /**
     * 加法运算
     */
    public static Date addDate(Date date, long size, TimeUnit timeUnit) {
        return opt(date, size, timeUnit);
    }

    public static Date addDate(Date date, long millisecond) {
        return addDate(date, millisecond, TimeUnit.MILLISECONDS);
    }

    /**
     * 加减运算
     *
     * @param date     计算的日期时间
     * @param size     加减运算的值（正数为加，负数为减）
     * @param timeUnit 单位
     * @return 计算后的新日期
     */
    public static Date opt(Date date, long size, TimeUnit timeUnit) {
        return new Date(date.getTime() + timeUnit.toMillis(size));
    }

    /**
     * 两个时间减法
     *
     * @param left  左边的时间
     * @param right 右边的时间
     * @param type  时间类型，参考：Calendar.xxx
     * @return 指定时间类型的差值
     */
    public static long sub(Date left, Date right, int type) {
        long subms = left.getTime() - right.getTime();
        if (subms < 0) {
            throw new RuntimeException("left日期小于right日期");
        }

        switch (type) {
            case Calendar.SECOND:
                return subms % 1000 == 0 ? subms / 1000 : subms / 1000 + 1;
            case Calendar.MINUTE:
                return subms % 1000 / 60 == 0 ? subms / 1000 / 60 : subms / 1000 / 60 + 1;
            case Calendar.HOUR:
                return subms % 1000 / 60 / 60 == 0 ? subms / 1000 / 60 / 60 : subms / 1000 / 60 / 60 + 1;
            case Calendar.DAY_OF_MONTH:
                return subms % 1000 / 60 / 60 / 24 == 0
                        ? subms / 1000 / 60 / 60 / 24
                        : subms / 1000 / 60 / 60 / 24 + 1;
            case Calendar.YEAR:
                return subms % 1000 / 60 / 60 / 24 / 365 == 0
                        ? subms / 1000 / 60 / 60 / 24 / 365
                        : subms / 1000 / 60 / 60 / 24 / 365 + 1;
            default:
                return subms;
        }
    }

    /**
     * 取得日期：年
     *
     * @param date 日期
     * @return 年
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        return year;
    }

    /**
     * 取得日期：月份
     *
     * @param date 日期
     * @return 月份
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        return month + 1;
    }

    /**
     * 取得日期：号数
     *
     * @param date 日期
     * @return 号数（月份内，1-31）
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int da = c.get(Calendar.DAY_OF_MONTH);
        return da;
    }

    /**
     * 取得当天日期是周几
     *
     * @param date 日期
     * @return 星期几（周日：0，周一：1，周二：2，周三：3，周四：4，周五：5，周六：6）
     */
    public static int getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int weekOfYear = c.get(Calendar.DAY_OF_WEEK);
        return weekOfYear - 1;
    }

    /**
     * 获取一年的第几周
     *
     * @param date 日期
     * @return 年度星期数
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int weekOfYear = c.get(Calendar.WEEK_OF_YEAR);
        return weekOfYear;
    }

    /**
     * 指定日期同周一的日期
     *
     * @param date 日期
     * @return 同周一的日期
     */
    public static Date getMondayOfWeek(Date date) {
        Calendar monday = Calendar.getInstance();
        monday.setTime(date);
        monday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
        monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return monday.getTime();
    }

    /**
     * 同周日日期
     *
     * @param date 指定日期
     * @return 同周日日期
     */
    public static Date getSundayOfWeek(Date date) {
        Calendar sunday = Calendar.getInstance();
        sunday.setTime(date);
        sunday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
        sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return sunday.getTime();
    }

    /**
     * 取得月的剩余天数
     *
     * @param date 日期
     * @return 剩余天数
     */
    public static int getRemainDayOfMonth(Date date) {
        int dayOfMonth = getDayOfMonth(date);
        int day = getPassDayOfMonth(date);
        return dayOfMonth - day;
    }

    /**
     * 取得月已经过的天数
     *
     * @param date 日期
     * @return 已经过的天数
     */
    public static int getPassDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得月天数
     *
     * @param date 日期
     * @return 当月的天数
     */
    public static int getDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得月第一天
     *
     * @param date 日期
     * @return 月第一天的日期
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得月最后一天
     *
     * @param date 日期
     * @return 当月最后一天的日期
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得季度第一天
     *
     * @param date 日期
     * @return 本季度第一天的日期
     */
    public static Date getFirstDateOfSeason(Date date) {
        return getFirstDateOfMonth(getQuarterDate(date)[0]);
    }

    /**
     * 取得季度最后一天
     *
     * @param date 日期
     * @return 本季度最后一天的日期
     */
    public static Date getLastDateOfSeason(Date date) {
        return getLastDateOfMonth(getQuarterDate(date)[2]);
    }

    /**
     * 取得季度天数
     *
     * @param date 日期
     * @return 季度天数
     */
    public static int getDayOfSeason(Date date) {
        int day = 0;
        Date[] seasonDates = getQuarterDate(date);
        for (Date date2 : seasonDates) {
            day += getDayOfMonth(date2);
        }
        return day;
    }

    /**
     * 取得季度剩余天数
     *
     * @param date 日期
     * @return 当前季度剩余天数
     */
    public static int getRemainDayOfSeason(Date date) {
        return getDayOfSeason(date) - getPassDayOfSeason(date);
    }

    /**
     * 取得季度已过天数
     *
     * @param date 日期
     * @return 季度已过天数
     */
    public static int getPassDayOfSeason(Date date) {
        int day = 0;

        Date[] seasonDates = getQuarterDate(date);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);

        if (month == Calendar.JANUARY
                || month == Calendar.APRIL
                || month == Calendar.JULY
                || month == Calendar.OCTOBER) { // 季度第一个月
            day = getPassDayOfMonth(seasonDates[0]);
        } else if (month == Calendar.FEBRUARY
                || month == Calendar.MAY
                || month == Calendar.AUGUST
                || month == Calendar.NOVEMBER) { // 季度第二个月
            day = getDayOfMonth(seasonDates[0]) + getPassDayOfMonth(seasonDates[1]);
        } else if (month == Calendar.MARCH
                || month == Calendar.JUNE
                || month == Calendar.SEPTEMBER
                || month == Calendar.DECEMBER) { // 季度第三个月
            day =
                    getDayOfMonth(seasonDates[0])
                            + getDayOfMonth(seasonDates[1])
                            + getPassDayOfMonth(seasonDates[2]);
        }
        return day;
    }

    /**
     * 取得季度月
     *
     * @param date 日期
     * @return 季度月
     */
    public static Date[] getQuarterDate(Date date) {
        Date[] quarters = new Date[3];

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int nq = getQuarter(date);
        if (nq == 1) { // 第一季度
            c.set(Calendar.MONTH, Calendar.JANUARY);
            quarters[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.FEBRUARY);
            quarters[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MARCH);
            quarters[2] = c.getTime();
        } else if (nq == 2) { // 第二季度
            c.set(Calendar.MONTH, Calendar.APRIL);
            quarters[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MAY);
            quarters[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.JUNE);
            quarters[2] = c.getTime();
        } else if (nq == 3) { // 第三季度
            c.set(Calendar.MONTH, Calendar.JULY);
            quarters[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.AUGUST);
            quarters[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);
            quarters[2] = c.getTime();
        } else if (nq == 4) { // 第四季度
            c.set(Calendar.MONTH, Calendar.OCTOBER);
            quarters[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.NOVEMBER);
            quarters[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.DECEMBER);
            quarters[2] = c.getTime();
        }
        return quarters;
    }

    /**
     * 获取指定日期在一年的第几季度
     *
     * @param date 日期
     * @return 第几季度
     */
    public static int getQuarter(Date date) {
        int quarter = 0;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                quarter = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                quarter = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                quarter = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                quarter = 4;
                break;
            default:
                break;
        }
        return quarter;
    }

    private static SimpleDateFormat getSimpleDateFormat(String defaultFormat) {
        if (Strings.isBlank(defaultFormat)) {
            defaultFormat = DEFAULT_DATE_FORMAT;
        }
        return new SimpleDateFormat(defaultFormat);
    }

    /**
     * 收集起始时间到结束时间之间所有的时间并以字符串集合方式返回
     *
     * @param timeStart Date
     * @param timeEnd   Date
     * @return 日期集合
     */
    public static List<String> collectLocalDates(Date timeStart, Date timeEnd) {
        return collectLocalDates(
                format(timeStart, CHINESE_DATE_FORMAT_LINE), format(timeEnd, CHINESE_DATE_FORMAT_LINE));
    }

    /**
     * 收集起始时间到结束时间之间所有的时间并以字符串集合方式返回
     *
     * @param timeStart 格式："2017-07-11"
     * @param timeEnd   格式："2017-07-11"
     * @return 日期集合
     */
    public static List<String> collectLocalDates(String timeStart, String timeEnd) {
        return collectLocalDates(LocalDate.parse(timeStart), LocalDate.parse(timeEnd));
    }

    /**
     * 收集起始时间到结束时间之间所有的时间并以字符串集合方式返回
     *
     * @param start 开始时间(包括当天)
     * @param end   结束时间(包括当天)
     * @return 日期集合
     */
    public static List<String> collectLocalDates(LocalDate start, LocalDate end) {
        return Stream.iterate(start, localDate -> localDate.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .map(LocalDate::toString)
                .collect(Collectors.toList());
    }

    /**
     * 判断是否存日期
     * （time部分为0）
     *
     * @param date 日期
     * @return true：只是日期yyyy-MM-dd/false:不是，含有效时间部分
     */
    public static boolean isDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.SECOND) == 0;
    }


    /**
     * 判断日期时间段是否重叠
     *
     * @param period  日期时间段
     * @param periods 日期时间段集合
     * @return true:重叠/false:不重叠
     */
    public static boolean isOverlap(Pair<Date, Date> period, List<Pair<Date, Date>> periods) {
        for (Pair<Date, Date> p : periods) {
            if (isOverlap(period, p)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOverlap(Pair<Date, Date> period1, Pair<Date, Date> period2) {
        return !(lt(period1.getRight(), period2.getLeft()) || gt(period1.getLeft(), period2.getRight()));
    }

    /**
     * 判断日期是否在start（含）和end（含）之间
     * <p>
     * 包括start和end
     *
     * @param date  日期
     * @param start 开始日期
     * @param end   结束日期
     * @return true: date在start和end之间，false: date不在start和end之间
     */
    public static boolean between(Date date, Date start, Date end) {
        return gte(date, start) && lte(date, end);
    }


    /**
     * 日期时间大于比较
     * <p>
     * left > right: true
     *
     * @param left 左边日期
     * @param right 右边日期
     * @return true: left > right，false: left <= right
     */
    public static boolean gt(Date left, Date right) {
        return left.compareTo(right) > 0;
    }

    /**
     * 日期时间大于等于比较
     * <p>
     * left >= right: true
     *
     * @param left 日期时间
     * @param right 日期时间
     * @return true: left >= right, false: left < right
     */
    public static boolean gte(Date left, Date right) {
        return left.compareTo(right) >= 0;
    }


    /**
     * 日期时间小于比较
     * <p>
     * left < right: true
     *
     * @param left 左边日期
     * @param right     右边日期
     * @return true: left < right, false: left >= right
     */
    public static boolean lt(Date left, Date right) {
        return left.compareTo(right) < 0;
    }

    /**
     * 日期时间小于等于比较
     * <p>
     * left <= right: true
     *
     * @param left  日期时间
     * @param right 日期时间
     * @return true: left <= right, false: left > right
     */
    public static boolean lte(Date left, Date right) {
        return left.compareTo(right) <= 0;
    }
}
