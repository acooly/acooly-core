package com.acooly.core.test.mock;

import com.acooly.core.common.service.MockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author qiuboboy@qq.com
 * @date 2018-09-03 09:17
 */
@Slf4j
//@Component
//@Primary
@MockService
public class XServiceMock implements XService {
    @Override
    public String echo(String body) {
        log.info("{}", body);
        return body;
    }
}
