/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validate.predicate;

import com.google.common.base.Predicate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 身份证号码格式断言
 *
 * @author zhangpu
 */
public class CertNoPredicate implements Predicate<String> {

  public static final CertNoPredicate INSTANCE = new CertNoPredicate();

  private static final Map<String, String> AREA = initArea();
  private static final String VERIFY_CODES = "10X98765432";

  private static final Pattern LEAP_YEAR_15 =
      Pattern.compile(
          "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$");
  private static final Pattern NORMAL_YEAR_15 =
      Pattern.compile(
          "^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$");

  private static final Pattern LEAP_YEAR_18 =
      Pattern.compile(
          "^[1-9][0-9]{5}[1-9][0-9]{3}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$");
  private static final Pattern NORMAL_YEAR_18 =
      Pattern.compile(
          "^[1-9][0-9]{5}[1-9][0-9]{3}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$");

  private CertNoPredicate() {}

  private static Map<String, String> initArea() {
    Map<String, String> areaMap = new HashMap<String, String>();
    areaMap.put("11", "北京");
    areaMap.put("12", "天津");
    areaMap.put("13", "河北");
    areaMap.put("14", "山西");
    areaMap.put("15", "内蒙古");
    areaMap.put("21", "辽宁");
    areaMap.put("22", "吉林");
    areaMap.put("23", "黑龙江");
    areaMap.put("31", "上海");
    areaMap.put("32", "江苏");
    areaMap.put("33", "浙江");
    areaMap.put("34", "安徽");
    areaMap.put("35", "福建");
    areaMap.put("36", "江西");
    areaMap.put("37", "山东");
    areaMap.put("41", "河南");
    areaMap.put("42", "湖北");
    areaMap.put("43", "湖南");
    areaMap.put("44", "广东");
    areaMap.put("45", "广西");
    areaMap.put("46", "海南");
    areaMap.put("50", "重庆");
    areaMap.put("51", "四川");
    areaMap.put("52", "贵州");
    areaMap.put("53", "云南");
    areaMap.put("54", "西藏");
    areaMap.put("61", "陕西");
    areaMap.put("62", "甘肃");
    areaMap.put("63", "青海");
    areaMap.put("64", "宁夏");
    areaMap.put("65", "新疆");
    areaMap.put("71", "台湾");
    areaMap.put("81", "香港");
    areaMap.put("82", "澳门");
    areaMap.put("91", "国外");
    return areaMap;
  }

  @Override
  public boolean apply(String certNo) {
    if (certNo == null || (certNo.length() != 15 && certNo.length() != 18)) {
      // 身份证号码位数不对
      return false;
    }

    char[] certNoArr = certNo.toCharArray();

    String areaCode = String.copyValueOf(certNoArr, 0, 2);
    if (AREA.get(areaCode) == null) {
      // 身份证地区非法
      return false;
    }

    switch (certNo.length()) {
      case 15:
        // 15位身份号码检测
        Pattern p;
        int year = Integer.parseInt(String.copyValueOf(certNoArr, 6, 2)) + 1900;
        if (year % 4 == 0 || (year % 100 == 0 && year % 4 == 0)) {
          p = LEAP_YEAR_15;
        } else {
          p = NORMAL_YEAR_15;
        }
        if (!p.matcher(certNo).matches()) {
          return false;
        }
        break;
      case 18:
        // 18位身份号码检测
        year = Integer.parseInt(String.copyValueOf(certNoArr, 6, 4));
        if (year % 4 == 0 || (year % 100 == 0 && year % 4 == 0)) {
          p = LEAP_YEAR_18;
        } else {
          p = NORMAL_YEAR_18;
        }

        if (!p.matcher(certNo).matches()) {
          return false;
        }

        // 计算校验位
        int[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
          sum += (certNoArr[i] - 48) * factor[i];
        }
        int index = sum % 11;

        // 判断校验位
        char verifyCode = VERIFY_CODES.charAt(index);
        if (verifyCode != certNoArr[17]) {
          return false;
        }
        break;
      default:
        return false;
    }

    return true;
  }
}
