/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-09 15:07 创建
 */
package com.acooly.core.utils;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.net.ServerSocket;

/** @author qiubo@yiji.com */
public class Ports {
  public static boolean isPortUsing(int port) {
    try {
      ServerSocket serverSocket =
          ServerSocketFactory.getDefault()
              .createServerSocket(port, 1, InetAddress.getByName("localhost"));
      serverSocket.close();
      return false;
    } catch (Exception ex) {
      return true;
    }
  }
}
