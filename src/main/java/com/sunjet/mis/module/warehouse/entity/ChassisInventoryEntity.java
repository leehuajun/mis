package com.sunjet.mis.module.warehouse.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 底盘库存
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_chassis_inventory")
public class ChassisInventoryEntity extends DocEntity {
    // 订单号
    @ExcelColumn("订单号")
    @Column(name = "order_id_",length = 100)
    private String orderId;
    //VIN
    @ExcelColumn("VIN")
    @Column(name = "vin_",length = 100)
    private String vin;
    //VSN
    @ExcelColumn("VSN")
    @Column(name = "vsn_",length = 100)
    private String vsn;
    //发动机号
    @ExcelColumn("发动机号")
    @Column(name = "engine_code_", length = 100)
    private String engineCode;
    // 发运日期
    @ExcelColumn("发运日期")
    @Column(name = "shipment_data_", length = 100)
    private String shipmentData;
    // 生产批次
    @ExcelColumn("生产批次")
    @Column(name = "batch_number_", length = 100)
    private String batchNumber;
    // 颜色
    @ExcelColumn("颜色")
    @Column(name = "color_",length = 100)
    private String color;
    // 合格证号
    @ExcelColumn("合格证号")
    @Column(name = "qualified_certificate_", length = 50)
    private String qualifiedCertificate;
    // 基地代码
    @ExcelColumn("基地代码")
    @Column(name = "base_code_", length = 20)
    private String baseCode;
    // 制造日期
    @ExcelColumn("制造日期")
    @Column(name = "make_data_", length = 20)
    private String makeData;
    // 车型
    @ExcelColumn("车型代码")
    @Column(name = "vehicle_model_",length = 100)
    private String vehicleModel;
    // 数据来源
    @Column(name = "data_source_",length = 100)
    private String dataSource;

    // 品种
    @Column(name = "variety_", length = 20)
    private String variety;

}
