package com.acooly.core.common.web.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * JSON Response Basic data
 *
 * @author zhangpu
 */
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "javassistLazyInitializer"})
public class JsonResult {

    private boolean success = true;

    private String code = "";

    private String message = "";

    private Map<Object, Object> data = Maps.newHashMap();

    public JsonResult() {
        super();
    }

    public JsonResult(boolean success) {
        super();
        this.success = success;
    }

    public JsonResult(String code, String message) {
        super();
        this.code = code;
        this.message = message;
        this.success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<Object, Object> getData() {
        return data;
    }

    public void setData(Map<Object, Object> data) {
        this.data = data;
    }

    public void appendData(Object key, Object value) {
        this.data.put(key, value);
    }

    public void appendData(Map<?, ?> map) {
        this.data.putAll(map);
    }
}
