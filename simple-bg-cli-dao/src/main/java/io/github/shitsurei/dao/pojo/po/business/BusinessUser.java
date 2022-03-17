package io.github.shitsurei.dao.pojo.po.business;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import io.github.shitsurei.dao.pojo.po.system.SystemFile;
import io.github.shitsurei.dao.pojo.po.system.SystemUser;
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
 * 业务用户
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/17 16:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "business_t_user")
public class BusinessUser implements Serializable {

    private static final long serialVersionUID = 4754099147614309689L;

    // -------------------------------------------------  主键约束  -------------------------------------------------

    @Id
    @GeneratedValue(generator = "uuidStrategy")
    @GenericGenerator(name = "uuidStrategy", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "varchar(64) COMMENT '业务用户主键'")
    private String businessUserId;

    // -------------------------------------------------  identifyColumn  -------------------------------------------------

    @Column(columnDefinition = "varchar(64) COMMENT '用户姓名'")
    private String name;

    @Column(columnDefinition = "varchar(64) COMMENT '职位'")
    private String title;

    @Column(columnDefinition = "varchar(64) COMMENT '联系方式'")
    private String phone;

    // -------------------------------------------------  外键约束  -------------------------------------------------

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "system_user_id")
    private SystemUser systemUser;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    private SystemFile profile;

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
