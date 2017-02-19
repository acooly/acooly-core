package com.acooly.module.cms.service;


import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.service.EntityService;
import com.acooly.module.cms.domain.Content;

import java.io.Serializable;

/**
 * 内容主表 Service
 *
 * Date: 2013-07-12 15:06:46
 *
 * @author Acooly Code Generator
 *
 */
public interface ContentService extends EntityService<Content> {

	void updateStatusBatch(Integer status, Serializable... ids);

	PageInfo<Content> search(PageInfo<Content> pageInfo, String q, int status);

	void delects(Serializable... ids);

	void moveTop(Long valueOf);

	void moveUp(Long valueOf);

	Content getLatestByTypeCode(String typeCode);

	Content getLatestByTypeCode(String typeCode, String carrierCode);

	Content getByKeycode(String keycode);
}
