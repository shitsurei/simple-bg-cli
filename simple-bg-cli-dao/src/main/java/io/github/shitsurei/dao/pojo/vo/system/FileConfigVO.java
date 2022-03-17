package io.github.shitsurei.dao.pojo.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件配置VO
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/12 17:10
 */
@Data
@ApiModel("文件配置")
public class FileConfigVO {
    @ApiModelProperty("类型名")
    private String configName;

    @ApiModelProperty("类型编码")
    private String configCode;

    @ApiModelProperty("支持文件类型")
    private String supportFileTypes;

    @ApiModelProperty("最大上传数量限制（-1为不限制）")
    private Integer maxUploadNum;

    @ApiModelProperty("最大上传大小限制（mb，-1为不限制）")
    private Integer maxUploadSize;
}
