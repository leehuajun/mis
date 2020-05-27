package com.sunjet.mis.module.warehouse.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/***
 *  重庆发柳州
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_chongqing_send_liuzhou")
public class ChongQingSendLiuZhouEntity extends DocEntity {
    // 订单号
    @ExcelColumn("订单号")
    @Column(name = "order_number_", length = 100)
    private String orderNumber;
    // VIN
    @ExcelColumn("VIN")
    @Column(name = "vin_",length = 50)
    private String vin;
    // vsn
    @ExcelColumn("VSN")
    @Column(name = "vsn_",length = 100)
    private String vsn;
    // 发运日期
    @ExcelColumn("发运日期")
    @Column(name = "shipment_data_", length = 100)
    private String shipmentData;
    // 生产批次
    @ExcelColumn("生产批次")
    @Column(name = "shares_produce_number_", length = 50)
    private String sharesProduceNumber;
    // 基地代码
    @ExcelColumn("基地代码")
    @Column(name = "base_code_", length = 20)
    private String baseCode;
    // 车型代码
    @ExcelColumn("车型代码")
    @Column(name = "vehicle_model_", length = 100)
    private String vehicleModel;
    //品种
    @Column(name = "variety_", length = 20)
    private String variety;
}
