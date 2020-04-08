package com.acooly.core.utils.conversion;

/**
 * 如果在转换过程中发生错误时会抛出该异常。
 *
 * @author Agreal·Lee (e-mail:lixiang@yiji.com)
 */
public class TypeConversionException extends RuntimeException {

    /**
     * 版本号
     */
    private static final long serialVersionUID = -7926442618855461965L;

    public TypeConversionException() {
        super();
    }

    public TypeConversionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TypeConversionException(String msg) {
        super(msg);
    }

    public TypeConversionException(Throwable cause) {
        super(cause);
    }
}
