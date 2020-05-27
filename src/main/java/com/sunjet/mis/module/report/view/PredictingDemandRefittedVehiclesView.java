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
 * @Title: PredictingDemandRefittedVehiclesView
 * @ProjectName mis
 * @Description: 货改车月预测改装车销售需求计划
 * @date 2019/4/1817:59
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
@Subselect("SELECT DISTINCT#货改车 ABS-VSN 产品名称，产品类别，产地的匹配表\n" +
        "btsp.id_,\n" +
        "btsp.production_,\n" +
        "btsp.product_category_,\n" +
        "btsp.product_name_,\n" +
        "HGC.vehicle_Type1_,\n" +
        "btsp.abs_vsn_,\n" +
        "rrtm.no_abs_vsn_,\n" +
        "HGC.ac AS ztime,\n" +
        "HGC.ZS AS zycphs,\n" +
        "ifnull( rttb.finished_balance_, 0 ) AS syjys,\n" +
        "HGC.ZS - ifnull( rttb.finished_balance_, 0 ) AS scjh \n" +
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
        "	) HGC\n" +
        "	LEFT JOIN basic_truck_svariety_provenanc btsp ON btsp.abs_vsn_ = HGC.vsn_ #`货改车匹配表\n" +
        "	LEFT JOIN rpt_transforming_trucks_balance rttb ON btsp.abs_vsn_ = rttb.abs_vsn_ #货改车月成品结余\n" +
        "	\n" +
        "	AND DATE_FORMAT( rttb.date_year_month_, '%Y-%m' ) IN ( SELECT DATE_FORMAT( DATE_SUB( rsvmpb.date_year_month_, INTERVAL 1 MONTH ), '%Y-%m' ) FROM rpt_special_vehicle_monthly_plan_balance rsvmpb )\n" +
        "	LEFT JOIN rpt_refitting_truck_material rrtm ON rrtm.vsn_ = btsp.abs_vsn_ #改装车月物料申报表 主要是获取非vsn\n" +
        "	\n" +
        "WHERE\n" +
        "	HGC.vsn_ = btsp.abs_vsn_ #货改车的vsn等于平衡表货改的vsn\n" +
        "	\n" +
        "GROUP BY\n" +
        "	btsp.production_,\n" +
        "	btsp.product_category_,\n" +
        "	btsp.product_Name_,\n" +
        "	btsp.abs_vsn_,\n" +
        "	HGC.ac DESC")

//专用车月计划平衡详细
//改装车月物料申报表 这张表的时间也要跟 专用车月计划平衡详细 时间匹配  目前这两张表缺少时间匹配
//货改车月成品结余
//货改产品品种，产地，vsn的匹配表

public class PredictingDemandRefittedVehiclesView {

    @Id
    @Column(name = "id_")
    private String id;
    // 产地
    @Column(name = "production_")
    private String production;

    // 产品类别
    @Column(name = "product_category_")
    private String productCategory;

    //产品名称
    @Column(name = "product_Name_")
    private String productName;


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
    @Column(name = "ztime")
    private Date ztime;

    //专用车月平衡后订单
    @Column(name = "zycphs")
    private Integer zycphs;

    //成品结余
    @Column(name = "syjys")
    private Integer finishedBalance;

    //  月生产计划
    @Column(name = "scjh")
    private Integer scjh;

    @Transient
    private Date startDate = DateHelper.getFirstOfYear(); // 开始日期

    @Transient
    private Date endDate = DateHelper.getEndDateTime();
//    //
//    @Column(name = "vsn_")
//    private String vsn;


}

