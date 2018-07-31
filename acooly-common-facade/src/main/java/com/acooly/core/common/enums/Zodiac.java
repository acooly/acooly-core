package com.acooly.core.common.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.Date;

/**
 * @author qiuboboy@qq.com
 * @date 2018-07-31 13:46
 */
public enum Zodiac implements Messageable {
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

    private Date from;
    private Date to;
    private final String code;
    private final String message;


    Zodiac(Date from, Date to, String code, String message) {
        this.from = from;
        this.to = to;
        this.code = code;
        this.message = message;
    }

    private static Date newDate(int monthOfYear, int dayOfMonth) {
        return new Date(1, monthOfYear - 1, dayOfMonth);
    }

    public static Zodiac to(int monthOfYear, int dayOfMonth) {
        Date date = new Date(1, monthOfYear-1, dayOfMonth);
        for (Zodiac zodiac : values()) {
            Date fromWithYear = new Date(zodiac.from.getTime());
            Date toWithYear = new Date(zodiac.to.getTime());

            if (monthOfYear == 12 && Zodiac.CAPRICORN.equals(zodiac)) {
                toWithYear.setYear(2);
            } else if (monthOfYear == 1 && Zodiac.CAPRICORN.equals(zodiac)) {
                fromWithYear.setYear(0);
            }
            if ((fromWithYear.before(date) || fromWithYear.equals(date))
                    && (toWithYear.after(date) || toWithYear.equals(date))) {
                return zodiac;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Zodiac{");
        sb.append("code='").append(code).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
