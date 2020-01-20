package com.acooly.core.utils;

import com.acooly.core.utils.system.Hex;
import com.acooly.core.utils.system.IPUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
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
        return Did.getInstance().getId();
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
        private static final int SEQU_LENGTH = 5;
        private static final int SEQU_MAX = Double.valueOf(Math.pow(10, SEQU_LENGTH) - 1).intValue();
        private static final char PAD_CHAR = '0';
        private static final int PID_LENGTH = 4;
        private static final String DEFAULT_DATE_FORMAT = "yyMMddHHmmss";

        private static Lock lock = new ReentrantLock();
        private static LongAdder counter = new LongAdder();
        private volatile int sequ = 0;
        private static Did did = new Did();
        private static String pidStr = "0";
        private static String ipStr;

        private Did() {
            super();
        }

        public static Did getInstance() {
            return did;
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
            // 当前时间(12位)
            sb.append(getTimestamp());
            // 考虑在容器中PID无效，恢复为IP最后3位(3位)
            sb.append(getIp());
            // 如果size > MIN_LENGTH(20),可选加入随机数或PID
            int randomLength = size - MIN_LENGTH;
            if (randomLength >= PID_LENGTH) {
                randomLength = randomLength - PID_LENGTH;
                sb.append(getPid());
            }
            sb.append(RandomNumberGenerator.getNewString(randomLength));
            // 序列号(5位)
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
         * 获取节点IP后缀3位
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
                    result = Strings.substringAfterLast(ip, ".");
                } else {
                    result = Strings.right(IPUtil.getHostName(), 3);
                }
                result = StringUtils.leftPad(result, 3, PAD_CHAR);
            } catch (Exception e) {
                logger.warn("生产DID要素本机IP获取失败:" + e.getMessage());
            }
            return result;
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
