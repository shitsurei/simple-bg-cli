package io.github.shitsurei.service.system;

import io.github.shitsurei.dao.enumerate.system.RoleType;
import io.github.shitsurei.dao.pojo.po.system.SystemRole;
import io.github.shitsurei.dao.pojo.vo.system.MenuVO;
import io.github.shitsurei.dao.pojo.vo.system.RoleVO;

import java.util.List;

/**
 * @author zgr
 * @Description 系统角色业务层
 * @createTime 2022年01月22日 22:43:00
 */
public interface ISystemRoleBusiness {
    /**
     * 查询角色列表
     *
     * @param roleName
     * @param roleCode
     * @return
     */
    List<RoleVO> roleList(String roleName, String roleCode);

    /**
     * 查询角色绑定权限菜单
     *
     * @param roleCode
     * @return
     */
    List<MenuVO> authMenuList(String roleCode);

    /**
     * 新建角色
     *
     * @param roleName
     * @param roleType
     * @param roleCode
     * @param remark
     * @return
     */
    boolean create(String roleName, RoleType roleType, String roleCode, String remark);

    /**
     * 删除角色
     *
     * @param roleCode
     * @return
     */
    boolean delete(String roleCode);

    /**
     * 绑定权限
     *
     * @param roleCode
     * @param menuCodeList
     * @return
     */
    boolean bind(String roleCode, List<String> menuCodeList);

    /**
     * 解绑权限
     *
     * @param roleCode
     * @param menuCodeList
     * @return
     */
    boolean unbind(String roleCode, List<String> menuCodeList);

    /**
     * 通过code获取角色，不存在时抛异常
     *
     * @param roleCode
     * @return
     */
    SystemRole checkRoleByCode(String roleCode);
}
