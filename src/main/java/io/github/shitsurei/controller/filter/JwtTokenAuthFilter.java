package io.github.shitsurei.controller.filter;

import io.github.shitsurei.common.resource.GsonManager;
import io.github.shitsurei.common.util.JwtUtil;
import io.github.shitsurei.common.util.RedisUtil;
import io.github.shitsurei.dao.constants.RedisKeyPrefix;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.pojo.bo.system.LoginUser;
import io.github.shitsurei.dao.exception.CustomAuthenticationException;
import io.github.shitsurei.service.handler.AuthenticationExceptionHandler;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * jwt验证过滤器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 17:27
 */
@Component
public class JwtTokenAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AuthenticationExceptionHandler loginExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 获取token
        String token = request.getHeader("token");
        // token不存在时直接放行，执行后续过滤链逻辑
        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        Claims claims = jwtUtil.parseToken(token);
        // 从缓存中尝试获取登录的用户对象
        String userId = claims.getSubject();
        Object userJson = redisUtil.get(RedisKeyPrefix.SYS_USER_LOGIN_TOKEN + userId);
        if (Objects.isNull(userJson)) {
            // 过滤链中异常处理方式
            loginExceptionHandler.commence(request, response, new CustomAuthenticationException(GlobalExceptionEnum.LOGIN_STATUS_EXPIRE));
            return;
        }
        LoginUser loginUser = GsonManager.getInstance(true).fromJson((String) userJson, LoginUser.class);
        // 注意token的参数为实现UserDetails的用户对象
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        // 将已经通过登录认证的token信息放入上下文，可直接跳过账号密码过滤器中
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);
    }
}
