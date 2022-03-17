package io.github.shitsurei.dao.repository.system;

import io.github.shitsurei.dao.pojo.po.system.SystemLog;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author zgr
 * @Description 系统日志持久化接口
 * @createTime 2022年02月07日 22:32:00
 */
public interface SystemLogRepository extends PagingAndSortingRepository<SystemLog, String>, QuerydslPredicateExecutor<SystemLog> {
}
