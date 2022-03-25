package io.github.shitsurei.dao.enumerate.system;

import io.github.shitsurei.dao.constants.AuthorityConstant;
import io.github.shitsurei.dao.exception.GlobalException;

/**
 * 角色类型
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/2/14 14:24
 */
public enum RoleType {
    SYSTEM_ROLE("系统用户"),
    BUSINESS_ROLE("业务用户"),
    ;

    private String msg;

    RoleType(String msg) {
        this.msg = msg;
    }

    //获取msg
    public String msg() {
        return msg;
    }

    /**
     * 封装角色编码
     *
     * @param roleType
     * @param subCode
     * @return
     */
    public static String buildCode(RoleType roleType, String subCode) {
        StringBuilder code = new StringBuilder();
        switch (roleType) {
            case SYSTEM_ROLE:
                code.append(AuthorityConstant.SYSTEM_PREFIX);
                break;
            case BUSINESS_ROLE:
                code.append(AuthorityConstant.BUSINESS_PREFIX);
                break;
            default:
        }
        code.append(AuthorityConstant.ROLE_CONNECT_SYMBOL).append(subCode);
        return code.toString();
    }

    /**
     * 解析角色编码，判断角色类型
     *
     * @param roleCode
     * @return
     */
    public static RoleType parseCode(String roleCode) {
        if (!roleCode.contains(AuthorityConstant.ROLE_CONNECT_SYMBOL)) {
            throw new GlobalException(GlobalExceptionEnum.CODE_PARAM_INVALID);
        }
        switch (roleCode.split(AuthorityConstant.ROLE_CONNECT_SYMBOL)[0]) {
            case AuthorityConstant.SYSTEM_PREFIX:
                return SYSTEM_ROLE;
            case AuthorityConstant.BUSINESS_PREFIX:
                return BUSINESS_ROLE;
            default:
                throw new GlobalException(GlobalExceptionEnum.CODE_PARAM_INVALID);
        }
    }
}
