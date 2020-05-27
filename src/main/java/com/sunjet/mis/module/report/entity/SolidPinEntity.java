package com.sunjet.mis.module.report.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import com.sunjet.mis.helper.DateHelper;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @author SUNJET-YFS
 * @Title: 客户信息卡
 * @ProjectName mis
 * @Description: 客户信息卡
 * @date 2019/1/2315:06
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_solid_pin")
public class SolidPinEntity extends DocEntity {

    // 车架号
    @ExcelColumn("车架号")
    @Column(name = "vin_", length = 50)
    private String vin;

    // 经销商编号
    @ExcelColumn("经销商号")
    @Column(name = "distributor_code_", length = 50)
    private String distributorCode;

    // 经销商名称 二级代理商
    @ExcelColumn("经销商简称")
    @Column(name = "distributor_name_", length = 100)
    private String distributorName;

    // 客户编号
    @ExcelColumn("客户编号")
    @Column(name = "shopper_code_", length = 100)
    private String shopperCode;
    //销售员
    @ExcelColumn("销售员")
    @Column(name = "salesman_", length = 100)
    private String salesman;

    // 客户名称
    @ExcelColumn("客户名称")
    @Column(name = "shopper_name_", length = 100)
    private String shopperName;


    //使用性质
    @ExcelColumn("使用性质")
    @Column(name = "usage_Mode_", length = 50)
    private String usageMode;

    //集团车
    @ExcelColumn("集团车")
    @Column(name = "group_Car_", length = 50)
    private String groupCar;

    //车牌号
    @ExcelColumn("车牌号")
    @Column(name = "license_Number_", length = 50)
    private String licenseNumber;

    // 出生日期
    @ExcelColumn("出生年月日")
    // @Temporal(TemporalType.DATE)
    @Column(name = "birthday_date_")
    private String birthdayDate;

    //证件类型
    @ExcelColumn("证件类型")
    @Column(name = "certificate_type_", length = 100)
    private String certificateType;
    //证件号码
    @ExcelColumn("证件号码")
    @Column(name = "certificate_code_", length = 100)
    private String certificateCode;

    // 所在省份	province
    @ExcelColumn("所在省份")
    @Column(name = "address_province_", length = 20)
    private String addressProvince;

    // 所在城市	province
    @ExcelColumn("所属城市")
    @Column(name = "address_city_", length = 20)
    private String addressCity;

    //所在区域
    @ExcelColumn("所属地区")
    @Column(name = "address_region_", length = 20)
    private String addressRegion;

    //客户性别
    @ExcelColumn("客户性别")
    @Column(name = "yourgender_", length = 20)
    private String yourgender;

    // 行业
    @ExcelColumn("行业")
    @Column(name = "industry_", length = 20)
    private String industry;

    // 职业
    @ExcelColumn("职业")
    @Column(name = "vocation_", length = 20)
    private String vocation;

    //文化程度
    @ExcelColumn("文化程度")
    @Column(name = "culture_", length = 20)
    private String culture;

    //个人月收入水平

    @ExcelColumn("个人月收入水平")
    @Column(name = "personal_monthly_income_")
    private String personalMonthlyIncome;

    //家庭月收入水平

    @ExcelColumn("家庭月收入水平")
    @Column(name = "family_monthly_income_")
    private String familyMonthlyIncome;

    //是否愿意参加活动
    @ExcelColumn("是否愿意参加活动")
    @Column(name = "activitye_", length = 20)
    private String activity;

    //车辆使用场所 Place of use
    @ExcelColumn("车辆使用场所")
    @Column(name = "place_of_use_", length = 50)
    private String placeOfUse;

    //vehicular applications  车辆用途
    @ExcelColumn("车辆用途")
    @Column(name = "vehicular_applications_", length = 50)
    private String vehicularApplications;

    // 联系人
    @ExcelColumn("联系人")
    @Column(name = "contacts_", length = 50)
    private String contacts;

    // 座机
    @ExcelColumn("座机")
    @Column(name = "telephone_", length = 50)
    private String telephone;

