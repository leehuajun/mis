package com.sunjet.mis.module.warehouse.entity;

import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 车辆分配单实体对象
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wh_vehicle_allot")
public class VehicleAllotEntity extends DocEntity {
    @Column(name = "no_",length = 50)
    private String no;
}
