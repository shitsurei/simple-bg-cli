package io.github.shitsurei.dao.pojo.dto.system;

import io.github.shitsurei.dao.enumerate.system.LogType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zgr
 * @Description 日志检索DTO
 * @createTime 2022年02月07日 21:28:00
 */
@Data
@ApiModel("日志检索参数")
public class LogDTO extends BasePageDTO{
    @ApiModelProperty("用户账号模糊匹配")
    private String accountKey;

    @ApiModelProperty("日志类型")
    private LogType logType;

    @ApiModelProperty("日志生成时间区间开始")
    private Date startTime;

    @ApiModelProperty("日志生成时间区间结束")
    private Date endTime;
}
