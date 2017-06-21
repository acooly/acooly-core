package com.acooly.core.utils.system;

import java.lang.management.ManagementFactory;

public class Envs {

  /**
   * 获取当前进程Id
   *
   * @return
   */
  public static String getPid() {
    String name = ManagementFactory.getRuntimeMXBean().getName();
    return name.split("@")[0];
  }
}
