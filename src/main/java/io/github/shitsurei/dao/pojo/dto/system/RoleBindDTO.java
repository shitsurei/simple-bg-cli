package io.github.shitsurei.dao.pojo.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author zgr
 * @Description 角色绑定解绑DTO
 * @createTime 2022年01月24日 10:05:00
 */
@Data
@ApiModel("角色绑定解绑参数")
public class RoleBindDTO {

    @ApiModelProperty("角色编码")
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    @ApiModelProperty("权限编码集合")
    @NotEmpty(message = "权限编码集合不能为空")
    private List<String> menuCodeList;
}
