package com.sunjet.mis.module.basic.entity;

import com.sunjet.mis.base.entity.IdEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: wushi
 * @description: 车辆类型
 * @Date: Created in 9:09 2019/3/8
 * @Modify by: wushi
 * @ModifyDate by: 9:09 2019/3/8
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "basic_vehicle_type")
public class VehicleTypeEntity extends IdEntity {


    /**
     * 车型关键字
     */
    @Column(name = "key_", length = 50)
    private String key;

    /**
     * 车辆型号
     */
    @Column(name = "vehicle_type_", length = 100)
    private String vehicleType;


}
