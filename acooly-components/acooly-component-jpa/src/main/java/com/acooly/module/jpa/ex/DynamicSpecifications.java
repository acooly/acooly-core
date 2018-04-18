package com.acooly.module.jpa.ex;

import com.acooly.core.common.dao.support.EnhanceDefaultConversionService;
import com.acooly.core.common.dao.support.SearchFilter;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Dates;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 动态查询条件组装器
 *
 * @author zhangpu
 * @date 2012年6月30日
 */
public class DynamicSpecifications {
    private static final ConversionService conversionService =
            EnhanceDefaultConversionService.INSTANCE;

    public static <T> Specification<T> bySearchFilter(
            final Collection<SearchFilter> filters, final Class<T> clazz) {
        return new Specification<T>() {
            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (Collections3.isNotEmpty(filters)) {

                    List<Predicate> predicates = Lists.newArrayList();
                    for (SearchFilter filter : filters) {
                        // nested path translate, 如Task的名为"user.name"的filedName,
                        // 转换为Task.user.name属性
                        String[] names = StringUtils.split(filter.fieldName, ".");
                        Path expression = root.get(names[0]);
                        for (int i = 1; i < names.length; i++) {
                            expression = expression.get(names[i]);
                        }

                        // convert value from string to target type
                        Class attributeClass = expression.getJavaType();
                        if (!attributeClass.equals(String.class)
                                && filter.value instanceof String
                                && conversionService.canConvert(String.class, attributeClass)) {
                            // 对小于等于特殊处理.
                            if (filter.operator == SearchFilter.Operator.LTE
                                    && attributeClass.isAssignableFrom(Date.class)) {
                                String oriValue = (String) filter.value;
                                if (oriValue.length() == Dates.CHINESE_DATE_FORMAT_LINE.length()) {
                                    filter.value = Dates.addDay(Dates.parse(oriValue));
                                }
                            }
                            filter.value = conversionService.convert(filter.value, attributeClass);
                        }
                        // logic operator
                        switch (filter.operator) {
                            case EQ:
                                predicates.add(builder.equal(expression, filter.value));
                                break;
                            case NEQ:
                                predicates.add(builder.notEqual(expression, filter.value));
                                break;
                            case LIKE:
                                predicates.add(builder.like(expression, "%" + filter.value + "%"));
                                break;
                            case LLIKE:
                                predicates.add(builder.like(expression, "%" + filter.value));
                                break;
                            case RLIKE:
                                predicates.add(builder.like(expression, filter.value + "%"));
                                break;
                            case GT:
                                predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
                                break;
                            case LT:
                                predicates.add(builder.lessThan(expression, (Comparable) filter.value));
                                break;
                            case GTE:
                                predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
                                break;
                            case LTE:
                                predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
                                break;
                            case NULL:
                                predicates.add(expression.isNull());
                                break;
                            case NOTNULL:
                                predicates.add(expression.isNotNull());
                                break;
                            case IN:
                                predicates.add(buildInExpression(expression, filter));
                                break;
                            case NOTIN:
                                predicates.add(builder.not(buildInExpression(expression, filter)));
                                break;
                        }
                    }
                    // 将所有条件用 and 联合起来
                    if (predicates.size() > 0) {
                        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                    }
                }

                return builder.conjunction();
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            private Predicate buildInExpression(Path root, SearchFilter filter) {
                Predicate predicate = null;
                if (filter.value instanceof String) {
                    String[] values = String.valueOf(filter.value).split(",");
                    predicate = root.in(Arrays.asList(values));
                } else if (filter.value instanceof String[]) {
                    predicate = root.in(Arrays.asList((String[]) filter.value));
                } else {
                    predicate = root.in((Collection<Object>) filter.value);
                }
                return predicate;
            }
        };
    }
}
