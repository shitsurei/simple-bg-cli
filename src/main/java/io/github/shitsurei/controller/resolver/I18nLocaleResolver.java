package io.github.shitsurei.controller.resolver;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 自定义国际化解析器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/29 16:18
 */
public class I18nLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String language = request.getHeader("lang");
        // 如果没有获取到就使用系统默认的
        Locale locale = Locale.getDefault();
        //如果请求链接不为空
        if (StringUtils.isNotBlank(language)) {
            //分割请求参数
            String[] split = language.split("_");
            //国家，地区
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
