package io.github.shitsurei.service.system;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.pojo.po.system.SystemRole;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
import io.github.shitsurei.dao.pojo.vo.system.LoginVO;
import io.github.shitsurei.dao.pojo.vo.system.RoleVO;
import io.github.shitsurei.dao.pojo.vo.system.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 系统用户业务类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 9:58
 */
public interface ISystemUserBusiness {

    /**
     * 注册新用户
     *
     * @param account
     * @param password
     * @param email
     * @param token
     * @return
     */
    boolean register(String account, String password, String email, String token);

    /**
     * 找回账户
     *
     * @param email
     * @return
     */
    boolean retrieve(String email);

    /**
     * 重置密码
     *
     * @param password
     * @param captcha
     * @return
     */
    boolean reset(String password, String captcha);

    /**
     * 用户登录
     *
     * @param account
     * @param password
     * @return
     */
    LoginVO login(String account, String password);

    /**
     * 注销
     *
     * @return
     */
    boolean logout();


    /**
     * 永久注销
     *
     * @return
     */
    Boolean cancel();

    /**
     * 获取验证码图片
     *
     * @param request
     * @param response
     * @return
     */
    boolean getCaptcha(HttpServletRequest request, HttpServletResponse response);

    /**
     * 查询用户列表
     *
     * @param accountKey
     * @param dataStatus
     * @param startTime
     * @param endTime
     * @return
     */
    List<UserVO> userList(String accountKey, DataStatus dataStatus, Date startTime, Date endTime);

    /**
     * 查询用户绑定角色
     *
     * @param account
     * @return
     */
    List<RoleVO> authRoleList(String account);

    /**
     * 绑定角色
     *
     * @param account
     * @param roleCode
     * @return
     */
    boolean bind(String account, String roleCode);

    /**
     * 解绑角色
     *
     * @param account
     * @param roleCode
     * @return
     */
    boolean unbind(String account, String roleCode);

    /**
     * 通过账户获取用户，不存在时抛异常
     *
     * @param account
     * @return
     */
    SystemUser checkUserByAccount(String account);

    /**
     * 停用用户
     *
     * @param account
     * @return
     */
    Boolean ban(String account);

    /**
     * 恢复用户
     *
     * @param account
     * @return
     */
    Boolean unban(String account);

    /**
     * 查询绑定某角色的用户
     *
     * @param role
     * @return
     */
    List<SystemUser> queryUserByRole(SystemRole role);

    /**
     * 通过主键查询用户（带校验）
     *
     * @param systemUserId
     * @return
     */
    SystemUser findSystemUserById(String systemUserId);

    /**
     * 激活用户账户
     *
     * @param systemUserId
     * @param token
     * @return
     */
    boolean active(String systemUserId, String token);

    /**
     * 通过邮箱查询用户（不带校验）
     *
     * @param email
     * @return
     */
    SystemUser findSystemUserByEmail(String email);
}
