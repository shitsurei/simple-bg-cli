package io.github.shitsurei.controller.system;

import io.github.shitsurei.common.annotation.NoRepeatSubmit;
import io.github.shitsurei.common.annotation.PostParam;
import io.github.shitsurei.common.annotation.SysMenu;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.dao.constants.AuthorityConstant;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import io.github.shitsurei.dao.pojo.dto.system.MenuDTO;
import io.github.shitsurei.dao.pojo.vo.system.MenuVO;
import io.github.shitsurei.service.system.ISystemMenuBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 系统菜单权限控制器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/14 14:44
 */
@RestController
@RequestMapping("/systemMenu")
@Api(value = "系统菜单权限服务", tags = {"查询权限列表", "查询用户权限", "加入校验", "取消校验"})
@SysMenu(menuName = "系统菜单权限控制器", menuCode = AuthorityConstant.SYSTEM_PREFIX + AuthorityConstant.MENU_CONNECT_SYMBOL + "menu")
@Validated
public class SystemMenuController {

    @Autowired
    private ISystemMenuBusiness menuBusiness;

    @ApiOperation(value = "查询权限列表", httpMethod = "GET")
    @GetMapping("/list")
    @SysMenu(menuName = "查询权限列表", menuCode = "list")
    public ResponseResult<List<MenuVO>> list(@RequestBody MenuDTO menuDTO) {
        return ResponseUtil.buildSuccessResult(menuBusiness.menuList(menuDTO.getMenuName(), menuDTO.getMethodPath(),
                menuDTO.getUrlPath(), menuDTO.getHttpMethod()));
    }

    @ApiOperation(value = "查询用户权限", httpMethod = "GET")
    @GetMapping("/userAuthList")
    @SysMenu(menuName = "查询用户权限", menuCode = "userAuthList")
    public ResponseResult<List<MenuVO>> userAuthList(@RequestParam("systemUserId") @NotBlank(message = "用户id不能为空") String systemUserId) {
        return ResponseUtil.buildSuccessResult(menuBusiness.userAuthList(systemUserId));
    }

    @ApiOperation(value = "加入校验", httpMethod = "POST")
    @PostMapping("/limit")
    @SysMenu(menuName = "加入校验", menuCode = "limit")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> limit(@PostParam(message = "权限菜单编码不能为空") String menuCode) {
        return ResponseUtil.buildSuccessResult(menuBusiness.limit(menuCode));
    }

    @ApiOperation(value = "取消校验", httpMethod = "POST")
    @PostMapping("/unLimit")
    @SysMenu(menuName = "取消校验", menuCode = "unLimit")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> unLimit(@PostParam(message = "权限菜单编码不能为空") String menuCode) {
        return ResponseUtil.buildSuccessResult(menuBusiness.unLimit(menuCode));
    }
}
