package com.sunjet.mis.module.report.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;


import javax.persistence.*;
import java.util.Date;

/**
 * @author SUNJET-YFS
 * @Title: DistributorDemandStatementEntity
 * @ProjectName mis
 * @Description: 经销商需求表格
 * @date 2019/3/1211:10
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rep_distributor_demand_statement")
public class DistributorDemandStatementEntity extends DocEntity {

    //省份
    @ExcelColumn("省份")
    @Column(name = "customer_province_", length = 100)
    private String customerProvince;
    //制单人
    @ExcelColumn("制单人")
    @Column(name = "single_person_", length = 100)
    private String singlePerson;
    //单据类型
    @ExcelColumn("单据类型")
    @Column(name = "document_type_", length = 100)
    private String documentType;
    //销售订单
    @ExcelColumn("系统销售订单号")
    @Column(name = "sales_order_number_", length = 100)
    private String salesOrderNumber;
    //特殊需求单号
    @ExcelColumn("特殊需求单号")
    @Column(name = "special_needs_number_", length = 100)
    private String specialNeedsNumber;
    // 客户名称
    @ExcelColumn("客户名称")
    @Column(name = "customer_name_", length = 100)
    private String customerName;
    //车辆类型
   /* @ExcelColumn("车辆型号")
    @Column(name = "type_", length = 100)
    private String type;*/
    // 车辆型号	model
    @ExcelColumn("车辆型号")
    @Column(name = "model_", length = 100)
    private String model;
    //VSN
    @ExcelColumn("VSN")
    @Column(name = "vsn_", length = 100)
    private String vsn;
    //车辆颜色
    @ExcelColumn("车辆颜色")
    @Column(name = "color_", length = 100)
    private String color;
    //配置描述
    @ExcelColumn("配置描述")
    @Column(name = "configuration_description_", length = 100)
    private String configurationDescription;
    //需求量
    @ExcelColumn("需求量")
    @Column(name = "request_num_", length = 20)
    private double requestNum;
    //已配车量
    @ExcelColumn("已配车量")
    @Column(name = "allot_count_", length = 20)
    private double allotCount;

   /* //未配车
    @ExcelColumn("未配车")
    @Column(name = "surplus_num_", length = 20)
    private double surplusNum;
*/
    //制单日期
    @ExcelColumn("制单日期")
   // @Temporal(TemporalType.DATE)
    @Column(name = "producer_date_", length = 20)
    private Date producerDate;
    //合同交货日期
    @ExcelColumn("合同交货日期")
   // @Temporal(TemporalType.DATE)
    @Column(name = "contract_delivery_date_", length = 50)
    private Date contractDeliveryDate;
    //生产交货日期
    @ExcelColumn("生产交货日期")
  //  @Temporal(TemporalType.DATE)
    @Column(name = "production_delivery_date_", length = 50)
    private Date productionDeliveryDate;
    //客户送货地址
    @ExcelColumn("客户送货地址")
    @Column(name = "customer_address_", length = 100)
    private String customerAddress;
    //收车人
    @ExcelColumn("收车人")
    @Column(name = "receiver_", length = 20)
    private String receiver;
    //联系电话
    @ExcelColumn("联系电话")
    @Column(name = "contact_phone_", length = 20)
    private String contactPhone;

    // 销售A价
    @ExcelColumn("销售A价")
    @Column(name = "price_")
    private Double price;

    // 增减后价
    @ExcelColumn("增减后价格")
    @Column(name = "adjusted_price_")
    private Double adjustedPrice;

    // 合同单价
    @ExcelColumn("合同价格")
    @Column(name = "contract_price_")
    private Double contractPrice;

    //销售合同号
    @ExcelColumn("销售合同号")
    @Column(name = "sales_contract_number_")
    private String salesContractNumber;
    // 备注
    @ExcelColumn("备注")
    @Column(name = "note_", length = 100)
    private String note;
    //客户编码
    @ExcelColumn("客户编码")
    @Column(name = "customer_code_", length = 20)
    private String customerCode;
    // 客户发车余额
    @Builder.Default
    @ExcelColumn("客户发车余额")
    @Column(name = "balance_")
    private Double balance = 0.0;
    // 车辆名称
    @ExcelColumn("车辆名称")
    @Column(name = "vehicle_name_", length = 100)
    private String vehicleName;


}































































