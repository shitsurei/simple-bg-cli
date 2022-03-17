package io.github.shitsurei.service.handler;

import io.github.shitsurei.dao.constants.CustomProperties;
import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.pojo.bo.system.LoginUser;
import io.github.shitsurei.dao.pojo.po.system.SystemMenu;
import io.github.shitsurei.service.system.ISystemMenuBusiness;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 自定义权限校验
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/30 17:44
 */
@Component
@Slf4j
public class RbacAuthorityServiceImpl {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private ISystemMenuBusiness menuBusiness;

    @Autowired
    private CustomProperties properties;

    /**
     * 记录当前纳入权限验证的接口
     */
    private Map<String, Boolean> authUrlMap;

    /**
     * 记录系统中所有URL请求
     */
    private Map<String, Boolean> allUrlMap;

    /**
     * 判断用户访问权限
     *
     * @param request
     * @param authentication
     * @return
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        if (!checkRequest(request)) {
            return true;
        }
        // 去掉统一前缀路由
        SystemMenu menu = menuBusiness.findSystemMenuByUrlAndHttpMethod(request.getRequestURI().replace(properties.getContextPath(), ""), HttpMethod.resolve(request.getMethod()));
        /**
         * 情况1：查询不到，说明接口未通过注解加入权限校验
         * 情况2：数据状态为INVALID说明接口不进行权限校验
         */
        if (Objects.isNull(menu) || menu.getDataStatus() == DataStatus.INVALID) {
            return true;
        }
        Object userInfo = authentication.getPrincipal();
        if (!(userInfo instanceof UserDetails)) {
            return false;
        }
        boolean hasPermission = false;
        LoginUser principal = (LoginUser) userInfo;
        List<SystemMenu> systemMenuList = menuBusiness.queryByMenuCode(principal.getMenuList());
        for (SystemMenu systemMenu : systemMenuList) {
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(systemMenu.getUrlPath(), systemMenu.getHttpMethod().name());
            if (antPathMatcher.matches(request)) {
                hasPermission = true;
                break;
            }
        }
        return hasPermission;
    }

    /**
     * 校验请求是否存在，并且返回接口是否纳入权限管控（未纳入权限管控的请求直接放行）
     *
     * @param request 请求
     * @return true表示纳入权限管控
     */
    private boolean checkRequest(HttpServletRequest request) {
        // 去掉统一前缀路由
        String key = request.getRequestURI().replace(properties.getContextPath(), "") + "_" + request.getMethod();
        if (!getAllUrlMap().containsKey(key)) {
            return false;
        }
        if (getAuthUrlMap().containsKey(key)) {
            return true;
        }
        return false;
    }

    /**
     * 获取 所有URL Mapping，返回格式为{"/test":["GET","POST"],"/sys":["GET","DELETE"]}
     *
     * @return {@link ArrayListMultimap} 格式的 URL Mapping
     */
    private Map<String, Boolean> getAllUrlMap() {
        if (Objects.nonNull(allUrlMap)) {
            return allUrlMap;
        }
        synchronized (RbacAuthorityServiceImpl.class) {
            allUrlMap = Maps.newHashMap();
            // 获取url与类和方法的对应信息
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
            handlerMethods.forEach((mappingInfo, handlerMethod) -> {
                // 获取当前 key 下的获取所有URL
                Set<String> urls = mappingInfo.getPatternsCondition().getPatterns();
                Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();
                urls.forEach(url -> methods.forEach(method -> allUrlMap.put(url + "_" + method, Boolean.TRUE)));
            });
        }
        return allUrlMap;
    }

    /**
     * 获取纳入权限管控的URL集合
     *
     * @return
     */
    private Map<String, Boolean> getAuthUrlMap() {
        if (Objects.nonNull(authUrlMap)) {
            return authUrlMap;
        }
        synchronized (RbacAuthorityServiceImpl.class) {
            authUrlMap = Maps.newHashMap();
            menuBusiness.findAll().forEach(menu -> authUrlMap.put(menu.getUrlPath() + "_" + menu.getHttpMethod().name(), Boolean.TRUE));
        }
        return authUrlMap;
    }

}
