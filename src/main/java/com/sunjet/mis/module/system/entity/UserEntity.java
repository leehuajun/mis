package com.sunjet.mis.module.system.entity;

import com.sunjet.mis.base.entity.IdEntity;
import com.sunjet.mis.base.entity.IdSystemEntity;
import com.sunjet.mis.helper.UserType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * Created by lhj on 2015/9/6.
 * 用户账号实体
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user")
public class UserEntity extends IdSystemEntity {
    private static final long serialVersionUID = -3786601921455785818L;
    /**
     * 登录名
     */
    @Column(name = "log_id_", length = 100, nullable = false, unique = true)
    private String logId;
    /**
     * 加密后的密码
     */
    @Column(name = "password_", length = 50, nullable = false)
    private String password;
    /**
     * 密码加密 盐
     */
    @Column(name = "salt_", length = 40, nullable = false)
    private String salt;
    /**
     * 姓名
     */
    @Column(name = "name_", length = 50, nullable = false)
    private String name;

    @Builder.Default
    @Column(name = "user_type_")
    private Integer userType = UserType.INTERNAL.getIndex();
    /**
     * 电话
     */
    @Column(name = "phone_", length = 100)
    private String phone;

    /**
     * 电子邮件地址
     */
    @Column(name = "email_", length = 100)
    private String email;

    /**
     * 用户所属角色列表
     */
    @Builder.Default
    @ManyToMany
    @JoinTable(name = "sys_user_role",
    joinColumns = @JoinColumn(name = "user_id_",referencedColumnName = "id_"),
    inverseJoinColumns = @JoinColumn(name = "role_id_",referencedColumnName = "id_"))
    private Set<RoleEntity> roles = new HashSet<>();

    /**
     * 用户所属组织列表
     */
    @Builder.Default
    @ManyToMany
    @JoinTable(name = "sys_user_org",
    joinColumns = @JoinColumn(name = "user_id_",referencedColumnName = "id_"),
    inverseJoinColumns = @JoinColumn(name = "org_id_",referencedColumnName = "id_"))
    private Set<OrgEntity> orgs = new HashSet<>();

    @Builder.Default
    @Transient
    private List<PermissionEntity> permissions = new ArrayList<>();
    @Builder.Default
    @Transient
    private List<MenuEntity> menus = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return name;
    }
}
