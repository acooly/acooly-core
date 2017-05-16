package com.acooly.module.sms.sender.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.mapper.JsonMapper;
import com.acooly.core.utils.net.HttpResult;
import com.acooly.core.utils.net.Https;
import com.acooly.module.sms.SmsProperties;
import com.acooly.module.sms.sender.ShortMessageSendException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 阿里云短信接口
 *
 * @author shuijing
 */
@Service("aliyunMessageSender")
public class AliyunMessageSender extends AbstractShortMessageSender {

    private final String SEND_URL = "http://sms.market.alicloudapi.com/singleSendSms";

    @Autowired
    private SmsProperties properties;

    private static TypeReference<Map<String, String>> mapTypeReference = new TypeReference<Map<String, String>>() {
    };

    @Override
    public String send(String mobileNo, String content) {
        ArrayList<String> list = Lists.newArrayListWithCapacity(1);
        list.add(mobileNo);
        return send(list, content);
    }

    @Override
    public String send(List<String> mobileNos, String content) {
        String mobileNo = Joiner.on(",").join(mobileNos);

        Map<String, String> templateParam = properties.getAliyun().getTemplateParam();
        String paramString;
        paramString = (templateParam != null && templateParam.size() > 0) ? JsonMapper.nonEmptyMapper().toJson(templateParam) : "{}";

        Map<String, String> headers = Maps.newHashMapWithExpectedSize(1);
        headers.put("Authorization", "APPCODE " + properties.getAliyun().getAppCode());

        Map<String, String> dataMap = Maps.newHashMap();
        dataMap.put("ParamString", paramString);
        dataMap.put("RecNum", mobileNo);
        dataMap.put("SignName", properties.getAliyun().getSignName());
        dataMap.put("TemplateCode", properties.getAliyun().getTemplateCode());

        Https instance = Https.getInstance();
        instance.connectTimeout(timeout / 2);
        instance.readTimeout(timeout / 2);
        try {
            HttpResult httpResult = instance.get(SEND_URL, dataMap, headers, "utf-8");
            String result = handleResult(httpResult);
            logger.info("发送短信完成  {mobile:{},SignName:{},TemplateCode:{},ParamString:{},result:{}}", mobileNo, properties.getAliyun().getSignName(), properties.getAliyun().getTemplateCode(), paramString, result);
            return result;
        } catch (Exception e) {
            logger.warn("发送短信失败 {号码:" + mobileNo + ",内容:" + content + "}, 原因:" + e.getMessage());
            if (e instanceof BusinessException) {
                String code = ((BusinessException) e).getCode();
                throw new ShortMessageSendException(code, ((BusinessException) e).message(), e.getMessage());
            }
            if (e instanceof IOException) {
                throw new ShortMessageSendException("-1", "解析返回数据出错", e.getMessage());
            }
            throw new ShortMessageSendException("-1", "请求失败", e.getMessage());
        }
    }

    protected String handleResult(HttpResult result) throws IOException {
        if (result.getStatus() != 200) {
            throw new BusinessException("http StatusCode=" + result.getStatus());
        } else {
            String body = result.getBody();
            if (StringUtils.isEmpty(body)) {
                throw new BusinessException("返回数据为空");
            }
            Map<String, String> bodyMap = JsonMapper.nonEmptyMapper().getMapper().readValue(body, mapTypeReference);
            Boolean success = Boolean.valueOf(bodyMap.get("success"));
            if (success) {
                return "发送成功";
            }
            String message = bodyMap.get("message");
            if (!StringUtils.isEmpty(message)) {
                throw new BusinessException(message);
            } else {
                throw new BusinessException("返回message为空");
            }
        }
    }

    @Override
    public String getProvider() {
        return "阿里云";
    }

}
