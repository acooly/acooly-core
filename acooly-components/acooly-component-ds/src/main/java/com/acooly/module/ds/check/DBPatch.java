package com.acooly.module.ds.check;

import lombok.Data;

import java.util.Set;

/**
 * @author qiubo@yiji.com
 */
@Data
public class DBPatch {
    private String patchSql;
    /**
     * 多个字段用逗号隔开
     */
    private Set<String> columnName;
}
