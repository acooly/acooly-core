/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2024-01-19 11:16
 */
package com.acooly.core.test.i18n;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.exception.CommonErrorCodes;
import com.acooly.core.utils.enums.SimpleStatus;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * 异常和枚举国际化方案测试
 * <p>
 * http://127.0.0.1:8083/test/i18n/exception?lang=zh_CN
 * http://127.0.0.1:8083/test/i18n/enums?lang=en_US
 * http://127.0.0.1:8083/test/i18n/beanValidate?lang=zh_CN
 *
 * @author zhangpu
 * @date 2024-01-19 11:16
 */
@Slf4j
@RestController
@RequestMapping("/test/i18n")
public class I18nTestController {


    @RequestMapping("/beanValidate")
    public ResponseEntity testBeanValidate(@Valid Customer customer, BindingResult result) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("当前语言:", LocaleContextHolder.getLocale());
        if (result.getErrorCount() > 0) {
            Map<String, String> errors = Maps.newHashMap();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            data.put("errors", errors);
        }
        return ResponseEntity.ok().body(data);
    }


    @RequestMapping("/enums")
    public ResponseEntity testEnum() {
        Map<String, Object> data = Maps.newLinkedHashMap();
        data.put("当前环境国际化开关 acooly.i18n.enable:", System.getProperty("acooly.i18n.enable"));
        data.put("当前国际化语言环境", LocaleContextHolder.getLocale());

        data.put("BusinessStatus.YES.message", BusinessStatus.YES.i18nMessage());
        data.put("BusinessStatus.NO.message", BusinessStatus.NO.i18nMessage());

        data.put("SimpleStatus.enable.message", SimpleStatus.enable.i18nMessage(this.getClass().getName()));
        data.put("SimpleStatus.disable.message", SimpleStatus.disable.i18nMessage(this.getClass().getName()));

        return ResponseEntity.ok(data);
    }

    @RequestMapping("/exception")
    public ResponseEntity testException() {
        Map<String, Object> data = Maps.newLinkedHashMap();
        data.put("当前环境国际化开关 acooly.i18n.enable:", System.getProperty("acooly.i18n.enable"));
        data.put("当前国际化语言环境", LocaleContextHolder.getLocale());

        // 两元国际化消息支持，注意：所有的key配置都是异常的code，没有前缀，要注意去重
        data.put("new BusinessException(BusinessErrors.USER_NOT_EXIST)",
                printException(new BusinessException(BusinessErrors.USER_NOT_EXIST)));
        data.put("new BusinessException(BusinessErrors.USER_NOT_EXIST, \"会员编码对应的会员不存在\")",
                printException(new BusinessException(BusinessErrors.USER_NOT_EXIST, "会员编码对应的会员不存在")));
        data.put("new BusinessException(CommonErrorCodes.PARAMETER_ERROR, \"会员编码不能为空\")",
                printException(new BusinessException(BusinessErrors.USER_NOT_EXIST)));

        data.put("new BusinessException(\"USER_MOBILE_NO_FORMAT_ERROR\", \"用户手机号码格式错误\")",
                printException(new BusinessException("USER_MOBILE_NO_FORMAT_ERROR", "用户手机号码格式错误")));
        data.put("new BusinessException(\"USER_ALREADY_EXISTS\", \"用户已经存在\", \"不能重复注册哈\")",
                printException(new BusinessException("USER_ALREADY_EXISTS", "用户已经存在", "不能重复注册哈")));
        data.put("new BusinessException(\"用户已经存在\", \"USER_ALREADY_EXISTS\")",
                printException(new BusinessException("用户已经存在", "USER_ALREADY_EXISTS")));

        // 新的国际化异常推荐方式,支持三元的国际化
        data.put("new BusinessException(\"PARAMETER_ERROR\", \"参数错误\", \"PARAMETER_ERROR_MOBILE_ERROR\", \"手机号码错误\")",
                printException(new BusinessException("PARAMETER_ERROR", "参数错误", "PARAMETER_ERROR_MOBILE_ERROR", "手机号码错误")));
        data.put("new BusinessException(CommonErrorCodes.PARAMETER_ERROR, \"PARAMETER_ERROR_MOBILE_ERROR\", \"手机号码错误\")",
                printException(new BusinessException(CommonErrorCodes.PARAMETER_ERROR, "PARAMETER_ERROR_MOBILE_ERROR", "手机号码错误")));
        return ResponseEntity.ok(data);
    }


    protected String printException(BusinessException e) {
        return e.getCode() + ", " + e.getMessage() + ", " + e.getDetail();
    }

}
