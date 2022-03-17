package io.github.shitsurei.controller.system;

import io.github.shitsurei.common.annotation.NoRepeatSubmit;
import io.github.shitsurei.common.annotation.PostParam;
import io.github.shitsurei.common.annotation.SysMenu;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.common.util.ValidationUtil;
import io.github.shitsurei.dao.constants.AuthorityConstant;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import io.github.shitsurei.dao.pojo.dto.system.UserBindDTO;
import io.github.shitsurei.dao.pojo.dto.system.UserDTO;
import io.github.shitsurei.dao.pojo.vo.system.RoleVO;
import io.github.shitsurei.dao.pojo.vo.system.UserVO;
import io.github.shitsurei.service.system.ISystemUserBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author zgr
 * @Description 系统用户控制器
 * @createTime 2022年01月21日 09:11:00
 */
@RestController
@RequestMapping("/systemUser")
@Api(value = "系统用户服务", tags = {"查询用户列表", "查询用户角色", "绑定角色", "解绑角色", "停用用户", "恢复用户"})
@SysMenu(menuName = "系统用户控制器", menuCode = AuthorityConstant.SYSTEM_PREFIX + AuthorityConstant.MENU_CONNECT_SYMBOL + "user")
@Validated
public class SystemUserController {
    @Autowired
    private ISystemUserBusiness userBusiness;

    @ApiOperation(value = "查询用户列表", httpMethod = "GET")
    @GetMapping("/list")
    @SysMenu(menuName = "查询用户列表", menuCode = "list")
    public ResponseResult<List<UserVO>> list(@RequestBody UserDTO userDTO) {
        return ResponseUtil.buildSuccessResult(userBusiness.userList(userDTO.getAccountKey(),
                userDTO.getDataStatus(), userDTO.getStartTime(), userDTO.getEndTime()));
    }

    @ApiOperation(value = "查询用户角色", httpMethod = "GET")
    @GetMapping("/authRole")
    @SysMenu(menuName = "查询用户角色", menuCode = "authRole")
    public ResponseResult<List<RoleVO>> authRole(@NotBlank(message = "用户账户不能为空") String account) {
        return ResponseUtil.buildSuccessResult(userBusiness.authRoleList(account));
    }

    @ApiOperation(value = "绑定角色", httpMethod = "POST")
    @PostMapping("/bind")
    @SysMenu(menuName = "绑定角色", menuCode = "bind")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> bind(@RequestBody UserBindDTO userBindDTO) {
        ValidationUtil.validateObject(userBindDTO);
        return ResponseUtil.buildSuccessResult(userBusiness.bind(userBindDTO.getAccount(), userBindDTO.getRoleCode()));
    }

    @ApiOperation(value = "解绑角色", httpMethod = "POST")
    @PostMapping("/unbind")
    @SysMenu(menuName = "解绑角色", menuCode = "unbind")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> unbind(@RequestBody UserBindDTO userBindDTO) {
        ValidationUtil.validateObject(userBindDTO);
        return ResponseUtil.buildSuccessResult(userBusiness.unbind(userBindDTO.getAccount(), userBindDTO.getRoleCode()));
    }

    @ApiOperation(value = "停用用户", httpMethod = "POST")
    @PostMapping("/ban")
    @SysMenu(menuName = "停用用户", menuCode = "ban")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> ban(@PostParam(message = "用户账户不能为空") String account) {
        return ResponseUtil.buildSuccessResult(userBusiness.ban(account));
    }

    @ApiOperation(value = "恢复用户", httpMethod = "POST")
    @PostMapping("/unban")
    @SysMenu(menuName = "恢复用户", menuCode = "unban")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> unban(@PostParam(message = "用户账户不能为空") String account) {
        return ResponseUtil.buildSuccessResult(userBusiness.unban(account));
    }
}
