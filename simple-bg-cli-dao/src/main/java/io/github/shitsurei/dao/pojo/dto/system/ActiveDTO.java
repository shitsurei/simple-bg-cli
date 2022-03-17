package io.github.shitsurei.dao.pojo.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 账户激活DTO
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/3/3 14:45
 */
@Data
@ApiModel("账户激活接口参数")
public class ActiveDTO {
    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String id;

    @ApiModelProperty("令牌")
    @NotBlank(message = "令牌不能为空")
    private String token;
}
