package com.sunjet.mis.module.warehouse.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 分车统计
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_distribution_vehicle")
public class DistributionVehicleEntity extends DocEntity {
    // 订单号
    @ExcelColumn("订单号")
    @Column(name = "order_number_", length = 100)
    private String orderNumber;
    // 单位代码
    @ExcelColumn("单位代码")
    @Column(name = "unit_code_", length = 100)
    private String unitCode;
    //单位名称
    @ExcelColumn("单位名称")
    @Column(name = "unit_name_", length = 100)
    private String unitName;
    // 办事处
    @ExcelColumn("办事处")
    @Column(name = "office_", length = 100)
    private String office;
    //收车单位
    @ExcelColumn("收车单位")
    @Column(name = "collect_unit_", length = 100)
    private String collectUnit;
    //收车地址
    @ExcelColumn("收车地址")
    @Column(name = "collect_address_", length = 100)
    private String collectAddress;
    //VSN码
    @ExcelColumn("VSN码")
    @Column(name = "vsn_",length = 100)
    private String vsn;
    //车型
    @ExcelColumn("车型")
    @Column(name = "vehicle_model_", length = 100)
    private String vehicleModel;
    //数量
    @ExcelColumn("数量")
    @Column(name = "amount_",length = 100)
    private Integer amount;
    //发货凭证号
    @ExcelColumn("发货凭证号")
    @Column(name = "delivery_prove_",length = 100)
    private String deliveryProve;
    //分货日期
    @ExcelColumn("分货日期")
    @Column(name = "cargo_data_",length = 100)
    private String cargoData;
    //发货日期
    @ExcelColumn("发货日期")
    @Column(name = "delivery_data_",length = 100)
    private String deliveryData;
    //出库仓库
    @ExcelColumn("出库仓库")
    @Column(name = "outbound_warehouse_",length = 100)
    private String outboundWarehouse;
    //承运商
    @ExcelColumn("承运商")
    @Column(name = "carriers_",length = 100)
    private String carriers;
    //发票号
    @ExcelColumn("发票号")
    @Column(name = "invoice_no_",length = 100)
    private String invoiceNo;
    //开票日期
    @ExcelColumn("开票日期")
    @Column(name = "invoice_data_",length = 100)
    private String invoiceData;
    //发票金额
    @ExcelColumn("发票金额")
    @Column(name = "invoice_amount_",length = 100)
    private String invoiceAmount;
    //CUR
    @ExcelColumn("CUR")
    @Column(name = "cur_",length = 100)
    private String cur;
    //分车单价
    @ExcelColumn("分车单价")
    @Column(name = "points_price_",length = 100)
    private String pointsPrice;
    //分车金额
    @ExcelColumn("分车金额")
    @Column(name = "points_amount_",length = 100)
    private String pointsAmount;
    //分车月份
    @ExcelColumn("分车月份")
    @Column(name = "points_month_",length = 100)
    private Integer pointsMonth;
    //品种
//    @ExcelColumn("品种")
    @Column(name = "variety_", length = 20)
    private String variety;
    //批次号
//    @ExcelColumn("批次号")
    @Column(name = "batch_number_", length = 100)
    private String batchNumber;
    //颜色
//    @ExcelColumn("颜色")
//    @Column(name = "color_",length = 20)
//    private String color;
    //分车月份
    @Column(name = "points_year_",length = 100)
    private Integer pointsYear;




}
