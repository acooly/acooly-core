/*
 * 修订记录:
 * zhike@yiji.com 2017-04-20 17:32 创建
 *
 */
package com.acooly.module.certification.cert.impl;

import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.module.certification.cert.RealNameAuthenticationException;
import com.acooly.module.certification.common.Response;
import com.acooly.module.certification.enums.CertResult;
import com.acooly.module.certification.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 修订记录：
 *
 * @author zhike@yiji.com
 */
@Slf4j
@Service("aliRealNameAuthentication")
public class AliRealNameAuthentication extends AbstractRealNameAuthentication {

    @Override
    public CertResult certification(String realName, String idCardNo) {
        String path = "/lianzhuo/idcard";
        String host = service;
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);
        Map<String, String> querys = new HashMap<>();
        querys.put("cardno", idCardNo);
        querys.put("name", realName);
        CertResult result = new CertResult();
        result.setRealName(realName);
        result.setIdCardNo(idCardNo);
        try {
            Response response = HttpUtil.httpGet(host, path, timeout, headers, querys);
            if (StringUtils.isNotBlank(response.getBody())) {
                JSONObject resultObj = JSON.parseObject(response.getBody());
                JSONObject resp = resultObj.getJSONObject("resp");
                JSONObject data = resultObj.getJSONObject("data");
                String code = resp.getString("code");
                if (StringUtils.equals(code, "0")) {
                    result.setResultCode(ResultStatus.success.getCode());
                    result.setResultMessage(messages.get(code));
                    result.setSex(data.getString("sex"));
                    result.setAddress(data.getString("address"));
                    result.setBirthday(data.getString("birthday"));
                    log.warn("实名认证成功:{}", messages.get(code));
                } else {
                    log.warn("实名认证失败:{}", messages.get(code));
                    throw new RealNameAuthenticationException(ResultStatus.failure.getCode(), messages.get(code));
                }
            } else {
                log.warn("实名认证失败,statusCode={},errorMessage={}",response.getStatusCode(),response.getErrorMessage());
                throw new RealNameAuthenticationException(ResultStatus.failure.getCode(), "认证失败");
            }
        } catch (ConnectTimeoutException e) {
            log.warn("实名认证连接超时:{}", e.getMessage());
            throw new RealNameAuthenticationException(ResultStatus.failure.getCode(), "连接超时");
        } catch (RealNameAuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.warn("实名认证未知异常:{}", e.getMessage());
            throw new RealNameAuthenticationException(ResultStatus.failure.getCode(), "未知异常");
        }
        return result;
    }

    @Override
    public String getProvider() {
        return "阿里实名认证";
    }

    public static Map<String, String> messages = new LinkedHashMap<String, String>() {
        /** UId */
        private static final long serialVersionUID = -847699194658395108L;

        {
            put("0", "匹配");
            put("5", "不匹配");
            put("14", "无此身份证号码");
            put("96", "交易失败，请稍后重试");
        }
    };

}