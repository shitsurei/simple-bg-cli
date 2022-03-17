package io.github.shitsurei.service.handler;

import io.github.shitsurei.common.annotation.PostParam;
import io.github.shitsurei.common.resource.GsonManager;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author zgr
 * @Description post请求参数解析器
 * @createTime 2022年02月07日 20:02:00
 */
@Slf4j
public class PostMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String POST = "post";
    private static final String APPLICATION_JSON = "application/json";

    /**
     * 判断是否需要处理该参数
     *
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 只处理带有@PostParam注解的参数
        return parameter.hasParameterAnnotation(PostParam.class);
    }

    /**
     * 解析参数
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String contentType = Objects.requireNonNull(servletRequest).getContentType();

        if (contentType == null || !contentType.contains(APPLICATION_JSON)) {
            log.error("解析参数异常，contentType需为{}", APPLICATION_JSON);
            throw new RuntimeException("解析参数异常，contentType需为application/json");
        }

        if (!POST.equalsIgnoreCase(servletRequest.getMethod())) {
            log.error("解析参数异常，请求类型必须为post");
            throw new RuntimeException("解析参数异常，请求类型必须为post");
        }
        return bindRequestParams(parameter, servletRequest);
    }

    private Object bindRequestParams(MethodParameter parameter, HttpServletRequest servletRequest) {
        PostParam postParam = parameter.getParameterAnnotation(PostParam.class);

        Class<?> parameterType = parameter.getParameterType();
        String requestBody = ((CustomHttpServletRequestWrapper) servletRequest).getBodyAsString();
        Map params = GsonManager.getInstance(false).fromJson(requestBody, Map.class);

        params = CollectionUtils.isEmpty(params) ? new HashMap<>(0) : params;
        String name = StringUtils.isBlank(postParam.name()) ? parameter.getParameterName() : postParam.name();
        Object value = params.get(name);

        if (parameterType.equals(String.class) && StringUtils.isBlank((String) value)) {
            throw new GlobalException(GlobalExceptionEnum.PARAM_EXCEPTION, postParam.message());
        }
        if (parameterType.equals(Collection.class) && CollectionUtils.isEmpty((Collection<?>) value)) {
            throw new GlobalException(GlobalExceptionEnum.PARAM_EXCEPTION, postParam.message());
        }
        if (parameterType.equals(Map.class) && CollectionUtils.isEmpty((Map<?, ?>) value)) {
            throw new GlobalException(GlobalExceptionEnum.PARAM_EXCEPTION, postParam.message());
        }

        if (postParam.required() && Objects.isNull(value)) {
            throw new GlobalException(GlobalExceptionEnum.PARAM_EXCEPTION, postParam.message());
        }

        if (postParam.defaultValue().equals(ValueConstants.DEFAULT_NONE) && Objects.isNull(value)) {
            log.error("参数解析异常,require=false,必须指定默认值");
            throw new RuntimeException("参数解析异常,require=false,必须指定默认值");
        }

        if (Objects.isNull(value)) {
            value = postParam.defaultValue();
        }
        return ConvertUtils.convert(value, parameterType);
    }
}
