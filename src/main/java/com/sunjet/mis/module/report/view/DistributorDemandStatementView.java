package com.sunjet.mis.module.report.view;
import com.sunjet.mis.helper.DateHelper;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author SUNJET-YFS
 * @Title: DistributorDemandStatementView
 * @ProjectName mis
 * @Description: 经销商需求表格
 * @date 2019/3/1214:30
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
@Subselect("SELECT DISTINCT\n" +
        "\tdds.id_,\n" +
        "\tdds.customer_province_,\n" +
        "\tdds.single_person_,\n" +
        "\tdds.document_type_,\n" +
        "\tdds.sales_order_number_,\n" +
        "\tdds.special_needs_number_,\n" +
        "\tdds.customer_name_,\n" +
        "\tdds.configuration_description_,\n" +
        "\tdds.model_,\n" +
        "\tdds.vsn_,\n" +
        "\tdds.color_,\n" +
        "\tdds.request_num_,\n" +
        "\tdds.allot_count_,\n" +
        "\tdds.producer_date_,\n" +
        "\tdds.contract_delivery_date_,\n" +
        "\tdds.production_delivery_date_,\n" +
        "\tdds.customer_address_,\n" +
        "\tdds.receiver_,\n" +
        "\tdds.contact_phone_,\n" +
        "\tdds.price_,\n" +
        "\tdds.adjusted_price_,\n" +
        "\tdds.contract_price_,\n" +
        "\tdds.sales_contract_number_,\n" +
        "\tdds.note_,\n" +
        "\tdds.customer_code_,\n" +
        "\tdds.balance_,\n" +
        "\tdds.vehicle_name_,\n" +
        "\tdds.request_num_ - dds.allot_count_ AS surplus_num_ \n" +
        "FROM\n" +
        "\trep_distributor_demand_statement dds")
public class DistributorDemandStatementView {
//"\tdds.request_num_ - dds.allot_count_ AS surplus_num_ \n" +
    @Id
    @Column(name = "id_")
    private String id;
    //省份
    @Column(name = "customer_province_", length = 100)
    private String customerProvince;
    //制单人
    @Column(name = "single_person_", length = 100)
    private String singlePerson;
    //单据类型
    @Column(name = "document_type_", length = 100)
    private String documentType;
    //销售订单
    @Column(name = "sales_order_number_", length = 100)
    private String salesOrderNumber;
    //特殊需求单号
    @Column(name = "special_needs_number_", length = 100)
    private String specialNeedsNumber;
    // 客户名称
    @Column(name = "customer_name_", length = 100)
    private String customerName;
   /* //车辆类型
    @Column(name = "type_", length = 100)
    private String type;*/
    // 车辆型号	model
    @Column(name = "model_", length = 100)
    private String model;
    //VSN
    @Column(name = "vsn_", length = 100)
    private String vsn;
    //车辆颜色
    @Column(name = "color_", length = 100)
    private String color;
    //配置描述
    @Column(name = "configuration_description_", length = 100)
    private String configurationDescription;
    //需求量
    @Column(name = "request_num_", length = 20)
    private double requestNum;
    //已配车量
    @Column(name = "allot_count_", length = 20)
    private double allotCount;

    //未配车
    @Column(name = "surplus_num_", length = 20)
    private double surplusNum;

    //制单日期
    @Column(name = "producer_date_", length = 20)
    private Date producerDate;
    //合同交货日期
    @Column(name = "contract_delivery_date_", length = 50)
    private Date contractDeliveryDate;
    //生产交货日期
    @Column(name = "production_delivery_date_", length = 50)
    private Date productionDeliveryDate;
    //客户送货地址
    @Column(name = "customer_address_", length = 100)
    private String customerAddress;
    //收车人
    @Column(name = "receiver_", length = 20)
    private String receiver;
    //联系电话
    @Column(name = "contact_phone_", length = 20)
    private String contactPhone;
    //销售A价
    @Column(name = "price_")
    private Double price;
    //增减后价
    @Column(name = "adjusted_price_")
    private Double adjustedPrice;
    //合同单价
    @Column(name = "contract_price_")
    private Double contractPrice;
    //销售合同号
    @Column(name = "sales_contract_number_")
    private String salesContractNumber;
    //备注
    @Column(name = "note_", length = 100)
    private String note;
    //客户编码
    @Column(name = "customer_code_", length = 20)
    private String customerCode;
    //客户发车余额
    @Column(name = "balance_")
    private Double balance = 0.0;
    //车辆名称
    @Column(name = "vehicle_name_", length = 100)
    private String vehicleName;

    @Transient
    private Date startDate = DateHelper.getFirstOfYear();         // 开始日期
    @Transient
    private Date endDate = new Date();           // 结束日期
}

