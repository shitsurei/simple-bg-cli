package io.github.shitsurei.dao.pojo.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author zgr
 * @Description 枚举字典VO
 * @createTime 2022年02月07日 21:16:00
 */
@Data
@Builder
@ApiModel("枚举字典")
public class EnumVO {
    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("含义")
    private String message;
}
