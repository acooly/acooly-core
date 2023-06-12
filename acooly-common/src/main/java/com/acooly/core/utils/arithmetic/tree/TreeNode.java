package com.acooly.core.utils.arithmetic.tree;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author zhangpu
 * @date 2019-01-06 23:09
 */
public interface TreeNode<T extends TreeNode> {

    /**
     * ID,唯一标志
     *
     * @return ID
     */
    Long getId();

    /**
     * 父ID
     *
     * @return 父ID
     */
    Long getParentId();

    /**
     * 子对象列表
     *
     * @return 子对象列表
     */
    List<T> getChildren();

    /**
     * 设置子对象
     *
     * @param children 子对象列表
     */
    void setChildren(List<T> children);

    /**
     * 添加子到子对象集合中
     *
     * @param treeNode 节点对象
     */
    default void addChild(T treeNode) {
        if (getChildren() == null) {
            setChildren(Lists.newArrayList());
        }
        getChildren().add(treeNode);
    }
}
