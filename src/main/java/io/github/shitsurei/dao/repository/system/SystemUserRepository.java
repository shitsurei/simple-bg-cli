package io.github.shitsurei.dao.repository.system;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.pojo.po.system.SystemRole;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 系统用户持久化接口
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 19:17
 */
public interface SystemUserRepository extends PagingAndSortingRepository<SystemUser,String>, QuerydslPredicateExecutor<SystemUser> {
    /**
     * 通过邮箱查询系统用户（未永久注销用户）
     * @param email
     * @param dataStatus
     * @return
     */
    SystemUser findByEmailAndDataStatusNot(String email, DataStatus dataStatus);

    /**
     * 通过账号查询系统用户
     * @param account
     * @return
     */
    SystemUser findByAccount(String account);

    /**
     * 查询绑定系统角色的用户
     * @param systemRole
     * @param dataStatus
     * @return
     */
    List<SystemUser> queryAllBySystemRoleListContainsAndDataStatus(SystemRole systemRole, DataStatus dataStatus);
}
