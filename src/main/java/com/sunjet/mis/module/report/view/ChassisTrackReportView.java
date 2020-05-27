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
//@Subselect("SELECT DISTINCT\n" +
//        "\tcp.id_,\n" +
//        "\tcp.vehicle_model_,\n" +
//        "\tcp.variety_,\n" +
//        "\tcp.allocation_ AS config_,\n" +
//        "\tSUM(cp.amount_) AS procurement_plan_,\n" +
//        "\tcp.year_,\n" +
//        "\tcp.month_\n" +
//        "FROM\n" +
//        "\trpt_chassis_procurement cp\n" +
//        "GROUP BY\n" +
//        "\tcp.vehicle_model_,\n" +
//        "\tcp.variety_,\n" +
//        "\tcp.allocation_")
@Subselect("SELECT DISTINCT\n" +
        "\tcp.id_,\n" +
        "\tcp.vehicle_model_,\n" +
        "\tcp.variety_,\n" +
        "\tcp.batch_number_,\n" +
        "\tcp.allocation_ AS config_,\n" +
        "\tcp.amount_ AS procurement_plan_,\n" +
        "\tcp.year_,\n" +
        "\tcp.month_\n" +
        "FROM\n" +
        "\trpt_chassis_procurement cp\n")
/**
 * 底盘跟踪报表汇总
 * */
public class ChassisTrackReportView {
    @Id
    @Column(name = "id_")
    private String id;
    //车系
    @Column(name = "vehicle_model_")
    private String vehicleModel;
    //配置
    @Column(name = "config_")
    private String config;
    //品种
    @Column(name = "variety_")
    private String variety;
    //批次号
    @Column(name = "batch_number_")
    private String batchNumber;
    //采购计划
    @Column(name = "procurement_plan_")
    private Integer procurementPlan;
    //股份已分车
    @Builder.Default
    @Transient
    private Integer sharesAlready = 0;
    //股份待分车
    @Builder.Default
    @Transient
    private Integer sharesWaiting = 0;
    //已入底盘库
    @Builder.Default
    @Transient
    private Integer alreadyWarehouse = 0;
    //已分车待提
    @Builder.Default
    @Transient
    private Integer waitingExtract = 0;
    //重庆底盘库库存
    @Builder.Default
    @Transient
    private Integer CHONGQINGInventory = 0;
    //昆明前置库库存
    @Builder.Default
    @Transient
    private Integer KUNMINGInventory = 0;
    //发柳州在途
    @Builder.Default
    @Transient
    private Integer LIUZHOUOnTheWay = 0;
    //青岛底盘库库存
    @Builder.Default
    @Transient
    private Integer QINGDAOInventory = 0;
    //青岛缓冲区库存
    @Builder.Default
    @Transient
    private Integer QINGDAOBufferInventory = 0;
    //柳州底盘库
    @Builder.Default
    @Transient
    private Integer LIUZHOUInventory = 0;
    //柳州在制
    @Builder.Default
    @Transient
    private Integer LIUZHOUMaking = 0;
    //实车库存小计
    @Builder.Default
    @Transient
    private Integer vehicleSum = 0;
    //资源合计
    @Builder.Default
    @Transient
    private Integer resourcesSum = 0;
    //year
    @Column(name = "year_")
    private Integer year;
    //month
    @Column(name = "month_")
    private Integer month;
}
