package io.github.shitsurei.common.annotation;

import java.lang.annotation.*;

/**
 * 系统扫描权限注解，作用于类和方法
 * 类上添加注解标识该controller下的方法需要加入权限控制
 * 方法上添加注解并指明属性将方法注入权限控制
 * 最终入库权限菜单会采用拼接方式
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/31 9:54
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysMenu {

    /**
     * 权限名
     *
     * @return
     */
    String menuName();

    /**
     * 权限编码
     *
     * @return
     */
    String menuCode();

    /**
     * 是否隐藏，默认显示
     *
     * @return
     */
    boolean hidden() default false;

    /**
     * 是否启用校验，默认启用
     *
     * @return
     */
    boolean limit() default true;
}
