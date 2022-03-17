package io.github.shitsurei.service.system;

import io.github.shitsurei.dao.enumerate.system.LogType;
import io.github.shitsurei.dao.pojo.po.system.SystemLog;
import io.github.shitsurei.dao.pojo.vo.system.BasePageVO;
import io.github.shitsurei.dao.pojo.vo.system.LogVO;

import java.util.Date;
import java.util.List;

/**
 * @author zgr
 * @Description 系统日志业务类
 * @createTime 2022年02月07日 21:40:00
 */
public interface ISystemLogBusiness {
    /**
     * 日志列表
     *
     * @param accountKey
     * @param logType
     * @param startTime
     * @param endTime
     * @param currentPage
     * @param pageSize
     * @return
     */
    BasePageVO<LogVO> logList(String accountKey, LogType logType, Date startTime, Date endTime, Integer currentPage, Integer pageSize);

    /**
     * 新增日志
     *
     * @param logType
     * @param content
     * @param remark
     * @return
     */
    boolean saveLog(LogType logType, String content, String remark);

    /**
     * 删除日志
     *
     * @param logIdList
     * @return
     */
    boolean delete(List<String> logIdList);

    /**
     * 清理日志
     *
     *
     * @param logType
     * @param startTime
     * @param endTime
     * @return
     */
    boolean clean(LogType logType, Date startTime, Date endTime);

    /**
     * 查询日志（带校验）
     * @param logId
     * @return
     */
    SystemLog checkLogById(String logId);
}
