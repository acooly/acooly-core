package com.acooly.core.utils;

import com.google.common.base.Splitter;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * <p>
 * <li>继承自Apache Commons StringUtils</li>
 * <li>提供一些额外的常用字符串处理方法，包括：mask,pinyin,驼峰,首/尾字等</li>
 */
public class Strings extends StringUtils {
    private static final char UNDERLINE = '_';
    private static UrlValidator httpUrlValidator = new UrlValidator(new String[]{"http", "https"});
    private static int ONE_WORD_LEN = 1;


    /**
     * 字符串首字符
     *
     * @param text 字符串
     * @return 首字符
     */
    public static String first(String text) {
        return Strings.left(text, ONE_WORD_LEN);
    }


    /**
     * 汉字转拼音
     *
     * @param hanzi 汉字字符串
     * @return 拼音字符串
     */
    public static String toPinyin(String hanzi) {
        if (Strings.isBlank(hanzi)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        String[] wordArray;
        for (char c : hanzi.toCharArray()) {
            wordArray = PinyinHelper.toHanyuPinyinStringArray(c);
            if (null != wordArray) {
                sb.append(wordArray[0].replaceAll("\\d", ""));
            }
        }
        return Strings.upperCase(sb.toString());
    }

    /**
     * 汉字转拼音首字母
     *
     * @param hanzi 汉字字符串
     * @return 每个汉子拼音首字母组成的字符串
     */
    public static String toPinyinFistWord(String hanzi) {
        if (Strings.isBlank(hanzi)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        String[] oneWord;
        for (char c : hanzi.toCharArray()) {
            oneWord = PinyinHelper.toHanyuPinyinStringArray(c);
            if (oneWord != null && oneWord.length > 0) {
                sb.append(oneWord[0].charAt(0));
            }
        }
        return Strings.upperCase(sb.toString());
    }

    /**
     * 汉字字符串首字的首字母
     *
     * @param hanzi 汉字字符串
     * @return 汉字字符串首字的首字母，单个字母
     */
    public static String toPinyinFistLetter(String hanzi) {
        return Strings.substring(toPinyinFistWord(hanzi), 0, 1);
    }

    /**
     * 判断是否HTTP的URL
     *
     * @param url 链接字符串
     * @return true:是HTTP的URL，false:不是HTTP的URL
     */
    public static boolean isHttpUrl(String url) {
        return httpUrlValidator.isValid(url);
    }

    /**
     * 驼峰转下划线
     *
     * @param param 驼峰格式的字符串
     * @return 下划线分割的字符串
     */
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

    /**
     * 判断空值设置默认值
     *
     * @param text         被判断的值（可以是非字符串对象）
     * @param defaultValue 如果text为空字符或空对象时，设置的默认值
     * @param <T>          被判断的值的泛型类型
     * @return 结果值
     */
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

    /**
     * 判断正则匹配
     * <p>
     * 迁移到`Regexs`类中，建议直接使用`Regexs`类相关方法
     *
     * @param regex 正则表达式
     * @param value 被判断的字符串
     * @return 是否匹配（true:匹配）
     * @see Regexs
     */
    @Deprecated
    public static boolean matcher(String regex, String value) {
        Pattern p = Pattern.compile(regex);
        return p.matcher(value).matches();
    }

    /**
     * 前置和后置mask（*）
     *
     * @param text     文本
     * @param preSize  前置mask长度
     * @param postSize 后置mask长度
     * @return mask后的文本
     */
    public static String mask(String text, int preSize, int postSize) {
        return mask(text, preSize, postSize, '*');
    }

    /**
     * 前置和后置mask
     *
     * @param text        文本
     * @param preSize     前置mask长度
     * @param postSize    后置mask长度
     * @param replaceChar 替换字符
     * @return mask后的文本
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

    /**
     * 预定义mask
     *
     * @param text     文本
     * @param maskType mask类型
     * @return mask后的文本
     */
    public static String mask(String text, MaskType maskType) {
        Asserts.notEmpty(text);
        Asserts.notNull(maskType);
        if (maskType == MaskType.Email) {
            return maskEmail(text);
        } else {
            return maskReverse(text, maskType.start, maskType.end);
        }

    }

    /**
     * 用户名掩码处理
     *
     * @param text 用户名
     * @return 掩码后的用户名
     */
    public static String maskUserName(String text) {
        return maskReverse(text, 2, 1);
    }

    /**
     * 银行卡号掩码处理
     *
     * @param text 银行卡号
     * @return 掩码后的银行卡号
     */
    public static String maskBankCardNo(String text) {
        return maskReverse(text, 4, 3);
    }

    /**
     * 身份证号掩码处理
     *
     * @param text 身份证号
     * @return 掩码后的身份证号
     */
    public static String maskIdCardNo(String text) {
        return maskReverse(text, 3, 4);
    }

    /**
     * 手机号掩码处理
     *
     * @param text 手机号
     * @return 掩码后的手机号
     */
    public static String maskMobileNo(String text) {
        return maskReverse(text, 3, 3);
    }

    /**
     * 邮箱掩码处理
     *
     * @param text 邮箱
     * @return 掩码后的邮箱
     */
    public static String maskEmail(String text) {
        if (Strings.isNotBlank(text) && Strings.contains(text, "@")) {
            List<String> parts = Splitter.on("@").omitEmptyStrings().splitToList(text);
            if (parts != null && parts.size() == 2) {
                return maskReverse(parts.get(0), 2, 0) + "@" + parts.get(1);
            }
        }
        return maskReverse(text, 2, 3);
    }

    /**
     * mask翻转模式(*)
     *
     * @param text  原始字符串
     * @param start 保留的前缀长度
     * @param end   保留的后缀长度
     * @return mask后的字符串
     */
    public static String maskReverse(String text, int start, int end) {
        return maskReverse(text, start, end, '*');
    }

    /**
     * mask翻转模式
     *
     * @param text        原始字符串
     * @param start       保留的前缀长度
     * @param end         保留的后缀长度
     * @param replaceChar mask的字符，可以空，默认为‘*’
     * @return mask后的字符串
     */
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
     * @return true:数字/false:非全数字
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

    /**
     * mask 类型
     */
    public static enum MaskType {
        All("全Mask", 0, 0),
        UserName("用户名", 2, 1),
        IdCardNo("身份证", 3, 4),
        BankCardNo("银行卡", 4, 3),
        MobileNo("手机号", 3, 3),
        Email("邮箱", 2, 3);

        private final String title;
        private final int start;
        private final int end;

        MaskType(String title, int start, int end) {
            this.title = title;
            this.start = start;
            this.end = end;
        }
    }

}
