/*
 * acooly.cn Inc.
 * Copyright (c) 2020 All Rights Reserved.
 * create by acooly
 * date:2020-07-03
 */
package com.acooly.core.test.core.service.impl;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.utils.Money;
import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.test.core.service.AppService;
import com.acooly.core.test.core.dao.AppDao;
import com.acooly.core.test.core.entity.App;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * app Service实现
 *
 * @author acooly
 * @date 2020-07-03 16:28:38
 */
@Service("appService")
public class AppServiceImpl extends EntityServiceImpl<App, AppDao> implements AppService {

}
