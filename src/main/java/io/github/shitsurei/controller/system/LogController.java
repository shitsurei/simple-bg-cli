package io.github.shitsurei.controller.system;

import io.github.shitsurei.common.annotation.NoRepeatSubmit;
import io.github.shitsurei.common.annotation.PostParam;
import io.github.shitsurei.common.annotation.SysMenu;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.dao.constants.AuthorityConstant;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import io.github.shitsurei.dao.pojo.dto.system.LogCleanDTO;
import io.github.shitsurei.dao.pojo.dto.system.LogDTO;
import io.github.shitsurei.dao.pojo.vo.system.BasePageVO;
import io.github.shitsurei.dao.pojo.vo.system.LogVO;
import io.github.shitsurei.service.system.ISystemLogBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zgr
 * @Description 日志控制器
 * @createTime 2022年02月07日 21:03:00
 */
@RestController
@RequestMapping("/log")
@Api(value = "日志服务", tags = {"查询日志列表", "删除日志", "清理日志"})
@SysMenu(menuName = "日志控制器", menuCode = AuthorityConstant.SYSTEM_PREFIX + AuthorityConstant.MENU_CONNECT_SYMBOL + "log")
public class LogController {

    @Autowired
    private ISystemLogBusiness logBusiness;

    @ApiOperation(value = "查询日志列表", httpMethod = "GET")
    @GetMapping("/list")
    @SysMenu(menuName = "查询日志列表", menuCode = "list")
    public ResponseResult<BasePageVO<LogVO>> list(@RequestBody LogDTO logDTO) {
        return ResponseUtil.buildSuccessResult(logBusiness.logList(logDTO.getAccountKey(), logDTO.getLogType(), logDTO.getStartTime(),
                logDTO.getEndTime(), logDTO.getCurrentPage(), logDTO.getPageSize()));
    }

    @ApiOperation(value = "删除日志", httpMethod = "POST")
    @PostMapping("/delete")
    @SysMenu(menuName = "删除日志", menuCode = "delete")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> delete(@PostParam(message = "日志id集合不能为空！") List<String> logIdList) {
        return ResponseUtil.buildSuccessResult(logBusiness.delete(logIdList));
    }

    @ApiOperation(value = "清理日志", httpMethod = "POST")
    @PostMapping("/clean")
    @SysMenu(menuName = "清理日志", menuCode = "clean")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> clean(@RequestBody LogCleanDTO logCleanDTO) {
        return ResponseUtil.buildSuccessResult(logBusiness.clean(logCleanDTO.getLogType(), logCleanDTO.getStartTime(), logCleanDTO.getEndTime()));
    }
}
