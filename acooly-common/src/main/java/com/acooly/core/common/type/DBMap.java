package com.acooly.core.common.type;

import com.acooly.core.utils.mapper.JsonMapper;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库中存储json的map
 * @author qiuboboy@qq.com
 * @date 2018-04-19 11:33
 */
public class DBMap<K, V> extends HashMap<K, V> {
    public DBMap() {
    }

    public DBMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public String toJson() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }

    public static <K, V> DBMap<K, V> fromJson(String json) {
        if(json==null){
            return null;
        }
        if(Strings.isNullOrEmpty(json)){
            return new DBMap<>();
        }
        return JsonMapper.nonEmptyMapper().fromJson(json, DBMap.class);
    }
}
