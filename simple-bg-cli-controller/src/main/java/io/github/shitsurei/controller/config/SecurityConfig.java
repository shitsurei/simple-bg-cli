package io.github.shitsurei.controller.config;

import io.github.shitsurei.service.handler.LogRegEncryptEncoder;
import io.github.shitsurei.controller.filter.CaptchaAuthFilter;
import io.github.shitsurei.controller.filter.JwtTokenAuthFilter;
import io.github.shitsurei.controller.filter.PostMethodFilter;
import io.github.shitsurei.dao.constants.UrlPathConstant;
import io.github.shitsurei.service.handler.AccessExceptionHandler;
import io.github.shitsurei.service.handler.AccountSelectServiceImpl;
import io.github.shitsurei.service.handler.AuthenticationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * 登录配置类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 13:52
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountSelectServiceImpl accountSelectServiceImpl;

    @Autowired
    private LogRegEncryptEncoder logRegEncryptEncoder;

    @Autowired
    private JwtTokenAuthFilter jwtTokenAuthFilter;

    @Autowired
    private CaptchaAuthFilter captchaAuthFilter;

    @Autowired
    private PostMethodFilter postMethodFilter;

    @Autowired
    private AuthenticationExceptionHandler authenticationExceptionHandler;

    @Autowired
    private AccessExceptionHandler accessExceptionHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountSelectServiceImpl).passwordEncoder(logRegEncryptEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 认证相关配置
        http
                // 关闭csrf（前后端分离项目天然防范csrf攻击）
                .csrf().disable()
                // 不通过session获取security上下文
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                // 不限制登录状态的接口
                .antMatchers(UrlPathConstant.PERMIT_ALL_PATH).permitAll()
                // 仅允许匿名访问的接口
                .antMatchers(UrlPathConstant.ANONYMOUS_PATH).anonymous()
                // 使用RBAC权限体系校验用户认证后访问的接口
                .anyRequest().access("@rbacAuthorityServiceImpl.hasPermission(request,authentication)")
                // 自定义异常处理
                .and().exceptionHandling().authenticationEntryPoint(authenticationExceptionHandler).accessDeniedHandler(accessExceptionHandler)
                // 允许跨域
                .and().cors()
        ;
        // 将jwt token验证过滤器加在账号密码验证过滤器之前
        http.addFilterBefore(jwtTokenAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // 将验证码核验过滤器加在账号密码验证过滤器之前
        http.addFilterBefore(captchaAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // 将http请求体包装过滤器加在验证码读取过滤器之前
        http.addFilterBefore(postMethodFilter, CaptchaAuthFilter.class);
    }

    /**
     * 配置地址栏不能识别 // 的情况
     *
     * @return
     */
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        //此处可添加别的规则,目前只设置 允许双 //
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }
}
