package com.acooly.module.security.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.security.dao.PortalletDao;
import com.acooly.module.security.domain.Portallet;
import com.acooly.module.security.service.PortalletService;
import org.springframework.transaction.annotation.Transactional;

@Service("portalletService")
@Transactional
public class PortalletServiceImpl extends EntityServiceImpl<Portallet, PortalletDao> implements PortalletService {

	@Override
	public List<Portallet> queryByUserName(String userName) {
		return getEntityDao().queryByUserName(userName);
	}

}