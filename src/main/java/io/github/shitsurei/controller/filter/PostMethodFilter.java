package io.github.shitsurei.controller.filter;

import io.github.shitsurei.dao.constants.CustomProperties;
import io.github.shitsurei.dao.constants.UrlPathConstant;
import io.github.shitsurei.service.handler.CustomHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 需要对post或者put请求进行数据权限验证时的filter
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/3/14 17:08
 */
@Slf4j
@Component
public class PostMethodFilter extends OncePerRequestFilter {

    @Autowired
    private CustomProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        CustomHttpServletRequestWrapper customHttpServletRequestWrapper = null;
        String requestURI = httpServletRequest.getRequestURI().replace(properties.getContextPath(), "");
        try {
            if (StringUtils.equals(requestURI, UrlPathConstant.LOGIN_PATH) || StringUtils.equals(requestURI, UrlPathConstant.REGISTER_PATH)) {
                customHttpServletRequestWrapper = new CustomHttpServletRequestWrapper(httpServletRequest);
            }
        } catch (Exception e) {
            log.error("请求【{}】执行PostMethodFilter异常，异常信息为:{}", httpServletRequest.getRequestURI(), e.getMessage());
        }
        filterChain.doFilter((Objects.isNull(customHttpServletRequestWrapper) ? httpServletRequest : customHttpServletRequestWrapper), httpServletResponse);
    }
}
