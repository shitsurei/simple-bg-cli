package io.github.shitsurei.dao.exception;

import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import org.springframework.security.core.AuthenticationException;

/**
 * 自定义认证异常
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/28 14:31
 */
public class CustomAuthenticationException extends AuthenticationException {
    private GlobalExceptionEnum exceptionEnum;

    public CustomAuthenticationException(GlobalExceptionEnum exceptionEnum) {
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
