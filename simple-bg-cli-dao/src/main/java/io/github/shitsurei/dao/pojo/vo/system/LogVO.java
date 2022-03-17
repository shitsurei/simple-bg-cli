package io.github.shitsurei.dao.pojo.vo.system;

import io.github.shitsurei.dao.enumerate.system.LogType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zgr
 * @Description 日志
 * @createTime 2022年02月07日 21:36:00
 */
@Data
@ApiModel("日志")
public class LogVO {
    @ApiModelProperty("日志主键")
    private String systemLogId;

    @ApiModelProperty("日志内容")
    private String content;

    @ApiModelProperty("日志类型")
    private LogType logType;

    @ApiModelProperty("用户账户")
    private String account;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("生成时间")
    private Date startTime;
}
