/**
 * create by zhangpu
 * date:2015年8月28日
 */
package com.acooly.module.sms.blacklist;

import com.acooly.module.sms.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangpu
 *
 */
@Service
public class SmsBlacklistServiceImpl implements SmsBlacklistService {
	@Autowired
	private SmsProperties smsProperties;
	
	@Override
	public List<String> getAll() {
		return smsProperties.getBlacklist();
	}
	
	@Override
	public boolean inBlacklist(String mobileNo) {
		return smsProperties.getBlacklist().contains(mobileNo);
	}
	
}
