/**
 * create by zhangpu
 * date:2015年9月11日
 */
package com.acooly.module.appopenapi.service;

import com.acooly.core.utils.mapper.BeanMapper;
import com.acooly.module.app.domain.AppCrash;
import com.acooly.module.app.service.AppCrashService;
import com.acooly.module.appopenapi.enums.ApiOwners;
import com.acooly.module.appopenapi.message.AppCrashReportRequest;
import com.acooly.module.appopenapi.message.AppCrashReportResponse;
import com.yiji.framework.openapi.common.enums.ApiServiceResultCode;
import com.yiji.framework.openapi.common.exception.ApiServiceException;
import com.yiji.framework.openapi.core.meta.OpenApiService;
import com.yiji.framework.openapi.core.service.base.BaseApiService;
import com.yiji.framework.openapi.core.service.enums.ResponseType;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * APP崩溃上报
 * 
 * @author zhangpu
 * @date 2015年9月11日
 */
@OpenApiService(name = "appCrashReport", desc = "崩溃上报", responseType = ResponseType.SYN, owner = ApiOwners.COMMON)
public class AppCrashReportService extends BaseApiService<AppCrashReportRequest, AppCrashReportResponse> {
	
	@Autowired
	private AppCrashService appCrashService;
	
	@Override
	protected void doService(AppCrashReportRequest request, AppCrashReportResponse response) {
		try {
			AppCrash appCrash = new AppCrash();
			BeanMapper.copy(request, appCrash);
			appCrash.setCrashDate(new Date());
			appCrash.setStackTrace(new String(Base64.decodeBase64(appCrash.getStackTrace())));
			appCrashService.save(appCrash);
		} catch (ApiServiceException ae) {
			throw ae;
		} catch (Exception e) {
			throw new ApiServiceException(ApiServiceResultCode.INTERNAL_ERROR, e.getMessage());
		}
	}
	
}