package io.github.shitsurei.dao.pojo.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 时间区间DTO
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/3/15 16:43
 */
@Data
@ApiModel("时间区间参数")
public class BaseTimeRangeDTO {

    @ApiModelProperty("时间区间开始")
    @NotNull(message = "时间区间开始不能为空！")
    private Date startTime;

    @ApiModelProperty("时间区间结束")
    @NotNull(message = "时间区间结束不能为空！")
    private Date endTime;

}
