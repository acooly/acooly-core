package com.acooly.core.common.exception;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.Messageable;

/**
 * 标记事务可回滚的业务异常,配合声明式事务使用
 *
 * <p>业务系统可以根据业务需求，继承该类定义具体业务相关的业务。如：NoFoundException, ParameterInvaidException 等。
 * <p>
 * 改造BusinessException为三元消息模式:code,message,detail,推荐使用三元构造参数，废弃老的RuntimeException的构造模式（不推荐），
 * 已实现与service,facade,controller和api各层的异常处理一体化。@date 2021-03-30
 *
 * @author pu.zhang
 */
public class BusinessException extends RuntimeException implements Messageable {

    private static final long serialVersionUID = 1L;

    private Messageable errorCode = CommonErrorCodes.INTERNAL_ERROR;

    private String code = CommonErrorCodes.INTERNAL_ERROR.code();

    private String message = CommonErrorCodes.INTERNAL_ERROR.message();

    private String detail;

    public BusinessException() {
        super();
    }

    @Deprecated
    public BusinessException(String message) {
        this(CommonErrorCodes.INTERNAL_ERROR.getCode(), message, message);
    }

    /**
     * @param message            错误消息
     * @param writableStackTrace 是否收集线程栈信息，对于业务明确的异常，请关闭,设置为false为关闭
     */
    @Deprecated
    public BusinessException(String message, boolean writableStackTrace) {
        this(CommonErrorCodes.INTERNAL_ERROR.getCode(), message, message);
    }

    /**
     * @param message            错误消息
     * @param writableStackTrace 是否收集线程栈信息，对于业务明确的异常，请关闭,设置为false为关闭
     */
    @Deprecated
    public BusinessException(String message, String code, boolean writableStackTrace) {
        this(code, message, message);
    }

    @Deprecated
    public BusinessException(String message, Throwable cause) {
        this(CommonErrorCodes.INTERNAL_ERROR.getCode(), message, message);
    }

    @Deprecated
    public BusinessException(Throwable cause) {
        this(CommonErrorCodes.INTERNAL_ERROR, cause.getMessage());
    }

    @Deprecated
    public BusinessException(Throwable cause, String code) {
        this(code, cause.getMessage(), cause.getMessage());
    }

    @Deprecated
    public BusinessException(String message, String code) {
        this(code, message, message);
    }

    @Deprecated
    public BusinessException(String message, Throwable cause, String code) {
        this(code, message, cause.getMessage());
    }

    public BusinessException(String code, String message, String detail) {
        super(detail);
        this.code = code;
        this.message = message;
        this.errorCode = new Messageable() {
            @Override
            public String code() {
                return code;
            }
            @Override
            public String message() {
                return message;
            }
            @Override
            public String toString() {
                return this.code()+":"+this.message()
            }
        };
        this.detail = detail;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.errorCode = new Messageable() {
            @Override
            public String code() {
                return code;
            }

            @Override
            public String message() {
                return message;
            }

            @Override
            public String toString() {
                return this.code()+":"+this.message()
            }
        };
        this.detail = cause.getMessage();
    }


    public BusinessException(Messageable messageable) {
        super(messageable.message());
        this.errorCode = messageable;
        this.code = messageable.code();
        this.message = messageable.message();
    }

    public BusinessException(Messageable messageable, String detail) {
        this(messageable);
        this.detail = detail;
    }

    public BusinessException(Messageable messageable, Throwable cause) {
        super(messageable.message(), cause);
        this.errorCode = messageable;
        this.code = messageable.code();
        this.message = messageable.message();
        this.detail = cause.getMessage();
    }

    @Deprecated
    public void setCode(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }

    public Messageable getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.code).append(":").append(this.message);
        if (Strings.isNoneBlank(this.detail)) {
            sb.append(":").append(this.detail);
        }
        return sb.toString();
    }
}
