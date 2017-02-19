package com.acooly.module.cms.service;


import com.acooly.core.common.service.EntityService;
import com.acooly.module.cms.domain.ContentType;

import java.util.List;

/**
 * 内容类型 Service
 * 
 * Date: 2013-07-12 15:06:45
 * 
 * @author Acooly Code Generator
 * 
 */
public interface ContentTypeService extends EntityService<ContentType> {

	List<ContentType> getLevel(String parentId);

	ContentType getContentType(String code);
}
