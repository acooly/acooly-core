package com.acooly.core.utils.arithmetic.tree;

import com.acooly.core.utils.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
public class QuickTree {


    /**
     * 快速构建树
     * <p>
     * comparator的快速构建建议采用lambda方式。
     * <i>1. 单属性排序（null处理和反序），demo：Comparator.nullsLast(Comparator.comparing(SortEntity::getBirthday).reversed());<i/>
     * <i>2. 多属性排序（先按性别字符排序，然后按ID倒序），demo：Comparator.nullsLast(Comparator.comparing(SortEntity::getGender).thenComparing(t -> -t.getId));<i/>
     *
     * @param list       待排序的树形结构集合
     * @param comparator 排序比较器
     * @param <T>        成员泛型
     * @return 已排序的树形结构集合
     */
    public static <T extends TreeNode<T>> List<T> quickTree(List<T> list, Long topParentId, Comparator<T> comparator) {
        // 初始化结构
        Map<Long, T> data = Maps.newHashMap();
        List<T> tree = Lists.newArrayList();
        Collections.sort(list, comparator);
        for (T t : list) {
            data.put(t.getId(), t);
            if (longEquals(t.getParentId(), topParentId)) {
                tree.add(t);
            }
        }

        // 构建树
        for (T t : list) {
            if (data.get(t.getParentId()) != null) {
                data.get(t.getParentId()).addChild(t);
            }
        }
        return tree;
    }


    public static <T extends TreeNode<T>> List<T> quickTree(List<T> list, Comparator<T> comparator) {
        return quickTree(list, null, comparator);
    }


    private static boolean longEquals(Long id, Long topParentId) {
        return Strings.equals(String.valueOf(id), String.valueOf(topParentId));
    }
}
