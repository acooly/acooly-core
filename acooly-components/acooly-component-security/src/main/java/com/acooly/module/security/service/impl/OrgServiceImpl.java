/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by shuijing
 * date:2017-05-26
 */
package com.acooly.module.security.service.impl;

import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.module.security.dao.OrgDao;
import com.acooly.module.security.domain.Org;
import com.acooly.module.security.service.OrgService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 组织机构 Service实现
 *
 * <p>Date: 2017-05-26 16:48:57
 *
 * @author shuijing
 */
@Service("orgService")
public class OrgServiceImpl extends EntityServiceImpl<Org, OrgDao> implements OrgService {

  @Override
  public Map<Long, Object> getOrganizeInfo(long parentId) {
    Map<Long, Object> maps = Maps.newLinkedHashMap();
    Org pOrganize = getEntityDao().get(parentId);
    maps.put(pOrganize.getId(), pOrganize.getName());
    List<Org> organizeList = getEntityDao().findByParentId(parentId);
    for (Org organize : organizeList) {
      maps.put(organize.getId(), organize.getName());
    }
    return maps;
  }

  @Override
  public List<Org> getTreeList(Long orgId) {
    List<Org> result = Lists.newArrayList();

    List<Org> organizeList = getEntityDao().getAll();
    Map<Long, Org> maps = Maps.newHashMap();
    for (Org organize : organizeList) {
      // 设置下拉树，展示节点名称
      organize.setText(organize.getName());
      if (organize.getStatus() != null) {
        organize.setStatusView(organize.getStatus().getMessage());
      }
      maps.put(organize.getId(), organize);
    }

    Set<Long> mapKey = maps.keySet();
    for (Long keyId : mapKey) {
      Org organize = maps.get(keyId);
      if (orgId == 0 && organize.getParentId() == 0) {
        // 传入orgId=0时，查询所有节点
        result.add(organize);
      } else if (organize.getId().longValue() == orgId.longValue()) {
        // 传入orgId!=0时，查询该节点下的所有子节点
        result.add(organize);
      } else {
        Org parentData = maps.get(organize.getParentId());
        if (parentData == null) {
          continue;
        }
        if (parentData.getChildren() != null) {
          parentData.getChildren().add(organize);
        } else {
          List<Org> children = Lists.newArrayList();
          children.add(organize);
          parentData.setChildren(children);
        }
      }
    }
    return result;
  }
}
