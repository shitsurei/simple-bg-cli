package io.github.shitsurei.controller.filter;

import io.github.shitsurei.common.resource.GsonManager;
import io.github.shitsurei.common.util.RedisUtil;
import io.github.shitsurei.dao.constants.CustomProperties;
import io.github.shitsurei.dao.constants.RedisKeyPrefix;
import io.github.shitsurei.dao.constants.UrlPathConstant;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.exception.CustomAuthenticationException;
import io.github.shitsurei.service.handler.AuthenticationExceptionHandler;
import io.github.shitsurei.service.handler.CustomHttpServletRequestWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * 验证码核验过滤器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/27 14:49
 */
@Component
public class CaptchaAuthFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AuthenticationExceptionHandler loginExceptionHandler;

    @Autowired
    private CustomProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 登录注册接口需要验证码核验（删除全局路由前缀）
        String accessPath = request.getRequestURI().replace(properties.getContextPath(), "");
        if (properties.getCaptchaEnable() &&
                (StringUtils.equals(accessPath, UrlPathConstant.LOGIN_PATH) || StringUtils.equals(accessPath, UrlPathConstant.REGISTER_PATH))) {
            String requestBody = ((CustomHttpServletRequestWrapper) request).getBodyAsString();
            Map map = GsonManager.getInstance(false).fromJson(requestBody, Map.class);
            if (!map.containsKey("captcha")) {
                loginExceptionHandler.commence(request, response, new CustomAuthenticationException(GlobalExceptionEnum.CAPTCHA_EMPTY));
                return;
            }
            String inputCaptcha = map.get("captcha").toString();
            String redisKey = RedisKeyPrefix.SYS_USER_LOGIN_SESSION + request.getSession().getId();
            Object captcha = redisUtil.get(redisKey);
            if (Objects.isNull(captcha)) {
                loginExceptionHandler.commence(request, response, new CustomAuthenticationException(GlobalExceptionEnum.CAPTCHA_EXPIRE));
                return;
            }
            if (!StringUtils.equals(inputCaptcha, captcha.toString())) {
                loginExceptionHandler.commence(request, response, new CustomAuthenticationException(GlobalExceptionEnum.LOGIN_FAILURE));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
