package io.github.shitsurei.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口防重提注解
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/2/15 15:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {

    /**
     * 间隔时间，单位秒（s）
     *
     * @return
     */
    int gapSecond() default 3;
}
