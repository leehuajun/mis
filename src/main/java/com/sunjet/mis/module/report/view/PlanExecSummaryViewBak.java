package com.sunjet.mis.module.report.view;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
@Subselect("select \n" +
        "distinct year_,month_,distributor_code_,`distributor_sgmw_code_`,distributor_name_,vsn_,\n" +
        "(select uuid()) id_, \n" +
        "(select  sum(required_amount_)  from rpt_plan where \n" +
        "year_=p1.`year_` and month_=p1.month_ and distributor_code_=p1.`distributor_code_` and vsn_=p1.vsn_) required_amount_,\n" +
        "(select  sum(`agreed_amount_`)  from rpt_plan where \n" +
        "year_=p1.`year_` and month_=p1.month_ and distributor_code_=p1.`distributor_code_` and vsn_=p1.vsn_) agreed_amount_,\n" +
        "(select  sum(first_week_amount_)+sum(second_week_amount_)+sum(third_week_amount_)+sum(fourth_week_amount_)+sum(fifth_week_amount_)+sum(sixth_week_amount_) as amount from rpt_plan where \n" +
        "year_=p1.`year_` and month_=p1.month_ and distributor_code_=p1.`distributor_code_` and vsn_=p1.vsn_) manth_plan_amount_,\n" +
        "(select credit_balance_ from rpt_balance where distributor_sgmw_code_=p1.distributor_sgmw_code_) balance_,\n" +
        "(select count(*) from rpt_allot_vehicle where distributor_sgmw_code_=p1.distributor_sgmw_code_ and vsn_=p1.vsn_ and year(invoice_date_)=p1.year_ and month(invoice_date_)=p1.month_) allot_amount_, \n" +
        "(select count(*) from rpt_vehicle_inv where distributor_sgmw_code_=p1.distributor_sgmw_code_ and vsn_=p1.vsn_ and year(invoice_time_)=p1.year_ and month(invoice_time_)=p1.month_ and status_='已出库') delived_amount_ \n" +
        "from rpt_plan p1")
public class PlanExecSummaryViewBak {
    @Id
    @Column(name = "id_")
    private String id;
    @Column(name = "year_")
    private Integer year;
    @Column(name = "month_")
    private Integer month;
    @Column(name = "distributor_code_")
    private String distributorCode;
    @Column(name = "distributor_sgmw_code_")
    private String sgmwCode;
    @Column(name = "distributor_name_")
    private String distributorName;
    @Column(name = "vsn_")
    private String vsn;
    @Column(name = "required_amount_")
    private Integer requiredAmount;
    @Column(name = "agreed_amount_")
    private Integer agreedAmount;
    @Column(name = "manth_plan_amount_")
    private Integer monthPlanAmount;
    @Column(name = "balance_")
    private Double balance;
    @Column(name = "allot_amount_")
    private Integer allotAmount;
    @Column(name = "delived_amount_")
    private Integer delivedAmount;
}
