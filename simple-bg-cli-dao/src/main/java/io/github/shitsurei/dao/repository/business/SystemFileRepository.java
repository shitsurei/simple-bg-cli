package io.github.shitsurei.dao.repository.business;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.pojo.po.system.SystemFile;
import io.github.shitsurei.dao.pojo.po.system.SystemFileConfig;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 系统文件持久化接口
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/7 10:10
 */
public interface SystemFileRepository extends PagingAndSortingRepository<SystemFile, String> {

    /**
     * 根据绑定业务数据查询文件
     *
     * @param bindIdList
     * @param dataStatus
     * @return
     */
    List<SystemFile> queryAllByBindIdInAndDataStatus(List<String> bindIdList, DataStatus dataStatus);

    /**
     * 统计用户上传单个类型文件数量
     *
     * @param systemFileConfig
     * @param bindId
     * @param dataStatus
     * @return
     */
    Integer countAllBySystemFileConfigAndBindIdAndDataStatus(SystemFileConfig systemFileConfig, String bindId, DataStatus dataStatus);
}
