package io.github.shitsurei.service.system.impl;

import io.github.shitsurei.common.util.JPAUtil;
import io.github.shitsurei.common.util.SessionUtil;
import io.github.shitsurei.dao.enumerate.system.*;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.dao.pojo.po.system.QSystemRole;
import io.github.shitsurei.dao.pojo.po.system.SystemMenu;
import io.github.shitsurei.dao.pojo.po.system.SystemRole;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import io.github.shitsurei.dao.pojo.vo.system.MenuVO;
import io.github.shitsurei.dao.pojo.vo.system.RoleVO;
import io.github.shitsurei.dao.repository.system.SystemRoleRepository;
import io.github.shitsurei.service.system.ISystemLogBusiness;
import io.github.shitsurei.service.system.ISystemMenuBusiness;
import io.github.shitsurei.service.system.ISystemRoleBusiness;
import io.github.shitsurei.service.system.ISystemUserBusiness;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author zgr
 * @Description 系统角色业务层
 * @createTime 2022年01月22日 22:44:00
 */
@Service
@Slf4j
public class SystemRoleBusinessImpl implements ISystemRoleBusiness {

    @Autowired
    private SystemRoleRepository roleRepository;

    @Autowired
    private ISystemMenuBusiness menuBusiness;

    @Autowired
    private ISystemUserBusiness userBusiness;

    @Autowired
    private ISystemLogBusiness logBusiness;

    @Override
    public List<RoleVO> roleList(String roleName, String roleCode) {
        QSystemRole systemRole = QSystemRole.systemRole;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (StringUtils.isNotBlank(roleName)) {
            booleanBuilder.and(systemRole.roleName.like(JPAUtil.like(roleName)));
        }
        if (StringUtils.isNotBlank(roleCode)) {
            booleanBuilder.and(systemRole.roleCode.like(JPAUtil.like(roleCode)));
        }
        Iterable<SystemRole> all = roleRepository.findAll(booleanBuilder);
        return StreamSupport.stream(all.spliterator(), false).map(role -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            return roleVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MenuVO> authMenuList(String roleCode) {
        SystemRole role = checkRoleByCode(roleCode);
        return role.getSystemMenuList().stream().map(menu -> {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menu, menuVO);
            return menuVO;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(String roleName, RoleType roleType, String roleCode, String remark) {
        SystemRole role = roleRepository.findByRoleCode(roleCode);
        if (Objects.nonNull(role)) {
            throw new GlobalException(GlobalExceptionEnum.DUPLICATE_ROLE_CODE);
        }
        SystemUser loginUser = SessionUtil.getLoginUser();
        Date now = new Date();
        role = SystemRole.builder()
                .roleName(roleName)
                .roleCode(RoleType.buildCode(roleType, roleCode))
                .createPerson(loginUser.getSystemUserId())
                .createTime(now)
                .updatePerson(loginUser.getSystemUserId())
                .updateTime(now)
                .remark(remark)
                .dataStatus(DataStatus.VALID)
                .build();
        roleRepository.save(role);
        String logContent = String.format("创建【%s】类型角色【%s】，角色编码【%s】", roleType.msg(), roleName, roleCode);
        logBusiness.saveLog(LogType.ROLE_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String roleCode) {
        SystemRole role = checkRoleByCode(roleCode);
        List<SystemUser> bindRoleUserList = userBusiness.queryUserByRole(role);
        if (!CollectionUtils.isEmpty(bindRoleUserList)) {
            throw new GlobalException(GlobalExceptionEnum.EXIST_BIND_USER);
        }
        role.setDataStatus(DataStatus.DELETE);
        SystemUser loginUser = SessionUtil.getLoginUser();
        Date now = new Date();
        role.setUpdatePerson(loginUser.getSystemUserId());
        role.setUpdateTime(now);
        roleRepository.save(role);
        String logContent = String.format("删除角色【%s】", role.getRoleName());
        logBusiness.saveLog(LogType.ROLE_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bind(String roleCode, List<String> menuCodeList) {
        List<SystemMenu> bindMenuList = menuCodeList.stream().
                map(menuCode -> menuBusiness.checkMenuByCode(menuCode)).collect(Collectors.toList());
        SystemRole role = checkRoleByCode(roleCode);
        // 系统角色和业务角色之间权限隔离，不允许穿插绑定
        RoleType roleType = RoleType.parseCode(role.getRoleCode());
        bindMenuList.forEach(menu -> {
            if (!MenuType.parseCode(menu.getMenuCode()).match(roleType)) {
                throw new GlobalException(GlobalExceptionEnum.ROLE_MENU_NOT_MATCH);
            }
        });
        List<SystemMenu> existMenuList = role.getSystemMenuList();
        if (CollectionUtils.isEmpty(existMenuList)) {
            existMenuList = Lists.newArrayList();
        }
        existMenuList.addAll(bindMenuList);
        role.setSystemMenuList(existMenuList.stream().distinct().collect(Collectors.toList()));
        SystemUser loginUser = SessionUtil.getLoginUser();
        Date now = new Date();
        role.setUpdatePerson(loginUser.getSystemUserId());
        role.setUpdateTime(now);
        roleRepository.save(role);
        String logContent = String.format("角色【%s】绑定权限【%s】", role.getRoleName(), StringUtils.join(bindMenuList.stream().map(SystemMenu::getMenuName).collect(Collectors.toList()), ","));
        logBusiness.saveLog(LogType.ROLE_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbind(String roleCode, List<String> menuCodeList) {
        List<SystemMenu> unbindMenuList = menuCodeList.stream().
                map(menuCode -> menuBusiness.checkMenuByCode(menuCode)).collect(Collectors.toList());
        SystemRole role = checkRoleByCode(roleCode);
        List<SystemMenu> existMenuList = role.getSystemMenuList();
        if (CollectionUtils.isEmpty(existMenuList)) {
            existMenuList = Lists.newArrayList();
        }
        existMenuList.removeAll(unbindMenuList);
        List<SystemMenu> newBindMenuList = existMenuList.stream().distinct().collect(Collectors.toList());
        role.setSystemMenuList(newBindMenuList);
        SystemUser loginUser = SessionUtil.getLoginUser();
        Date now = new Date();
        role.setUpdatePerson(loginUser.getSystemUserId());
        role.setUpdateTime(now);
        roleRepository.save(role);
        String logContent = String.format("角色【%s】取消绑定权限【%s】", role.getRoleName(), StringUtils.join(unbindMenuList.stream().map(SystemMenu::getMenuName).collect(Collectors.toList()), ","));
        logBusiness.saveLog(LogType.ROLE_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    public SystemRole checkRoleByCode(String roleCode) {
        SystemRole systemRole = roleRepository.findByRoleCode(roleCode);
        if (Objects.isNull(systemRole)) {
            throw new GlobalException(GlobalExceptionEnum.NO_MATCH_ROLE);
        }
        return systemRole;
    }
}
