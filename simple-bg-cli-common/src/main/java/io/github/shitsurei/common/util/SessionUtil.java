package io.github.shitsurei.common.util;

import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.dao.pojo.bo.system.LoginUser;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * session工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/7 15:34
 */
public class SessionUtil {
    /**
     * 获取登录用户
     *
     * @return
     */
    public static SystemUser getLoginUser() {
        try {
            // 获取security上下文中的用户id
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return ((LoginUser) authentication.getPrincipal()).getSystemUser();
        } catch (ClassCastException e) {
            throw new GlobalException(GlobalExceptionEnum.LOGOUT_FAILURE);
        }
    }

    /**
     * @return HTTP请求实例
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes());
        return servletRequestAttributes.getRequest();
    }

    /**
     * @return HTTP响应实例
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes());
        return servletRequestAttributes.getResponse();
    }

    /**
     * 解析请求ip
     *
     * @param request
     * @return
     */
    public static String parseRequestHost(HttpServletRequest request) {
        return Objects.isNull(request.getHeader("x-forwarded-for")) ? request.getRemoteHost() : request.getHeader("x-forwarded-for");
    }

    /**
     * 解析请求ip（不带参数）
     *
     * @return
     */
    public static String parseRequestHost() {
        return parseRequestHost(getRequest());
    }
}
