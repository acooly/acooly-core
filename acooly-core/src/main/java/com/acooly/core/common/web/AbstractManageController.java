package com.acooly.core.common.web;

import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.service.EntityService;

/**
 * 标记抽象类：专用于后台管理BOSS的基类
 *
 * @param <T>
 * @param <M>
 * @author zhangpu
 */
public abstract class AbstractManageController<T extends Entityable, M extends EntityService<T>>
        extends AbstractJQueryEntityController<T, M> {

}
