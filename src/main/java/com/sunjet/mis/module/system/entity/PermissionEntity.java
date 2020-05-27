package com.sunjet.mis.module.system.entity;


import com.sunjet.mis.base.entity.IdEntity;
import com.sunjet.mis.base.entity.IdSystemEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by lhj on 2015/9/6.
 * 权限实体
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_permission")
public class PermissionEntity extends IdSystemEntity {
    private static final long serialVersionUID = -2461553527985758890L;

    @ManyToOne
    @JoinColumn(name = "operation_id_",referencedColumnName = "id_")
    private OperationEntity operation;

    @ManyToOne
    @JoinColumn(name = "resource_id_",referencedColumnName = "id_")
    private ResourceEntity resource;

    @Column(name = "seq_")
    private Integer seq;

    @Builder.Default
    @ManyToMany(mappedBy = "permissions")
    private Set<RoleEntity> roles = new HashSet<>();

    @Override
    public String toString() {
        return resource.getName() + ":" + operation.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionEntity that = (PermissionEntity) o;
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
