package io.github.shitsurei.dao.pojo.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 文件上传DTO
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/7 15:16
 */
@Data
@ApiModel("文件绑定接口参数")
public class FileBindDTO {
    @ApiModelProperty("文件主键")
    @NotBlank(message = "文件主键不能为空！")
    private String fileId;

    @ApiModelProperty("绑定业务数据主键")
    @NotBlank(message = "绑定业务数据主键不能为空！")
    private String bindId;
}
