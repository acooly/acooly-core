package com.acooly.core.utils.arithmetic.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 快速排序
 * <p>
 * 针对场景：id, parentid
 *
 * @author zhangpu
 * @date 2019-01-06 23:07
 */
@Slf4j
public class QuickTree {


    /**
     * 快速构建树
     *
     * @param list
     * @param comparator
     * @param <T>
     * @return
     */
    public static <T extends TreeNode> List<T> quickTree(List<T> list, Comparator comparator) {
        Map<Long, T> data = Maps.newHashMap();
        for (T t : list) {
            data.put(t.getId(), t);
        }

        List<T> tree = Lists.newArrayList();
        for (T t : list) {
            if (t.getParentId() == null) {
                tree.add(t);
            } else {
                if (data.get(t.getParentId()) != null) {
                    data.get(t.getParentId()).addChild(t);
                }
            }
        }

        if (comparator != null) {
            Collections.sort(tree, comparator);
        }
        return tree;
    }

}
