package com.sunjet.mis.module.report.view;

import lombok.*;

/**
 * @Author: wushi
 * @description: 经销商季度完成汇总表
 * @Date: Created in 14:55 2019/3/29
 * @Modify by: wushi
 * @ModifyDate by: 14:55 2019/3/29
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistributorQuarterlyCompletionView {

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
     * 奖励合计
     */
    private double rewardTotal = 0.0;

    /**
     * (货改车)指标合计
     */
    private double truckTargetTotal = 0.0;

    /**
     * (货改车)完成合计
     */
    private double truckCompleteTotal = 0.0;

    /**
     * (货改车)指标差距
     */
    private double truckTargetDifference = 0.0;

    /**
     * (货改车)完成率
     */
    private double truckCompletionRate = 0.0;


    /**
     * (货改车)奖励标准(元/辆)
     */
    private double truckRewardCriterion = 0.0;

    /**
     * (货改车)小计
     */
    private double truckSubtotal = 0.0;


    /**
     * (客改车)指标合计
     */
    private double busTargetTotal = 0.0;

    /**
     * (客改车)完成合计
     */
    private double busCompleteTotal = 0.0;

    /**
     * (客改车)指标差距
     */
    private double busTargetDifference = 0.0;

    /**
     * (客改车)完成率
     */
    private double busCompletionRate = 0.0;


    /**
     * (客改车)奖励标准(元/辆)
     */
    private double busRewardCriterion = 0.0;

    /**
     * (客改车)小计
     */
    private double busSubtotal = 0.0;

    /**
     * 第一个月指标(货改车)
     */
    private double firstMonthTruckTarget;

    /**
     * 第一个月实际完成(货改车)
     */
    private double firstMonthTruckComplete;

    /**
     * 第一个月指标(客改车)
     */
    private double firstMonthBusTarget;

    /**
     * 第一个月实际完成(客改车)
     */
    private double firstMonthBusComplete;
    /**
     * 第二个月指标(货改车)
     */
    private double secondMonthTruckTarget;

    /**
     * 第二个月实际完成(货改车)
     */
    private double secondMonthTruckComplete;

    /**
     * 第二个月指标(客改车)
     */
    private double secondMonthBusTarget;

    /**
     * 第二个月实际完成(客改车)
     */
    private double secondMonthBusComplete;


    /**
     * 第三个月指标(货改车)
     */
    private double thirdMonthTruckTarget;

    /**
     * 第三个月实际完成(货改车)
     */
    private double thirdMonthTruckComplete;


    /**
     * 第三个月指标(客改车)
     */
    private double thirdMonthBusTarget;

    /**
     * 第三个月实际完成(客改车)
     */
    private double thirdMonthBusComplete;





    private int year;

    private int quarterStartMonth;

    private int quarterEndMonth;

    private String quarter;


}
