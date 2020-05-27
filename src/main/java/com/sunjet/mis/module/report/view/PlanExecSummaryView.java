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
@Subselect("select distinct year_,month_,distributor_code_,`distributor_sgmw_code_`,distributor_name_,vsn_,\n" +
        "(select uuid()) id_ \n" +
        "from rpt_plan p1")
public class PlanExecSummaryView {
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

    @Builder.Default
    @Transient
    private Integer requiredAmount = 0;
    @Builder.Default
    @Transient
    private Integer agreedAmount = 0;
    @Builder.Default
    @Transient
    private Integer monthPlanAmount = 0;
    @Builder.Default
    @Transient
    private Double balance = 0.0;
    @Builder.Default
    @Transient
    private Integer allotAmount = 0;
    @Builder.Default
    @Transient
    private Integer delivedAmount = 0;
}
