/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月19日
 *
 */
package com.acooly.core.utils.validate.predicate;

import com.acooly.core.utils.Strings;
import com.google.common.base.Predicate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码格式 断言
 *
 * @author zhangpu
 */
public final class MobileNoPredicate implements Predicate<String> {

  public static final Pattern VALID_MOBILE_NUMBER_REGEX =
      Pattern.compile(
          "^(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[57])[0-9]{8}$", Pattern.CASE_INSENSITIVE);

  public static final MobileNoPredicate INSTANCE = new MobileNoPredicate();

  private MobileNoPredicate() {}

  @Override
  public boolean apply(String input) {
    if (Strings.isBlank(input)) {
      return false;
    }
    Matcher matcher = VALID_MOBILE_NUMBER_REGEX.matcher(input);
    return matcher.find();
  }
}
