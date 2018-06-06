package com.acooly.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class Strings extends StringUtils {
    private static final char UNDERLINE = '_';

    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static <T> T isBlankDefault(T text, T defaultValue) {
        if (text == null) {
            return defaultValue;
        }
        if (text.getClass().isAssignableFrom(String.class)) {
            return Strings.isBlank((String) text) ? defaultValue : text;
        } else {
            return text;
        }
    }

    public static boolean matcher(String regex, String value) {
        Pattern p = Pattern.compile(regex);
        return p.matcher(value).matches();
    }

    /**
     * 前置和后置mask
     *
     * @param text
     * @param preSize
     * @param postSize
     * @return
     */
    public static String mask(String text, int preSize, int postSize) {
        return mask(text, preSize, postSize, '*');
    }

    /**
     * 前置和后置mask
     *
     * @param text
     * @param preSize
     * @param postSize
     * @param replaceChar
     * @return
     */
    public static String mask(String text, int preSize, int postSize, Character replaceChar) {
        String source = trimToEmpty(text);
        if (isBlank(source)) {
            return text;
        }
        if (replaceChar == null) {
            replaceChar = '*';
        }
        if (preSize + postSize >= text.length()) {
            return leftPad("", text.length(), replaceChar);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(leftPad("", preSize, replaceChar));
        sb.append(substring(text, preSize, text.length() - postSize));
        sb.append(leftPad("", postSize, replaceChar));
        return sb.toString();
    }

    public static String maskUserName(String text) {
        return maskReverse(text, 2, 1);
    }

    public static String maskBankCardNo(String text) {
        return maskReverse(text, 4, 3);
    }

    public static String maskIdCardNo(String text) {
        return maskReverse(text, 3, 4);
    }

    public static String maskMobileNo(String text) {
        return maskReverse(text, 3, 3);
    }

    public static String maskReverse(String text, int start, int end) {
        return maskReverse(text, start, end, '*');
    }

    public static String maskReverse(String text, int start, int end, Character replaceChar) {
        return maskReverse(text, start, end, '*', 0);
    }

    /**
     * mask翻转模式
     *
     * <p>制定不mask的前缀和后缀长度
     *
     * @param text        原始字符串
     * @param start       保留的前缀长度
     * @param end         保留的后缀长度
     * @param replaceChar mask的字符，可以空，默认为‘*’
     * @param maskLength  mask的长度，0表示根据text计算的实际长度，大于0，则表示根据指定长度显示
     * @return mask后的字符串
     */
    public static String maskReverse(
            String text, int start, int end, Character replaceChar, int maskLength) {
        String source = trimToEmpty(text);
        if (isBlank(source)) {
            return source;
        }
        if (replaceChar == null) {
            replaceChar = '*';
        }

        // 左右保留的长度大于source本身长度，表示全部都不mask
        if (start >= source.length() || end >= source.length() || source.length() - start - end < 0) {
            return source;
        }

        int mLength = text.length() - start - end;
        mLength = maskLength == 0 ? mLength : maskLength;
        return left(text, start) + leftPad("", mLength, replaceChar) + right(text, end);
    }

    /**
     * 是否数字
     *
     * <p>包括：小数
     *
     * @param cs
     * @return
     */
    public static boolean isNumber(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(cs.charAt(i)) == false && cs.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

   /* public static void main(String[] args) {
        String text = "zhangpu";
        System.out.println("用户名: " + maskReverse(text, 6, 0, '*', 5));
        text = "510221198209476371";
        System.out.println("身份证: " + maskReverse(text, 3, 4, '*'));
        text = "13896177630";
        System.out.println("手机号: " + maskReverse(text, 3, 3, '*'));
        text = "6221880231092323876";
        System.out.println("银行卡: " + maskReverse(text, 4, 3, '*', 3));
    }*/
}
