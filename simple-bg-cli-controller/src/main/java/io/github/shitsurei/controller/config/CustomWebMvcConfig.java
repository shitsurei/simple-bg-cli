package io.github.shitsurei.controller.config;

import io.github.shitsurei.controller.interceptor.HostAccessLimitInterceptor;
import io.github.shitsurei.controller.interceptor.NoRepeatSubmitInterceptor;
import io.github.shitsurei.dao.constants.UrlPathConstant;
import io.github.shitsurei.service.handler.PostMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * mvc配置类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/27 10:51
 */
@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private NoRepeatSubmitInterceptor noRepeatSubmitInterceptor;

    @Autowired
    private HostAccessLimitInterceptor hostAccessLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //1.加入的顺序就是拦截器执行的顺序，
        //2.按顺序执行所有拦截器的preHandle
        //3.所有的preHandle 执行完再执行全部postHandle 最后是postHandle
        registry.addInterceptor(hostAccessLimitInterceptor).addPathPatterns("/**");
        registry.addInterceptor(noRepeatSubmitInterceptor).addPathPatterns("/**")
                .excludePathPatterns(UrlPathConstant.PERMIT_ALL_PATH);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // 设置允许跨域请求的路径
                .addMapping("/**")
                // 设置允许cookie
                .allowCredentials(true)
                // 设置允许跨域请求的HTTP方法
                .allowedMethods("GET", "POST")
                // 设置允许的header熟悉
                .allowedHeaders("*")
                // 设置允许时间
                .maxAge(3600);
    }

    /**
     * 添加自定义参数解析器
     *
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PostMethodArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
