package io.github.shitsurei.dao.repository.system;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.pojo.po.system.SystemFileConfig;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 系统文件配置持久化接口
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/7 10:13
 */
public interface SystemFileConfigRepository extends PagingAndSortingRepository<SystemFileConfig, String> {

    /**
     * 查询全部文件配置
     *
     * @param dataStatus
     * @return
     */
    List<SystemFileConfig> queryAllByDataStatus(DataStatus dataStatus);

    /**
     * 模糊匹配编码查询文件配置
     *
     * @param dataStatus
     * @return
     */
    List<SystemFileConfig> findAllByConfigCodeLikeAndDataStatus(String configCodeKey, DataStatus dataStatus);

    /**
     * 根据文件配置编码查询配置项
     *
     * @param configCode
     * @param dataStatus
     * @return
     */
    SystemFileConfig getSystemFileConfigByConfigCodeAndDataStatus(String configCode, DataStatus dataStatus);
}
