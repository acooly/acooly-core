package com.acooly.core.utils.arithmetic.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

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
     * comparator的快速构建建议采用lambda方式。
     * <i>1. 单属性排序（null处理和反序），demo：Comparator.nullsLast(Comparator.comparing(SortEntity::getBirthday).reversed());<i/>
     * <i>2. 多属性排序（先按性别字符排序，然后按ID倒序），demo：Comparator.nullsLast(Comparator.comparing(SortEntity::getGender).thenComparing(SortEntity::getId).reversed());<i/>
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
