package com.acooly.core.utils;

import com.acooly.core.utils.system.Hex;
import com.acooly.core.utils.system.IPUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Id生成器
 *
 * <p>简单实现：支持分布式环境，单机秒级并发9999
 *
 * @author zhangpu
 */
public class Ids {

    public static final char PADCHAR = '0';
    private static final short PROCESS_IDENTIFIER;

    static {
        try {
            PROCESS_IDENTIFIER = ObjectId.getGeneratedProcessIdentifier();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        sb.append(padding(reserved, 8));
        sb.append(ObjectId.get().toHexString());
        return sb.toString();
    }

    public static String gid(String systemCode) {
        return gid(systemCode, null);
    }

    /**
     * 长度24位全球唯一标识
     */
    public static String gid() {
        return "G" + new ObjectId().toHexString();
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
        return Strings.leftPad(txt, size, PADCHAR);
    }

    /**
     * ID生成器
     *
     *
     *
     * <p>20字符长度 yyMMddHHmmss+3位本机IP末三位+5位随机数字
     *
     * @author zhangpu
     */
    public static class Did {
        private static final Logger logger = LoggerFactory.getLogger(Ids.class);
        private static final int MIN_LENGTH = 20;
        private static final int SEQU_LENGTH = 5;
        private static final int SEQU_MAX = 9999;
        private static Did did = new Did();
        private static String pidStr = null;
        private AtomicLong sequence = new AtomicLong(1);
        private String nodeFlag;
        private Object nodeFlagLock = new Object();

        private Did() {
            super();
        }

        public static Did getInstance() {
            return did;
        }

        private static short short2(final short x) {
            short b = (short) (x % (short) 100);
            return b <= 10 ? 10 : b;
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
         * @param size 位数需大于20
         */
        public String getId(int size) {
            if (size < MIN_LENGTH) {
                throw Exceptions.runtimeException("did最小长度为" + MIN_LENGTH);
            }
            StringBuilder sb = new StringBuilder();
            // 当前时间(12位)
            sb.append(format(new Date(), "yyMMddHHmmss"));
            // 随机数字(size-18位)
            sb.append(RandomNumberGenerator.getNewString((size - MIN_LENGTH)));
            // 进程id(4位)
            sb.append(getPid());
            // 序列号(4位)
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

        public String getSequ() {

            long timeCount = 0;
            while (true) {
                timeCount = sequence.get();
                if (sequence.compareAndSet(SEQU_MAX, 1)) {
                    timeCount = 1;
                    break;
                }
                if (sequence.compareAndSet(timeCount, timeCount + 1)) {
                    break;
                }
            }
            return Strings.leftPad(String.valueOf(timeCount), SEQU_LENGTH, '0');
        }

        private synchronized String getNodeFlag() {
            if (this.nodeFlag == null) {
                this.nodeFlag = generateNodeFlag();
            }
            return this.nodeFlag;
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
        private String generateNodeFlag() {
            String result = null;
            try {
                String ip = IPUtil.getFirstNoLoopbackIPV4Address();
                if (Strings.isNotBlank(ip)) {
                    result = Strings.substringAfterLast(ip, ".");
                } else {
                    result = Strings.right(IPUtil.getHostName(), 3);
                }
                result = StringUtils.leftPad(result, 3, "0");
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
            //IntStream.range(0, random.length)
//             .forEach(
//                    i -> {
//                        final int index = Math.abs(random[i] % PRINTABLE_CHARACTERS.length);
//                        output[i] = PRINTABLE_CHARACTERS[index];
//                    });
            for (int i = 0; i < random.length; i++) {
                final int index = Math.abs(random[i] % PRINTABLE_CHARACTERS.length);
                output[i] = PRINTABLE_CHARACTERS[index];
            }

            return new String(output);
        }
    }

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(date);
    }

    private static SimpleDateFormat getSimpleDateFormat(String defaultFormat) {
        if (Strings.isBlank(defaultFormat)) {
            defaultFormat = DEFAULT_DATE_FORMAT;
        }
        return new SimpleDateFormat(defaultFormat);
    }

    public static String format(Date date) {
        return format(date, DEFAULT_DATE_FORMAT);
    }


}
