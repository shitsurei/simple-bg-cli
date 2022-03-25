package io.github.shitsurei.common.interceptor;

import io.github.shitsurei.common.resource.GsonManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 日志拦截类
 */
@Slf4j
@Component
@Aspect
@Order(1)
public class WebLogAspect {

    private static final String TRACE_ID = "TRACE_ID";

    @Pointcut("execution(* io.github.shitsurei..controller.*..*(..))")
    public void WebLogAspect() {
    }

    @Before("WebLogAspect()")
    public void doBefore(JoinPoint joinPoint) {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String traceId = StringUtils.isNotBlank(request.getHeader("traceId")) ? request.getHeader("traceId") : UUID.randomUUID().toString().replaceAll("-", "");
        MDC.put(TRACE_ID, traceId);
        // 打印请求 url
        log.info("ip => {}", request.getRemoteAddr());
        log.info("method => {}", request.getMethod());
        log.info("url => {}", request.getRequestURL().toString());
        // 打印请求入参
        Object[] args = joinPoint.getArgs();
        List<Object> argList = Arrays.stream(args).filter(o -> !(o instanceof HttpServletRequest) && !(o instanceof HttpServletResponse)).collect(Collectors.toList());
        log.info("Request Args: {}", GsonManager.getInstance(false).toJson(argList));
    }

    /**
     * 在切点之后织入
     */
    @After("WebLogAspect()")
    public void doAfter() {
        // 接口结束后换行，方便分割查看
        MDC.clear();
    }

    @Around("WebLogAspect()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Exception e) {
            log.error("exception:{}", e.getMessage());
            throw e;
        } finally {
            // 打印出参
            log.info("Response Args: {}", GsonManager.getInstance(false).toJson(result));
            // 执行耗时
            log.info("Time-Consuming: {} ms", System.currentTimeMillis() - startTime);
        }
        return result;
    }

}
