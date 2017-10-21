package com.acooly.module.olog.collector.logger;

import com.acooly.module.olog.facade.order.OlogOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Olog 日志处理器
 * 
 * @author zhangpu
 */
public interface OlogCollector {

    OlogOrder collect(HttpServletRequest request, HttpServletResponse response, OlogTarget target,
                      Map<String, Object> context);

    OlogOrder collect(HttpServletRequest request, HttpServletResponse response, OlogTarget target);
}
