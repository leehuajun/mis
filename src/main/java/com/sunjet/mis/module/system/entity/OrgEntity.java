package com.sunjet.mis.module.system.entity;


import com.sunjet.mis.base.entity.IdSystemEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * 组织
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_org")
public class OrgEntity extends IdSystemEntity {
    private static final long serialVersionUID = -6056297580918896377L;
    /**
     * 组织编号
     */
    @Column(name = "code_", length = 50, unique = true)
    private String code;
    /**
     * 组织名称
     */
    @Column(name = "name_", length = 100)
    private String name;
    /**
     * 父组织
     */
    @ManyToOne
    @JoinColumn(name = "parent_id_")
    private OrgEntity parent;
    /**
     * 排序
     */
    @Column(name = "seq_")
    private Integer seq;

    @Builder.Default
    @ManyToMany(mappedBy = "orgs")
    private Set<UserEntity> users = new HashSet<>();

    @Transient
    private String usersDesc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrgEntity that = (OrgEntity) o;
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
