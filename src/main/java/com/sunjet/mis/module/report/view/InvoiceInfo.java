package com.sunjet.mis.module.report.view;

import lombok.*;

import java.util.Date;

/**
 * @Author: wushi
 * @description: 开票统计
 * @Date: Created in 9:42 2019/2/22
 * @Modify by: wushi
 * @ModifyDate by: 9:42 2019/2/22
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceInfo {


    /**
     * 经销商编码
     */
    private String distributor_sgmw_code_;

    /**
     * 经销商名称
     */
    private String distributor_name_;

    /**
     * 客车
     */
    private double bus_invoice_ = 0.0;

    /**
     * 货车
     */
    private double truck_invoice_ = 0.0;


    /**
     * 开票统计
     */
    private double total_invoice_ = 0.0;
    /**
     * 开票时间
     */
    private Date invoice_date_;

}
