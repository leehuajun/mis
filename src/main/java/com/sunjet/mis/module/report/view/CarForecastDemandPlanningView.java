package com.sunjet.mis.module.report.view;

import com.sunjet.mis.annotation.ExcelColumn;
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
 * @Title: CarForecastDemandPlanningView
 * @ProjectName mis
 * @Description: 客厢车月预测客厢车销售需求计划 - 客厢车
 * @date 2019/4/289:50  bpcpcs.id_,
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
@Subselect("SELECT DISTINCT\n" +
        "	bpcpcs.id_,\n" +
        "	KXC.ac AS scsj,\n" +
        "	bpcpcs.production_,\n" +
        "	bpcpcs.product_category_,\n" +
        "	bpcpcs.product_name_,\n" +
        "	KXC.vehicle_Type1_,\n" +
        "	bpcpcs.abs_vsn_,\n" +
        "	rrtm.no_abs_vsn_,\n" +
        "	KXC.ZS AS zycphs,\n" +
        "	ifnull( rcb.finished_balance_, 0 ) AS syjys,\n" +
        "	KXC.ZS - ifnull( rcb.finished_balance_, 0 ) AS cgs \n" +
        "FROM\n" +
        "	(\n" +
        "	SELECT\n" +
        "		rsvmpb.vsn_,\n" +
        "		rsvmpb.vehicle_Type1_,\n" +
        "		rsvmpb.date_year_month_ AS ac,\n" +
        "		SUM( headquarters_number_ ) AS ZS \n" +
        "	FROM\n" +
        "		rpt_special_vehicle_monthly_plan_balance rsvmpb # 专用车平衡表\n" +
        "		\n" +
        "	WHERE\n" +
        "		rsvmpb.date_year_month_ \n" +
        "	GROUP BY\n" +
        "		rsvmpb.vsn_ \n" +
        "	) KXC\n" +
        "	LEFT JOIN basic_passenger_compartmen_provenanc_car_series bpcpcs ON bpcpcs.abs_vsn_ = KXC.vsn_ #`客厢车匹配表\n" +
        "	LEFT JOIN rpt_passenge_car_balance rcb ON bpcpcs.abs_vsn_ = rcb.abs_vsn_ #客厢车月成品结余\n" +
        "	\n" +
        "	AND DATE_FORMAT( rcb.date_year_momth_, '%Y-%m' ) IN ( SELECT DATE_FORMAT( DATE_SUB( rsvmpb.date_year_month_, INTERVAL 1 MONTH ), '%Y-%m' ) FROM rpt_special_vehicle_monthly_plan_balance rsvmpb )\n" +
        "	LEFT JOIN rpt_refitting_truck_material rrtm ON rrtm.vsn_ = bpcpcs.abs_vsn_ #客厢车车月物料申报表 主要是获取非vsn\n" +
        "	\n" +
        "WHERE\n" +
        "	KXC.vsn_ = bpcpcs.abs_vsn_ \n" +
        "GROUP BY\n" +
        "	bpcpcs.production_,\n" +
        "	bpcpcs.product_category_,\n" +
        "	bpcpcs.product_Name_,\n" +
        "	scsj DESC,\n" +
        "	KXC.vsn_")
public class CarForecastDemandPlanningView {

    @Id
    @Column(name = "id_")
    private String id;
    // 产地
    @Column(name = "production_")
    private String production;
    // 车系
    @ExcelColumn("产品名称")
    @Column(name = "product_Name_", length = 50)
    private String productName;

    @ExcelColumn("产品类别")
    @Column(name = "product_category_", length = 50)
    private String  productCategory;

    // 车型
    @Column(name = "vehicle_Type1_")
    private String vehicleType1;

    // @ExcelColumn("非ABS-VSN")
    @Column(name = "no_abs_vsn_")
    private String noabsvsn;

    // ABS-VSN
    @Column(name = "abs_vsn_")
    private String absvsn;

    //专用车时间
    @Column(name = "scsj")
    private Date ztime;

    //月生产计划
    @Column(name = "zycphs")
    private Integer zycphs;

    //成品结余
    @Column(name = "syjys")
    private Integer finishedBalance;

    //  采购数
    @Column(name = "cgs")

    private Integer scjh;

    @Transient
    private Date startDate = DateHelper.getFirstOfYear(); // 开始日期

    @Transient
    private Date endDate = DateHelper.getEndDateTime();

//    //
//    @Column(name = "vsn_")
//    private String vsn;



}
