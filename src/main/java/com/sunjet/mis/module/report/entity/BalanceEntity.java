package com.sunjet.mis.module.report.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * 客户余额表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_balance")
public class BalanceEntity extends DocEntity {

    // 科目编码
    @ExcelColumn("科目编码")
    @Column(name = "subject_code_",length = 20)
    private String subjectCode;

    // 客户编码
    @ExcelColumn("客户编码")
    @Column(name = "distributor_code_",length = 50)
    private String distributorCode;

    // 客户编码(SGMW的客户编码)
    @Column(name = "distributor_sgmw_code_",length = 50)
    private String sgmwCode;

    // 客户名称
    @ExcelColumn("客户名称")
    @Column(name = "distributor_name_",length = 100)
    private String distributorName;

    // 方向
    @ExcelColumn("方向")
    @Column(name = "direction_",length = 20)
    private String direction;

    // 期初本币余额
    @Builder.Default
    @ExcelColumn("期初本币余额")
    @Column(name = "initial_balance_")
    private Double initialBalance = 0.0;

    // 开票金额
    @Builder.Default
    @ExcelColumn("开票金额")
    @Column(name = "invoice_amount_")
    private Double invoiceAmount = 0.0;

    // 回款金额
    @Builder.Default
    @ExcelColumn("回款金额")
    @Column(name = "cashed_amount_")
    private Double cashedAmount = 0.0;

    // 方向20
    @ExcelColumn("方向20")
    @Column(name = "direction20_",length = 20)
    private String direction20;

    // 期末本币余额
    @Builder.Default
    @ExcelColumn("期末余额本币")
    @Column(name = "closing_balance_")
    private Double closingBalance = 0.0;

    //在途资金
    @Builder.Default
    @ExcelColumn("在途资金")
    @Column(name = "transit_fund_")
    private Double transitFund = 0.0;

    // 信用金额
    @Builder.Default
    @ExcelColumn("信用金额")
    @Column(name = "credit_amount_")
    private Double creditAmount = 0.0;

    // 客户发车余额（含授信额度）
    @Builder.Default
    @ExcelColumn("客户发车余额（含授信额度）")
    @Column(name = "credit_balance_")
    private Double creditBalance = 0.0;

    // 客户发车余额（不含授信额度）
    @Builder.Default
    @ExcelColumn("客户发车余额（不含授信额度）")
    @Column(name = "balance_")
    private Double balance =0.0;

    // 最后操作员
    @ExcelColumn("最后操作员")
    @Column(name = "last_operator_",length = 50)
    private String lastOperator;

    // 最后操作时间
    @ExcelColumn("最后操作时间")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_operate_time_")
    private Date lastOperateTime;

    @Override
    public String toString() {
        return "BalanceEntity{" +
                "subjectCode='" + subjectCode + '\'' +
                ", distributorCode='" + distributorCode + '\'' +
                ", distributorName='" + distributorName + '\'' +
                ", direction='" + direction + '\'' +
                ", initialBalance=" + initialBalance +
                ", invoiceAmount=" + invoiceAmount +
                ", cashedAmount=" + cashedAmount +
                ", direction20='" + direction20 + '\'' +
                ", closingBalance=" + closingBalance +
                ", creditAmount=" + creditAmount +
                ", creditBalance=" + creditBalance +
                ", balance=" + balance +
                ", lastOperator='" + lastOperator + '\'' +
                ", lastOperateTime=" + lastOperateTime +
                '}';
    }
}
