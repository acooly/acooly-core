/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-07-01 13:55
 */
package com.acooly.module.web.jackson;

import com.acooly.core.utils.BigMoney;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @author zhangpu
 * @date 2021-07-01 13:55
 */
@Slf4j
@JsonComponent
public class BigMoneyJsonSerializer extends JsonSerializer<BigMoney> {


    @Override
    public void serialize(BigMoney value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeNumber(value.getAmount());
        }
    }

    @Override
    public Class<BigMoney> handledType() {
        return BigMoney.class;
    }
}
