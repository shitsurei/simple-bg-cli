package io.github.shitsurei.dao.pojo.po.system;

import io.github.shitsurei.dao.enumerate.system.DataStatus;
import com.google.common.base.Objects;
import com.google.gson.annotations.Expose;
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
 * 系统用户实体类
 *
 * @author zhanggr-b
 * @version 1.0
 * @date 2021/12/20 15:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "system_t_user")
public class SystemUser implements Serializable {

    private static final long serialVersionUID = -913686938217950514L;

    // -------------------------------------------------  主键约束  -------------------------------------------------

    @Id
    @GeneratedValue(generator = "uuidStrategy")
    @GenericGenerator(name = "uuidStrategy", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "varchar(64) COMMENT '系统用户主键'")
    @Expose(serialize = true, deserialize = true)
    private String systemUserId;

    // -------------------------------------------------  identifyColumn  -------------------------------------------------

    @Column(columnDefinition = "varchar(32) COMMENT '登录用户名'")
    @Expose(serialize = true, deserialize = true)
    private String account;

    @Column(columnDefinition = "varchar(64) COMMENT '登录密码'")
    @Expose(serialize = true, deserialize = true)
    private String password;

    @Column(columnDefinition = "varchar(64) COMMENT '邮箱'")
    @Expose(serialize = true, deserialize = true)
    private String email;

    // -------------------------------------------------  外键约束  -------------------------------------------------

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "system_r_user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<SystemRole> systemRoleList;

    // -------------------------------------------------  baseColumn  -------------------------------------------------

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "varchar(32) COMMENT '创建时间'")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "varchar(32) COMMENT '修改时间'")
    private Date updateTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(16) COMMENT '数据状态'")
    @Expose(serialize = true, deserialize = true)
    private DataStatus dataStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemUser that = (SystemUser) o;
        return Objects.equal(systemUserId, that.systemUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(systemUserId);
    }
}
