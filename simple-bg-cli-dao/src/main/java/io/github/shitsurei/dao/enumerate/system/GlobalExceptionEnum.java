package io.github.shitsurei.dao.enumerate.system;

/**
 * 全局异常枚举
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 10:30
 */
public enum GlobalExceptionEnum {

    // -------------------------------------------------------------  系统异常  --------------------------------------------------------------

    // 基础异常
    IO_EXCEPTION(101, "system.exception.101"),
    ENUM_DICT_NOTFOUND(102, "system.exception.102"),
    PARAM_EXCEPTION(103, "system.exception.103"),
    DATA_EXCEPTION(104, "system.exception.104"),
    CODE_PARAM_INVALID(105, "system.exception.105"),
    CRYPT_ERROR(106, "system.exception.106"),
    CACHE_EXPIRE(107, "system.exception.107"),
    INTERFACE_INVALID(108, "system.exception.108"),

    // 基础HTTP异常
    BODY_NOT_MATCH(402, "system.http.response.402"),
    HTTP_URL_METHOD_NOT_MATCH(404, "system.exception.401"),
    SERVER_BUSY(503, "system.exception.503"),
    NULL_POINT_EXCEPTION(505, "system.exception.505"),
    HTTP_METHOD_NOT_MATCH(506, "system.exception.506"),
    URL_REPEAT_SUBMIT(507, "system.exception.507"),

    // 用户登录注册相关异常，10开头
    USER_ACCOUNT_EXIST(1001, "system.exception.1001"),
    LOGIN_FAILURE(1002, "system.exception.1002"),
    LOGIN_STATUS_EXPIRE(1003, "system.exception.1003"),
    CAPTCHA_EMPTY(1004, "system.exception.1004"),
    CAPTCHA_EXPIRE(1005, "system.exception.1005"),
    USER_NOTFOUND(1006, "system.exception.1006"),
    LOGOUT_FAILURE(1007, "system.exception.1007"),
    USER_ACCOUNT_DELETE(1008, "system.exception.1008"),
    SEND_REG_MAIL_ERROR(1009, "system.exception.1009"),
    USER_STATUS_ERROR(1010, "system.exception.1010"),
    EMAIL_ACTIVE_LINK_EXPIRE(1011, "system.exception.1011"),
    EMAIL_BIND_EXIST(1012, "system.exception.1012"),
    EMAIL_ACTIVE_LINK_INVALID(1013, "system.exception.1013"),
    EMAIL_BIND_NOT_EXIST(1014, "system.exception.1014"),
    EMAIL_INVITE_LINK_EXPIRE(1015, "system.exception.1015"),
    PASSWORD_RESET_CAPTCHA_EXPIRE(1016, "system.exception.1016"),
    LOGIN_FAILED_TIMES_TOP(1017, "system.exception.1017"),

    // 用户访问权限相关异常，20开头
    NO_AUTH_ERROR(2001, "system.exception.2001"),
    AUTH_CODE_DUPLICATE(2002, "system.exception.2002"),
    USER_AUTH_EMPTY(2003, "system.exception.2003"),
    NO_MATCH_ROLE(2004, "system.exception.2004"),
    DUPLICATE_ROLE_CODE(2005, "system.exception.2005"),
    NO_MATCH_MENU(2006, "system.exception.2006"),
    ROLE_MENU_NOT_MATCH(2007, "system.exception.2007"),
    SYSTEM_SUPER_ADMIN_CHANGE_ERROR(2008, "system.exception.2008"),
    EXIST_BIND_USER(2009, "system.exception.2009"),

    // 文件上传下载相关异常，30开头
    FILE_CONFIG_NOTFOUND(3001, "system.exception.3001"),
    FILE_UPLOAD_NUM_LIMIT(3002, "system.exception.3002"),
    FILE_TYPE_NOT_SUPPORT(3003, "system.exception.3003"),
    FILE_NOTFOUND(3004, "system.exception.3004"),
    FILE_NAME_EMPTY(3005, "system.exception.3005"),
    FILE_DOWNLOAD_FAILURE(3006, "system.exception.3006"),
    FILE_CONFIG_CODE_FAILURE(3007, "system.exception.3007"),
    FILE_BIND_FAILURE(3008, "system.exception.3008"),
    FILE_SIZE_OVERFLOW(3009, "system.exception.3009"),

    // -------------------------------------------------------------  业务异常  --------------------------------------------------------------

    ;

    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 错误描述
     */
    private String errorMsg;

    GlobalExceptionEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
