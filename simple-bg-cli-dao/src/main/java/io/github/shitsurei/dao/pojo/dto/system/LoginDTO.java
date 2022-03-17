package io.github.shitsurei.dao.pojo.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录DTO
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 12:26
 */
@Data
@ApiModel("用户登录接口参数")
public class LoginDTO {
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String account;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    private String captcha;
}
