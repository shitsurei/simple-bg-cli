package io.github.shitsurei.common.annotation;

import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zgr
 * @Description 解析单个post请求中的body参数
 * @createTime 2022年02月07日 20:00:00
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostParam {

    String value() default "";

    String name() default "";

    /**
     * 空内容提示
     *
     * @return
     */
    String message() default "";

    boolean required() default true;

    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
