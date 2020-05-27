package com.sunjet.mis.module.warehouse.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import com.sunjet.mis.helper.DateHelper;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author SUNJET-YFS
 * @Title: RefittingTruckMaterialEntity
 * @ProjectName mis
 * @Description: 改装车月物料申报表
 * @date 2019/4/119:44
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_refitting_truck_material")
public class RefittingTruckMaterialEntity extends DocEntity {

    @ExcelColumn("时间")
    @Column(name = "date_year_month_", length = 100)
    private Date dateYearMonth;
    // 物料号
    @ExcelColumn("物料号")
    @Column(name = "vsn_", length = 100)
    private String vsn;

    // 品种代码
    @ExcelColumn("品种代码")
    @Column(name = "brand_code_", length = 100)
    private String brandCode;

    // 平台
    @ExcelColumn("平台")
    @Column(name = "terrace_", length = 100)
    private String terrace;

    // 车系(APP)
    @ExcelColumn("车系(APP)")
    @Column(name = "audi_app_", length = 100)
    private String audiApp;

    // 车型(APP)
    @ExcelColumn("车型(APP)")
    @Column(name = "vehicle_type_app_", length = 100)
    private String vehicleTypeApp;

    //车型
    @ExcelColumn("车型")
    @Column(name = "vehicle_Type_", length = 60)
    private String vehicleType;

    // 车型名称-1
    @ExcelColumn("车型名称-1")
    @Column(name = "vehicle_type_name_", length = 100)
    private String vehicleTypeName;

    //发动机号
    @ExcelColumn("发动机号")
    @Column(name = "engine_code_", length = 100)
    private String engineCode;

    // 颜色
    @ExcelColumn("颜色")
    @Column(name = "color_", length = 100)
    private String color;

    // 空调
    @ExcelColumn("空调")
    @Column(name = "air_conditioner_", length = 100)
    private String airConditioner;

    // 车型平台
    @ExcelColumn("车型平台")
    @Column(name = "vehicle_platform_", length = 100)
    private String vehiclePlatform;

    // 排放标准
    @ExcelColumn("排放标准")
    @Column(name = "effluent_", length = 20)
    private String effluent;

    // 备注
    @ExcelColumn("备注")
    @Column(name = "note_",length = 300)
    private String note;

    //配置
    @ExcelColumn("配置")
    @Column(name = "allocation_",length = 350)
    private String allocation;

    // A价（ABS)
    @ExcelColumn("A价（ABS)")
    @Column(name = "a_price_", length = 100)
    private String aprice;

    // 物料号（对应非ABS代码）
    @ExcelColumn("物料号（对应非ABS代码）")
    @Column(name = "no_abs_vsn_", length = 100)
    private String noabsvsn;

    // 产地
    @ExcelColumn("产地")
    @Column(name = "production_", length = 50)
    private String production;


    @Transient
    private Date startDate = DateHelper.getFirstOfYear(); // 开始日期

    @Transient
    private Date endDate   = DateHelper.getEndDateTime();

}
