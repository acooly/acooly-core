#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * ${rootArtifactId}
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author ${author}
 * @date 2019-12-06 01:38
 */
package ${package}.common.enums;
/**
 * 统一异常处理的错误码定义
 *
 * @author ${author}
 * @date 2019-12-06 01:38
 */

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum ${projectName}ErrorCodeEnum implements Messageable {
    ;
    private final String code;
    private final String message;

    ${projectName}ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (${projectName}ErrorCodeEnum type : values()) {
            map.put(type.getCode(), type.getMessage());
        }
        return map;
    }

    /**
     * 通过枚举值码查找枚举值。
     *
     * @param code 查找枚举值的枚举值码。
     * @return 枚举值码对应的枚举值。
     * @throws IllegalArgumentException 如果 code 没有对应的 Status 。
     */
    public static ${projectName}ErrorCodeEnum find(String code) {
        for (${projectName}ErrorCodeEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<${projectName}ErrorCodeEnum> getAll() {
        List<${projectName}ErrorCodeEnum> list = new ArrayList<${projectName}ErrorCodeEnum>();
        for (${projectName}ErrorCodeEnum status : values()) {
            list.add(status);
        }
        return list;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static List<String> getAllCode() {
        List<String> list = new ArrayList<String>();
        for (${projectName}ErrorCodeEnum status : values()) {
            list.add(status.code());
        }
        return list;
    }

}
