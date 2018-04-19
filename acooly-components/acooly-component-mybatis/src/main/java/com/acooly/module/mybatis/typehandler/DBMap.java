package com.acooly.module.mybatis.typehandler;

import com.acooly.core.utils.mapper.JsonMapper;

import java.util.HashMap;

/**
 * 数据库中存储json的map
 * @author qiuboboy@qq.com
 * @date 2018-04-19 11:33
 */
public class DBMap<K, V> extends HashMap<K, V> {

    public String toJson() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }

    public static <K, V> DBMap<K, V> fromJson(String json) {
        return JsonMapper.nonEmptyMapper().fromJson(json, DBMap.class);
    }
}
