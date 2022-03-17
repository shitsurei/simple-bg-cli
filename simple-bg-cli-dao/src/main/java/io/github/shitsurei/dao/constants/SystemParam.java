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
     * 系统登录用户过期时间
     */
    public static final Integer SYS_USER_LOGIN_EXPIRE_TIME = 60 * 60 * 12;

    /**
     * 系统允许IP访问次数上限
     */
    public static final Integer IP_ACCESS_TOP_TIME = 30;

    /**
     * 系统允许IP访问次数上限时限（秒）
     */
    public static final Integer IP_ACCESS_TOP_GAP = 5;

    /**
     * 系统封禁IP时间（秒）
     */
    public static final Integer IP_BAN_GAP = 60 * 10;
}
