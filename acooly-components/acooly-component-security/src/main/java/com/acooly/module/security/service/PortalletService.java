package com.acooly.module.security.service;

import java.util.List;

import com.acooly.core.common.service.EntityService;
import com.acooly.module.security.domain.Portallet;

/**
 * 桌面管理 Service
 *
 * Date: 2013-05-02 15:40:57
 *
 * @author Acooly Code Generator
 *
 */
public interface PortalletService extends EntityService<Portallet> {

	List<Portallet> queryByUserName(String userName);

}
