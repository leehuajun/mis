package com.sunjet.mis.module.report.view;


import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
@Subselect("SELECT DISTINCT\n" +
        "\tsc.vsn_ AS id_,\n" +
        "\tsc.warehouse_area_ AS origin_,\n" +
        "\tvm.vehicle_series_,\n" +
        "\tsc.vehicle_name_,\n" +
        "\tsc.vehicle_model_,\n" +
        "\tsc.vsn_,\n" +
        "\tDATE_FORMAT(sc.inbound_date_, '%Y') AS year_,\n" +
        "\tDATE_FORMAT(sc.inbound_date_, '%c') AS month_\n" +
        "FROM\n" +
        "\trpt_available_inventory sc\n" +
        "INNER JOIN basic_vehicle_model vm ON sc.vehicle_model_ = vm.code_\n" +
        "\n" +
        "\n")
/**
 * 生产计划跟踪报表
 * */
public class ProductionPlanTrackingReportView {
    @Id
    @Column(name = "id_")
    private String id;
    //产地
    @Column(name = "origin_")
    private String origin;
    //车系
    @Column(name = "vehicle_series_")
    private String vehicleSeries;
    //产品名称
    @Column(name = "vehicle_name_")
    private String vehicleName;
    //型号
    @Column(name = "vehicle_model_")
    private String vehicleModel;
    //VSN
    @Column(name = "vsn_")
    private String vsn;
    //year
    @Column(name = "year_")
    private Integer year;
    //month
    @Column(name = "month_")
    private Integer month;

//    //day
//    @Column(name = "day_")
//    private Integer day;
//    //amount
//    @Column(name = "day_amount_")
//    private Integer dayAmount;

//    //1日
//    @Builder.Default
//    @Transient
//    private Integer oneDay = 0;
//    //2日
//    @Builder.Default
//    @Transient
//    private Integer twoDay = 0;
//    //3日
//    @Builder.Default
//    @Transient
//    private Integer threeDay = 0;
//    //4日
//    @Builder.Default
//    @Transient
//    private Integer fourDay = 0;
//    //5日
//    @Builder.Default
//    @Transient
//    private Integer fiveDay = 0;
//    //6日
//    @Builder.Default
//    @Transient
//    private Integer sixDay = 0;
//    //7日
//    @Builder.Default
//    @Transient
//    private Integer sevenDay = 0;
//    //8日
//    @Builder.Default
//    @Transient
//    private Integer eightDay = 0;
//    //9日
//    @Builder.Default
//    @Transient
//    private Integer nineDay = 0;
//    //10日
//    @Builder.Default
//    @Transient
//    private Integer tenDay = 0;
//    //11日
//    @Builder.Default
//    @Transient
//    private Integer elevenDay = 0;
//    //12日
//    @Builder.Default
//    @Transient
//    private Integer twelveDay = 0;
//    //13日
//    @Builder.Default
//    @Transient
//    private Integer thirteenDay = 0;
//    //14日
//    @Builder.Default
//    @Transient
//    private Integer fourteenDay = 0;
//    //15日
//    @Builder.Default
//    @Transient
//    private Integer fifteenDay = 0;
//    //16日
//    @Builder.Default
//    @Transient
//    private Integer sixteenDay = 0;
//    //17日
//    @Builder.Default
//    @Transient
//    private Integer seventeenDay = 0;
//    //18日
//    @Builder.Default
//    @Transient
//    private Integer eighteenDay = 0;
//    //19日
//    @Builder.Default
//    @Transient
//    private Integer nineteenDay = 0;
//    //20日
//    @Builder.Default
//    @Transient
//    private Integer twentyDay = 0;
//    //21日
//    @Builder.Default
//    @Transient
//    private Integer twentyOneDay = 0;
//    //22日
//    @Builder.Default
//    @Transient
//    private Integer twentyTwoDay = 0;
//    //23日
//    @Builder.Default
//    @Transient
//    private Integer twentyThreeDay = 0;
//    //24日
//    @Builder.Default
//    @Transient
//    private Integer twentyFourDay = 0;
//    //25日
//    @Builder.Default
//    @Transient
//    private Integer twentyFiveDay = 0;
//    //26日
//    @Builder.Default
//    @Transient
//    private Integer twentySixDay = 0;
//    //27日
//    @Builder.Default
//    @Transient
//    private Integer twentySevenDay = 0;
//    //28日
//    @Builder.Default
//    @Transient
//    private Integer twentyEightDay = 0;
//    //29日
//    @Builder.Default
//    @Transient
//    private Integer twentyNineDay = 0;
//    //30日
//    @Builder.Default
//    @Transient
//    private Integer thirtyDay = 0;
//    //31日

    @Builder.Default
    @Transient
    private Integer thirtyOneDay = 0;
    //月度计划
    @Builder.Default
    @Transient
    private Integer monthPlan = 0;
    //完成量
    @Builder.Default
    @Transient
    private Integer completeNumber = 0;
    //完成率
    @Builder.Default
    @Transient
    private String completion = "";
    //第1周计划
    @Builder.Default
    @Transient
    private Integer firstWeekPlan = 0;
    //第2周计划
    @Builder.Default
    @Transient
    private Integer secondWeekPlan = 0;
    //第3周计划
    @Builder.Default
    @Transient
    private Integer thirdWeekPlan = 0;
    //第4周计划
    @Builder.Default
    @Transient
    private Integer fourthWeekPlan = 0;
    //第5周计划
    @Builder.Default
    @Transient
    private Integer fifthWeekPlan = 0;
    //第1周完成
    @Builder.Default
    @Transient
    private Integer firstWeekComplete = 0;
    //第2周完成
    @Builder.Default
    @Transient
    private Integer secondWeekComplete = 0;
    //第3周完成
    @Builder.Default
    @Transient
    private Integer thirdWeekComplete = 0;
    //第4周完成
    @Builder.Default
    @Transient
    private Integer fourthWeekComplete = 0;
    //第5周完成
    @Builder.Default
    @Transient
    private Integer fifthWeekComplete = 0;
    @Transient
    LinkedList<Integer> days = new LinkedList<Integer>();
    @Transient
    List<Integer> firstWeek = new ArrayList<>();
    @Transient
    List<Integer> secondWeek = new ArrayList<>();
    @Transient
    List<Integer> thirdWeek = new ArrayList<>();
    @Transient
    List<Integer> fourthWeek = new ArrayList<>();
    @Transient
    List<Integer> fifthWeek = new ArrayList<>();


}
