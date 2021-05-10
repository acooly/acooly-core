/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-04-07 00:10
 */
package com.acooly.core.common.exception;
/**
 * 文件上传的错误码定义
 *
 * @author zhangpu
 * @date 2021-04-07 00:10
 */

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum FileOperateErrorCodes implements Messageable {

    File_INPUT_NAME_ILLEGAL("File_INPUT_NAME_ILLEGAL", "文件上传的表单字段命名规则错误"),
    File_PATH_FIELD_NOT_EXIST("File_PATH_FIELD_NOT_EXIST", "没有找到文件上传表单字段名称对应的实体属性"),
    FILE_UPLOAD_TRANSFER_ERROR("FILE_UPLOAD_TRANSFER_ERROR","文件传输/拷贝失败"),
    FILE_UPLOAD_SIZE_LIMIT("FILE_UPLOAD_SIZE_LIMIT","上传文件超过最大限制"),
    FILE_UPLOAD_TYPE_LIMIT("FILE_UPLOAD_TYPE_LIMIT","上传文件类型非法"),

    OPT_UN_SAFETY_FS_ROOT("OPT_UN_SAFETY_FS_ROOT","危险操作文件系统根目录"),
    OPT_UN_SAFETY_OS_DIR("OPT_UN_SAFETY_OS_DIR","危险操作操作系统保留目录");

    private final String code;
    private final String message;

    FileOperateErrorCodes(String code, String message) {
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
        for (FileOperateErrorCodes type : values()) {
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
    public static FileOperateErrorCodes find(String code) {
        for (FileOperateErrorCodes status : values()) {
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
    public static List<FileOperateErrorCodes> getAll() {
        List<FileOperateErrorCodes> list = new ArrayList<FileOperateErrorCodes>();
        for (FileOperateErrorCodes status : values()) {
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
        for (FileOperateErrorCodes status : values()) {
            list.add(status.code());
        }
        return list;
    }

}
