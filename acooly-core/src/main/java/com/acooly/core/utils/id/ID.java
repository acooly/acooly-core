/*
 * www.yiji.com Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2017-03-21 14:09 创建
 */
package com.acooly.core.utils.id;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import sun.jvmstat.monitor.MonitoredHost;

import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class ID {
	private final static char[] digits = {	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
											'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
											'u', 'v', 'w', 'x', 'y', 'z' };
	private final static int RADIX = digits.length;
	private final static Long BEGIN = 1451606400L;
	/**
	 * 每秒内生成id最大数
	 */
	private static final int COUNT_IN_SECOND = 1000;
	
	private static AtomicLong lastTime = new AtomicLong(0);
	private static final int MACHINE_IDENTIFIER;
	private static final int PROCESS_IDENTIFIER;
	private static final int LOW_ORDER_THREE_BYTES = 0x00ffffff;
	private static long nodeId = 0;
	
	/**
	 * 生成长度(len=14)的唯一id
	 */
	public static String newID() {
		StringBuilder sb = new StringBuilder();
		Date now = new Date();
		long time = createTime(now.getTime() / 1000 - BEGIN);
		sb.append(toString(nodeId));
		sb.append(toString(time));
		return Strings.leftPad(sb.toString(), 14, '0');
	}

    /**
     * 生成长度(len==20)的唯一数字id
     */
	public static String newIDDigital() {
		StringBuilder sb = new StringBuilder();
		Date now = new Date();
		long time = createTime(now.getTime() / 1000 - BEGIN);
		sb.append(nodeId);
		sb.append(time);
        return Strings.leftPad(sb.toString(), 20, '0');
	}
	
	/**
	 * 根据当前时间获取到唯一的时间标识
	 * @param currentTime 当前时间:秒
	 * @return 时间标识
	 */
	private static long createTime(long currentTime) {
		long timeMillis = currentTime * COUNT_IN_SECOND;
		while (true) {
			long last = lastTime.get();
			if (timeMillis > last) {
				if (lastTime.compareAndSet(last, timeMillis)) {
					break;
				}
			} else {
				if (lastTime.compareAndSet(last, last + 1)) {
					timeMillis = last + 1;
					break;
				}
			}
		}
		return timeMillis;
	}
	
	public static String toString(long i) {
		char[] buf = new char[65];
		int charPos = 64;
		boolean negative = (i < 0);
		
		if (!negative) {
			i = -i;
		}
		
		while (i <= -RADIX) {
			buf[charPos--] = digits[(int) (-(i % RADIX))];
			i = i / RADIX;
		}
		buf[charPos] = digits[(int) (-i)];
		
		if (negative) {
			buf[--charPos] = '-';
		}
		return new String(buf, charPos, (65 - charPos));
	}
	
	static {
		try {
			MACHINE_IDENTIFIER = createMachineIdentifier();
			PROCESS_IDENTIFIER = createProcessIdentifier();
			try {
				int idx = getProcessIdx();
				nodeId = MACHINE_IDENTIFIER * 100 + idx;
				log.info("id生成器节点编码:{}", nodeId);
			} catch (Exception e) {
				log.warn("id生成器降级:{}", e.getMessage());
				nodeId = (MACHINE_IDENTIFIER + PROCESS_IDENTIFIER * 7) * 10 + new SecureRandom().nextInt(10);
				log.info("id生成器节点编码:{}", nodeId);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static int createMachineIdentifier() {
		// build a 2-byte machine piece based on NICs info
		int machinePiece;
		try {
			StringBuilder sb = new StringBuilder();
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface ni = e.nextElement();
				sb.append(ni.toString());
				byte[] mac = ni.getHardwareAddress();
				if (mac != null) {
					ByteBuffer bb = ByteBuffer.wrap(mac);
					try {
						sb.append(bb.getChar());
						sb.append(bb.getChar());
						sb.append(bb.getChar());
					} catch (BufferUnderflowException shortHardwareAddressException) { //NOPMD
						// mac with less than 6 bytes. continue
					}
				}
			}
			machinePiece = sb.toString().hashCode();
		} catch (Throwable t) {
			// exception sometimes happens with IBM JVM, use random
			machinePiece = (new SecureRandom().nextInt());
			log.warn("Failed to get machine identifier from network interface, using random number instead", t);
		}
		machinePiece = machinePiece & LOW_ORDER_THREE_BYTES;
		return machinePiece;
	}
	
	// Creates the process identifier.  This does not have to be unique per class loader because
	// NEXT_COUNTER will provide the uniqueness.
	public static int createProcessIdentifier() {
		int processId;
		try {
			String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
			if (processName.contains("@")) {
				processId = Integer.parseInt(processName.substring(0, processName.indexOf('@')));
			} else {
				processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
			}
			
		} catch (Throwable t) {
			processId = (short) new SecureRandom().nextInt();
			log.warn("Failed to get process identifier from JMX, using random number instead");
		}
		
		return processId;
	}
	
	public static int getProcessIdx() throws Exception {
		MonitoredHost local = MonitoredHost.getMonitoredHost("localhost");
		List<Integer> list = Lists.newArrayList();
		list.addAll(local.activeVms());
		Collections.sort(list);
		return list.indexOf(PROCESS_IDENTIFIER);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(createProcessIdentifier());
		System.out.println("mac:" + createMachineIdentifier());

		String id = Ids.gid();
		System.out.println("len:" + id.length() + "    " + id);
        String idDigital = newIDDigital();
        System.out.println("len:" + idDigital.length() + "    " + idDigital);
		
		//		for (int i = 0; i < 100; i++) {
		//			System.out.println(newID());
		//		}
		
	}
}
