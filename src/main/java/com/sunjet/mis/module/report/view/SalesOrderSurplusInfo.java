package com.sunjet.mis.module.report.view;

import lombok.*;

import java.util.Date;

/**
 * 销售订单需求数
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesOrderSurplusInfo {

    /**
     * 客户编码
     */
    private String customer_code_;

    /**
     * 经销商名称
     */
    private String distributor_name_;

    /**
     * 客车需求数
     */
    private double bus_surplus_num_;

    /**
     * 货车需求数
     */
    private double truck_surplus_num_;

    /**
     * 创建时间
     */
    private Date created_time_;


}
