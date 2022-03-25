package io.github.shitsurei.controller.system;

import io.github.shitsurei.common.annotation.SysMenu;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.dao.constants.AuthorityConstant;
import io.github.shitsurei.dao.constants.CustomProperties;
import io.github.shitsurei.dao.enumerate.system.EnumType;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import io.github.shitsurei.dao.pojo.vo.system.EnumVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zgr
 * @Description 通用服务控制器
 * @createTime 2022年01月21日 11:02:00
 */
@RestController
@RequestMapping("/common")
@Api(value = "通用功能服务器", tags = {"健康检测", "枚举字典"})
@SysMenu(menuName = "通用服务控制器", menuCode = AuthorityConstant.BUSINESS_PREFIX + AuthorityConstant.MENU_CONNECT_SYMBOL + "common")
public class CommonController {

    @Autowired
    private CustomProperties customProperties;

    @ApiOperation(value = "健康检测", httpMethod = "GET")
    @GetMapping("/healthCheck")
    public ResponseResult<Boolean> healthCheck() {
        return ResponseUtil.buildSuccessResult(Boolean.TRUE);
    }

    @ApiOperation(value = "配置动态刷新检测", httpMethod = "GET")
    @GetMapping("/configRefreshCheck")
    public ResponseResult<String> configRefreshCheck() {
        return ResponseUtil.buildSuccessResult(customProperties.getTestConfig());
    }

    @ApiOperation(value = "枚举字典", httpMethod = "GET")
    @GetMapping("/dict")
    @SysMenu(menuName = "枚举字典", menuCode = "dict")
    public ResponseResult<List<EnumVO>> dict(@RequestParam(required = false) String enumCode) {
        return ResponseUtil.buildSuccessResult(EnumType.getEnumArray(enumCode));
    }
}
