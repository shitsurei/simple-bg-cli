package io.github.shitsurei.dao.pojo.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpMethod;

/**
 * @author zgr
 * @Description 权限DTO
 * @createTime 2022年01月21日 10:40:00
 */
@Data
@ApiModel("权限参数")
public class MenuDTO {
    @ApiModelProperty("权限名模糊匹配")
    private String menuName;

    @ApiModelProperty("权限路由地址模糊匹配")
    private String urlPath;

    @ApiModelProperty("接口HTTP方法")
    private HttpMethod httpMethod;

    @ApiModelProperty("接口全类名模糊匹配")
    private String methodPath;
}
