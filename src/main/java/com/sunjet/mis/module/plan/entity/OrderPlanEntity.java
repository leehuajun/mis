package com.sunjet.mis.module.plan.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 订单计划
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_order_plan")
public class OrderPlanEntity extends DocEntity {
    // 年度
    @ExcelColumn("年度")
    @Column(name = "year_")
    private Integer year;
    // 月份
    @ExcelColumn("月份")
    @Column(name = "month_")
    private Integer month;
    // 经销商代码
    @ExcelColumn("经销商代码")
    @Column(name = "distributor_code_",length = 50)
    private String distributorCode;

    // 经销商编码(SGMW的编码)
    @Column(name = "distributor_sgmw_code_",length = 50)
    private String sgmwCode;

    // 经销商名称
    @ExcelColumn("经销商名称")
    @Column(name = "distributor_name_",length = 100)
    private String distributorName;
    // 省份
    @ExcelColumn("省份")
    @Column(name = "province_",length = 20)
    private String province;
    // 区域
    @ExcelColumn("区域")
    @Column(name = "region_",length = 20)
    private String region;
    // 申报类型
    @ExcelColumn("申报类型")
    @Column(name = "plan_type_",length = 50)
    private String planType;
    // 类型
    @ExcelColumn("类型")
    @Column(name = "type_",length = 100)
    private String type;
    // 物料号
    @ExcelColumn("物料号")
    @Column(name = "vsn_",length = 100)
    private String vsn;
    // 车型名称
    @ExcelColumn("车型名称")
    @Column(name = "model_name_",length = 100)
    private String modelName;
    // 型号
    @ExcelColumn("型号")
    @Column(name = "model_",length = 100)
    private String model;
    // 车型
    @ExcelColumn("车型")
    @Column(name = "vehicle_model_",length = 100)
    private String vehicleModel;
    // 车型平台
    @ExcelColumn("车型平台")
    @Column(name = "vehicle_platform_",length = 100)
    private String vehiclePlatform;
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
    @ExcelColumn("经销商申报数")
    @Column(name = "required_amount_")
    private Integer requiredAmount=0;
    // 全月满足数
    @Builder.Default
    @ExcelColumn("全月满足数")
    @Column(name = "agreed_amount_")
    private Integer agreedAmount =0;
    // 不满足原因
    @ExcelColumn("不满足原因")
    @Column(name = "reason_",length = 200)
    private String reason;
    // 第1周满足计划
    @Builder.Default
    @ExcelColumn("第1周满足计划")
    @Column(name = "first_week_amount_")
    private Integer firstWeekAmount=0;
    // 第2周满足计划
    @Builder.Default
    @ExcelColumn("第2周满足计划")
    @Column(name = "second_week_amount_")
    private Integer secondWeekAmount=0;
    // 第3周满足计划
    @Builder.Default
    @ExcelColumn("第3周满足计划")
    @Column(name = "third_week_amount_")
    private Integer thirdWeekAmount=0;
    // 第4周满足计划
    @Builder.Default
    @ExcelColumn("第4周满足计划")
    @Column(name = "fourth_week_amount_")
    private Integer fourthWeekAmount=0;
    // 第5周满足计划
    @Builder.Default
    @ExcelColumn("第5周满足计划")
    @Column(name = "fifth_week_amount_")
    private Integer fifthWeekAmount = 0;
    // 第6周满足计划
    @Builder.Default
    @ExcelColumn("第6周满足计划")
    @Column(name = "sixth_week_amount_")
    private Integer sixthWeekAmount = 0;
    // 第6周满足计划
    @ExcelColumn("备注")
    @Column(name = "comment_",length = 1000)
    private String comment;
}
