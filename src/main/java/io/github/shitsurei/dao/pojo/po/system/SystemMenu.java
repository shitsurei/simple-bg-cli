package io.github.shitsurei.dao.pojo.po.system;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统权限实体类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/24 10:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "system_t_menu")
public class SystemMenu implements Serializable {
    private static final long serialVersionUID = -913686938217950514L;

    // -------------------------------------------------  主键约束  -------------------------------------------------

    @Id
    @GeneratedValue(generator = "uuidStrategy")
    @GenericGenerator(name = "uuidStrategy", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "varchar(64) COMMENT '系统权限主键'")
    private String systemMenuId;

    // -------------------------------------------------  identifyColumn  -------------------------------------------------

    @Column(columnDefinition = "varchar(128) COMMENT '权限名'")
    private String menuName;

    @Column(columnDefinition = "varchar(128) COMMENT '权限路由地址'")
    private String urlPath;

    @Column(columnDefinition = "varchar(32) COMMENT '接口HTTP方法'")
    @Enumerated(EnumType.STRING)
    private HttpMethod httpMethod;

    @Column(columnDefinition = "varchar(128) COMMENT '权限编码'")
    private String menuCode;

    @Column(columnDefinition = "varchar(128) COMMENT '接口全类名'")
    private String methodPath;

    @Column(columnDefinition = "tinyint COMMENT '是否可见：1隐藏0显示'")
    private Boolean hidden;

    // -------------------------------------------------  baseColumn  -------------------------------------------------

    @Column(columnDefinition = "varchar(64) COMMENT '创建人'")
    private String createPerson;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "varchar(32) COMMENT '创建时间'")
    private Date createTime;

    @Column(columnDefinition = "varchar(64) COMMENT '修改人'")
    private String updatePerson;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "varchar(32) COMMENT '修改时间'")
    private Date updateTime;

    @Column(columnDefinition = "text COMMENT '备注'")
    private String remark;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(16) COMMENT '数据状态'")
    private DataStatus dataStatus;
}
