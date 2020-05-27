package com.sunjet.mis.module.warehouse.entity;
import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import com.sunjet.mis.helper.DateHelper;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


/**
 * @author SUNJET-YFS
 * @Title: SpecialVehicleMonthlyPlanBalanceEntity
 * @ProjectName mis
 * @Description: 专用车月计划平衡详细表
 * @date 2019/4/1011:49
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_special_vehicle_monthly_plan_balance")
public class SpecialVehicleMonthlyPlanBalanceEntity extends DocEntity {

    // ID
    @ExcelColumn("ID")
    @Column(name = "ssid_", length = 100)
    private String ssid;

    // 年度
    @ExcelColumn("时间")
    @Temporal(TemporalType.DATE)
    @Column(name = "date_year_month_",length = 50)
    private Date dateYearMonth;

    // 经销商代码
    @ExcelColumn("经销商代码")
    @Column(name = "distributor_code_",length = 50)
    private String distributorCode;

    // 申报类型
    @ExcelColumn("申报类型")
    @Column(name = "plan_type_",length = 50)
    private String planType;

    //类型
    @ExcelColumn("类型")
    @Column(name = "type_",length = 100)
    private String type;

    //物料号
    @ExcelColumn("物料号")
    @Column(name = "vsn_", length = 100)
    private String vsn;

    // 车型
    @ExcelColumn("车型A")
    @Column(name = "vehicle_model_", length = 100)
    private String vehicleModel;

    // 品种代码
    @ExcelColumn("品种代码")
    @Column(name = "brand_code_",length = 100)
    private String brandCode;

    // 颜色
    @ExcelColumn("颜色")
    @Column(name = "color_",length = 100)
    private String color;

    // 排放标准
    @ExcelColumn("排放标准")
    @Column(name = "effluent_",length = 20)
    private String effluent;

    // 物料品种
    @ExcelColumn("物料品种")
    @Column(name = "vehicle_breed_",length = 20)
    private String vehicleBreed;

    // 经销商申报数
    @Builder.Default
    @ExcelColumn("上报数")
    @Column(name = "required_amount_")
    private Integer requiredAmount=0;

    @ExcelColumn("SGMW大区满足数")
    @Column(name = "region_number_", length = 100)
    private Integer regionNumber=0;

    @ExcelColumn("SGMW总部满足数")
    @Column(name = "headquarters_number_", length = 100)
    private Integer headquartersNumber=0;

    @ExcelColumn("是否集团车")
    @Column(name = "is_no_group_car_", length = 100)
    private String isNoGroupCar;

    @ExcelColumn("是否异地配送")
    @Column(name = "allopatry_delivery_", length = 100)
    private String allopatryDelivery;

    @ExcelColumn("是否套色")
    @Column(name = "color_register_", length = 100)
    private String colorRegister;

    @ExcelColumn("考核第一周")
    @Column(name = "first_week_assessment_", length = 100)
    private String firstWeekAssessment;

    @ExcelColumn("考核第二周")
    @Column(name = "second_week_assessment_", length = 100)
    private String secondWeekAssessment;

    @ExcelColumn("考核第三周")
    @Column(name = "third_week_assessment_", length = 100)
    private String thirdWeekAssessment;

    @ExcelColumn("考核第四周")
    @Column(name = "fourth_week_assessment_", length = 100)
    private String fourthWeekAssessment;

    @ExcelColumn("考核第五周")
    @Column(name = "fifth_week_assessment_", length = 100)
    private String fifthWeekAssessment;

    @ExcelColumn("考核第六周")
    @Column(name = "sixth_week_assessment_", length = 100)
    private String sixthWeekAssessment;

    @ExcelColumn("备注")
    @Column(name = "remarks_", length = 500)
    private String remarks;

    @ExcelColumn("备注2")
    @Column(name = "remarks2_", length = 300)
    private String remarks2;

    // 经销商
    @ExcelColumn("经销商")
    @Column(name = "distributor_name_", length = 100)
    private String distributorName;

    @ExcelColumn("所属省份")
    @Column(name = "province_", length = 100)
    private String province;

    @ExcelColumn("所属区域")
    @Column(name = "region_", length = 100)
    private String region;

    @ExcelColumn("可申报区域")
    @Column(name = "declarable_region", length = 200)
    private String declarableRegion;

    @ExcelColumn("车型")
    @Column(name = "vehicle_Type_", length = 60)
    private String vehicleType;

    @ExcelColumn("车型1")
    @Column(name = "vehicle_Type1_", length = 60)
    private String vehicleType1;

    //车系
    @ExcelColumn("车系")
    @Column(name = "vehicle_series_", length = 100)
    private String vehicleSeries;

    @ExcelColumn("排量")
    @Column(name = "displacement_", length = 100)
    private String displacement;

    @Transient
    private Date startDate = DateHelper.getFirstOfYear(); // 开始日期

    @Transient
    private Date endDate   = DateHelper.getEndDateTime();

}

