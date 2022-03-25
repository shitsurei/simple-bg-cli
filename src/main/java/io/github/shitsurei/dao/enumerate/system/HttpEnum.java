package io.github.shitsurei.dao.enumerate.system;

/**
 * http状态码
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 17:35
 */
public enum HttpEnum {
    /**
     * 请求处理正常
     */
    OK(200, "system.http.response.200"),

    /**
     * 请求成功并且服务器创建了新的资源。
     */
    CREATED(201, "system.http.response.201"),
    /**
     * 用户发出的请求有错误，服务器没有进行新建或修改数据的操作
     */
    INVALID_REQUEST(400, "system.http.response.400"),
    /**
     * 访问内容不存在
     */
    NOTFOUND(404, "system.http.response.404"),
    /**
     * 表示用户没有权限（令牌、用户名、密码错误）
     */
    UNAUTHORIZED(401,"system.http.response.401"),
    /**
     * 表示用户得到授权（与401错误相对），但是访问是被禁止的
     */
    FORBIDDEN(403,"system.http.response.403"),
    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR(500, "system.http.response.500");

    private String msg;

    private int code;

    HttpEnum(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    //获取code
    public int code(){
        return code;
    }
    //获取msg
    public String msg(){
        return msg;
    }
}
