#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * ${rootArtifactId}
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author ${author}
 * @date 2019-12-06 01:36
 */
package ${package}.common.exception;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.enums.Messageable;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目基础异常
 * 继承与框架BusinessException，便于框架自动化异常处理。
 *
 * @author ${author}
 * @date 2019-12-06 01:36
 */
@Slf4j
public class AllinoneBusinessException extends BusinessException {

    public AllinoneBusinessException() {
    }

    public AllinoneBusinessException(String message) {
        super(message);
    }

    public AllinoneBusinessException(String message, boolean writableStackTrace) {
        super(message, writableStackTrace);
    }

    public AllinoneBusinessException(String message, String code, boolean writableStackTrace) {
        super(message, code, writableStackTrace);
    }

    public AllinoneBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public AllinoneBusinessException(Throwable cause) {
        super(cause);
    }

    public AllinoneBusinessException(String message, String code) {
        super(message, code);
    }

    public AllinoneBusinessException(String message, Throwable cause, String code) {
        super(message, cause, code);
    }

    public AllinoneBusinessException(Throwable cause, String code) {
        super(cause, code);
    }

    public AllinoneBusinessException(Messageable messageable) {
        super(messageable);
    }

    public AllinoneBusinessException(Messageable messageable, String msg) {
        super(messageable, msg);
    }

    public AllinoneBusinessException(Messageable messageable, Throwable cause) {
        super(messageable, cause);
    }
}
