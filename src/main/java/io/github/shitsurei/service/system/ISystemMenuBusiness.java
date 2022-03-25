package io.github.shitsurei.service.system;

import io.github.shitsurei.dao.pojo.po.system.SystemMenu;
import io.github.shitsurei.dao.pojo.vo.system.MenuVO;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 系统权限业务类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/31 9:23
 */
public interface ISystemMenuBusiness {

    /**
     * 获取权限列表
     *
     * @param menuName
     * @param methodPath
     * @param urlPath
     * @param httpMethod
     * @return
     */
    List<MenuVO> menuList(String menuName, String methodPath, String urlPath, HttpMethod httpMethod);

    /**
     * 通过code获取权限，不存在时抛异常
     *
     * @param menuCode
     * @return
     */
    SystemMenu checkMenuByCode(String menuCode);

    /**
     * 接口加入权限校验
     *
     * @param menuCode
     * @return
     */
    boolean limit(String menuCode);

    /**
     * 接口取消权限校验
     *
     * @param menuCode
     * @return
     */
    boolean unLimit(String menuCode);

    /**
     * 根据接口路由和方法查询系统权限
     * @param requestPath
     * @param httpMethod
     * @return
     */
    SystemMenu findSystemMenuByUrlAndHttpMethod(String requestPath, HttpMethod httpMethod);

    /**
     * 根据权限编码批量查询权限
     * @param menuList
     * @return
     */
    List<SystemMenu> queryByMenuCode(List<String> menuList);

    /**
     * 获取所有系统菜单
     * @return
     */
    Iterable<SystemMenu> findAll();

    /**
     * 查询用户权限集合
     * @param systemUserId
     * @return
     */
    List<MenuVO> userAuthList(String systemUserId);
}
