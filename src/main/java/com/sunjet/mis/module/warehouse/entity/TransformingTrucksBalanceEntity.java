package com.sunjet.mis.module.warehouse.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import com.sunjet.mis.helper.DateHelper;
import lombok.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;


/**
 * @author SUNJET-YFS
 * @Title: TransformingTrucksBalance
 * @ProjectName mis
 * @Description: 货改车月成品结余
 * @date 2019/4/1815:43
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_transforming_trucks_balance")
public class TransformingTrucksBalanceEntity extends DocEntity {

    //date_year_month_
    @ExcelColumn("时间")
    @Column(name = "date_year_month_", length = 100)
    private Date dateYearMonth;

    // 非ABS-VSN
    @ExcelColumn("非ABS-VSN")
    @Column(name = "no_abs_vsn_", length = 100)
    private String noabsvsn;

    // ABS-VSN   finished balance
    @ExcelColumn("ABS-VSN")
    @Column(name = "abs_vsn_", length = 50)
    private String absvsn;

    //月底成品结余
    @ExcelColumn("月底成品结余")
    @Column(name = "finished_balance_", length = 50)
    private Integer finishedBalance=0;

    @Transient
    private Date startDate = DateHelper.getFirstOfYear(); // 开始日期

    @Transient
    private Date endDate = DateHelper.getEndDateTime();

}























