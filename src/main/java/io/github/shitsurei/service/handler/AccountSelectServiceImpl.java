package io.github.shitsurei.service.handler;

import io.github.shitsurei.common.util.LocaleUtil;
import io.github.shitsurei.dao.enumerate.system.GlobalExceptionEnum;
import io.github.shitsurei.dao.pojo.bo.system.LoginUser;
import io.github.shitsurei.dao.pojo.po.system.SystemMenu;
import io.github.shitsurei.dao.pojo.po.system.SystemRole;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import io.github.shitsurei.dao.repository.system.SystemUserRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户名密码校验
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 11:55
 */
@Service
public class AccountSelectServiceImpl implements UserDetailsService {

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private LocaleUtil localeUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查库获取用户登录信息
        SystemUser systemUser = systemUserRepository.findByAccount(username);
        if (Objects.isNull(systemUser)) {
            // 该异常会被spring security拦截后统一封装成Bad credentials返回前端
            throw new UsernameNotFoundException(localeUtil.getLocaleMessage(GlobalExceptionEnum.USER_NOTFOUND.getErrorMsg()));
        }
        // 查询用户所有角色
        List<SystemRole> systemRoleList = systemUser.getSystemRoleList();
        List<String> roleList = systemRoleList.stream().map(SystemRole::getRoleCode).collect(Collectors.toList());
        List<String> menuList = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(systemRoleList)) {
            // 查询用户所有角色下的权限
            List<SystemMenu> systemMenuList = systemRoleList.stream().map(SystemRole::getSystemMenuList).flatMap(Collection::stream).distinct().collect(Collectors.toList());
            menuList = systemMenuList.stream().map(SystemMenu::getMenuCode).collect(Collectors.toList());
        }
        return new LoginUser(systemUser, null, menuList, roleList);
    }
}
