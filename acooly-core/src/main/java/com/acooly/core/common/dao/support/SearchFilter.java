package com.acooly.core.common.dao.support;

import com.acooly.core.utils.Asserts;
import com.acooly.core.utils.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.acooly.core.common.dao.support.SearchFilter.Operator.NOTNULL;
import static com.acooly.core.common.dao.support.SearchFilter.Operator.NULL;

/**
 * 动态查询 条件
 *
 * @author zhangpu
 * @date 2012年6月30日
 */
public class SearchFilter {

    public static final int PARTS_COUNT = 2;

    public String fieldName;
    public Object value;
    public Operator operator;

    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public static List<SearchFilter> parse(Map<String, Object> searchParams) {
        List<SearchFilter> filters = Lists.newArrayList();
        for (Entry<String, Object> entry : searchParams.entrySet()) {
            SearchFilter searchFilter = parse(entry.getKey(), entry.getValue());
            if (searchFilter != null) {
                filters.add(searchFilter);
            }
        }
        return filters;
    }

    public static SearchFilter parse(String param, Object value) {
        Asserts.notEmpty(param);
        String[] names = StringUtils.split(param, "_");
        if (names.length != PARTS_COUNT) {
            throw new IllegalArgumentException(param + " is not a valid search filter name");
        }
        Operator op = Operator.valueOf(names[0].toUpperCase());
        if (value == null || Strings.isBlank(value.toString())) {
            if (!(op == NULL || op == NOTNULL)) {
                return null;
            }
        }
        return new SearchFilter(names[1], op, value);
    }

    /**
     * 操作符号
     */
    public enum Operator {
        /**
         * 等于
         */
        EQ,
        NEQ,
        LIKE,
        LLIKE,
        RLIKE,
        GT,
        LT,
        GTE,
        LTE,
        IN,
        NOTIN,
        NULL,
        NOTNULL,
    }
}
