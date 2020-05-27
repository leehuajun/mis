package com.sunjet.mis.module.report.view;

import lombok.*;

/**
 * @Author: wushi
 * @description: 经销商完成指标视图
 * @Date: Created in 9:42 2019/2/22
 * @Modify by: wushi
 * @ModifyDate by: 9:42 2019/2/22
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistributorCompleteReportView {


    /**
     * 地区
     */
    private String regionName;

    /**
     * 省份
     */
    private String provinceName;

    /**
     * 级别
     */
    private String level;


    /**
     * 经销商编码
     */
    private String code;

    /**
     * SGMW的经销商编码
     */
    private String distributorCode;

    /**
     * 经销商名称
     */
    private String distributorName;

    /**
     * 年度目标(合计)
     */
    private double yearTotalTarget = 0.0;

    /**
     * 年度开票(合计)
     */
    private double yearTotalInvoice = 0.0;

    /**
     * 年度差距(合计)
     */
    private double yearTotalDifference = 0.0;

    /**
     * 年度完成率(合计)
     */
    private double yearTotalCompletionRate = 0.0;

    /**
     * 年度目标(客改车合计)
     */
    private double yearTotalBusTarget = 0.0;

    /**
     * 年度开票(客改车合计)
     */
    private double yearTotalBusInvoice = 0.0;

    /**
     * 年度差距(客改车合计)
     */
    private double yearTotalBusDifference;

    /**
     * 年度完成率(客改车合计)
     */
    private double yearTotalBusCompletionRate;


    /**
     * 年度目标(货改车合计)
     */
    private double yearTotalTruckTarget = 0.0;

    /**
     * 年度开票(货改车合计)
     */
    private double yearTotalTruckInvoice = 0.0;

    /**
     * 年度差距(货改车合计)
     */
    private double yearTotalTruckDifference;

    /**
     * 年度完成率(货改车合计)
     */
    private double yearTotalTruckCompletionRate;


    /**
     * 月目标(合计)
     */
    private double monthTotalTarget = 0.0;

    /**
     * 月开票(合计)
     */
    private double monthTotalInvoice = 0.0;

    /**
     * 月差距(合计)
     */
    private double monthTotalDifference;

    /**
     * 月完成率(合计)
     */
    private double monthTotalCompletionRate = 0.0;
    /**
     * 月缺口(客改)
     */
    private double monthSurplus;


    /**
     * 月目标(货改)
     */
    private double monthTotalTruckTarget = 0.0;

    /**
     * 月开票(货改)
     */
    private double monthTotalTruckInvoice = 0.0;

    /**
     * 月差距(货改)
     */
    private double monthTotalTruckDifference;

    /**
     * 月完成率(货改)
     */
    private double monthTotalTruckCompletionRate;
    /**
     * 月缺口(货改)
     */
    private double monthTruckSurplus;


    /**
     * 月目标(客改)
     */
    private double monthTotalBusTarget = 0.0;

    /**
     * 月开票(客改)
     */
    private double monthTotalBusInvoice = 0.0;

    /**
     * 月差距(客改)
     */
    private double monthTotalBusDifference;

    /**
     * 月完成率(客改)
     */
    private double monthTotalBusCompletionRate;
    /**
     * 月缺口(客改)
     */
    private double monthBusSurplus;

    /**
     * 发车余额含授信
     */
    private double creditBalance = 0.0;

    /**
     * 分车条件
     */
    private String distribution;



    private int year;

    private int month;

}
