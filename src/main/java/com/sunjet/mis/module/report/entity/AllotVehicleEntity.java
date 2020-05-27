package com.sunjet.mis.module.report.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * 配车单明细表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_allot_vehicle")
public class AllotVehicleEntity extends DocEntity {

    // 开票审核状态
    @ExcelColumn("开票审核状态")
    @Column(name = "audit_status_",length = 100)
    private String auditStatus;

    // U9返回信息
    @ExcelColumn("U9返回信息")
    @Column(name = "u9_result_",length = 100)
    private String u9Result;

    // 配车单号
    @ExcelColumn("配车单号")
    @Column(name = "allot_no_",length = 50)
    private String allotNo;

    // 配车员
    @ExcelColumn("配车员")
    @Column(name = "allot_operator_",length = 50)
    private String allotOperator;

    // 特殊需求单号
    @ExcelColumn("特殊需求单号")
    @Column(name = "special_request_no_",length = 50)
    private String specialRequestNo;

    // 客户编码
    @ExcelColumn("客户编码")
    @Column(name = "distributor_code_",length = 50)
    private String distributorCode;

    // 客户编码(SGMW的客户编码)
    @Column(name = "distributor_sgmw_code_",length = 50)
    private String sgmwCode;

    // 客户名称
    @ExcelColumn("客户名称")
    @Column(name = "distributor_name_",length = 100)
    private String distributorName;

    // 配车量
    @ExcelColumn("配车量")
    @Column(name = "allot_count_")
    private Integer allotCount;

    // 销售A价
    @ExcelColumn("销售A价")
    @Column(name = "price_")
    private Double price;

    // 增减后价
    @ExcelColumn("增减后价")
    @Column(name = "adjusted_price_")
    private Double adjustedPrice;

    // 合同单价
    @ExcelColumn("合同单价")
    @Column(name = "contract_price_")
    private Double contractPrice;

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

    // 颜色
    @ExcelColumn("颜色")
    @Column(name = "color_",length = 20)
    private String color;

    // 仓库名称
    @ExcelColumn("仓库名称")
    @Column(name = "storage_",length = 50)
    private String storage;

    // 库位
    @ExcelColumn("库位")
    @Column(name = "storage_location_",length = 50)
    private String storageLocation;

    // 入库时间
    @ExcelColumn("入库时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "entry_time_")
    private Date entryTime;

    // 单据日期
    @ExcelColumn("单据日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "invoice_date_")
    private Date invoiceDate;

    // 销售订单号
    @ExcelColumn("销售订单号")
    @Column(name = "order_no_",length = 50)
    private String orderNo;

    // 配车用途
    @ExcelColumn("配车用途")
    @Column(name = "allot_usage_",length = 50)
    private String allotUsage;

    // 省份
    @ExcelColumn("省份")
    @Column(name = "province_",length = 20)
    private String province;

    // 客户地址
    @ExcelColumn("客户地址")
    @Column(name = "distributor_address_",length = 100)
    private String distributorAddress;

    // 客户联系人
    @ExcelColumn("客户联系人")
    @Column(name = "distributor_contact_",length = 50)
    private String distributorContact;

    // 联系方式
    @ExcelColumn("联系方式")
    @Column(name = "contact_phone_",length = 50)
    private String contactPhone;

    /**
     * 车辆型号
     */
    @Column(name = "vehicle_type_", length = 100)
    private String vehicleType;

    @Override
    public String toString() {
        return "AllotVehicleEntity{" +
                "auditStatus='" + auditStatus + '\'' +
                ", u9Result='" + u9Result + '\'' +
                ", allotNo='" + allotNo + '\'' +
                ", allotOperator='" + allotOperator + '\'' +
                ", specialRequestNo='" + specialRequestNo + '\'' +
                ", distributorCode='" + distributorCode + '\'' +
                ", sgmwCode='" + sgmwCode + '\'' +
                ", distributorName='" + distributorName + '\'' +
                ", allotCount=" + allotCount +
                ", price=" + price +
                ", adjustedPrice=" + adjustedPrice +
                ", contractPrice=" + contractPrice +
                ", vin='" + vin + '\'' +
                ", vsn='" + vsn + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", vehicleName='" + vehicleName + '\'' +
                ", color='" + color + '\'' +
                ", storage='" + storage + '\'' +
                ", storageLocation='" + storageLocation + '\'' +
                ", entryTime=" + entryTime +
                ", invoiceDate=" + invoiceDate +
                ", orderNo='" + orderNo + '\'' +
                ", allotUsage='" + allotUsage + '\'' +
                ", province='" + province + '\'' +
                ", distributorAddress='" + distributorAddress + '\'' +
                ", distributorContact='" + distributorContact + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                '}';
    }
}
