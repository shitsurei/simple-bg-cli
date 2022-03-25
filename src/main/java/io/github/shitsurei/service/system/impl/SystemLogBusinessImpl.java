package io.github.shitsurei.service.system.impl;

import io.github.shitsurei.common.util.JPAUtil;
import io.github.shitsurei.common.util.PageUtil;
import io.github.shitsurei.common.util.SessionUtil;
import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.enumerate.system.LogType;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.dao.pojo.po.system.QSystemLog;
import io.github.shitsurei.dao.pojo.po.system.SystemLog;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import io.github.shitsurei.dao.pojo.vo.system.BasePageVO;
import io.github.shitsurei.dao.pojo.vo.system.LogVO;
import io.github.shitsurei.dao.repository.system.SystemLogRepository;
import io.github.shitsurei.service.system.ISystemLogBusiness;
import io.github.shitsurei.service.system.ISystemUserBusiness;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author zgr
 * @Description 系统日志业务类
 * @createTime 2022年02月07日 22:00:00
 */
@Service
@Slf4j
public class SystemLogBusinessImpl implements ISystemLogBusiness {

    @Autowired
    private SystemLogRepository logRepository;

    @Autowired
    private ISystemUserBusiness userBusiness;

    @Override
    public BasePageVO<LogVO> logList(String accountKey, LogType logType, Date startTime, Date endTime, Integer currentPage, Integer pageSize) {
        QSystemLog systemLog = QSystemLog.systemLog;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (StringUtils.isNotBlank(accountKey)) {
            booleanBuilder.and(systemLog.systemUser.account.like(JPAUtil.like(accountKey)));
        }
        if (Objects.nonNull(logType)) {
            booleanBuilder.and(systemLog.logType.eq(logType));
        }
        if (Objects.nonNull(startTime)) {
            booleanBuilder.and(systemLog.createTime.after(startTime));
        }
        if (Objects.nonNull(endTime)) {
            booleanBuilder.and(systemLog.createTime.before(endTime));
        }
        booleanBuilder.and(systemLog.dataStatus.eq(DataStatus.VALID));
        Page<SystemLog> all = logRepository.findAll(booleanBuilder, PageRequest.of(currentPage, pageSize, Sort.by(Sort.Order.desc("createTime"))));
        List<LogVO> logVOList = StreamSupport.stream(all.spliterator(), false).map(log -> {
            LogVO logVO = new LogVO();
            BeanUtils.copyProperties(log, logVO);
            logVO.setAccount(Objects.nonNull(log.getSystemUser()) ? log.getSystemUser().getAccount() : null);
            logVO.setStartTime(log.getCreateTime());
            return logVO;
        }).collect(Collectors.toList());
        return PageUtil.buildPageVO(all, logVOList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveLog(LogType logType, String content, String remark) {
        SystemUser loginUser = null;
        try {
            loginUser = SessionUtil.getLoginUser();
        } catch (Exception e) {
            remark += " session缺失";
        }
        SystemLog systemLog = SystemLog.builder()
                .content(content)
                .logType(logType)
                .dataStatus(DataStatus.VALID)
                .createPerson(Objects.nonNull(loginUser) ? loginUser.getSystemUserId() : null)
                .systemUser(Objects.isNull(loginUser) ? null : userBusiness.findSystemUserById(loginUser.getSystemUserId()))
                .createTime(new Date())
                .remark(remark)
                .build();
        logRepository.save(systemLog);
        return true;
    }

    @Override
    public boolean delete(List<String> logIdList) {
        List<SystemLog> systemLogList = logIdList.stream().map(this::checkLogById).collect(Collectors.toList());
        systemLogList.forEach(log -> log.setDataStatus(DataStatus.DELETE));
        logRepository.saveAll(systemLogList);
        return true;
    }

    @Override
    public boolean clean(LogType logType, Date startTime, Date endTime) {
        QSystemLog qLog = QSystemLog.systemLog;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (Objects.nonNull(logType)) {
            booleanBuilder.and(qLog.logType.eq(logType));
        }
        booleanBuilder.and(qLog.dataStatus.eq(DataStatus.VALID));
        booleanBuilder.and(qLog.createTime.gt(startTime));
        booleanBuilder.and(qLog.createTime.lt(endTime));
        Iterable<SystemLog> all = logRepository.findAll(booleanBuilder);
        return delete(StreamSupport.stream(all.spliterator(), false).map(SystemLog::getSystemLogId).collect(Collectors.toList()));
    }

    @Override
    public SystemLog checkLogById(String logId) {
        return logRepository.findById(logId).orElseThrow(() -> new GlobalException(GlobalExceptionEnum.DATA_EXCEPTION, "日志查询结果为空！"));
    }
}
