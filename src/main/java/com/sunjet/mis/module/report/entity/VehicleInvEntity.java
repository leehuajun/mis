package com.sunjet.mis.module.report.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * 库存车辆状态跟踪表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_vehicle_inv")
public class VehicleInvEntity extends DocEntity {
    // 库存状态
    @ExcelColumn("库存状态")
    @Column(name = "status_",length = 50)
    private String status;

    // VIN
    @ExcelColumn("VIN")
    @Column(name = "vin_",length = 50)
    private String vin;

    // VSN
    @ExcelColumn("VSN")
    @Column(name = "vsn_",length = 50)
    private String vsn;

    // 车辆型号
    @ExcelColumn("车辆型号")
    @Column(name = "vehicle_model_",length = 50)
    private String vehicleModel;

    // 车辆名称
    @ExcelColumn("车辆名称")
    @Column(name = "vehicle_name_",length = 100)
    private String vehicleName;

    // 仓库
    @ExcelColumn("仓库")
    @Column(name = "storage_",length = 50)
    private String storage;

    // 入库时间
    @ExcelColumn("入库时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "entry_time_")
    private Date entryTime;

    // 运单生成时间
    @ExcelColumn("运单生成时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "waybill_time_")
    private Date waybillTime;

    // 出库时间
    @ExcelColumn("出库时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "out_time_")
    private Date outTime;

    // 销售订单号
    @ExcelColumn("销售订单号")
    @Column(name = "order_no_",length = 50)
    private String orderNo;

    // 客户订单号
    @ExcelColumn("客户订单号")
    @Column(name = "distributor_order_no_",length = 50)
    private String distributorOrderNo;

    // 客户省份
    @ExcelColumn("客户省份")
    @Column(name = "province_",length = 20)
    private String province;

    // 客户编码
    @Column(name = "distributor_code_",length = 100)
    private String distributorCode;

    // 客户编码(SGMW)
    @Column(name = "distributor_sgmw_code_",length = 100)
    private String sgmwCode;


    // 客户名称
    @ExcelColumn("客户名称")
    @Column(name = "distributor_name_",length = 100)
    private String distributorName;

    // 客户送车地址
    @ExcelColumn("客户送车地址")
    @Column(name = "distributor_address_",length = 100)
    private String distributorAddress;

    // 承运商
    @ExcelColumn("承运商")
    @Column(name = "carrier_",length = 100)
    private String carrier;

    // 配车单号
    @ExcelColumn("配车单号")
    @Column(name = "allot_no_",length = 50)
    private String allotNo;

    // 配车人
    @ExcelColumn("配车人")
    @Column(name = "allot_operator_",length = 50)
    private String allotOperator;

    // 开票通知单号
    @ExcelColumn("开票通知单号")
    @Column(name = "invoice_advice_no_",length = 50)
    private String invoiceAdviceNo;

    // 开票日期
    @ExcelColumn("开票日期")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "invoice_time_")
    private Date invoiceTime;

    // 制单人
    @ExcelColumn("制单人")
    @Column(name = "originator_",length = 50)
    private String originator;

    // 审核人
    @ExcelColumn("审核人")
    @Column(name = "auditor_",length = 50)
    private String auditor;

    // 发运单号
    @ExcelColumn("发运单号")
    @Column(name = "dispatch_no_",length = 50)
    private String dispatchNo;

}