    // 手机
    @ExcelColumn("手机")
    @Column(name = "phone_", length = 50)
    private String phone;

    // 实际地址
    @ExcelColumn("实际地址")
    @Column(name = "actual_address_", length = 50)
    private String actualAddress;

    // 邮政编码
    @ExcelColumn("邮编")
    @Column(name = "postcode_", length = 50)
    private String postcode;

    //email电子邮件QQ
    @ExcelColumn("电子邮件")
    @Column(name = "email_", length = 50)
    private String email;

    //QQ
    @ExcelColumn("QQ")
    @Column(name = "qq_", length = 50)
    private String qq;

    //微信
    @ExcelColumn("微信")
    @Column(name = "wechat_", length = 50)
    private String wechat;

    //身份证地址所在省份
    @ExcelColumn("身份证地址所在省份")
    @Column(name = "address_identity_province_", length = 50)
    private String addressIdentityProvince;

    //身份证地址所在城市county
    @ExcelColumn("身份证地址所在城市")
    @Column(name = "address_identity_city_", length = 50)
    private String addressIdentityCity;

    //身份证地址所在县区county
    @ExcelColumn("身份证地址所在县区")
    @Column(name = "address_identity_county_", length = 50)
    private String addressIdentityCounty;

    //身份证地址
    @ExcelColumn("身份证地址")
    @Column(name = "address_identity_", length = 50)
    private String addressIdentity;

    // VSN
    @ExcelColumn("物料号")
    @Column(name = "vsn_", length = 100)
    private String vsn;

    //车系
    @ExcelColumn("车系")
    @Column(name = "vehicle_series_", length = 100)
    private String vehicleSeries;

    // 车辆型号	model
    @ExcelColumn("型号")
    @Column(name = "model_", length = 100)
    private String model;

    // 颜色
    @ExcelColumn("颜色")
    @Column(name = "color_", length = 100)
    private String color;

    //发动机号
    @ExcelColumn("发动机号")
    @Column(name = "engine_code_", length = 100)
    private String engineCode;

    //合格证
    @ExcelColumn("合格证号")
    @Column(name = "qualified_code_", length = 100)
    private String qualifiedCode;

    // 制造日期
    @ExcelColumn("制造日期")
    //@Temporal(TemporalType.DATE)
    @Column(name = "manufacturing_date_")
    private String manufacturingDate;

    //基地代码
    @ExcelColumn("基地代码")
    @Column(name = "base_code_", length = 100)
    private String baseCode;


    // 车型代码
    @ExcelColumn("车型代码")
    @Column(name = "vehicle_model_", length = 100)
    private String vehicleModel;


    // 建档时间
    @ExcelColumn("建档时间")
    // @Temporal(TemporalType.TIME)
    @Column(name = "inputting_time_")
    private String inputtingTime;


    // 交车日期
    @ExcelColumn("交车日期")
    // @Temporal(TemporalType.DATE)
    @Column(name = "transaction_vehicle_date_")
    private String transactionVehicleDate;

    // 开票日期
    @ExcelColumn("开票日期")
    // @Temporal(TemporalType.DATE)
    @Column(name = "sale_Date_")
    private String saleDate;


    //统计日期
    @ExcelColumn("统计日期")
    // @Temporal(TemporalType.DATE)
    @Column(name = "statistical_date_")
    private String statisticalDate;

    //开票价格
    @ExcelColumn("开票价格")
    @Column(name = "price_", length = 100)
    private Double price;

    //付款方式一次性方式
    @ExcelColumn("付款方式一次性方式")
    @Column(name = "full_payment_", length = 100)
    private String fullPayment;

    //付款方式分期首付金额
    @ExcelColumn("付款方式分期首付金额")
    @Column(name = "down_payment_amount_", length = 100)
    private Double downPaymentAmount;

    //付款方式分期贷款金额
    @ExcelColumn("付款方式分期贷款金额")
    @Column(name = "loan_amount_", length = 100)
    private Double loanAmount;

