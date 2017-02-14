/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-21 15:27 创建
 *
 */
package com.acooly.core.test.dubbo;

import com.acooly.core.common.facade.SingleOrder;
import com.acooly.core.common.facade.SingleResult;

/**
 * @author qiubo@yiji.com
 */
public interface DemoFacade {
	SingleResult<String> echo(SingleOrder<String> msg);
}