package io.github.shitsurei.controller.business;

import io.github.shitsurei.common.annotation.NoRepeatSubmit;
import io.github.shitsurei.common.annotation.SysMenu;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.common.util.ValidationUtil;
import io.github.shitsurei.dao.constants.AuthorityConstant;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import io.github.shitsurei.dao.pojo.dto.business.UserModifyDTO;
import io.github.shitsurei.dao.pojo.vo.business.BusinessUserVO;
import io.github.shitsurei.service.business.IBusinessUserBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zgr
 * @Description 业务用户控制器
 * @createTime 2022年02月26日 19:55:00
 */
@RestController
@RequestMapping("/businessExample")
@Api(value = "业务演示服务", tags = {"获取登录用户信息", "编辑用户信息"})
@SysMenu(menuName = "业务演示控制器", menuCode = AuthorityConstant.BUSINESS_PREFIX + AuthorityConstant.MENU_CONNECT_SYMBOL + "example")
@Validated
public class BusinessExampleController {

    @Autowired
    private IBusinessUserBusiness userBusiness;

    @ApiOperation(value = "获取登录用户信息", httpMethod = "GET")
    @GetMapping("/getLoginUserDetail")
    @SysMenu(menuName = "获取登录用户信息", menuCode = "getLoginUserDetail")
    public ResponseResult<BusinessUserVO> getLoginUserDetail() {
        return ResponseUtil.buildSuccessResult(userBusiness.getLoginUserDetail());
    }

    @ApiOperation(value = "编辑用户信息", httpMethod = "POST")
    @PostMapping("/update")
    @SysMenu(menuName = "编辑用户信息", menuCode = "update")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> update(@RequestBody UserModifyDTO userModifyDTO) {
        ValidationUtil.validateObject(userModifyDTO);
        return ResponseUtil.buildSuccessResult(userBusiness.update(userModifyDTO));
    }
}
