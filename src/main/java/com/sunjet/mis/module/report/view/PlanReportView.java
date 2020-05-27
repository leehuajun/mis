package com.sunjet.mis.module.report.view;


import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
@Subselect("SELECT\n" +
        "\ta.region_,\n" +
        "\ta.province_,\n" +
        "\tSUM(a.agreed_amount_) AS agreed_amount_,\n" +
        "\tSUM(a.required_amount_) AS required_amount_,\n" +
        "\tSUM(a.allot_) AS allot_,\n" +
        "\t\tCONCAT(round(SUM(a.allot_) / SUM(a.agreed_amount_) * 100),'%') AS completion_,\n\n" +
        "\ta.month_,\n" +
        "\ta.year_,\n" +
        "\ta.type_,\n" +
        "\tMIN(a.id_) AS id_\n" +
        "FROM\n" +
        "\t(\n" +
        "\t\tSELECT DISTINCT\n" +
        "\t\t\trp.id_,\n" +
        "\t\t\trp.distributor_sgmw_code_,\n" +
        "\t\t\trp.vsn_,\n" +
        "\t\t\trp.region_,\n" +
        "\t\t\trp.province_,\n" +
        "\t\t\trp.agreed_amount_,\n" +
        "\t\t\trp.required_amount_,\n" +
        "\t\t\t(\n" +
        "\t\t\t\tSELECT\n" +
        "\t\t\t\t\tCOUNT(*)\n" +
        "\t\t\t\tFROM\n" +
        "\t\t\t\t\trpt_allot_vehicle rav\n" +
        "\t\t\t\tWHERE\n" +
        "\t\t\t\t\trav.distributor_sgmw_code_ = rp.distributor_sgmw_code_\n" +
        "\t\t\t\tAND rav.vsn_ = rp.vsn_\n" +
        "\t\t\t) AS allot_,\n" +
        "\t\t\trp.year_,\n" +
        "\t\t\trp.month_,\n" +
        "\t\t\trp.type_\n" +
        "\t\tFROM\n" +
        "\t\t\trpt_plan rp\n" +
        "\t\tINNER JOIN rpt_allot_vehicle rav ON rav.distributor_sgmw_code_ = rp.distributor_sgmw_code_\n" +
        "\t\tAND rav.vsn_ = rp.vsn_\n" +
        "\t\t\n" +
        "\t) AS a\n" +
        "GROUP BY\n" +
        "\ta.province_,\n" +
        "\ta.region_,\n" +
        "\ta.month_,\n" +
        "\ta.type_,\n" +
        "\ta.year_")
/**
 * 计划执行汇总报表
 * */
public class PlanReportView {
    @Id
    @Column(name = "id_")
    private String id;
    @Column(name = "year_")
    private Integer year;
    @Column(name = "month_")
    private Integer month;
    @Column(name = "region_")
    private String region;
    @Column(name = "province_")
    private String province;
    @Column(name = "agreed_amount_")
    private String agreedAmount;
    @Column(name = "required_amount_")
    private String requiredAmount;
    @Column(name = "allot_")
    private String allot;
    @Column(name = "completion_")
    private String completion;
    @Column(name = "type_")
    private String type;

}
