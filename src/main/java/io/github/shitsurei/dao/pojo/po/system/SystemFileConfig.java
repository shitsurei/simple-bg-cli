package io.github.shitsurei.dao.pojo.po.system;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统文件配置
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/5 15:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "system_t_file_config")
public class SystemFileConfig implements Serializable {
    private static final long serialVersionUID = -4670738151266170387L;

    // -------------------------------------------------  主键约束  -------------------------------------------------

    @Id
    @GeneratedValue(generator = "uuidStrategy")
    @GenericGenerator(name = "uuidStrategy", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "varchar(64) COMMENT '系统文件配置主键'")
    private String systemFileConfigId;

    // -------------------------------------------------  identifyColumn  -------------------------------------------------

    @Column(columnDefinition = "varchar(128) COMMENT '类型名'")
    private String configName;

    @Column(columnDefinition = "varchar(128) COMMENT '类型编码'")
    private String configCode;

    @Column(columnDefinition = "varchar(128) COMMENT '支持文件类型'")
    private String supportFileTypes;

    @Column(columnDefinition = "int COMMENT '最大上传数量限制（-1为不限制）'")
    private Integer maxUploadNum;

    @Column(columnDefinition = "int COMMENT '最大上传文件大小（单位mb，-1为不限制）'")
    private Integer maxUploadSize;

    @Column(columnDefinition = "tinyint COMMENT '是否物理删除：1是0否'")
    private Boolean realDelete;

    @Column(columnDefinition = "tinyint COMMENT '操作权限限制，是否允许非上传者查看：1是0否'")
    private Boolean retrieveLimit;

    @Column(columnDefinition = "tinyint COMMENT '操作权限限制，是否允许非上传者修改：1是0否'")
    private Boolean updateLimit;

    @Column(columnDefinition = "tinyint COMMENT '操作权限限制，是否允许非上传者删除：1是0否'")
    private Boolean deleteLimit;

    // -------------------------------------------------  外键约束  -------------------------------------------------

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id")
    private List<SystemFile> systemFileList;

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
