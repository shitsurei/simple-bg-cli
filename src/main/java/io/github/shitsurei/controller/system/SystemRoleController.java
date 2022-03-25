package io.github.shitsurei.controller.system;

import io.github.shitsurei.common.annotation.NoRepeatSubmit;
import io.github.shitsurei.common.annotation.PostParam;
import io.github.shitsurei.common.annotation.SysMenu;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.common.util.ValidationUtil;
import io.github.shitsurei.dao.constants.AuthorityConstant;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import io.github.shitsurei.dao.pojo.dto.system.RoleBindDTO;
import io.github.shitsurei.dao.pojo.dto.system.RoleDTO;
import io.github.shitsurei.dao.pojo.vo.system.MenuVO;
import io.github.shitsurei.dao.pojo.vo.system.RoleVO;
import io.github.shitsurei.service.system.ISystemRoleBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 系统角色控制器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/14 14:44
 */
@RestController
@RequestMapping("/systemRole")
@Api(value = "系统角色服务", tags = {"查询角色列表", "查看角色权限", "创建角色", "删除角色", "绑定权限", "解绑权限"})
@SysMenu(menuName = "系统角色控制器", menuCode = AuthorityConstant.SYSTEM_PREFIX + AuthorityConstant.MENU_CONNECT_SYMBOL + "role")
@Validated
public class SystemRoleController {

    @Autowired
    private ISystemRoleBusiness roleBusiness;

    @ApiOperation(value = "查询角色列表", httpMethod = "GET")
    @GetMapping("/list")
    @SysMenu(menuName = "查询角色列表", menuCode = "list")
    public ResponseResult<List<RoleVO>> list(@RequestBody RoleDTO roleDTO) {
        return ResponseUtil.buildSuccessResult(roleBusiness.roleList(roleDTO.getRoleNameKey(), roleDTO.getRoleCodeKey()));
    }

    @ApiOperation(value = "查看角色权限", httpMethod = "GET")
    @GetMapping("/authMenu")
    @SysMenu(menuName = "查看角色权限", menuCode = "authMenu")
    public ResponseResult<List<MenuVO>> authMenu(@NotBlank(message = "角色编码不能为空") String roleCode) {
        return ResponseUtil.buildSuccessResult(roleBusiness.authMenuList(roleCode));
    }

    @ApiOperation(value = "创建角色", httpMethod = "POST")
    @PostMapping("/create")
    @SysMenu(menuName = "创建角色", menuCode = "create")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> create(@RequestBody RoleDTO roleDTO) {
        ValidationUtil.validateObject(roleDTO);
        return ResponseUtil.buildSuccessResult(roleBusiness.create(roleDTO.getRoleName(), roleDTO.getRoleType(), roleDTO.getRoleCode(), roleDTO.getRemark()));
    }

    @ApiOperation(value = "删除角色", httpMethod = "POST")
    @PostMapping("/delete")
    @SysMenu(menuName = "删除角色", menuCode = "delete")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> delete(@PostParam(message = "角色编码不能为空！") String roleCode) {
        return ResponseUtil.buildSuccessResult(roleBusiness.delete(roleCode));
    }

    @ApiOperation(value = "绑定权限", httpMethod = "POST")
    @PostMapping("/bind")
    @SysMenu(menuName = "绑定权限", menuCode = "bind")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> bind(@RequestBody RoleBindDTO roleBindDTO) {
        ValidationUtil.validateObject(roleBindDTO);
        return ResponseUtil.buildSuccessResult(roleBusiness.bind(roleBindDTO.getRoleCode(), roleBindDTO.getMenuCodeList()));
    }

    @ApiOperation(value = "解绑权限", httpMethod = "POST")
    @PostMapping("/unbind")
    @SysMenu(menuName = "解绑权限", menuCode = "unbind")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> unbind(@RequestBody RoleBindDTO roleBindDTO) {
        ValidationUtil.validateObject(roleBindDTO);
        return ResponseUtil.buildSuccessResult(roleBusiness.unbind(roleBindDTO.getRoleCode(), roleBindDTO.getMenuCodeList()));
    }
}
