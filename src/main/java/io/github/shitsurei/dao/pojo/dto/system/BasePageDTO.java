package io.github.shitsurei.dao.pojo.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 基本分页参数
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/2/15 17:53
 */
@Data
@ApiModel("分页参数")
public class BasePageDTO {

    @ApiModelProperty("当前页（不传默认1）")
    @Min(value = 1)
    private Integer currentPage = 1;

    @ApiModelProperty("每页条数（不传默认10）")
    @Min(value = 1)
    private Integer pageSize = 10;

    public Integer getCurrentPage() {
        return currentPage - 1;
    }
}
