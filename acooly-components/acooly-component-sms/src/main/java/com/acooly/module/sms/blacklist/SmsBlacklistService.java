/** create by zhangpu date:2015年8月28日 */
package com.acooly.module.sms.blacklist;

import java.util.List;

/** @author zhangpu */
public interface SmsBlacklistService {

  List<String> getAll();

  boolean inBlacklist(String mobileNo);
}
