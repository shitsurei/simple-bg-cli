package io.github.shitsurei.dao.pojo.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpMethod;

/**
 * @author zgr
 * @Description 权限VO
 * @createTime 2022年01月21日 10:21:00
 */
@Data
@ApiModel("权限")
public class MenuVO {
    @ApiModelProperty("权限名")
    private String menuName;

    @ApiModelProperty("权限路由地址")
    private String urlPath;

    @ApiModelProperty("接口HTTP方法")
    private HttpMethod httpMethod;

    @ApiModelProperty("权限编码")
    private String menuCode;

    @ApiModelProperty("接口全类名")
    private String methodPath;

    @ApiModelProperty("是否可见：1隐藏0显示")
    private Boolean hidden;
}
