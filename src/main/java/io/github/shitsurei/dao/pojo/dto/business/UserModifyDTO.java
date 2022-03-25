package io.github.shitsurei.dao.pojo.dto.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户更新DTO
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/3/2 21:20
 */
@Data
@ApiModel("用户更新参数")
public class UserModifyDTO {

    @ApiModelProperty("业务用户id")
    @NotBlank(message = "业务用户id不能为空！")
    private String businessUserId;

    @ApiModelProperty("用户头像id")
    private String userProfileId;

    @ApiModelProperty("用户姓名")
    @NotBlank(message = "用户姓名不能为空！")
    private String name;

    @ApiModelProperty("职位")
    private String title;

    @ApiModelProperty("联系方式")
    private String phone;
}
