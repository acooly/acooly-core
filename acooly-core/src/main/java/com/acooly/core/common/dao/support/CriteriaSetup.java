package com.acooly.core.common.dao.support;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author calvin
 */
public class CriteriaSetup {
    public void setup(Criteria criteria, Map<String,Object> filter) {
        if (filter != null && !filter.isEmpty()) {
            Set<String> keys = filter.keySet();
            for (Object key : keys) {
                String value = (String) filter.get(key);
                if (StringUtils.isNotBlank(value))
                    criteria.add(Restrictions.eq((String) key, value));
            }
        }
    }
}

