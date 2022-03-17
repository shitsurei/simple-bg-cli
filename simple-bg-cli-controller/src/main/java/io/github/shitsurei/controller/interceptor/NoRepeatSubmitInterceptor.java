package io.github.shitsurei.controller.interceptor;

import io.github.shitsurei.common.annotation.NoRepeatSubmit;
import io.github.shitsurei.common.resource.GsonManager;
import io.github.shitsurei.common.util.HttpUtil;
import io.github.shitsurei.common.util.RedisUtil;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.dao.constants.RedisKeyPrefix;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import io.github.shitsurei.dao.pojo.po.system.SystemMenu;
import io.github.shitsurei.service.system.ISystemMenuBusiness;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * 接口防重提拦截器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/2/15 16:06
 */
@Component
@Slf4j
public class NoRepeatSubmitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISystemMenuBusiness menuBusiness;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURI();
        String method = request.getMethod();
        SystemMenu menu = menuBusiness.findSystemMenuByUrlAndHttpMethod(requestUrl, HttpMethod.resolve(method));
        // 未纳入权限校验的接口直接放行
        if (Objects.isNull(menu)) {
            return true;
        }
        String methodPath = menu.getMethodPath();
        int i = methodPath.lastIndexOf(".");
        String classPath = methodPath.substring(0, i);
        String methodName = methodPath.substring(i + 1);
        NoRepeatSubmit noRepeatSubmit = parseNoRepeatSubmit(classPath, methodName);
        // 未纳入权限校验的接口走IP池限制
        if (Objects.isNull(noRepeatSubmit)) {
            return true;
        }
        if (redisUtil.hasKey(RedisKeyPrefix.REQUEST_URL_REPEAT_SUBMIT + requestUrl)) {
            ResponseResult<Object> errorResponseResult = ResponseUtil.buildFailureResult(GlobalExceptionEnum.URL_REPEAT_SUBMIT);
            HttpUtil.renderString(response, 200, GsonManager.getInstance(false).toJson(errorResponseResult));
            log.warn(errorResponseResult.getMsg());
            return false;
        }
        redisUtil.set(RedisKeyPrefix.REQUEST_URL_REPEAT_SUBMIT + requestUrl, 1, noRepeatSubmit.gapSecond());
        return true;
    }

    /**
     * 判断接口是否包含防提注解
     *
     * @param classPath
     * @param methodName
     * @return 包含返回注解对象，不包含返回null
     */
    private NoRepeatSubmit parseNoRepeatSubmit(String classPath, String methodName) {
        NoRepeatSubmit annotation = null;
        try {
            Method targetMethod = Arrays.stream(Class.forName(classPath).getMethods()).filter(method -> StringUtils.equals(method.getName(), methodName)).findAny().get();
            annotation = AnnotationUtils.findAnnotation(targetMethod, NoRepeatSubmit.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return annotation;
    }
}
