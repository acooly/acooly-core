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

import com.acooly.core.utils.service.SingleValueOrder;
import com.acooly.core.utils.service.SingleValueResult;

/**
 * @author qiubo@yiji.com
 */
public interface DemoService1 {
	SingleValueResult<String> echo(SingleValueOrder<String> msg);
}
