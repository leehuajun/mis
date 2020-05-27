package com.sunjet.mis.module.report.entity;
import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


/**
 * @author SUNJET-YFS
 * @Title: ModifiedBusTransitSummary
 * @ProjectName mis
 * @Description: 改装车在途汇总表
 * @date 2019/1/2817:21
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_modified_bus_transit_summary")
public class ModifiedBusTransitSummaryEntity extends DocEntity {

    //车辆类别
    @ExcelColumn("类别")
    @Column(name = "vehicle_category_", length = 20)
    private String vehicleCategory;

    //区域
    @ExcelColumn("区域")
    @Column(name = "region_", length = 50)
    private String region;

    //负责人
    @ExcelColumn("负责人")
    @Column(name = "functionary_", length = 50)
    private String functionary;

    //发运单号
    @ExcelColumn("发运单号")
    @Column(name = "shipping_order_code_", length = 50)
    private String shippingOrderCode;

    // 发运日期
    @ExcelColumn("发运日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "ship_date_")
    private Date shipDate;

    //月份
    @ExcelColumn("月份")
    @Column(name = "month_", length = 50)
    private String month;

    //省份
    @ExcelColumn("省份")
    @Column(name = "province_", length = 50)
    private String province;

    // 客户名称
    @ExcelColumn("客户名称")
    @Column(name = "distributor_name_", length = 100)
    private String distributorName;

    // 客户地址
    @ExcelColumn("客户地址")
    @Column(name = "distributor_address_", length = 100)
    private String distributorAddress;

    // 承运商名称
    @ExcelColumn("承运商名称")
    @Column(name = "carrier_name_", length = 100)
    private String carrierName;

    // 运输方式
    @ExcelColumn("运输方式")
    @Column(name = "transport_types_", length = 100)
    private String transportTypes;

    // 运输线路
    @ExcelColumn("线路")
    @Column(name = "hauling_track_", length = 100)
    private String haulingTrack;

    // 送达地
    @ExcelColumn("送达地")
    @Column(name = "destination_", length = 100)
    private String destination;

    // 发运台数
    @ExcelColumn("发运台数")
    @Column(name = "shipment_number_", length = 100)
    private String shipmentNumber;

    // 车架号
    @ExcelColumn("VIN")
    @Column(name = "vin_", length = 50)
    private String vin;

    // VSN
    @ExcelColumn("VSN")
    @Column(name = "vsn_", length = 100)
    private String vsn;

    // 车辆型号	model
    @ExcelColumn("车辆型号")
    @Column(name = "model_", length = 100)
    private String model;

    // 车辆名称
    @ExcelColumn("车辆名称")
    @Column(name = "vehicle_name_", length = 100)
    private String vehicleName;

    // 库区名称
    @ExcelColumn("库区名称")
    @Column(name = "reservoir_area_name_", length = 100)
    private String reservoirAreaName;

    // 开票日期
    @ExcelColumn("开票日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "invoice_date_")
    private Date invoiceDate;

    // 运单日期
    @ExcelColumn("运单日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "waybill_date_")
    private Date waybillDate;

    // 出库日期
    @ExcelColumn("出库日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "outdate_date_")
    private Date outdateDate;

    // 起运日期
    @ExcelColumn("起运日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "sail_depart_date_")
    private Date sailDepartDate;

    //要求送达期限 错误
    @ExcelColumn("要求送达期限")
    @Temporal(TemporalType.DATE)
    @Column(name = "deadline_date_")
    private Date deadlineDate;

    //状态
    @ExcelColumn("状态")
    @Column(name = "status_", length = 100)
    private String status;

    //状态
    @ExcelColumn("当前地址")
    @Column(name = "current_Address_", length = 100)
    private String currentAddress;

    // 预计送达日期
    @ExcelColumn("预计送达日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "estimated_date_of_date_")
    private Date estimatedDateOfService;

    // 车队提交验收日期
    @ExcelColumn("车队提交验收日期")
    @Temporal(TemporalType.DATE)
    @Column(name = " motorcade_acceptance_date_")
    private Date  motorcadeAcceptanceDate;

    //是否超时起运
    @ExcelColumn("是否超时起运")
    @Column(name = "overtime_shipment_", length = 100)
    private String overtimeShipment;

    //预警级别
    @ExcelColumn("预警级别")
    @Column(name = "warning_level_", length = 100)
    private String warningLevel;

    //超时未起运标注
    @ExcelColumn("超时未起运标注")
    @Column(name = "overtime_without_departure_marking_", length = 100)
    private String overtimeWithoutDepartureMarking;

    //是否按时出库
    @ExcelColumn("是否按时出库")
    @Column(name = "outgoing_on_time_", length = 100)
    private String   outgoingOnTime;

    //是否验收
    @ExcelColumn("是否验收")
    @Column(name = "acceptance_", length = 100)
    private String   acceptance;

    //是否按时送达
    @ExcelColumn("是否按时送达")
    @Column(name = "deliver_on_time_", length = 100)
    private String  deliverOnTime;

    //在库天数
    @ExcelColumn("在库天数")
    @Column(name = "number_of_days_in_Library_", length = 100)
    private String  numberOfDaysInLibrary;

    //超出规定天数送达
    @ExcelColumn("超出规定天数送达")
    @Column(name = "excess_number_", length = 100)
    private String  excessNumber;

    //公里数
    @ExcelColumn("公里数")
    @Column(name = "km_", length = 100)
    private String  km;

    // 调拨日期
    @ExcelColumn("调拨日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "allocation_date_")
    private Date allocationDate;

    // 验收日期
    @ExcelColumn("验收日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "acceptance_date_")
    private Date acceptanceDate;

    //验收月
    @ExcelColumn("验收月")
    @Column(name = "acceptance_month_", length = 100)
    private String  acceptanceMonth;


}
