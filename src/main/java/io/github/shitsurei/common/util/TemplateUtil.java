package io.github.shitsurei.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.util.Map;

/**
 * 模板工具类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/2/16 18:00
 */
@Component
public class TemplateUtil {
    /**
     * 模板引擎
     */
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 使用 Thymeleaf 渲染 HTML
     *
     * @param template HTML模板
     * @param params   参数
     * @return 渲染后的HTML
     */
    public String renderHtml(String template, Map<String, Object> params) {
        /**
         * thymeleaf提供了两个实现IContext的具体实现类，分别是：
         * org.thymeleaf.context.Context（extends AbstractContext implements IContext）
         * org.thymeleaf.context.WebContext (extends AbstractContext implements IWebContext extends IContext)
         * 要使用相对定位，必须指定一个实现IWebContext接口的对象，IWebContext对象可以传入request,response,servletContext参数，可以用来地位应用程序的根路径
         */
        WebContext context = new WebContext(SessionUtil.getRequest(), SessionUtil.getResponse(), SessionUtil.getRequest().getServletContext());
        context.setVariables(params);
        return templateEngine.process(template, context);
    }
}
