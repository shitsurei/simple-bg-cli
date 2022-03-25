package io.github.shitsurei.dao.pojo.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 重置密码DTO
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/2/18 16:39
 */
@Data
@ApiModel("重置密码接口参数")
public class ResetPwdDTO {
    @ApiModelProperty("新密码")
    @NotBlank(message = "新密码不能为空")
    private String password;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    private String captcha;
}
