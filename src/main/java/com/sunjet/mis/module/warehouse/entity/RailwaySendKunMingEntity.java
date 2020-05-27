package com.sunjet.mis.module.warehouse.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/***
 *  中铁发昆明
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_railway_send_kunming")
public class RailwaySendKunMingEntity extends DocEntity {
    // 简写
    @ExcelColumn("简写")
    @Column(name = "dealers_code_",length = 100)
    private String dealersCode;
    // 序号
    @ExcelColumn("序号")
    @Column(name = "serial_number_",length = 100)
    private String serialNumber;
    // 火车号
    @ExcelColumn("火车号")
    @Column(name = "train_number_",length = 100)
    private String trainlNumber;
    // VIN
    @ExcelColumn("VIN")
    @Column(name = "vin_",length = 50)
    private String vin;
    //合格证号
    @ExcelColumn("合格证编号")
    @Column(name = "qualified_code_", length = 100)
    private String qualifiedCode;
    // vsn
    @ExcelColumn("VSN")
    @Column(name = "vsn_",length = 100)
    private String vsn;
    // 批次
    @ExcelColumn("批次号")
    @Column(name = "batch_number_", length = 100)
    private String batchNumber;
    // 配置
    @ExcelColumn("配置")
    @Column(name = "configuration_", length = 100)
    private String configuration;
    // 车型
    @ExcelColumn("车型")
    @Column(name = "vehicle_model_",length = 100)
    private String vehicleModel;
    //品种
    @Column(name = "variety_", length = 20)
    private String variety;
    // 发运日期
    @ExcelColumn("发运日期")
    @Column(name = "shipment_data_", length = 100)
    private String shipmentData;
}
