package io.github.shitsurei.dao.exception;

import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import org.springframework.security.access.AccessDeniedException;

/**
 * 自定义授权异常
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/28 14:53
 */
public class CustomAccessDeniedException extends AccessDeniedException {
    private GlobalExceptionEnum exceptionEnum;

    public CustomAccessDeniedException(GlobalExceptionEnum exceptionEnum) {
        super(exceptionEnum.getErrorMsg());
        this.exceptionEnum = exceptionEnum;
    }

    public GlobalExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

    public void setExceptionEnum(GlobalExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }
}
