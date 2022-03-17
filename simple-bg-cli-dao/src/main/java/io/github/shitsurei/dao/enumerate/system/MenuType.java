package io.github.shitsurei.dao.enumerate.system;

import io.github.shitsurei.dao.constants.AuthorityConstant;
import io.github.shitsurei.dao.exception.GlobalException;

import java.util.Objects;

/**
 * 菜单类型
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/2/14 15:10
 */
public enum MenuType {
    SYSTEM_MENU("系统权限"),
    BUSINESS_MENU("业务权限"),
    ;

    private String msg;

    MenuType(String msg) {
        this.msg = msg;
    }

    //获取msg
    public String msg() {
        return msg;
    }

    /**
     * 判断角色
     * @param roleType
     * @return
     */
    public boolean match(RoleType roleType) {
        switch (roleType) {
            case SYSTEM_ROLE:
                return Objects.equals(this, SYSTEM_MENU);
            case BUSINESS_ROLE:
                return Objects.equals(this, BUSINESS_MENU);
            default:
                return false;
        }
    }

    /**
     * 解析权限编码，判断权限类型
     *
     * @param menuCode
     * @return
     */
    public static MenuType parseCode(String menuCode) {
        if (!menuCode.contains(AuthorityConstant.MENU_CONNECT_SYMBOL)) {
            throw new GlobalException(GlobalExceptionEnum.CODE_PARAM_INVALID);
        }
        switch (menuCode.split(AuthorityConstant.MENU_CONNECT_SYMBOL)[0]) {
            case AuthorityConstant.SYSTEM_PREFIX:
                return SYSTEM_MENU;
            case AuthorityConstant.BUSINESS_PREFIX:
                return BUSINESS_MENU;
            default:
                throw new GlobalException(GlobalExceptionEnum.CODE_PARAM_INVALID);
        }
    }
}
