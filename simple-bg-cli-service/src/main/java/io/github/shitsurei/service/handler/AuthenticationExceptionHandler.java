package io.github.shitsurei.service.handler;

import io.github.shitsurei.common.resource.GsonManager;
import io.github.shitsurei.common.util.HttpUtil;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.exception.CustomAuthenticationException;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * spring security 认证异常处理
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/23 16:10
 */
@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        if (!response.isCommitted()) {
            GlobalExceptionEnum exceptionEnum = null;
            if (authException instanceof CustomAuthenticationException) {
                CustomAuthenticationException exception = (CustomAuthenticationException) authException;
                exceptionEnum = exception.getExceptionEnum();
            }
            ResponseResult<Object> errorResponseResult = Objects.isNull(exceptionEnum) ? ResponseUtil.buildFailureResult(GlobalExceptionEnum.LOGIN_STATUS_EXPIRE) : ResponseUtil.buildFailureResult(exceptionEnum);
            HttpUtil.renderString(response, 200, GsonManager.getInstance(false).toJson(errorResponseResult));
        }
    }
}
