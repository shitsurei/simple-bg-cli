package io.github.shitsurei.dao.pojo.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author zgr
 * @Description 分页基础VO
 * @createTime 2022年03月07日 01:19:00
 */
@Data
@ApiModel("分页实体")
@Builder
public class BasePageVO<T> {

    @ApiModelProperty("总页数")
    private Integer totalPage;

    @ApiModelProperty("总条数")
    private Integer totalElement;

    @ApiModelProperty("实体数据列表")
    private List<T> dataList;
}
