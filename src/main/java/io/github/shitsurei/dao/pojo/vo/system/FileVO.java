package io.github.shitsurei.dao.pojo.vo.system;

import io.github.shitsurei.dao.enumerate.system.FileSuffix;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/13 15:14
 */
@Data
@ApiModel("文件")
public class FileVO {
    @ApiModelProperty("文件主键")
    private String systemFileId;

    @ApiModelProperty("文件绑定主键")
    private String bindId;

    @ApiModelProperty("文件配置编码")
    private String fileConfigCode;

    @ApiModelProperty("原文件名")
    private String originalFileName;

    @ApiModelProperty("文件类型")
    private FileSuffix fileSuffix;

    @ApiModelProperty("文件大小（kb）")
    private Long fileSize;

    @ApiModelProperty("备注")
    private String remark;
}
