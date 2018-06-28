package com.acooly.core.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * 把字符串hash后转换成64进制的字符串，可以用于短域名场景使用
 *
 * @author qiubo
 * @date 2018-06-25 13:56
 */
public class Hashs {
    private static char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    public static String hash(String text) {
        HashCode l = Hashing.murmur3_32().hashString(text, StandardCharsets.UTF_8);
        int i = l.asInt();
        return toString(i);
    }

    private static String toString(int i) {
        int radix = chars.length;
        char[] buf = new char[20];
        int charPos = 19;
        boolean negative = (i < 0);
        if (!negative) {
            i = -i;
        }
        while (i <= -radix) {
            buf[charPos--] = chars[(-(i % radix))];
            i = i / radix;
        }
        buf[charPos] = chars[(-i)];

        if (negative) {
            buf[--charPos] = '-';
        }
        return new String(buf, charPos, (20 - charPos));
    }
}
