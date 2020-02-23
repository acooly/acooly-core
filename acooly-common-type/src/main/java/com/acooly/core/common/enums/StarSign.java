package com.acooly.core.common.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.*;

/**
 * @author qiuboboy@qq.com
 * @author zhangpu: 改名重构，去除过时API
 * @date 2018-07-31 13:46
 */
public enum StarSign implements Messageable {
    /**
     * 白羊座
     */
    ARIES(newDate(3, 21), newDate(4, 20), "Aries", "白羊座"),
    TAURUS(newDate(4, 21), newDate(5, 21), "Taurus", "金牛座"),
    GEMINI(newDate(5, 22), newDate(6, 21), "Gemini", "双子座"),
    CANCER(newDate(6, 22), newDate(7, 22), "Cancer", "巨蟹座"),
    LEO(newDate(7, 23), newDate(8, 22), "Leo", "狮子座"),
    VIRGO(newDate(8, 23), newDate(9, 23), "Virgo", "处女座"),
    LIBRA(newDate(9, 24), newDate(10, 23), "Libra", "天秤座"),
    SCORPIO(newDate(10, 24), newDate(11, 22), "Scorpio", "天蝎座"),
    SAGITTARIUS(newDate(11, 23), newDate(12, 21), "Sagittarius", "人马座"),
    CAPRICORN(newDate(12, 22), newDate(1, 20), "Capricorn", "摩羯座"),
    AQUARIUS(newDate(1, 21), newDate(2, 19), "Aquarius", "水瓶座"),
    PISCES(newDate(2, 20), newDate(3, 20), "Pisces", "双鱼座");

    private final Date from;
    private final Date to;
    private final String code;
    private final String message;


    StarSign(Date from, Date to, String code, String message) {
        this.from = from;
        this.to = to;
        this.code = code;
        this.message = message;
    }

    private static Date newDate(int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1970, monthOfYear, dayOfMonth);
        return calendar.getTime();
    }

    /**
     * 解析星座
     *
     * @param date
     * @return
     */
    public static StarSign to(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthOfYear = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return to(monthOfYear, dayOfMonth);
    }

    /**
     * 解析星座
     *
     * @param monthOfYear
     * @param dayOfMonth
     * @return
     */
    public static StarSign to(int monthOfYear, int dayOfMonth) {
        Date date = newDate(monthOfYear, dayOfMonth);
        long dating = date.getTime();
        for (StarSign starSign : values()) {
            long formTimes = starSign.from.getTime();
            long toTimes = starSign.to.getTime();

            if (StarSign.CAPRICORN.equals(starSign)) {
                if ((dating >= formTimes && dating <= newDate(12, 31).getTime())) {
                    return starSign;
                }
                if (dating >= newDate(1, 1).getTime() && dating <= toTimes) {
                    return starSign;
                }
            } else {
                if (dating >= formTimes && dating <= toTimes) {
                    return starSign;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }


    public static Map<String, String> mapping() {
        Map<String, String> map = new LinkedHashMap();
        for (StarSign type : values()) {
            map.put(type.getCode(), type.getMessage());
        }
        return map;
    }

    /**
     * 通过枚举值码查找枚举值。
     *
     * @param code 查找枚举值的枚举值码。
     * @return 枚举值码对应的枚举值。
     * @throws IllegalArgumentException 如果 code 没有对应的 Status 。
     */
    public static StarSign find(String code) {
        for (StarSign status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<StarSign> getAll() {
        List<StarSign> list = new ArrayList<StarSign>();
        for (StarSign status : values()) {
            list.add(status);
        }
        return list;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static List<String> getAllCode() {
        List<String> list = new ArrayList<String>();
        for (StarSign status : values()) {
            list.add(status.code());
        }
        return list;
    }

}
