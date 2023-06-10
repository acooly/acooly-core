/**
 * create by zhangpu date:2015年3月14日
 */
package com.acooly.core.utils.net;

import java.util.Map;

/**
 * Http通用请求结果对象
 *
 * @author zhangpu
 * @date 2016年8月23日
 */
public class HttpResult {

    /**
     * HTTP状态码
     */
    private int status;
    /**
     * HTTP头信息
     */
    private Map<String, String> headers;

    /**
     * HTTP响应体
     */
    private String body;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return String.format("HttpResult: {status:%s, headers:%s, body:%s}", status, headers, body);
    }
}
