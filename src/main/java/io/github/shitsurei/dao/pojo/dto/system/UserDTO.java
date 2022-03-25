package io.github.shitsurei.dao.pojo.dto.system;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zgr
 * @Description 系统用户DTO
 * @createTime 2022年01月24日 10:32:00
 */
@Data
@ApiModel("用户参数")
public class UserDTO {
    @ApiModelProperty("登录账户模糊匹配")
    private String accountKey;

    @ApiModelProperty("注册时间区间开始")
    private Date startTime;

    @ApiModelProperty("注册时间区间结束")
    private Date endTime;

    @ApiModelProperty("用户状态：VALID（正常），TEMP（未授权），INVALID（停用），DELETE（注销）")
    private DataStatus dataStatus;
}
