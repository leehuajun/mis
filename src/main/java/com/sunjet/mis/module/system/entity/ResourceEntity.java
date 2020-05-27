package com.sunjet.mis.module.system.entity;


import com.sunjet.mis.base.entity.IdSystemEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by hxf on 2015/11/05.
 * 资源实体
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_resource")
public class ResourceEntity extends IdSystemEntity {
    private static final long serialVersionUID = -5833217238641539363L;
    /**
     * 资源编码
     */
    @Column(name = "code_", length = 50, unique = true)
    private String code;

    /**
     * 资源名称
     */
    @Column(name = "name_", length = 50)
    private String name;


    /**
     * 当前资源允许的操作列表
     */
//    @Builder.Default
//    @ManyToMany
//    @JoinTable(name = "sys_resource_operation",
//            joinColumns = @JoinColumn(name = "resource_id_", referencedColumnName = "id_"),
//            inverseJoinColumns = @JoinColumn(name = "operation_id_", referencedColumnName = "id_"))
//    private Set<OperationEntity> operations = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceEntity that = (ResourceEntity) o;
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
