package com.sunjet.mis.module.basic.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.IdEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * 省份实体对象
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "basic_province")
public class ProvinceEntity extends IdEntity {
    /**
     * 名称
     */
    @Column(name = "name_", length = 50, nullable = false)
    private String name;


    /**
     * 区域
     */
    @ManyToOne
    @JoinColumn(name = "region_id_",referencedColumnName = "id_")
    private RegionEntity region;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ProvinceEntity that = (ProvinceEntity) o;
//        return this.getId().equals(that.getId());
//    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return name;
    }


}