    //付款方式分期贷款期限
    @ExcelColumn("付款方式分期贷款期限")
    @Column(name = "loan_period_", length = 100)
    private Integer loanPeriod;

    //付款方式分期贷款利率
    @ExcelColumn("付款方式分期贷款利率")
    @Column(name = "loan_rate_", length = 100)
    private Double loanRate;

    //购买类型Purchasing Types
    @ExcelColumn("购买类型")
    @Column(name = "purchasing_types_", length = 100)
    private String purchasingTypes;

    //增/换购前品牌车型
    @ExcelColumn("增/换购前品牌车型")
    @Column(name = "add_an_trade_", length = 100)
    private String addAnTrade;

    //购买方式
    @ExcelColumn("渠道")
    @Column(name = "operations_", length = 100)
    private String operations;

    //信息来源-介绍-姓名
    @ExcelColumn("信息来源-介绍-姓名")
    @Column(name = "sponsors_name_", length = 100)
    private String sponsorsName;

    //信息来源-介绍-姓名
    @ExcelColumn("信息来源-介绍-手机")
    @Column(name = "sponsors_name_phone_", length = 100)
    private String sponsorsNamePhone;

    //适合拜访的时间地点
    @ExcelColumn("适合拜访的时间地点")
    @Column(name = "visit_time_site_", length = 100)
    private String visitTimeSite;

    //用户兴趣
    @ExcelColumn("用户兴趣")
    @Column(name = "interest_", length = 100)
    private String interest;

    //家庭状况
    @ExcelColumn("家庭状况")
    @Column(name = "family_status_", length = 100)
    private String familyStatus;

    //新车保险-交强险
    @ExcelColumn("新车保险-交强险")
    @Column(name = "compulsory_insurance_", length = 100)
    private String compulsoryInsurance;

    //新车保险-商业险
    @ExcelColumn("新车保险-商业险")
    @Column(name = "commercial_insurance_", length = 100)
    private String commercialInsurance;

    //新车保险-承保公司
    @ExcelColumn("新车保险-承保公司")
    @Column(name = "insurance_carriers_", length = 100)
    private String insuranceCarriers;

    // 新车保险-金额
    @ExcelColumn("新车保险-金额")
    @Column(name = "insurance_amount_", length = 100)
    private Double insuranceAmount;

    //新车保险-其他
    @ExcelColumn("新车保险-其他")
    @Column(name = "insurance_other_", length = 100)
    private String insuranceOther;

    //顾客自费加装配件-品名1Two
    @ExcelColumn("顾客自费加装配件-品名1")
    @Column(name = "one_additional_assembly_", length = 100)
    private String oneAdditionalAssembly;

    //顾客自费加装配件-品名2
    @ExcelColumn("顾客自费加装配件-品名2")
    @Column(name = "two_additional_assembly_", length = 100)
    private String twoAdditionalAssembly;

    //介绍记录-顾客姓名1
    @ExcelColumn("介绍记录-顾客姓名1")
    @Column(name = "one_customer_", length = 100)
    private String oneCustomer;

    //介绍记录-顾客姓名2
    @ExcelColumn("介绍记录-顾客姓名2")
    @Column(name = "two_customer_", length = 100)
    private String twoCustomer;

    //介绍记录-顾客姓名3
    @ExcelColumn("介绍记录-顾客姓名3")
    @Column(name = "three_customer_", length = 100)
    private String threeCustomer;

    //重要事项纪要
    @ExcelColumn("重要事项纪要")
    @Column(name = "important_notes_", length = 100)
    private String importantNotes;

    // 最后修改时间
    @ExcelColumn("最后修改时间")
    // @Temporal(TemporalType.TIME)
    @Column(name = "final_modification_time_")
    private String finalModificationTime;

    //创建方式
    @ExcelColumn("创建方式")
    @Column(name = "creating_mode_", length = 100)
    private String creatingMode;

