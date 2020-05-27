package com.sunjet.mis.module.warehouse.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author SUNJET-YFS
 * @Title: StockInformationEntity
 * @ProjectName mis
 * @Description: 库存信息
 * @date 2019/1/3010:40
 */
public class StockInformationEntity extends DocEntity {

    // 经销商编号
    @ExcelColumn("经销商编号")
    @Column(name = "agency_code_", length = 50)
    private String agencyCode;


    // 销售卓越号
    @ExcelColumn("销售卓越号")
    @Column(name = "sales_excellence_", length = 50)
    private String salesExcellence;


    //发运单号
    @ExcelColumn("发运单号")
    @Column(name = "shipping_order_code_", length = 50)
    private String shippingOrderCode;


    // VSN
    @ExcelColumn("VSN")
    @Column(name = "vsn_", length = 50)
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


    // 底盘号
    @ExcelColumn("底盘号")
    @Column(name = "chassis_number_", length = 100)
    private String chassisNumber;


    //发动机号
    @ExcelColumn("发动机号")
    @Column(name = "engine_code_", length = 100)
    private String engineCode;


    //变速箱号
    @ExcelColumn("变速箱号")
    @Column(name = "gearbox_number_", length = 100)
    private String gearboxNumber;

    //合格证号
    @ExcelColumn("合格证号")
    @Column(name = "qualified_code_", length = 100)
    private String qualifiedCode;

    //批次号
    @ExcelColumn("批次号")
    @Column(name = "batch_number_", length = 100)
    private String batchNumber;

    //库位
    @ExcelColumn("库位")
    @Column(name = "storehouse_", length = 100)
    private String storehouse;

    //验收入库时间
    @ExcelColumn("验收入库时间")
    @Temporal(TemporalType.TIME)
    @Column(name = "acceptance_and_storage_time_")
    private Date acceptanceAndStorageTime;

    //验收人
    @ExcelColumn("验收人")
    @Column(name = "acceptor_", length = 100)
    private String acceptor;


    //入库PDI检查日期
    @ExcelColumn("入库PDI检查日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "inspection_input_pdi_")
    private Date inspectionInputPDI;


    //出库PDI检查日期
    @ExcelColumn("出库PDI检查日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "deposit_pdi_check_date_")
    private Date depositPDICheckDate;


    //三方协议车，表示为是
    @ExcelColumn("三方协议车，表示为是")
    @Column(name = "tripartite_agreement_vehicle_", length = 100)
    private String tripartiteAgreementVehicle;

    //ZT
    @ExcelColumn("ZT")
    @Column(name = "zt_", length = 100)
    private String zt;

    //进价
    @ExcelColumn("进价")
    @Column(name = "purchase_price_", length = 100)
    private Double purchasePrice;

    //MEMO
    @ExcelColumn("MEMO")
    @Column(name = "memo_", length = 100)
    private String memo;

    //配置类型
    @ExcelColumn("配置类型")
    @Column(name = "configuration_type_", length = 100)
    private String configurationType;

    //分公司号
    @ExcelColumn("分公司号")
    @Column(name = "branch_number_", length = 100)
    private String branchNumber;

    //销售价
    @ExcelColumn("销售价")
    @Column(name = "selling_price_", length = 100)
    private Double sellingPrice;


    //XZ
    @ExcelColumn("XZ")
    @Column(name = "zx_", length = 100)
    private String zx;

    //选装件
    @ExcelColumn("选装件")
    @Column(name = "optional_equipment_", length = 100)
    private String optionalEquipment;

    //内饰
    @ExcelColumn("内饰")
    @Column(name = "interior_trim_", length = 100)
    private String interiorTrim;

    // 制造日期
    @ExcelColumn("制造日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "manufacturing_date_")
    private Date manufacturingDate;

    //URL
    @ExcelColumn("URL")
    @Column(name = "url_", length = 100)
    private String url;
    //基地代码
    @ExcelColumn("基地代码")
    @Column(name = "base_code_", length = 100)
    private String baseCode;

    //分类
    @ExcelColumn("分类")
    @Column(name = "classification_", length = 100)
    private String classification;

    // 发车日期
    @ExcelColumn("发车日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "departure_date_")
    private Date departureDate;

    //收车地址
    @ExcelColumn("收车地址")
    @Column(name = "parking_address_", length = 100)
    private String parkingAddress;

    //收车方式
    @ExcelColumn("收车方式")
    @Column(name = "car_receiving_mode_", length = 100)
    private String carReceivingMode;

    //收车地址坐标
    @ExcelColumn("收车地址坐标")
    @Column(name = "receiving_address_coordinates_", length = 100)
    private String receivingAddressCoordinates;

    //收车单位
    @ExcelColumn("收车单位")
    @Column(name = "car_receiving_unit_", length = 100)
    private String carReceivingUnit;

    //原经销商号
    @ExcelColumn("原经销商号")
    @Column(name = "original_agency_number_", length = 100)
    private String originalAgencyNumber;

    //盘库地址
    @ExcelColumn("盘库地址")
    @Column(name = "inventory_address_", length = 100)
    private String inventoryAddress;

    //盘库地址坐标
    @ExcelColumn("盘库地址坐标")
    @Column(name = "disk_address_coordinates_", length = 100)
    private String diskAddressCoordinates;

    //盘库备案位置坐标
    @ExcelColumn("盘库备案位置坐标")
    @Column(name = "inventoryRecord_record_location_coordinates_", length = 100)
    private String inventoryRecordLocationCoordinates;

    //盘库地址与备案位置距离（米）
    @ExcelColumn("盘库地址与备案位置距离（米）")
    @Column(name = "position_distance_", length = 100)
    private Integer positionDistance;

    //是否盘库备案点发生
    @ExcelColumn("是否盘库备案点发生")
    @Column(name = "happen_", length = 100)
    private String happen;

    //盘库方式
    @ExcelColumn("盘库方式")
    @Column(name = "inventory_method_", length = 100)
    private String inventoryMethod;

    //盘库人
    @ExcelColumn("盘库人")
    @Column(name = "stockholder_person_", length = 100)
    private String stockholderPerson;


    // 盘库时间
    @ExcelColumn("盘库时间")
    @Temporal(TemporalType.TIME)
    @Column(name = "inventory_time_")
    private Date inventoryTime;

    //盘库标识(1表示已盘库)
    @ExcelColumn("盘库标识(1表示已盘库)")
    @Column(name = "inventory_identification_", length = 100)
    private String inventoryIdentification;


    //盘库备注
    @ExcelColumn("盘库备注")
    @Column(name = "inventory_remarks_", length = 100)
    private String inventoryRemarks;

    // 车型代码
    @ExcelColumn("车型代码")
    @Column(name = "vehicle_model_", length = 100)
    private String vehicleModel;

    // 出库状态(0表示已申请，1表示同意，2表示不同意)
    @ExcelColumn("出库状态(0表示已申请，1表示同意，2表示不同意)")
    @Column(name = "outgoing_state_", length = 100)
    private String outgoingState;


    // 出库申请人
    @ExcelColumn("出库申请人")
    @Column(name = "deposit_applicant_", length = 100)
    private String depositApplicant;


    // 出库申请日期
//    @ExcelColumn("出库申请日期")
//    @Temporal(TemporalType.DATE)
//    @Column(name = "outgoing_application_date_")
//    private Date outgoingApplicationDate;

    //outgoingApplicationDate

    // 出库申请日期
    @ExcelColumn("出库申请日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "outgoing_application_date_")
    private Date outgoingApplicationDate;



}






























