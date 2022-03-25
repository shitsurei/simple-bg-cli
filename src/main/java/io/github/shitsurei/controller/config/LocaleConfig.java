package io.github.shitsurei.controller.config;

import io.github.shitsurei.controller.resolver.I18nLocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 本地化配置
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/29 21:14
 */
@Configuration
public class LocaleConfig {
    /**
     * 自定义解析器
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new I18nLocaleResolver();
    }
}
