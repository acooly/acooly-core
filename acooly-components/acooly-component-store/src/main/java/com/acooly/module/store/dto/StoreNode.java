/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-08-31 19:32 创建
 */
package com.acooly.module.store.dto;

import com.acooly.core.utils.Money;
import com.acooly.core.utils.ToString;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 仓库库存节点
 *
 * @author zhangpu 2017-08-31 19:32
 */

@Getter
@Setter
public class StoreNode implements Serializable {

    public static final int NODE_TYPE_BRANCH = 0;
    public static final int NODE_TYPE_LEVEL = 1;

    /**
     * 唯一ID
     */
    @NotNull
    private Long id;
    /**
     * 父ID
     */
    private Long parentId;
    /**
     * 编码
     */
    @NotEmpty
    private String code;
    /**
     * 名称
     */
    @NotEmpty
    private String name;

    /**
     * 节点类型(0:目录，1:叶子)
     */
    private int nodeType = NODE_TYPE_BRANCH;

    /**
     * 价格
     */
    private Money price;
    /**
     * 库存
     */
    private int stock;
    /**
     * 单位
     */
    private String unit;
    /**
     * 备注
     */
    private String comments;

    private List<StoreNode> children = Lists.newLinkedList();

    public StoreNode() {
    }

    public StoreNode(Long id, String code, String name, int nodeType) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.nodeType = nodeType;
    }

    public void addChild(StoreNode storeNode) {
        children.add(storeNode);
    }

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
