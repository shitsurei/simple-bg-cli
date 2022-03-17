package io.github.shitsurei.service.system.impl;

import io.github.shitsurei.common.util.JPAUtil;
import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.enumerate.system.LogType;
import io.github.shitsurei.dao.exception.GlobalException;
import io.github.shitsurei.dao.pojo.po.system.QSystemMenu;
import io.github.shitsurei.dao.pojo.po.system.SystemMenu;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import io.github.shitsurei.dao.pojo.vo.system.MenuVO;
import io.github.shitsurei.dao.repository.system.SystemMenuRepository;
import io.github.shitsurei.service.system.ISystemLogBusiness;
import io.github.shitsurei.service.system.ISystemMenuBusiness;
import io.github.shitsurei.service.system.ISystemUserBusiness;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 系统权限业务类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/31 9:24
 */
@Service
@Slf4j
public class SystemMenuBusinessImpl implements ISystemMenuBusiness {

    @Autowired
    private SystemMenuRepository menuRepository;

    @Autowired
    private ISystemLogBusiness logBusiness;

    @Autowired
    private ISystemUserBusiness userBusiness;

    @Override
    public List<MenuVO> menuList(String menuName, String methodPath, String urlPath, HttpMethod httpMethod) {
        QSystemMenu systemMenu = QSystemMenu.systemMenu;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (StringUtils.isNotBlank(menuName)) {
            booleanBuilder.and(systemMenu.menuName.like(JPAUtil.like(menuName)));
        }
        if (StringUtils.isNotBlank(methodPath)) {
            booleanBuilder.and(systemMenu.methodPath.like(JPAUtil.like(methodPath)));
        }
        if (StringUtils.isNotBlank(urlPath)) {
            booleanBuilder.and(systemMenu.urlPath.like(JPAUtil.like(urlPath)));
        }
        if (Objects.nonNull(httpMethod)) {
            booleanBuilder.and(systemMenu.httpMethod.eq(httpMethod));
        }
        Iterable<SystemMenu> menuList = menuRepository.findAll(booleanBuilder);
        return StreamSupport.stream(menuList.spliterator(), false).map(menu -> {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menu, menuVO);
            return menuVO;
        }).collect(Collectors.toList());
    }

    @Override
    public SystemMenu checkMenuByCode(String menuCode) {
        SystemMenu systemMenu = menuRepository.findByMenuCode(menuCode);
        if (Objects.isNull(systemMenu)) {
            throw new GlobalException(GlobalExceptionEnum.NO_MATCH_MENU);
        }
        return systemMenu;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean limit(String menuCode) {
        SystemMenu systemMenu = checkMenuByCode(menuCode);
        if (systemMenu.getDataStatus() == DataStatus.VALID) {
            return true;
        }
        systemMenu.setDataStatus(DataStatus.VALID);
        menuRepository.save(systemMenu);
        String logContent = String.format("权限菜单【%s】开启校验", systemMenu.getMenuCode());
        logBusiness.saveLog(LogType.AUTH_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unLimit(String menuCode) {
        SystemMenu systemMenu = checkMenuByCode(menuCode);
        if (systemMenu.getDataStatus() == DataStatus.INVALID) {
            return true;
        }
        systemMenu.setDataStatus(DataStatus.INVALID);
        menuRepository.save(systemMenu);
        String logContent = String.format("权限菜单【%s】关闭校验", systemMenu.getMenuCode());
        logBusiness.saveLog(LogType.AUTH_CHANGE, logContent, null);
        log.info(logContent);
        return true;
    }

    @Override
    public SystemMenu findSystemMenuByUrlAndHttpMethod(String requestPath, HttpMethod httpMethod) {
        return menuRepository.findByUrlPathAndHttpMethod(requestPath, httpMethod);
    }

    @Override
    public List<SystemMenu> queryByMenuCode(List<String> menuList) {
        return menuRepository.queryAllByMenuCodeIn(menuList);
    }

    @Override
    public Iterable<SystemMenu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public List<MenuVO> userAuthList(String systemUserId) {
        SystemUser user = userBusiness.findSystemUserById(systemUserId);
        List<MenuVO> allRoleMenu = Lists.newArrayList();
        user.getSystemRoleList().forEach(systemRole -> allRoleMenu.addAll(
                systemRole.getSystemMenuList().stream().map(menu -> {
                    MenuVO menuVO = new MenuVO();
                    BeanUtils.copyProperties(menu, menuVO);
                    return menuVO;
                }).collect(Collectors.toList())));
        return allRoleMenu.stream().distinct().collect(Collectors.toList());
    }

}
