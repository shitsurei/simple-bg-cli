package io.github.shitsurei.controller.system;

import io.github.shitsurei.common.annotation.NoRepeatSubmit;
import io.github.shitsurei.common.annotation.PostParam;
import io.github.shitsurei.common.annotation.SysMenu;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.dao.constants.AuthorityConstant;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import io.github.shitsurei.dao.pojo.dto.system.FileConfigDTO;
import io.github.shitsurei.dao.pojo.vo.system.FileConfigVO;
import io.github.shitsurei.service.system.ISystemFileBusiness;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件配置控制器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/13 19:18
 */
@RestController
@RequestMapping("/file/config")
@Api(value = "文件配置服务", tags = {"文件配置列表查询","新增文件配置", "修改文件配置", "删除文件配置"})
@SysMenu(menuName = "文件配置控制器", menuCode = AuthorityConstant.SYSTEM_PREFIX + AuthorityConstant.MENU_CONNECT_SYMBOL + "fileConfig")
@Validated
public class FileConfigController {

    @Autowired
    private ISystemFileBusiness fileBusiness;

    @SysMenu(menuName = "文件配置列表查询", menuCode = "list")
    @ApiOperation(value = "文件配置列表", httpMethod = "GET")
    @GetMapping("/list")
    public ResponseResult<List<FileConfigVO>> configList() {
        return ResponseUtil.buildSuccessResult(fileBusiness.configList());
    }

    @SysMenu(menuName = "新增文件配置", menuCode = "create")
    @ApiOperation(value = "新增文件配置", httpMethod = "POST")
    @PostMapping("/create")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> create(@RequestBody FileConfigDTO fileConfigDTO) {
        return ResponseUtil.buildSuccessResult(fileBusiness.createFileConfig(fileConfigDTO.transform()));
    }

    @SysMenu(menuName = "修改文件配置", menuCode = "update")
    @ApiOperation(value = "修改文件配置", httpMethod = "POST")
    @PostMapping("/update")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> update(@RequestBody FileConfigDTO fileConfigDTO) {
        return ResponseUtil.buildSuccessResult(fileBusiness.updateFileConfig(fileConfigDTO.transform()));
    }

    @SysMenu(menuName = "删除文件配置", menuCode = "delete")
    @ApiOperation(value = "删除文件配置", httpMethod = "POST")
    @PostMapping("/delete")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> delete(@PostParam(message = "文件配置编码不能为空！") String fileCode) {
        return ResponseUtil.buildSuccessResult(fileBusiness.deleteFileConfig(fileCode));
    }
}
