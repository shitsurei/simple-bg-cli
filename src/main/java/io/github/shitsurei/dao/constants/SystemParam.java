package io.github.shitsurei.dao.constants;

/**
 * 系统配置常量
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/23 17:53
 */
public class SystemParam {
    /**
     * 系统RSA加密公钥私钥缓存时间
     */
    public static final Integer RSA_KEY_PAIR_CACHE_EXPIRE = 60;

    /**
     * 找回账户邮件生效时间（30分钟）
     */
    public static final Integer RETRIEVE_EMAIL_EXPIRE = 60 * 30;

    /**
     * 账号激活邮件生效时间（1小时）
     */
    public static final Integer ACCOUNT_ACTIVE_EMAIL_EXPIRE = 60 * 60;
}
