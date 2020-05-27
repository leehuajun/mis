//package com.sunjet.mis.module.warehouse.entity;
//
//import com.sunjet.mis.annotation.ExcelColumn;
//import com.sunjet.mis.base.entity.DocEntity;
//import com.sunjet.mis.helper.DateHelper;
//import lombok.*;
//import javax.persistence.*;
//import java.util.Date;
//
///**
// * @author SUNJET-YFS
// * @Title: VehicleDisembarksWarehousing
// * @ProjectName mis
// * @Description: 车辆下线入库 （生产入库）
// * @date 2019/3/815:54
// */
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "rpt_vehicle_disembarks_warehousing")
//public class VehicleDisembarksWarehousingEntity extends DocEntity {
//
//    // VSN
//    @ExcelColumn("VSN")
//    @Column(name = "vsn_", length = 50)
//    private String vsn;
//
//    // VIN
//    @ExcelColumn("VIN")
//    @Column(name = "vin_", length = 50)
//    private String vin;
//
//    // 库存状态
//    @ExcelColumn("库存状态")
//    @Column(name = "stock_status_", length = 50)
//    private String stockStatus;
//
//    // 仓库区域
//    @ExcelColumn("仓库区域")
//    @Column(name = "warehouse_area_", length = 100)
//    private String warehouseArea;
//
//    // 产品型号
//    @ExcelColumn("产品型号")
//    @Column(name = "vehicle_model_", length = 50)
//    private String vehicleModel;
//
//    //产品名称
//    @ExcelColumn("产品名称")
//    @Column(name = "vehicle_name_", length = 100)
//    private String vehicleName;
//
//    // 颜色
//    @ExcelColumn("产品颜色")
//    @Column(name = "color_", length = 100)
//    private String color;
//
//    // 订单号
//    @ExcelColumn("订单号")
//    @Column(name = "order_id_", length = 100)
//    private String orderId;
//
//    // 库位
//    @ExcelColumn("库位")
//    @Column(name = "location_", length = 100)
//    private String location;
//
//    // 车辆编号
//    @ExcelColumn("车辆编号")
//    @Column(name = "vehicle_number_", length = 100)
//    private String vehicleNumber;
//
//    // 入库日期
//    @ExcelColumn("入库日期")
//    //@Temporal(TemporalType.DATE)
//    @Column(name = "inbound_date_", length = 100)
//    private Date inboundDate;
//    // 备注
//    @ExcelColumn("备注")
//    @Column(name = "note_", length = 100)
//    private Date note;
//
//    @Transient
//    private Date startDate= DateHelper.getFirstOfYear();;
//    @Transient
//    private Date endDate=DateHelper.getEndDateTime();
//    //warehousing
//
//    // 这个也是入库日期用于页面查询的
//   // @Temporal(TemporalType.DATE)
////    @Column(name = "warehousing_date_", length = 100)
////    private Date warehousingDate;
//
//}

