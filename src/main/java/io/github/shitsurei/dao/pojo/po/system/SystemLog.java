package io.github.shitsurei.dao.pojo.po.system;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.enumerate.system.LogType;
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
 * @author zgr
 * @Description 系统日志
 * @createTime 2022年02月07日 21:07:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "system_t_log")
public class SystemLog implements Serializable {
    private static final long serialVersionUID = 6166866652191483428L;

    // -------------------------------------------------  主键约束  -------------------------------------------------

    @Id
    @GeneratedValue(generator = "uuidStrategy")
    @GenericGenerator(name = "uuidStrategy", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "varchar(64) COMMENT '系统日志主键'")
    private String systemLogId;

    // -------------------------------------------------  identifyColumn  -------------------------------------------------

    @Column(columnDefinition = "varchar(512) COMMENT '日志内容'")
    private String content;

    @Column(columnDefinition = "varchar(32) COMMENT '日志类型'")
    @Enumerated(EnumType.STRING)
    private LogType logType;

    // -------------------------------------------------  外键约束  -------------------------------------------------

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SystemUser systemUser;

    // -------------------------------------------------  baseColumn  -------------------------------------------------

    @Column(columnDefinition = "varchar(64) COMMENT '创建人'")
    private String createPerson;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "varchar(32) COMMENT '创建时间'")
    private Date createTime;

    @Column(columnDefinition = "text COMMENT '备注'")
    private String remark;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(16) COMMENT '数据状态'")
    private DataStatus dataStatus;
}