    //提交档案方式
    @ExcelColumn("提交档案方式")
    @Column(name = "file_submission_method_", length = 100)
    private String fileSubmissionMethod;

    //经销商申请修改状态
    @ExcelColumn("经销商申请修改状态")
    @Column(name = "distributor_status_", length = 100)
    private String distributorStatus;
    //经销商申请修改人
    @ExcelColumn("经销商申请修改人")
    @Column(name = "distributor_reviser_", length = 100)
    private String distributorReviser;

    // 经销商申请修改时间
    @ExcelColumn("经销商申请修改时间")
    // @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "distributor_modification_time_")
    private Date distributorModificationTime;

    //申请修改内容
    @ExcelColumn("申请修改内容")
    @Column(name = "modify_content_", length = 100)
    private String modifyContent;

    //状态
    @ExcelColumn("状态")
    @Column(name = "status_", length = 100)
    private String status;

    //经销商审核人
    @ExcelColumn("经销商审核人")
    @Column(name = "distributor_auditor_", length = 100)
    private String distributorAuditor;
    // 经销商审核时间
    @ExcelColumn("经销商审核时间")
    @Column(name = "distributor_audit_time_")
    private String distributorAuditTime;


    //SGMW审核结果(0表示不通过，1表示通过)
    @ExcelColumn("SGMW审核结果(0表示不通过，1表示通过)")
    @Column(name = "audit_result_", length = 100)
    private String auditResult;

    //审核说明
    @ExcelColumn("审核说明")
    @Column(name = "audit_specification_", length = 100)
    private String auditSpecification;


    //销售公司审核人
    @ExcelColumn("销售公司审核人")
    @Column(name = "sales_company_auditor_", length = 100)
    private String salesCompanyAuditor;


    // 经销商审核时间
    @ExcelColumn("销售公司审核时间")
    //@Temporal(TemporalType.TIME)
    @Column(name = "audit_time_sales_company_")
    private Date auditTimeSalesCompany;


    //客户类型
    @ExcelColumn("客户类型")
    @Column(name = "customer_type_", length = 100)
    private String customerType;


    //厂发线索渠道
    @ExcelColumn("厂发线索渠道")
    @Column(name = "factory_clue_channel_", length = 100)
    private String factoryClueChannel;


    //厂发线索编号
    @ExcelColumn("厂发线索编号")
    @Column(name = "factory_clue_code_", length = 100)
    private String factoryClueCode;


    //原厂发线索编号
    @ExcelColumn("原厂发线索编号")
    @Column(name = "Original_factory_sends_clue_code_", length = 100)
    private String originalFactorySendsClueCode;


    //自建线索流水号
    @ExcelColumn("自建线索流水号")
    @Column(name = "clue_running_code_", length = 100)
    private String clueRunningCode;


    //线索数据来源
    @ExcelColumn("线索数据来源")
    @Column(name = "clue_data_sources_", length = 100)
    private String clueDataSources;

    //用于用户数据中心接口的上传状态(1表示已上传，0表示未上传)
    @ExcelColumn("用于用户数据中心接口的上传状态(1表示已上传，0表示未上传)")
    @Column(name = "interface_status_", length = 100)
    private String interfaceStatus;

    // 用于用户数据中心接口的上传时间
    @ExcelColumn("用于用户数据中心接口的上传时间")
    //@Temporal(TemporalType.TIME)
    @Column(name = "upload_time_of_interface_")
    private String uploadTimeOfInterface;

    //用于用户数据中心接口的上传提示信息
    @ExcelColumn("用于用户数据中心接口的上传提示信息)")
    @Column(name = "interface_prompt_information_", length = 100)
    private String interfacePromptInformation;


    @Temporal(TemporalType.DATE)
    @Column(name = "invoice_Date_", length = 100)
    private Date invoiceDate;

    //一级代码
    @Column(name = "onecode", length = 50)
    private String onecode;
    @Transient
    private Date startDate = DateHelper.getFirstOfYear(); // 开始日期

    @Transient
    private Date endDate = DateHelper.getEndDateTime();



}
