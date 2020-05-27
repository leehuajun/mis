package com.sunjet.mis.module.sales.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 销售订单实体对象
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_sales_order")
public class SalesOrderEntity extends DocEntity {
    //@Column(name = "no_",length = 50)
    //private String no;

    @ExcelColumn("省份")
    @Column(name = "province_", length = 50)
    private String province;

    @ExcelColumn("制单人")
    @Column(name = "producer_", length = 50)
    private String producer;

    @ExcelColumn("单据类型")
    @Column(name = "doc_type_", length = 50)
    private String docType;

    @ExcelColumn("系统销售订单号")
    @Column(name = "order_no_", length = 50)
    private String orderNo;

    @ExcelColumn("特殊需求单号")
    @Column(name = "special_request_no_", length = 50)
    private String specialRequestNo;

    @ExcelColumn("客户名称")
    @Column(name = "distributor_name_", length = 50)
    private String distributorName;

    @ExcelColumn("车辆型号")
    @Column(name = "vehicle_model_", length = 50)
    private String vehicleModel;

    @ExcelColumn("VSN")
    @Column(name = "vsn_", length = 50)
    private String vsn;

    @ExcelColumn("车辆颜色")
    @Column(name = "vehicle_color_", length = 50)
    private String vehicleColor;

    /**
     * 车型分类
     */
    @Column(name = "vehicle_type_", length = 50)
    private String vehicleType;

    @ExcelColumn("配置描述")
    @Column(name = "config_description_", length = 100)
    private String configDescription;

    @ExcelColumn("需求量")
    @Column(name = "request_num_", length = 20)
    private double requestNum;

    @ExcelColumn("已配车量")
    @Column(name = "allot_count_", length = 20)
    private double allotCount;

    @ExcelColumn("缺口")
    @Column(name = "surplus_num_", length = 20)
    private double surplusNum;

    @ExcelColumn("制单日期")
    @Column(name = "producer_date_", length = 20)
    private Date producerDate;

    @ExcelColumn("合同交货日期")
    @Column(name = "contract_delivery_date_", length = 50)
    private Date contractDeliveryDate;

    @ExcelColumn("客户送货地址")
    @Column(name = "customer_shipping_address_", length = 100)
    private String customerShippingAddress;

    @ExcelColumn("收货人")
    @Column(name = "receiver_", length = 20)
    private String receiver;

    @ExcelColumn("联系电话")
    @Column(name = "contact_phone_", length = 20)
    private String contactPhone;

    @ExcelColumn("销售A价")
    @Column(name = "price_", length = 20)
    private Double price;
    //最终价格
    @ExcelColumn("增减后价格")
    @Column(name = "adjusted_price_", length = 20)
    private Double adjustedPrice;

    @ExcelColumn("合同价格")
    @Column(name = "contract_price_", length = 20)
    private Double contractPrice;

    @ExcelColumn("销售合同号")
    @Column(name = "sales_contract_number_", length = 20)
    private String salesContractNumber;

    @ExcelColumn("备注")
    @Column(name = "remarks_", length = 20)
    private String remarks;

    @ExcelColumn("客户编码")
    @Column(name = "customer_code_", length = 20)
    private String customerCode;

    @ExcelColumn("客户发车余额")
    @Column(name = "customer_departure_balance_", length = 20)
    private String customerDepartureBalance;

    @ExcelColumn("车辆名称")
    @Column(name = "vehicle_name_", length = 20)
    private String vehicleName;

    @ExcelColumn("品种")
    @Column(name = "variety_", length = 20)
    private String variety;

    @ExcelColumn("缺口需款")
    @Column(name = "surplus_quoted_price_", length = 20)
    private String surplusQuotedPrice;

    @ExcelColumn("款项情况")
    @Column(name = "price_status_", length = 20)
    private String priceStatus;

}
