package io.github.shitsurei.dao.repository.system;

import io.github.shitsurei.dao.pojo.po.system.SystemMenu;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 系统权限持久化接口
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/30 17:23
 */
public interface SystemMenuRepository extends PagingAndSortingRepository<SystemMenu, String>, QuerydslPredicateExecutor<SystemMenu> {
    /**
     * 通过code查询权限
     *
     * @param systemMenuCodeList
     * @return
     */
    List<SystemMenu> queryAllByMenuCodeIn(List<String> systemMenuCodeList);

    /**
     * 模糊查询权限编码
     *
     * @param codeLike
     * @return
     */
    List<SystemMenu> queryAllByMenuCodeLike(String codeLike);

    /**
     * 通过code查询权限
     *
     * @param menuCode
     * @return
     */
    SystemMenu findByMenuCode(String menuCode);

    /**
     * 通过路由地址和http方法类型查询菜单权限
     * @param urlPath
     * @param method
     * @return
     */
    SystemMenu findByUrlPathAndHttpMethod(String urlPath, HttpMethod method);
}
