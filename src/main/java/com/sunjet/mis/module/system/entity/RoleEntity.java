package com.sunjet.mis.module.system.entity;

import com.sunjet.mis.base.entity.IdEntity;
import com.sunjet.mis.base.entity.IdSystemEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by lhj on 2015/9/6.
 * 角色实体
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_role")
public class RoleEntity extends IdSystemEntity {
    private static final long serialVersionUID = 8512757228891325156L;
    /**
     * 角色编码
     */
    @Column(name = "code_", length = 64, unique = true, nullable = false)
    private String code;
    /**
     * 角色名称
     */
    @Column(name = "name_", length = 20, unique = true, nullable = false)
    private String name;

    /**
     * 用户列表
     */
    @Builder.Default
    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();

    /**
     * 权限列表
     */
    @Builder.Default
    @ManyToMany
    @JoinTable(name = "sys_role_permission",
            joinColumns = @JoinColumn(name = "role_id_", referencedColumnName = "id_"),
            inverseJoinColumns = @JoinColumn(name = "permission_id_", referencedColumnName = "id_"))
    private Set<PermissionEntity> permissions = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "org_id_",referencedColumnName = "id_")
    private OrgEntity org;

//    @Transient
//    private String usersDesc;
//    @Transient
//    private String permissionsDesc;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;
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
