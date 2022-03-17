package io.github.shitsurei.dao.pojo.vo.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务用户VO
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/3/2 20:14
 */
@Data
@ApiModel("用户")
public class BusinessUserVO {

    @ApiModelProperty("业务用户id")
    private String businessUserId;

    @ApiModelProperty("用户头像id")
    private String profileId;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("职位")
    private String title;

    @ApiModelProperty("联系方式")
    private String phone;
}
