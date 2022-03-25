package io.github.shitsurei.dao.pojo.vo.system;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zgr
 * @Description 用户VO
 * @createTime 2022年01月24日 10:47:00
 */
@Data
@ApiModel("用户")
public class UserVO {
    @ApiModelProperty("账号")
    private String account;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("用户状态")
    private DataStatus dataStatus;
}
