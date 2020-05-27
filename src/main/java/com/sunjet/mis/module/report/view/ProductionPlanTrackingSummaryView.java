package com.sunjet.mis.module.report.view;


import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.LinkedList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
@Subselect("\tSELECT\n" +
        "\t\tc.name_ AS id_,\n" +
        "\t\tc.warehouse_area_ AS origin_,\n" +
        "\t\tc.vehicle_series_,\n" +
        "\t\tc.name_ AS type_,\n" +
        "\t\tSUM(c.plan_) AS month_plan_,\n" +
        "\t\tc.year_,\n" +
        "\t\tc.month_,\n" +
        "\t\tSUM(c.complete_) AS complete_number_\n" +
        "\tFROM\n" +
        "\t\t(\n" +
        "\t\t\tSELECT\n" +
        "\t\t\t\tvm.name_ AS id_,\n" +
        "\t\t\t\tb.warehouse_area_,\n" +
        "\t\t\t\tvm.vehicle_series_,\n" +
        "\t\t\t\tvm.name_,\n" +
        "\t\t\t\tSUM(a.required_amount_) AS plan_,\n" +
        "\t\t\t\tb.year_,\n" +
        "\t\t\t\tb.month_,\n" +
        "\t\t\t\tb.complete_\n" +
        "\t\t\tFROM\n" +
        "\t\t\t\t(\n" +
        "\t\t\t\t\tSELECT\n" +
        "\t\t\t\t\t\trp.model_,\n" +
        "\t\t\t\t\t\trp.required_amount_\n" +
        "\t\t\t\t\tFROM\n" +
        "\t\t\t\t\t\trpt_plan rp\n" +
        "\t\t\t\t) AS a\n" +
        "\t\t\tINNER JOIN (\n" +
        "\t\t\t\tSELECT DISTINCT\n" +
        "\t\t\t\t\tky.vehicle_model_,\n" +
        "\t\t\t\t\tky.vehicle_name_,\n" +
        "\t\t\t\t\tky.warehouse_area_,\n" +
        "\t\t\t\t\tDATE_FORMAT(ky.inbound_date_, '%Y') AS year_,\n" +
        "\t\t\t\t\tDATE_FORMAT(ky.inbound_date_, '%c') AS month_,\n" +
        "\t\t\t\t\tCOUNT(ky.vehicle_model_) AS complete_\n" +
        "\t\t\t\tFROM\n" +
        "\t\t\t\t\trpt_available_inventory ky\n" +
        "\t\t\t\tGROUP BY\n" +
        "\t\t\t\t\tky.vehicle_model_,\n" +
        "\t\t\t\t\tky.vehicle_name_,\n" +
        "\t\t\t\t\tky.warehouse_area_,\n" +
        "\t\t\t\t\tDATE_FORMAT(ky.inbound_date_, '%Y'),\n" +
        "\t\t\t\t\tDATE_FORMAT(ky.inbound_date_, '%c')\n" +
        "\t\t\t) AS b ON a.model_ = b.vehicle_name_\n" +
        "\t\t\tINNER JOIN basic_vehicle_model vm ON vm.code_ = b.vehicle_model_\n" +
        "\t\t\tGROUP BY\n" +
        "\t\t\t\tvm.vehicle_series_,\n" +
        "\t\t\t\tvm.name_,\n" +
        "\t\t\t\tb.warehouse_area_,\n" +
        "\t\t\t\tb.year_,\n" +
        "\t\t\t\tb.month_,\n" +
        "\t\t\t\tb.complete_\n" +
        "\t\t) AS c\n" +
        "\tGROUP BY\n" +
        "\t\tc.warehouse_area_,\n" +
        "\t\tc.vehicle_series_,\n" +
        "\t\tc.name_,\n" +
        "\t\tc.year_,\n" +
        "\t\tc.month_\n")
//@Subselect("SELECT\n" +
//        "\tvm.name_ AS id_,\n" +
//        "\tb.warehouse_area_ AS origin_,\n" +
//        "\tvm.vehicle_series_,\n" +
//        "\tvm.name_ AS type_,\n" +
//        "\tSUM(a.required_amount_) AS month_plan_,\n" +
//        "\tb.year_,\n" +
//        "\tb.month_,\n" +
//        "\tb.complete_ AS complete_number_\n" +
//        "FROM\n" +
//        "\t(\n" +
//        "\t\tSELECT\n" +
//        "\t\t\trp.model_,\n" +
//        "\t\t\trp.required_amount_\n" +
//        "\t\tFROM\n" +
//        "\t\t\trpt_plan rp\n" +
//        "\t) AS a\n" +
//        "INNER JOIN (\n" +
//        "\tSELECT DISTINCT\n" +
//        "\t\tky.vehicle_model_,\n" +
//        "\t\tky.vehicle_name_,\n" +
//        "\t\tky.warehouse_area_,\n" +
//        "\t\tDATE_FORMAT(ky.inbound_date_, '%Y') AS year_,\n" +
//        "\t\tDATE_FORMAT(ky.inbound_date_, '%c') AS month_,\n" +
//        "\t\tCOUNT(ky.vehicle_model_) AS complete_\n" +
//        "\tFROM\n" +
//        "\t\trpt_available_inventory ky\n" +
//        "\tGROUP BY\n" +
//        "\t\tky.vehicle_model_,\n" +
//        "\t\tky.vehicle_name_,\n" +
//        "\t\tky.warehouse_area_,\n" +
//        "\t\tDATE_FORMAT(ky.inbound_date_, '%Y'),\n" +
//        "\t\tDATE_FORMAT(ky.inbound_date_, '%c')\n" +
//        ") AS b ON a.model_ = b.vehicle_name_\n" +
//        "INNER JOIN basic_vehicle_model vm ON vm.code_ = b.vehicle_model_\n" +
//        "GROUP BY\n" +
//        "\tvm.vehicle_series_,\n" +
//        "\tvm.name_,\n" +
//        "\tb.warehouse_area_,\n" +
//        "\tb.year_,\n" +
//        "\tb.month_,\n" +
//        "\tb.complete_\n" +
//        "\n" +
//        "\n")
/**
 * 生产计划跟踪汇总
 * */
public class ProductionPlanTrackingSummaryView {
    @Id
    @Column(name = "id_")
    private String id;
    //产地
    @Column(name = "origin_")
    private String origin;
    //车系
    @Column(name = "vehicle_series_")
    private String vehicleSeries;
    //类型
    @Column(name = "type_")
    private String type;
    //月度计划
    @Column(name = "month_plan_")
    private Integer monthPlan = 0;
    //第1周平衡
    @Builder.Default
    @Transient
    private Integer firstWeekBalance = 0;
    //第2周平衡
    @Builder.Default
    @Transient
    private Integer secondWeekBalance = 0;
    //第3周平衡
    @Builder.Default
    @Transient
    private Integer thirdWeekBalance = 0;
    //第4周平衡
    @Builder.Default
    @Transient
    private Integer fourthWeekBalance = 0;
    //第5周平衡
    @Builder.Default
    @Transient
    private Integer fifthWeekBalance = 0;
    //月度已入库完成
    @Column(name = "complete_number_")
    private Integer completeNumber = 0;
    //未完成数量
    @Builder.Default
    @Transient
    private Integer unfinishedNumber = 0;
    //完成率
    @Builder.Default
    @Transient
    private String completion = "";
    //year
    @Column(name = "year_")
    private Integer year;
    //month
    @Column(name = "month_")
    private Integer month;
}
