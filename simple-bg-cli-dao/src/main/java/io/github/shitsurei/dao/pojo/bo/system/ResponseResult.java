package io.github.shitsurei.dao.pojo.bo.system;

import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.enumerate.system.HttpEnum;

import java.io.Serializable;

/**
 * 响应体封装
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 17:36
 */
public class ResponseResult<T> implements Serializable {
    private static final long serialVersionUID = -7489751396893309153L;

    /**
     * 响应编码
     */
    private int code;

    /**
     * 响应接口调用成功
     */
    private boolean success;

    /**
     * 提示信息
     */
    private String msg;
    /**
     * 响应数据
     */
    private T data;

    /**
     * 根据属性构建ResponseResult
     *
     * @param code
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    private static <T> ResponseResult<T> build(int code, boolean success, String msg, T data) {
        return new ResponseResult<T>().setCode(code).setSuccess(success).setMsg(msg).setData(data);
    }


    /**
     * 请求正常
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> ok(String msg) {
        return build(HttpEnum.OK.code(), true, msg, null);
    }

    /**
     * 请求正常(带数据)
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> ok(T data) {
        return build(HttpEnum.OK.code(), true, HttpEnum.OK.msg(), data);
    }

    /**
     * 创建成功
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> created(String msg) {
        return build(HttpEnum.CREATED.code(), true, msg, null);
    }

    /**
     * 非法请求
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> invalidRequest(String msg) {
        return build(HttpEnum.INVALID_REQUEST.code(), false, msg, null);
    }

    /**
     * 访问内容不存在
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> notFound(String msg) {
        return build(HttpEnum.NOTFOUND.code(), false, msg, null);
    }

    /**
     * 没有权限
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> unauthorized(String msg) {
        return build(HttpEnum.UNAUTHORIZED.code(), false, msg, null);
    }

    /**
     * 禁止访问
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> forbidden(String msg) {
        return build(HttpEnum.FORBIDDEN.code(), false, msg, null);
    }

    /**
     * 自定义错误
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> internalServerError(int code, String msg, T data) {
        return build(code, false, msg, data);
    }

    /**
     * 自定义错误
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> internalServerError(GlobalExceptionEnum exceptionEnum) {
        return build(exceptionEnum.getErrorCode(), false, exceptionEnum.getErrorMsg(), null);
    }

    /**
     * 自定义错误
     *
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> internalServerError(String msg) {
        return build(HttpEnum.INTERNAL_SERVER_ERROR.code(), false, msg, null);
    }

    /**
     * getter和setter
     *
     * @return
     */
    public int getCode() {
        return code;
    }

    /**
     * 注意返回值类型（支持链式调用）
     *
     * @param code
     * @return
     */
    public ResponseResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return success;
    }

    /**
     * 注意返回值类型（支持链式调用）
     *
     * @param success
     * @return
     */
    public ResponseResult<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    /**
     * 注意返回值类型（支持链式调用）
     *
     * @param msg
     * @return
     */
    public ResponseResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    /**
     * 注意返回值类型
     *
     * @param data
     * @return
     */
    public ResponseResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}
