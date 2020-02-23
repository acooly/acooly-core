/**
 * acooly-core-parent
 * <ORDER_PATTERN>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-11-03 17:23
 */
package com.acooly.core.test.utils;

import com.acooly.core.common.facade.InfoBase;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.arithmetic.tree.QuickTree;
import com.acooly.core.utils.arithmetic.tree.TreeNode;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

/**
 * @author zhangpu
 * @date 2019-11-03 17:23
 */
@Slf4j
public class QuickTreeTest {


    @Test
    public void testTree() {
        List<TestNode> testNodes = Lists.newArrayList();

        testNodes.add(new TestNode(1L, 0L, 2L));
        testNodes.add(new TestNode(2L, 0L, 3L));
        testNodes.add(new TestNode(3L, 0L, 1L));
        testNodes.add(new TestNode(4L, 0L, 3L));

        // 为id=4的添加子节点3个
        testNodes.add(new TestNode(5L, 4L, 200L));
        testNodes.add(new TestNode(6L, 4L, 100L));
        testNodes.add(new TestNode(7L, 4L, 200L));


        log.info("original: {}", testNodes);

        // 第1个条件降序，第2个条件降序
        testNodes = QuickTree.quickTree(testNodes, 0L,
                Comparator.comparing((TestNode t) -> -t.getSortTime()).thenComparing(t -> -t.getId())
        );

        for (TestNode t : testNodes) {
            System.out.println(t);
            if (Collections3.isNotEmpty(t.getChildren())) {
                for (TestNode st : t.getChildren()) {
                    System.out.println("    " + st);
                }
            }
        }

    }


    @Getter
    @Setter
    public static class TestNode extends InfoBase implements TreeNode<TestNode> {
        private Long id;
        private Long parentId;
        private long sortTime;
        private List<TestNode> children;

        public TestNode() {
        }

        public TestNode(Long id, Long parentId, long sortTime) {
            this.id = id;
            this.parentId = parentId;
            this.sortTime = sortTime;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper("")
                    .add("id", id)
                    .add("sortTime", sortTime)
                    .toString();
        }
    }


}
