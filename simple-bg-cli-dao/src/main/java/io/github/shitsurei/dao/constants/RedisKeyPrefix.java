package io.github.shitsurei.dao.constants;

/**
 * 系统 redis key 前缀常量（system开头）
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 12:56
 */
public class RedisKeyPrefix {

    /**
     * 前后端非对称加密缓存
     */
    public static final String SYS_RSA_ENCRYPT_CACHE = "system:encrypt:cache:";

    /**
     * 系统用户激活邮件延时
     */
    public static final String SYS_USER_REGISTER_ACTIVE = "system:user:register:active:";

    /**
     * 系统用户邀请注册邮件延时
     */
    public static final String SYS_USER_REGISTER_INVITE = "system:user:register:invite:";

    /**
     * 系统用户登录token
     */
    public static final String SYS_USER_LOGIN_TOKEN = "system:user:login:token:";

    /**
     * 系统用户登录sessionId
     */
    public static final String SYS_USER_LOGIN_SESSION = "system:user:login:session:";

    /**
     * 登录失败用户账号
     */
    public static final String LOGIN_FAILED_ACCOUNT_ACCOUNT = "system:user:login:filed:account:";

    /**
     * 系统找回账户验证码
     */
    public static final String SYS_USER_RETRIEVE_CAPTCHA = "system:user:retrieve:captcha:";

    /**
     * 系统找回账户用户主键
     */
    public static final String SYS_USER_RETRIEVE_ID = "system:user:retrieve:id:";

    /**
     * 接口防重提前缀
     */
    public static final String REQUEST_URL_REPEAT_SUBMIT = "system:request:repeatSubmit:url:";

    /**
     * 高强度访问IP池
     */
    public static final String REQUEST_ACCESS_IP_POOL = "system:request:access:ip:";

    /**
     * 封禁IP池
     */
    public static final String REQUEST_BAN_IP_POOL = "system:request:ban:ip:";
}
