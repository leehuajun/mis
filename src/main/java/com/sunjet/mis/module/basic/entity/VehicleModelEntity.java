package com.sunjet.mis.module.basic.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.IdEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 车型实体对象
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "basic_vehicle_model")
public class VehicleModelEntity extends IdEntity {
    /**
     * 车型编号
     */
    @ExcelColumn("车型编号")
    @Column(name = "code_", length = 100, nullable = false)
    private String code;
    /**
     * 车型名称
     */
    @ExcelColumn("车型名称")
    @Column(name = "name_", length = 100, nullable = false)
    private String name;
    /**
     * 车系
     */
    @ExcelColumn("车系")
    @Column(name = "vehicle_series_", length = 100)
    private String vehicleSeries;
    /**
     * 排放
     */
    @ExcelColumn("排放")
    @Column(name = "effluent_", length = 20)
    private String effluent;
    /**
     * 排量
     */
    @ExcelColumn("排量")
    @Column(name = "displacement_", length = 100)
    private String displacement;
    /**
     * 说明
     */
    @ExcelColumn("说明")
    @Column(name = "comment_", length = 100)
    private String comment;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        VehicleModelEntity that = (VehicleModelEntity) o;
//        return this.getId().equals(that.getId());
//    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return code;
    }
}
