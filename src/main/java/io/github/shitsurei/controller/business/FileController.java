package io.github.shitsurei.controller.business;

import io.github.shitsurei.common.annotation.NoRepeatSubmit;
import io.github.shitsurei.common.annotation.PostParam;
import io.github.shitsurei.common.annotation.SysMenu;
import io.github.shitsurei.common.util.ResponseUtil;
import io.github.shitsurei.common.util.SessionUtil;
import io.github.shitsurei.dao.constants.AuthorityConstant;
import io.github.shitsurei.dao.enumerate.system.DataOperate;
import io.github.shitsurei.dao.pojo.bo.system.ResponseResult;
import io.github.shitsurei.dao.pojo.dto.system.FileBindDTO;
import io.github.shitsurei.dao.pojo.po.system.SystemFile;
import io.github.shitsurei.dao.pojo.vo.system.FileVO;
import io.github.shitsurei.service.system.ISystemFileBusiness;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;

/**
 * 文件服务控制器
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/1 13:39
 */
@RestController
@RequestMapping("/file")
@Api(value = "文件服务", tags = {"查询业务数据绑定文件列表", "上传文件", "下载文件", "删除文件", "文件换绑业务数据"})
@SysMenu(menuName = "文件服务控制器", menuCode = AuthorityConstant.BUSINESS_PREFIX + AuthorityConstant.MENU_CONNECT_SYMBOL + "file")
@Validated
public class FileController {

    @Autowired
    private ISystemFileBusiness fileBusiness;

    @ApiOperation(value = "文件列表", httpMethod = "GET")
    @GetMapping("/list")
    @SysMenu(menuName = "查询业务数据绑定文件列表", menuCode = "list")
    public ResponseResult<List<FileVO>> fileList(@RequestParam("businessId") String businessId) {
        return ResponseUtil.buildSuccessResult(fileBusiness.fileList(businessId));
    }

    @ApiOperation(value = "上传", httpMethod = "POST")
    @PostMapping("/upload")
    @SysMenu(menuName = "上传", menuCode = "upload")
    @NoRepeatSubmit()
    public ResponseResult<List<String>> upload(@RequestParam("files") MultipartFile[] files,
                                               @RequestParam("configCode") @NotBlank(message = "配置编码不能为空！") String configCode,
                                               @RequestParam(value = "businessId", required = false) String businessId) {
        if (ArrayUtils.isEmpty(files)) {
            return ResponseUtil.buildSuccessResult(Collections.emptyList());
        }
        List<SystemFile> systemFiles = fileBusiness.checkFileValid(Lists.newArrayList(files), configCode, businessId);
        return ResponseUtil.buildSuccessResult(fileBusiness.save(systemFiles, Lists.newArrayList(files)));
    }

    @ApiOperation(value = "下载", httpMethod = "GET")
    @GetMapping("/download")
    @SysMenu(menuName = "下载", menuCode = "download")
    public void download(@RequestParam("fileId") @NotBlank(message = "文件主键不能为空！") String fileId) {
        SystemFile file = fileBusiness.checkFileById(fileId);
        // 校验查看权限
        fileBusiness.checkOperateValid(file, DataOperate.RETRIEVE);
        // 向前端写入文件
        fileBusiness.output(file, SessionUtil.getRequest(), SessionUtil.getResponse());
    }

    @ApiOperation(value = "删除", httpMethod = "POST")
    @PostMapping("/delete")
    @SysMenu(menuName = "删除", menuCode = "delete")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> delete(@PostParam(message = "文件主键不能为空！") String fileId) {
        SystemFile file = fileBusiness.checkFileById(fileId);
        // 校验删除权限
        fileBusiness.checkOperateValid(file, DataOperate.DELETE);
        fileBusiness.delete(file);
        return ResponseUtil.buildSuccessResult(Boolean.TRUE);
    }

    @ApiOperation(value = "换绑业务数据", httpMethod = "POST")
    @PostMapping("/bind")
    @SysMenu(menuName = "换绑业务数据", menuCode = "bind")
    @NoRepeatSubmit()
    public ResponseResult<Boolean> bind(@RequestBody FileBindDTO fileBindDTO) {
        return ResponseUtil.buildSuccessResult(fileBusiness.bind(fileBindDTO.getFileId(), fileBindDTO.getBindId()));
    }
}
