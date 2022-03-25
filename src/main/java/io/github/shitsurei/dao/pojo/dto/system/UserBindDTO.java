package io.github.shitsurei.dao.pojo.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zgr
 * @Description 用户绑定解绑DTO
 * @createTime 2022年01月24日 12:22:00
 */
@Data
@ApiModel("用户绑定解绑参数")
public class UserBindDTO {

    @ApiModelProperty("用户账户")
    @NotBlank(message = "用户账户不能为空")
    private String account;

    @ApiModelProperty("角色编码")
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;
}
