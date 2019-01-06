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
     * 添加子到子对象集合中
     *
     * @param treeNode
     */
    default void addChild(T treeNode) {
        List<T> children = getChildren();
        if (children == null) {
            children = Lists.newArrayList();
        }
        children.add(treeNode);
    }
}
