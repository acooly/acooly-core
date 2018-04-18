package com.acooly.module.ds.check.dic;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 表元数据
 */
@Setter
@Getter
public class Table {

    /**
     * 表名
     */
    private String name;

    /**
     * 扩展信息
     */
    private Map<String, Object> properties = new HashMap<>();
    /**
     * 列信息
     */
    private List<Column> columns = new LinkedList<>();
}
