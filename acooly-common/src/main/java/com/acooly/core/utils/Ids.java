package com.acooly.core.utils;

import com.acooly.core.utils.system.Hex;
import com.acooly.core.utils.system.IPUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Id生成器
 *
 * <p>简单实现：支持分布式环境，单机秒级并发9999
 *
 * @author zhangpu
 */
public class Ids {

    public static final char PAD_CHAR = '0';
    private static final short PROCESS_IDENTIFIER;

    static {
        try {
            PROCESS_IDENTIFIER = ObjectId.getGeneratedProcessIdentifier();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String newId15() {
        return Did.getInstance().newId15();
    }

    /**
     * MongodbId生成方案
     * 长度24，格式：HEX
     *
     * @return
     */
    public static String mid() {
        return ObjectId.get().toHexString();
    }

    // ******* 特定场景ID ***********//

    /**
     * 生产GID
     *
     *
     *
     * <p>长度：36 格式：系统编码(4字节)+保留(8字节)+gid(24字节)
     *
     * @return
     */
    public static String gid(String systemCode, String reserved) {
        StringBuilder sb = new StringBuilder();
        sb.append(padding(systemCode, 4));
        if (Strings.isNotBlank(reserved)) {
            sb.append(padding(reserved, 8));
        }
        sb.append(Did.getInstance().getId());
        return sb.toString();
    }

    public static String gid(String systemCode) {
        return gid(systemCode, null);
    }

    /**
     * 长度24位全球唯一标识
     */
    public static String gid() {
        return "G" + Did.getInstance().getId();
    }

    /**
     * 统一不重复内部订单号
     *
     * @param systemCode 4字节系统编码
     * @return 24字节不重复编码
     */
    public static String oid(String systemCode) {
        StringBuilder sb = new StringBuilder();
        sb.append(padding(systemCode, 4));
        sb.append(Did.getInstance().getId(20));
        return sb.toString();
    }

    public static String oid() {
        StringBuilder sb = new StringBuilder();
        sb.append("O");
        sb.append(Did.getInstance().getId(20));
        return sb.toString();
    }

    /**
     * 获取分布式Id
     *
     * @return 默认20字符长度的数字不重复编码
     */
    public static String getDid() {
        return did();
    }

    public static String did() {
        return Did.getInstance().getId();
    }

    public static String did(int size) {
        return Did.getInstance().getId(size);
    }

    /**
     * @param size 位数需大于20
     */
    public static String getDid(int size) {
        return Did.getInstance().getId(size);
    }

    public static String getDid(String prefix) {
        return prefix + Did.getInstance().getId();
    }

    private static String padding(String text, int size) {
        String txt = Strings.trimToEmpty(text);
        if (Strings.length(txt) > size) {
            txt = Strings.substring(txt, Strings.length(txt) - size);
        }
        return Strings.leftPad(txt, size, PAD_CHAR);
    }

    /**
     * ID生成器
     *
     * <p>20字符长度 yyMMddHHmmss+3位本机IP末三位+(大于24:PID)+5位并发计数器
     *
     * @author zhangpu
     * @date 2020-1-17 重构
     */
    public static class Did {

        private static final Logger logger = LoggerFactory.getLogger(Ids.class);
        private static final int MIN_LENGTH = 20;
        private static final int SEQU_LENGTH = 4;
        private static final int SEQU_MAX = Double.valueOf(Math.pow(10, SEQU_LENGTH) - 1).intValue();
        private static final char PAD_CHAR = '0';
        private static final int PID_LENGTH = 4;
        private static final int IP_LENGTH = 4;
        private static final String DEFAULT_DATE_FORMAT = "yyMMddHHmmss";

        private static Lock lock = new ReentrantLock();
        private static Did did = new Did();
        private static String pidStr;
        private static String ipStr;
        private volatile int sequ = 0;

        private Did() {
            super();
        }

        public static Did getInstance() {
            return did;
        }

        /**
         * 15位唯一ID
         *
         * @return
         */
        public String newId15() {
            StringBuilder sb = new StringBuilder();
            // 固定：当前时间(7位)
            sb.append(getCompressTimestamp());
            // 固定：考虑在容器中PID无效，恢复为IP后两段(4位)
            sb.append(getIp());
            // 固定：序列号(4位)
            sb.append(getSequ());
            return Strings.upperCase(sb.toString());
        }


        /**
         * 生产新Id(20位)
         *
         * @return
         */
        public String getId() {
            return getId(MIN_LENGTH);
        }


        /**
         * 生产新Id
         *
         * @param size最少20位
         */
        public String getId(int size) {
            if (size < MIN_LENGTH) {
                size = MIN_LENGTH;
            }
            StringBuilder sb = new StringBuilder();
            // 固定：当前时间(12位)
            sb.append(getTimestamp());
            // 固定：考虑在容器中PID无效，恢复为IP后两段(4位)
            sb.append(getIp());
            // 动态：如果size > MIN_LENGTH(20),可选加入随机数或PID
            int randomLength = size - MIN_LENGTH;
            if (randomLength >= PID_LENGTH) {
                randomLength = randomLength - PID_LENGTH;
                sb.append(getPid());
            }
            if (randomLength > 0) {
                sb.append(RandomNumberGenerator.getNewString(randomLength));
            }
            // 固定：序列号(4位)
            sb.append(getSequ());
            return sb.toString();
        }

        /**
         * 获取两位pid
         */
        private String getPid() {
            if (pidStr == null) {
                StringBuilder sb = new StringBuilder();
                Hex.append(sb, PROCESS_IDENTIFIER);
                pidStr = Strings.upperCase(sb.toString());
            }
            return pidStr;
        }

        /**
         * 获取节点IP后缀4位
         *
         * @return
         */
        private String getIp() {
            if (ipStr == null) {
                ipStr = generateIpPostfix();
            }
            return ipStr;
        }

        /**
         * 计数器
         *
         * @return
         */
        public String getSequ() {
            try {
                lock.lock();
                if (sequ++ == SEQU_MAX) {
                    sequ = 0;
                }
                return Strings.leftPad(String.valueOf(sequ), SEQU_LENGTH, PAD_CHAR);
            } finally {
                lock.unlock();
            }
        }

        /**
         * 获取当前时间戳
         *
         * @return
         */
        public String getTimestamp() {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            return sdf.format(new Date());
        }

        /**
         * 压缩的时间戳
         * （7字符）
         *
         * @return
         */
        public String getCompressTimestamp() {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR) - 2000;
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            int minuteSecond = Integer.valueOf((minute < 10 ? "0" + minute : "" + minute) + (second < 10 ? "0" + second : "" + second));
            StringBuilder sb = new StringBuilder();
            // 年：1字符，32进制的2000年后2位年（目前22年，最大支持2032年）
            sb.append(Integer.toString(year, 32));
            // 月：1字符，16进制（最大12）
            sb.append(Integer.toString(month, 16));
            // 日：1字符，32进制（最大31）
            sb.append(Integer.toString(day, 32));
            // 时：1字符，32进制（最大24）
            sb.append(Integer.toString(hour, 32));
            // 分秒：3字符，32进制（mmss格式，最大：5959：5q7）
            String ms = Integer.toString(minuteSecond, 32);
            if (Strings.length(ms) < 3) {
                ms = Strings.leftPad(ms, 3, "0");
            }
            sb.append(Integer.toString(minuteSecond, 32));
            return sb.toString();
        }

        /**
         * 简单节点编码
         *
         *
         *
         * <p>逻辑：Ip地址后三位，便于快速知道是哪个节点
         *
         * @return
         */
        private String generateIpPostfix() {
            String result = null;
            try {
                String ip = IPUtil.getFirstNoLoopbackIPV4Address();
                if (Strings.isNotBlank(ip)) {
                    String[] ips = Strings.split(ip, ".");
                    short[] ipSegment = new short[2];
                    if (ips.length == IP_LENGTH) {
                        ipSegment[0] = Short.valueOf(ips[2]);
                        ipSegment[1] = Short.valueOf(ips[3]);
                    }
                    result = ipSegmentToHex(ipSegment);
                }
                if (Strings.isBlank(result)) {
                    result = Strings.right(IPUtil.getHostName(), IP_LENGTH);
                }
                result = Strings.upperCase(result);
                result = StringUtils.leftPad(result, IP_LENGTH, PAD_CHAR);
            } catch (Exception e) {
                logger.warn("生产DID要素本机IP获取失败:" + e.getMessage());
            }
            return result;
        }

        private String ipSegmentToHex(short[] segment) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < segment.length; i++) {
                String hex = Integer.toHexString(segment[i] & 0xFF);
                if (hex.length() < 2) {
                    sb.append(PAD_CHAR);
                }
                sb.append(hex);
            }
            return sb.toString();
        }
    }

    public static class RandomNumberGenerator {

        private static final SecureRandom randomizer = new SecureRandom();

        private static final char[] PRINTABLE_CHARACTERS = "0123456789".toCharArray();

        public RandomNumberGenerator() {
        }

        public static String getNewString(int maximumRandomLength) {
            final byte[] random = getNewStringAsBytes(maximumRandomLength);
            return convertBytesToString(random);
        }

        private static byte[] getNewStringAsBytes(int maximumRandomLength) {
            final byte[] random = new byte[maximumRandomLength];
            randomizer.nextBytes(random);
            return random;
        }

        private static String convertBytesToString(final byte[] random) {
            final char[] output = new char[random.length];
            for (int i = 0; i < random.length; i++) {
                final int index = Math.abs(random[i] % PRINTABLE_CHARACTERS.length);
                output[i] = PRINTABLE_CHARACTERS[index];
            }

            return new String(output);
        }
    }


}
