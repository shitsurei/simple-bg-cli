package io.github.shitsurei.service.business.impl;

import io.github.shitsurei.common.util.SessionUtil;
import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.enumerate.system.LogType;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.dao.pojo.dto.business.UserModifyDTO;
import io.github.shitsurei.dao.pojo.po.business.BusinessUser;
import io.github.shitsurei.dao.pojo.po.system.SystemFile;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import io.github.shitsurei.dao.pojo.vo.business.BusinessUserVO;
import io.github.shitsurei.dao.repository.business.BusinessUserRepository;
import io.github.shitsurei.service.business.IBusinessUserBusiness;
import io.github.shitsurei.service.system.ISystemFileBusiness;
import io.github.shitsurei.service.system.ISystemLogBusiness;
import io.github.shitsurei.service.system.ISystemUserBusiness;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * @author zgr
 * @Description 业务用户业务层
 * @createTime 2022年02月26日 20:31:00
 */
@Service
@Slf4j
public class BusinessUserBusinessImpl implements IBusinessUserBusiness {

    @Autowired
    private ISystemUserBusiness systemUserBusiness;

    @Autowired
    private ISystemFileBusiness fileBusiness;

    @Autowired
    private ISystemLogBusiness logBusiness;

    @Autowired
    private BusinessUserRepository userRepository;

    @Override
    public BusinessUser checkBusinessUserById(String businessUserId) {
        return userRepository.findById(businessUserId).orElseThrow(() -> new GlobalException(GlobalExceptionEnum.DATA_EXCEPTION, "业务用户查询结果为空！"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(UserModifyDTO userModifyDTO) {
        BusinessUser businessUser = checkBusinessUserById(userModifyDTO.getBusinessUserId());
        // 头像文件校验
        SystemFile systemFile = null;
        if (StringUtils.isNotBlank(userModifyDTO.getUserProfileId())) {
            systemFile = fileBusiness.checkSystemFileById(userModifyDTO.getUserProfileId());
        }
        // 头像文件校验
        BeanUtils.copyProperties(userModifyDTO, businessUser);
        businessUser.setProfile(systemFile);
        Date now = new Date();
        businessUser.setUpdateTime(now);
        userRepository.save(businessUser);
        String logContent = String.format("业务用户【%s】修改个人信息", businessUser.getName());
        logBusiness.saveLog(LogType.BUSINESS_USER_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    public BusinessUserVO getLoginUserDetail() {
        SystemUser loginUser = SessionUtil.getLoginUser();
        SystemUser systemUser = systemUserBusiness.findSystemUserById(loginUser.getSystemUserId());
        BusinessUser businessUser = userRepository.getBusinessUserBySystemUser(systemUser);
        // 用户首次登录系统默认创建并绑定业务用户
        if (Objects.isNull(businessUser)) {
            businessUser = createBindBusinessUser(systemUser);
        }
        BusinessUserVO businessUserVO = new BusinessUserVO();
        BeanUtils.copyProperties(businessUser, businessUserVO);
        if (Objects.nonNull(businessUser.getProfile())) {
            businessUserVO.setProfileId(businessUser.getProfile().getSystemFileId());
        }
        return businessUserVO;
    }

    @Override
    public BusinessUserVO transVO(BusinessUser businessUser) {
        BusinessUserVO businessUserVO = new BusinessUserVO();
        BeanUtils.copyProperties(businessUser, businessUserVO);
        businessUserVO.setProfileId(businessUser.getProfile().getSystemFileId());
        return businessUserVO;
    }

    /**
     * 创建并绑定新业务用户
     *
     * @param systemUser
     * @return
     */
    private BusinessUser createBindBusinessUser(SystemUser systemUser) {
        Date now = new Date();
        BusinessUser newBusinessUser = BusinessUser.builder()
                .systemUser(systemUser)
                .createPerson(systemUser.getSystemUserId())
                .createTime(now)
                .updatePerson(systemUser.getSystemUserId())
                .updateTime(now)
                .dataStatus(DataStatus.VALID)
                .remark("init business user")
                .build();
        userRepository.save(newBusinessUser);
        return newBusinessUser;
    }
}
