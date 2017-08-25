/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-01-09 15:07 创建
 */
package com.acooly.core.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/** @author qiubo@yiji.com */
public class Ports {
  public static boolean isPortUsing(int port) {
    boolean flag = false;
    InetAddress theAddress;
    try {
      theAddress = InetAddress.getByName("127.0.0.1");
    } catch (UnknownHostException e) {
      return true;
    }
    try {
        Socket socket = new Socket(theAddress, port);
        socket.close();
        flag = true;
    } catch (IOException e) {
      //do nothing
    }
    return flag;
  }
}
