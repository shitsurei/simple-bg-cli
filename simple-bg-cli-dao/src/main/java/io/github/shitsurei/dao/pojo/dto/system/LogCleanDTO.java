package io.github.shitsurei.dao.pojo.dto.system;

import io.github.shitsurei.dao.enumerate.system.LogType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 日志清理DTO
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/3/15 16:58
 */
@Data
@ApiModel("日志清理参数")
public class LogCleanDTO extends BaseTimeRangeDTO {

    @ApiModelProperty("日志类型")
    private LogType logType;
}
