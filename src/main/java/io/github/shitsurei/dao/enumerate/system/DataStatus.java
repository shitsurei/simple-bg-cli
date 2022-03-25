package io.github.shitsurei.dao.enumerate.system;

/**
 * 数据状态
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 19:50
 */
public enum DataStatus {
    VALID("有效", "正常"),
    INVALID("无效", "停用"),
    TEMP("暂存", "未授权"),
    DELETE("删除", "注销"),
    ;

    private String msg;

    /**
     * 用户数据状态含义
     */
    private String userMsg;

    DataStatus(String msg, String userMsg) {
        this.msg = msg;
        this.userMsg = userMsg;
    }

    public String getMsg() {
        return msg;
    }

    public String getUserMsg() {
        return userMsg;
    }
}
