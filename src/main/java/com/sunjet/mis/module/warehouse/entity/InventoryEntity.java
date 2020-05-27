package com.sunjet.mis.module.warehouse.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 库存信息表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_inventory")
public class InventoryEntity extends DocEntity {
    // 经销商代码
    @ExcelColumn("经销商号")
    @Column(name = "distributor_code_",length = 50)
    private String distributorCode;
    // 物料号
    @ExcelColumn("物料号")
    @Column(name = "vsn_",length = 100)
    private String vsn;
    //车系
    @ExcelColumn("车系")
    @Column(name = "vehicle_series_", length = 100)
    private String vehicleSeries;
    // 型号
    @ExcelColumn("型号")
    @Column(name = "model_",length = 100)
    private String model;
    // 颜色
    @ExcelColumn("颜色")
    @Column(name = "color_",length = 100)
    private String color;
    // 车架号
    @ExcelColumn("底盘号")
    @Column(name = "vin_", length = 50)
    private String vin;
    //发动机号
    @ExcelColumn("发动机号")
    @Column(name = "engine_code_", length = 100)
    private String engineCode;
    //车型代码
    @ExcelColumn("车型代码")
    @Column(name = "vehicle_model_", length = 100)
    private String vehicleModel;
    //批次号
    @ExcelColumn("批次号")
    @Column(name = "batch_number_", length = 100)
    private String batchNumber;
    //配置类型
    @ExcelColumn("配置类型")
    @Column(name = "config_type_", length = 100)
    private String configType;

}
