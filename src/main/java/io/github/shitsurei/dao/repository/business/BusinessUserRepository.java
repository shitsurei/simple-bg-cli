package io.github.shitsurei.dao.repository.business;

import io.github.shitsurei.dao.pojo.po.business.BusinessUser;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 业务用户持久化接口
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 19:17
 */
public interface BusinessUserRepository extends PagingAndSortingRepository<BusinessUser, String>, QuerydslPredicateExecutor<BusinessUser> {

    /**
     * 查询系统用户绑定的业务用户
     *
     * @param systemUser
     * @return
     */
    BusinessUser getBusinessUserBySystemUser(SystemUser systemUser);
}
