package io.github.shitsurei.dao.pojo.bo.system;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 登录用户
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 19:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    private static final long serialVersionUID = -8332266843191865160L;

    @Expose(serialize = true, deserialize = true)
    private SystemUser systemUser;

    @Expose(serialize = false, deserialize = false)
    private List<GrantedAuthority> authorityList;

    @Expose(serialize = true, deserialize = true)
    private List<String> menuList;

    @Expose(serialize = true, deserialize = true)
    private List<String> roleList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(menuList)) {
            return null;
        }
        authorityList = menuList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorityList;
    }

    @Override
    public String getPassword() {
        return systemUser.getPassword();
    }

    @Override
    public String getUsername() {
        return systemUser.getAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 用户是否未过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 用户是否未被锁定
        return Objects.nonNull(systemUser.getDataStatus()) && systemUser.getDataStatus() == DataStatus.VALID;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 用户权限是否未过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 用户是否可用
        return Objects.nonNull(systemUser.getDataStatus()) && systemUser.getDataStatus() == DataStatus.VALID;
    }
}
