/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-05 17:08 创建
 */
package com.acooly.module.web.jackson;

import com.acooly.core.common.boot.Apps;
import com.acooly.module.web.WebProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @author qiubo@yiji.com
 */
@JsonComponent
public class LongJsonSerializer extends JsonSerializer<Long> {
    private WebProperties webProperties;

    @Override
    public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        if (webProperties == null) {
            webProperties = Apps.buildProperties(WebProperties.class);
        }
        //long型超过16位到web前端序列化精度会丢失，超过16位时转换为string
        if (value != null && value.toString().length() > 16) {
            jgen.writeString(value.toString());
        }else {
            jgen.writeNumber(value);
        }
    }

    @Override
    public Class<Long> handledType() {
        return Long.class;
    }
}
