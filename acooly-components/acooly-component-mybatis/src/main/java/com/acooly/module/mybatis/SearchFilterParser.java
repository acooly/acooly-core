/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-05 17:29 创建
 */
package com.acooly.module.mybatis;

import com.acooly.core.common.dao.support.EnhanceDefaultConversionService;
import com.acooly.core.common.dao.support.SearchFilter;
import com.acooly.core.utils.Dates;
import com.acooly.core.utils.Strings;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 解析SearchFilter为sql
 *
 * @author qiubo@yiji.com
 */
public class SearchFilterParser {
  private static final ConversionService conversionService =
      EnhanceDefaultConversionService.INSTANCE;
  private static final char UNDERLINE = '_';

  public static String parseSqlField(SearchFilter searchFilter, Class proType) {
    StringBuilder sb = new StringBuilder();
    sb.append(" ");
    sb.append(Strings.camelToUnderline(searchFilter.fieldName));
    sb.append(" ");
    Object value = convert(searchFilter, proType);
    if (value instanceof Date) {
      value = Dates.format((Date) value);
    }
    //fixme: sql injection
    switch (searchFilter.operator) {
      case EQ:
        if (value instanceof String) {
          sb.append(" = '" + value + "'");
        } else {
          sb.append(" = " + value);
        }
        break;
      case NEQ:
        if (value instanceof String) {
          sb.append(" != '" + value + "'");
        } else {
          sb.append(" != " + value);
        }
        break;
      case LIKE:
        sb.append(" like '%" + value + "%'");
        break;
      case LLIKE:
        sb.append(" like '%" + value + "'");
        break;
      case RLIKE:
        sb.append(" like '" + value + "%'");
        break;
      case GT:
        if (value instanceof String) {
          sb.append(" > '" + value + "'");
        } else {
          sb.append(" > " + value);
        }
        break;
      case LT:
        if (value instanceof String) {
          sb.append(" < '" + value + "'");
        } else {
          sb.append(" < " + value);
        }
        break;
      case GTE:
        if (value instanceof String) {
          sb.append(" >= '" + value + "'");
        } else {
          sb.append(" >= " + value);
        }
        break;
      case LTE:
        if (value instanceof String) {
          sb.append(" <= '" + value + "'");
        } else {
          sb.append(" <= " + value);
        }
        break;
      case NULL:
        sb.append(" is null ");
        break;
      case NOTNULL:
        sb.append(" is not null ");
        break;
      case IN:
        sb.append(" in (");
        genInClause(proType, sb, (List) value);
        break;
      case NOTIN:
        sb.append(" not in (");
        genInClause(proType, sb, (List) value);
        break;
    }

    return sb.toString();
  }

  private static void genInClause(Class proType, StringBuilder sb, List value) {
    if (Number.class.isAssignableFrom(proType)) {
      StringBuilder tmp = new StringBuilder();
      for (Object o : value) {
        tmp.append(o + ",");
      }
      tmp.deleteCharAt(tmp.length() - 1);
      sb.append(tmp.toString()).append(")");
    } else {
      StringBuilder tmp = new StringBuilder();
      for (Object o : value) {
        tmp.append("'" + o + "',");
      }
      tmp.deleteCharAt(tmp.length() - 1);
      sb.append(tmp.toString()).append(")");
    }
  }

  private static Object convert(SearchFilter searchFilter, Class proType) {
    if (proType.isEnum()) {
      if (searchFilter.value instanceof String) {
        return searchFilter.value;
      } else if (searchFilter.value instanceof Enum) {
        return ((Enum) searchFilter.value).name();
      }
      return searchFilter.value;
    }
    Object value = null;
    if (searchFilter.operator == SearchFilter.Operator.IN
        || searchFilter.operator == SearchFilter.Operator.NOTIN) {
      if (searchFilter.value == null) {
        throw new RuntimeException("操作符[" + searchFilter.operator + "]时，值不能为null");
      }
      if (!(Number.class.isAssignableFrom(proType) || String.class.isAssignableFrom(proType))) {
        throw new RuntimeException("操作符[" + searchFilter.operator + "]时，支持属性为String或者数字");
      }
      List tmp;
      if (searchFilter.value.getClass().isArray()) {
        tmp = CollectionUtils.arrayToList(searchFilter.value);
      } else if (List.class.isAssignableFrom(searchFilter.value.getClass())) {
        tmp = (List) searchFilter.value;
      } else if (String.class.isAssignableFrom(searchFilter.value.getClass())) {
        tmp =
            Lists.newArrayList(
                Splitter.on(",").trimResults().splitToList((String) searchFilter.value));
      } else {
        throw new RuntimeException(
            "操作符["
                + searchFilter.operator
                + "]时，支持参数类型为数组，list，String，value="
                + searchFilter.value);
      }
      if (tmp.isEmpty()) {
        throw new RuntimeException(
            "操作符[" + searchFilter.operator + "]时，集合不能为空，value=" + searchFilter.value);
      }
      List result = Lists.newArrayList();
      for (int i = 0; i < tmp.size(); i++) {
        Object cur = tmp.get(i);
        if (!proType.isAssignableFrom(cur.getClass())) {
          result.add(conversionService.convert(cur, proType));
        } else {
          result.add(cur);
        }
      }
      value = result;
    } else {
      if (searchFilter.value != null) {
        if (!proType.isAssignableFrom(searchFilter.value.getClass())) {
          if (searchFilter.operator == SearchFilter.Operator.LTE
              && proType.isAssignableFrom(java.sql.Date.class)) {
            String oriValue = (String) searchFilter.value;
            if (oriValue.length() == Dates.CHINESE_DATE_FORMAT_LINE.length()) {
              searchFilter.value = Dates.addDay(Dates.parse(oriValue));
            }
          }
          value = conversionService.convert(searchFilter.value, proType);
        } else {
          value = searchFilter.value;
        }

      } else {
        if (!(searchFilter.operator == SearchFilter.Operator.NULL
            || searchFilter.operator == SearchFilter.Operator.NOTNULL)) {
          throw new RuntimeException("操作符[" + searchFilter.operator + "]时，值不能为null");
        }
      }
    }

    return value;
  }
}
