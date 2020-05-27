package com.sunjet.mis.module.basic.entity;

import com.sunjet.mis.base.entity.IdEntity;
import com.sunjet.mis.module.system.entity.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 区域实体对象
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "basic_region")
public class RegionEntity extends IdEntity {
    /**
     * 名称
     */
    @Column(name = "name_", length = 50, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id_")
    private UserEntity manager;

    @Builder.Default
    @OneToMany(mappedBy = "region")
    private Set<ProvinceEntity> provices = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionEntity that = (RegionEntity) o;
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
