package com.acooly.core.common.web;

import com.acooly.core.common.domain.Entityable;
import com.acooly.core.common.service.EntityService;

/**
 * JQuery和JQuery-easyui作为前端的基础功能模板控制器
 *
 * <p>说明： 使用JQuery和EasyUI作为前端的情况是JSTL和JSON开发模式的混合方式，该模式降低开发人员开发类EXTJS方式的前端的门槛。<br>
 * 规则：
 * <li>对于所有前导界面（index,create,edit等通过controller跳转到jsp使用JSTL初始化HTML界面） 的訪問任然使用傳統JSTL页面跳转方式。
 * <li>对于数据访问动作（list分页查询，query条件查询，save,update,remove,get,show等） 全部采用JSON方式实现前端UI功能操作免刷新
 * <li>数据导入导出任然使用传统方式
 * <p>
 * 2019-09-02：从v5开始废弃本类，使用AbstractJsonEntityController代替
 *
 * @param <T>
 * @param <M>
 * @author zhangpu
 * @see AbstractJsonEntityController
 */
@Deprecated
public abstract class AbstractJQueryEntityController<
        T extends Entityable, M extends EntityService<T>>
        extends AbstractJsonEntityController<T, M> {

}
