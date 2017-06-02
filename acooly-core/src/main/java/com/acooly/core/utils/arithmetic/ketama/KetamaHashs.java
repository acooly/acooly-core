/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月29日
 *
 */
package com.acooly.core.utils.arithmetic.ketama;

import com.acooly.core.utils.Strings;

/**
 * Ketama 一致性hash实现工具
 *
 * @author zhangpu
 */
public class KetamaHashs {

  public static final int NODES = 100;
  public static final int NODE_COPIES = 160;
  private static volatile KetamaHashs ketamaHashs;
  protected KetamaNodeLocator locator;
  private String nodeNamePrefix;
  private int nodes = NODES;
  private int nodeCopies = NODE_COPIES;
  private String nodeNameSplits = "_";

  private KetamaHashs(
      String nodeNamePrefix, String nodeNameSplits, Integer nodes, Integer nodeCopies) {
    this.nodeNamePrefix = nodeNamePrefix;
    this.nodeNameSplits = Strings.isBlankDefault(nodeNameSplits, "_");
    this.nodes = Strings.isBlankDefault(nodes, NODES);
    this.nodeCopies = Strings.isBlankDefault(nodeCopies, NODE_COPIES);
    locator =
        new KetamaNodeLocator(
            this.nodeNamePrefix, this.nodes, this.nodeCopies, this.nodeNameSplits);
  }

  public static KetamaHashs INSTANCE(String nodeNameProfix) {
    return INSTANCE(nodeNameProfix, null, null, null);
  }

  public static KetamaHashs INSTANCE(
      String nodeNamePrefix, String nodeNameSplits, Integer nodes, Integer nodeCopies) {
    if (ketamaHashs == null) {
      synchronized (KetamaHashs.class) {
        if (ketamaHashs == null) {
          ketamaHashs = new KetamaHashs(nodeNamePrefix, nodeNameSplits, nodes, nodeCopies);
        }
      }
    }
    return ketamaHashs;
  }

  public String getPrimary(String key) {
    KetamaNode node = locator.getPrimary(key);
    return this.nodeNamePrefix + this.nodeNameSplits + node.getName();
  }

  public KetamaNodeLocator getLocator() {
    return locator;
  }
}
