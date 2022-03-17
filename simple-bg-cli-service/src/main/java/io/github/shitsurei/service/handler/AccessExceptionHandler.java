package io.github.shitsurei.service.handler;

import io.github.shitsurei.common.resource.GsonManager;
import io.github.shitsurei.common.util.HttpUtil;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.exception.CustomAccessDeniedException;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * spring security 鉴权异常处理
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/23 17:03
 */
@Component
public class AccessExceptionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        if (!response.isCommitted()) {
            GlobalExceptionEnum exceptionEnum = null;
            if (accessDeniedException instanceof CustomAccessDeniedException) {
                CustomAccessDeniedException exception = (CustomAccessDeniedException) accessDeniedException;
                exceptionEnum = exception.getExceptionEnum();
            }
            ResponseResult<Object> errorResponseResult = Objects.isNull(exceptionEnum) ? ResponseUtil.buildFailureResult(GlobalExceptionEnum.NO_AUTH_ERROR) : ResponseUtil.buildFailureResult(exceptionEnum);
            HttpUtil.renderString(response, 200, GsonManager.getInstance(false).toJson(errorResponseResult));
        }
    }
}
