/**
 * create by zhangpu
 * date:2015年4月28日
 */
package com.acooly.module.appopenapi.support.login;

import com.acooly.module.appopenapi.support.AppApiLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhangpu
 *
 */
@Service("appApiLoginService")
public class AppApiLoginServiceProxyImpl implements AppApiLoginService, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(AppApiLoginServiceProxyImpl.class);

	@Resource
	private ApplicationContext applicationContext;

	private AppApiLoginService targetBean;

	@Override
	public String login(String userName, String password, Map<String, Object> context) {
		return targetBean.login(userName, password, context);
	}

	public AppApiLoginService getAppApiLoginService() {
		return targetBean;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, AppApiLoginService> beans = applicationContext.getBeansOfType(AppApiLoginService.class);
		Iterator<Map.Entry<String, AppApiLoginService>> it = beans.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, AppApiLoginService> entry = it.next();
			if (entry.getValue().getClass().equals(this.getClass())) {
				it.remove();
			}
		}
		if (beans.size() == 0) {
			this.targetBean = new AnonymousAppApiLoginService();
		} else {
			this.targetBean = beans.entrySet().iterator().next().getValue();
		}
		logger.info("Proxy target bean: {}", targetBean.getClass());
	}

}
