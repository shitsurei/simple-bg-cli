package io.github.shitsurei.dao.pojo.dto.system;

import io.github.shitsurei.dao.enumerate.system.FileSuffix;
import io.github.shitsurei.dao.pojo.po.system.SystemFileConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件配置增改接口DTO
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/14 10:02
 */
@Data
@ApiModel("文件配置增改接口参数")
public class FileConfigDTO {
    @ApiModelProperty("文件配置名称")
    @NotBlank(message = "文件配置名称不能为空！")
    private String configName;

    @ApiModelProperty("文件配置编码")
    @NotBlank(message = "文件配置编码不能为空！")
    private String configCode;

    @ApiModelProperty("支持文件类型")
    @NotEmpty(message = "至少需要一种支持文件类型！")
    private List<FileSuffix> supportFileSuffixList;

    @ApiModelProperty("最大上传数量限制（不传默认不限制）")
    private Integer maxUploadNum = -1;

    @ApiModelProperty("最大上传大小限制（mb）")
    private Integer maxUploadSize = -1;

    @ApiModelProperty("是否物理删除（不传默认是）")
    private Boolean realDelete = Boolean.TRUE;

    @ApiModelProperty("是否允许非上传者查看（不传默认是）")
    private Boolean retrieveLimit = Boolean.TRUE;

    @ApiModelProperty("是否允许非上传者修改（不传默认否）")
    private Boolean updateLimit = Boolean.FALSE;

    @ApiModelProperty("是否允许非上传者删除（不传默认否）")
    private Boolean deleteLimit = Boolean.FALSE;

    /**
     * 转化为文件配置实体对象
     *
     * @return
     */
    public SystemFileConfig transform() {
        SystemFileConfig fileConfig = new SystemFileConfig();
        BeanUtils.copyProperties(this, fileConfig);
        fileConfig.setSupportFileTypes(this.getSupportFileSuffixList().stream().map(Enum::name).collect(Collectors.joining(",")));
        return fileConfig;
    }
}
