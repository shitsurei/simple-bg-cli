package io.github.shitsurei.dao.constants;

/**
 * URL和路径常量集合
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 20:16
 */
public class UrlPathConstant {

    /**
     * 登录接口
     */
    public static final String LOGIN_PATH = "/common/login";

    /**
     * 注册接口
     */
    public static final String REGISTER_PATH = "/common/register";

    /**
     * 仅允许未登录访问接口（注册、登录、激活、找回、重置）
     */
    public static final String[] ANONYMOUS_PATH = new String[]{
            "/common/register",
            "/common/login",
            "/common/active",
            "/common/find",
            "/common/reset",
    };

    /**
     * 不限制权限登录接口（服务健康检测、配置动态刷新检测、获取验证码、获取公钥、swagger文档、对外接口【暂无】）
     */
    public static final String[] PERMIT_ALL_PATH = new String[]{
            "/common/healthCheck",
            "/common/configRefreshCheck",
            "/common/getCaptcha",
            "/common/getPublicKey",
            "/swagger-ui/**",
            "/api/**",
    };
}
