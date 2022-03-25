package io.github.shitsurei.dao.pojo.dto.system;

import io.github.shitsurei.dao.enumerate.system.RoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zgr
 * @Description 角色DTO
 * @createTime 2022年01月22日 22:46:00
 */
@Data
@ApiModel("角色参数")
public class RoleDTO {
    @ApiModelProperty("角色名模糊匹配")
    private String roleNameKey;

    @ApiModelProperty("角色编码模糊匹配")
    private String roleCodeKey;

    @ApiModelProperty("新增角色名")
    @NotBlank(message = "新增角色名不能为空")
    private String roleName;

    @ApiModelProperty("新增角色编码")
    @NotBlank(message = "新增角色编码不能为空")
    private String roleCode;

    @ApiModelProperty("新增角色类型")
    @NotNull(message = "新增角色类型不能为空")
    private RoleType roleType;

    @ApiModelProperty("新增角色备注")
    private String remark;
}
