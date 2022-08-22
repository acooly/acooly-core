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
public class ${projectName}BusinessException extends BusinessException {
    public ${projectName}BusinessException() {
    }

    public ${projectName}BusinessException(String code, String message) {
        super(code, message, "");
    }

    public ${projectName}BusinessException(String code, String message, String detail) {
        super(code, message, detail);
    }

    public ${projectName}BusinessException(Messageable messageable) {
        super(messageable);
    }

    public ${projectName}BusinessException(Messageable messageable, String msg) {
        super(messageable, msg);
    }

    public ${projectName}BusinessException(Messageable messageable, Throwable cause) {
        super(messageable, cause);
    }
}
