package io.github.shitsurei.dao.pojo.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 登录结果VO
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/21 17:47
 */
@Data
@Builder
@ApiModel("登录结果VO")
public class LoginVO {

    @ApiModelProperty("登录token")
    private String token;
}
