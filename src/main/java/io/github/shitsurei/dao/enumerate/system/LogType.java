package io.github.shitsurei.dao.enumerate.system;

/**
 * @author zgr
 * @Description 日志类型
 * @createTime 2022年02月07日 21:11:00
 */
public enum LogType {
    //---------------------- 系统日志 --------------------------------
    USER_REGISTER("用户注册"),
    USER_LOGIN("用户登录"),
    AUTH_CHANGE("权限变更"),
    ROLE_CHANGE("角色变更"),
    SYSTEM_USER_CHANGE("系统用户变更"),
    FILE_CONFIG_CHANGE("文件配置变更"),
    FILE_CHANGE("文件变更"),
    SEND_EMAIL("发送邮件"),
    //---------------------- 业务日志 --------------------------------
    BUSINESS_USER_CHANGE("业务用户变更"),
    ;

    private String msg;

    LogType(String msg) {
        this.msg = msg;
    }

    //获取msg
    public String msg() {
        return msg;
    }
}
