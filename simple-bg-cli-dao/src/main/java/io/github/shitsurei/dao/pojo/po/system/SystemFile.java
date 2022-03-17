package io.github.shitsurei.dao.pojo.po.system;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.enumerate.system.FileSuffix;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统文件
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2022/1/5 15:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "system_t_file")
public class SystemFile implements Serializable {
    private static final long serialVersionUID = -3181346560949587669L;

    // -------------------------------------------------  主键约束  -------------------------------------------------

    @Id
    @GeneratedValue(generator = "uuidStrategy")
    @GenericGenerator(name = "uuidStrategy", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "varchar(64) COMMENT '系统文件主键'")
    private String systemFileId;

    // -------------------------------------------------  identifyColumn  -------------------------------------------------

    @Column(columnDefinition = "varchar(64) COMMENT '绑定业务数据主键'")
    private String bindId;

    @Column(columnDefinition = "varchar(128) COMMENT '原始文件名'")
    private String originalFileName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(16) COMMENT '文件类型'")
    private FileSuffix fileSuffix;

    @Column(columnDefinition = "bigint(20) COMMENT '文件大小（kb）'")
    private Long fileSize;

    @Column(columnDefinition = "varchar(512) COMMENT '文件存储路径'")
    private String savePath;

    // -------------------------------------------------  外键约束  -------------------------------------------------

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id")
    private SystemFileConfig systemFileConfig;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader")
    private SystemUser uploader;

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
