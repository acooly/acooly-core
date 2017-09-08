/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by zhangpu
 * date:2017-08-21
 */
package com.acooly.module.store.service.impl;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Money;
import com.acooly.module.store.dao.StoreDao;
import com.acooly.module.store.dto.StoreNode;
import com.acooly.module.store.entity.Category;
import com.acooly.module.store.entity.Goods;
import com.acooly.module.store.entity.Store;
import com.acooly.module.store.service.CategoryService;
import com.acooly.module.store.service.GoodsService;
import com.acooly.module.store.service.StoreService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 仓库信息 Service实现
 * <p>
 * Date: 2017-08-21 01:56:34
 *
 * @author zhangpu
 */
@Service("storeService")
public class StoreServiceImpl extends EntityServiceImpl<Store, StoreDao> implements StoreService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GoodsService goodsService;

    @Override
    public List<Map<String, Object>> loadStoreMap(String storeCode) {
        List<Category> categories = categoryService.findByStoreCode(storeCode);
        List<Map<String, Object>> result = Lists.newLinkedList();
        Map<Long, Map<String, Object>> nodeMap = Maps.newHashMap();

        try {
            // 构造目录树结构
            for (Category category : categories) {
                nodeMap.put(category.getId(), convertMapFromCategory(category));
            }
            Map<String, Object> node = null;
            Object parentId = null;
            for (Map.Entry<Long, Map<String, Object>> entry : nodeMap.entrySet()) {
                node = entry.getValue();
                parentId = node.get("parentId");
                if (parentId == null) {
                    result.add(node);
                } else {
                    if (nodeMap.get(parentId) != null) {
                        putMapChild(nodeMap.get(parentId), node);
                    }
                }
            }
            // 给目录树增加叶子
            List<Goods> goodsList = goodsService.findByStoreCode(storeCode);
            for (Goods goods : goodsList) {
                node = nodeMap.get(goods.getCategoryId());
                if (node != null) {
                    putMapChild(node, convertMapFromGoods(goods));
                }
            }
        } catch (Exception e) {
            throw new BusinessException("加载仓库结构失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public List<StoreNode> loadStore(String storeCode) {

        List<Category> categories = categoryService.findByStoreCode(storeCode);

        Map<Long, StoreNode> nodeMap = Maps.newHashMap();
        List<StoreNode> data = Lists.newLinkedList();
        try {
            List<StoreNode> result = Lists.newLinkedList();
            // 构造目录树结构
            for (Category category : categories) {
                nodeMap.put(category.getId(), convertFromCategory(category));
            }
            for (Map.Entry<Long, StoreNode> entry : nodeMap.entrySet()) {
                StoreNode node = entry.getValue();
                if (node.getParentId() == null || node.getParentId() == 0) {
                    result.add(node);
                } else {
                    if (nodeMap.get(node.getParentId()) != null) {
                        nodeMap.get(node.getParentId()).addChild(node);
                    }
                }
            }
            // 给目录树增加叶子
            List<Goods> goodsList = goodsService.findByStoreCode(storeCode);
            StoreNode node = null;
            for (Goods goods : goodsList) {
                node = nodeMap.get(goods.getCategoryId());
                if (node != null) {
                    node.addChild(convertFromGoods(goods));
                }
            }


            for (StoreNode n : result) {
                if (Collections3.isNotEmpty(n.getChildren())) {
                    data.add(n);
                }
            }

        } catch (Exception e) {
            throw new BusinessException("加载仓库结构失败：" + e.getMessage());
        }
        return data;
    }


    protected <T> T convertCategory(Category categpory, Class<T> clazz) {
        if (StoreNode.class.isAssignableFrom(clazz)) {
            return (T) convertFromCategory(categpory);
        } else {
            return (T) convertMapFromCategory(categpory);
        }
    }

    protected <T> T convertGoods(Goods goods, Class<T> clazz) {
        if (StoreNode.class.isAssignableFrom(clazz)) {
            return (T) convertFromGoods(goods);
        } else {
            return (T) convertMapFromGoods(goods);
        }
    }

    protected StoreNode convertFromCategory(Category categpory) {
        return new StoreNode(categpory.getId(), "c" + categpory.getId(), categpory.getName(), StoreNode.NODE_TYPE_BRANCH);
    }

    protected StoreNode convertFromGoods(Goods goods) {
        StoreNode node = new StoreNode(goods.getId(), goods.getCode(), goods.getName(), StoreNode.NODE_TYPE_LEVEL);
        node.setPrice(Money.cent(goods.getPrice()));
        node.setStock(goods.getStock());
        node.setUnit(goods.getUnit());
        node.setComments(goods.getComments());
        return node;
    }

    protected Map<String, Object> convertMapFromCategory(Category categpory) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("id", categpory.getId());
        data.put("parentId", categpory.getParentId());
        data.put("code", "code" + categpory.getId());
        data.put("name", categpory.getName());
        data.put("nodeType", StoreNode.NODE_TYPE_BRANCH);
        data.put("children", Lists.newLinkedList());
        return data;
    }

    protected Map<String, Object> convertMapFromGoods(Goods goods) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("id", goods.getId());
        data.put("parentId", goods.getCategoryId());
        data.put("code", goods.getCode());
        data.put("name", goods.getName());
        data.put("nodeType", StoreNode.NODE_TYPE_LEVEL);
        data.put("price", Money.cent(goods.getPrice()));
        data.put("stock", goods.getStock());
        data.put("unit", goods.getUnit());
        data.put("comments", goods.getComments());
        return data;
    }

    protected void putMapChild(Map<String, Object> node, Map<String, Object> child) {
        if (node.get("children") == null) {
            node.put("children", Lists.newLinkedList());
        }
        List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
        children.add(child);
    }


}
