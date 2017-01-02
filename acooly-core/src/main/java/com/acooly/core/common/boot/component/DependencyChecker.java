/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-02 23:42 创建
 */
package com.acooly.core.common.boot.component;

import org.springframework.core.env.Environment;

/**
 * 我们应用有很多非标准化的依赖，比如依赖一个外部特殊的服务，比如依赖jdk提供某方面的能力。我们需要在系统启动阶段就能检查这样的能力， 如果系统不提供这样的能力，应该提前发现问题，让系统启动报错。
 *
 * @author qiubo@yiji.com
 */
public interface DependencyChecker {
	/**
	 * 检查依赖情况,如果条件不满足,请抛出异常
	 *
	 * 注意,在开发阶段某些依赖不是必须的,请根据不同的使用场景来检查依赖
	 */
	void check(Environment environment);
}
