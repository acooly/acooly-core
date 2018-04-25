package com.acooly.module.appopenapi.message;

import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author qiuboboy@qq.com
 * @date 2018-04-25 14:37
 */
@OpenApiMessage(service = "bLog", type = ApiMessageType.Request)
@Getter
@Setter
public class BusinessLogRequest extends ApiRequest {
    @OpenApiField(desc = "业务日志数据列表")
    private List<BLog> contents = Lists.newArrayList();

    public BusinessLogRequest addContents(BLog bLog) {
        this.contents.add(bLog);
        return this;
    }

    @Data
    public static class BLog {
        @OpenApiField(desc = "业务名称")
        private String name;
        @OpenApiField(desc = "业务体")
        private Map<String, Object> body;

        public BLog name(String name) {
            this.name = name;
            return this;
        }

        public BLog body(String k, Object v) {
            if (body == null) {
                body = Maps.newHashMap();
            }
            body.put(k, v);
            return this;
        }
    }
}
