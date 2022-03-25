package io.github.shitsurei.dao.repository.system;

import io.github.shitsurei.dao.pojo.po.system.SystemRole;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 系统角色持久化接口
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/30 17:24
 */
public interface SystemRoleRepository extends PagingAndSortingRepository<SystemRole, String>, QuerydslPredicateExecutor<SystemRole> {

    /**
     * 通过code查询角色
     * @param roleCode
     * @return
     */
    SystemRole findByRoleCode(String roleCode);
}
