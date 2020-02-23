package com.acooly.module.web.exceptionhandler;

import com.acooly.core.common.boot.Apps;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.view.ViewResult;
import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Sets;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 全局MVC异常处理器
 *
 * <p>异常处理策略如下:
 *
 * <p>1.设置响应statusCode (如果异常标注{@link ResponseStatus} ,则获取此annotation的定义statusCode;如果没有
 * ，则从attribute(key=javax.servlet.error.status_code)中获取)
 *
 * <p>2. 判断请求的返回是否为json(请求路径中包含.json后缀或者请求头 {@link GlobalExceptionHandler#ACCEPT_HEADER}包含 {@link
 * MediaType#APPLICATION_JSON_VALUE})
 *
 * <p>3.isjson
 *
 * <ul>
 * <li>通过异常构造{@link ViewResult}
 * <li>如果异常实现{@link Messageable}接口,用此接口设置{@link ViewResult}对象
 * </ul>
 * <p>
 * 2.如果请求的返回为非json
 *
 * <p>通过配置参数error.path配置渲染的view 提供如下属性：
 *
 * <ul>
 * <li>timestamp - 异常发生时间
 * <li>status - http 状态码
 * <li>exception - 抛出的异常
 * <li>message - 异常message
 * <li>path - 请求抛异常的路径
 * <li>errors - 抛出的异常为{@link BindingResult},会把{@link BindingResult#getAllErrors} 设置为errors
 * </ul>
 *
 * @author qiubo@yiji.com
 */
@ConditionalOnProperty(value = "acooly.web.enableMVCGlobalExceptionHandler", matchIfMissing = true)
@ControllerAdvice
public class GlobalExceptionHandler implements InitializingBean {

    private Set<String> restSet = Sets.newHashSet();

    public static final int DEFAULT_STATUS_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String ACCEPT_HEADER = "Accept";
    private static final String DEFAULT_ERROR_MESSAGE = "系统内部异常";
    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath = "/error";

    @ExceptionHandler({Exception.class})
    public Object badRequest( HttpServletRequest req, HttpServletResponse rep, HandlerMethod method,
            Exception exception ) {
        logger.error("", exception);

        if (!( exception instanceof BusinessException )) {
            exception = new BusinessException(DEFAULT_ERROR_MESSAGE);
        }
        if (isJsonRequest(req, method.getMethod())) {
            ViewResult info = new ViewResult();
            buildViewResultInfo(exception, info);
            return new ResponseEntity<>(info, HttpStatus.OK);
        } else {
            int statusCode = getStatus(req, exception);
            rep.setStatus(statusCode);
            ModelAndView mav = new ModelAndView();
            buildModelAndView(req, exception, statusCode, mav);
            return mav;
        }
    }

    private void buildModelAndView(
            HttpServletRequest req, Exception exception, int statusCode, ModelAndView mav ) {
        mav.addObject("timestamp", new Date());
        mav.addObject("status", statusCode);
        setException(exception, mav);
        setPath(req, mav);
        mav.setViewName("error");
    }

    private void buildViewResultInfo( Exception exception, ViewResult info ) {
        info.setSuccess(false);
        String msg;
        msg = exception.getMessage();
        if (exception instanceof Messageable) {
            info.setCode(( (Messageable) exception ).code());
            info.setDetail(msg);
        } else {
            info.setDetail(msg);
        }
    }

    private void setPath( HttpServletRequest req, ModelAndView mav ) {
        Object path = req.getAttribute("javax.servlet.error.request_uri");
        if (path != null) {
            mav.addObject("path", path);
        } else {
            mav.addObject("path", req.getRequestURL());
        }
    }

    private void setException( Exception exception, ModelAndView mav ) {
        while (exception instanceof ServletException && exception.getCause() != null) {
            exception = (Exception) exception.getCause();
        }
        mav.addObject("error", exception.getClass().getName());
        ResponseStatus responseStatus =
                AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
        if (responseStatus != null) {

            mav.addObject("message", responseStatus.reason());
        } else {
            if (!( exception instanceof BindingResult )) {
                mav.addObject("message", exception.getMessage());
            } else {
                BindingResult result = (BindingResult) exception;
                if (result.getErrorCount() > 0) {
                    mav.addObject("errors", result.getAllErrors());
                    mav.addObject(
                            "message",
                            "Validation failed for object='"
                                    + result.getObjectName()
                                    + "'. Error count: "
                                    + result.getErrorCount());
                } else {
                    mav.addObject("message", "No errors");
                }
            }
        }
    }

    private int getStatus( HttpServletRequest req, Exception exception ) {
        while (exception instanceof ServletException && exception.getCause() != null) {
            exception = (Exception) exception.getCause();
        }
        ResponseStatus responseStatus =
                AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
        int statusCode = DEFAULT_STATUS_CODE;
        if (responseStatus != null) {
            statusCode = responseStatus.code().value();
        } else {
            Object status = req.getAttribute("javax.servlet.error.status_code");
            if (status != null) {
                try {
                    statusCode = Integer.parseInt(status.toString());
                } catch (Exception e) {
                    // just ignore
                }
            }
        }
        return statusCode;
    }

    private boolean isJsonRequest( HttpServletRequest request, Method method ) {
        String uri = request.getRequestURI();
        if (uri.endsWith(".json") || uri.endsWith(".data")) {
            return true;
        }

        if (uri.endsWith(".html") || uri.endsWith(".htm")) {
            return false;
        }
        if (restSet.contains(method.toGenericString())) {
            return true;
        }

        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, HandlerMapping> matchingBeans =
                BeanFactoryUtils
                        .beansOfTypeIncludingAncestors(Apps.getApplicationContext(),
                                HandlerMapping.class, true, false);
        if (matchingBeans.size() > 0) {
            RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) matchingBeans
                    .get("requestMappingHandlerMapping");
            Map<RequestMappingInfo, HandlerMethod> maps = mapping.getHandlerMethods();
            for (Entry<RequestMappingInfo, HandlerMethod> entry : maps.entrySet()) {
                if (null != entry.getValue().getMethodAnnotation(ResponseBody.class) ||
                        null != AnnotatedElementUtils
                                .findMergedAnnotation(entry.getValue().getBeanType(),
                                        ResponseBody.class)) {
                    restSet.add(entry.getValue().getMethod().toGenericString());

                }

            }
        }

    }
}
