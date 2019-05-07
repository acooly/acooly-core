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
     * @return
     */
    Long getId();

    /**
     * 父ID
     *
     * @return
     */
    Long getParentId();

    /**
     * 子对象列表
     *
     * @return
     */
    List<T> getChildren();

    /**
     * 设置子对象
     *
     * @param children
     */
    void setChildren(List<T> children);

    /**
     * 添加子到子对象集合中
     *
     * @param treeNode
     */
    default void addChild(T treeNode) {
        if (getChildren() == null) {
            setChildren(Lists.newArrayList());
        }
        getChildren().add(treeNode);
    }
}
