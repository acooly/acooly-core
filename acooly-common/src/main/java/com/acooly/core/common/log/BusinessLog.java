package com.acooly.core.common.log;

import com.acooly.core.utils.system.IPUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

/**
 * @author qiuboboy@qq.com
 * @date 2018-04-12 13:56
 */
@Data
public class BusinessLog {
    public static final String LOGGER_NAME = "BUSINESS-LOG";
    private static final Logger LOGGER = LoggerFactory.getLogger(LOGGER_NAME);
    private static final String APP_NAME = System.getProperty("acooly.appName");
    private static final String ENV = System.getProperty("spring.profiles.active");
    private static final String DEFAULT_LOG_TYPE = "";

    private static final String IP;
    private static final String NULL = "null";

    static {
        Assert.notNull(APP_NAME, "应用名不能为空");
        IP = IPUtil.getFirstNoLoopbackIPV4Address();
    }

    private String appName;
    private String businessName;
    private String env;
    private String hostName;
    private Date timestamp;
    private Map<String, Object> body;

    public BusinessLog() {
        this.appName = APP_NAME;
        this.env = ENV;
        this.hostName = IP;
        this.timestamp = new Date();
        this.businessName = DEFAULT_LOG_TYPE;
    }

    /**
     * @param businessName 业务日志名称
     * @param bodys        业务体参数
     */
    public static void log(String businessName, Object... bodys) {
        BusinessLog bl = new BusinessLog();
        bl.setBusinessName(businessName);
        Assert.notNull(bodys, "日志类型不能为空");
        Assert.isTrue(bodys.length % 2 == 0, "业务信息为键值对形式");
        Map<String, Object> body = Maps.newHashMapWithExpectedSize(bodys.length / 2);

        for (int i = 0; i < bodys.length - 1; i += 2) {
            String key;
            if (bodys[i] == null) {
                key = "null";
            } else {
                key = bodys[i].toString();
            }

            body.put(key, bodys[i + 1]);
        }
        bl.body = body;
        LOGGER.info(bl.toJSONString());
    }

    /**
     * @param businessName 业务日志名称
     * @param body        业务体参数
     */
    public static void log(String businessName, Map<String, Object> body) {
        BusinessLog bl = new BusinessLog();
        bl.setBusinessName(businessName);
        Assert.notNull(body, "日志类型不能为空");
        bl.body = body;
        LOGGER.info(bl.toJSONString());
    }

    public static void log(BusinessLog bl) {
        LOGGER.info(bl.toJSONString());
    }

    public BusinessLog body(String key, Object value) {
        if (this.body == null) {
            this.body = Maps.newHashMap();
        }

        if (key == null) {
            key = NULL;
        }

        this.body.put(key, value);
        return this;
    }

    public String toJSONString() {
        Assert.notNull(this.businessName, "日志类型不能为空");
        return JSON.toJSONString(this, SerializerFeature.WriteDateUseDateFormat);
    }
}
