package com.sunjet.mis.module.report.view;

import lombok.*;

import java.util.Date;

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
public class TargetInfo {


    /**
     * 经销商编码
     */
    private String distributor_sgmw_code_;

    /**
     * 经销商名称
     */
    private String distributor_name_;

    /**
     * 目标(合计)
     */
    private double target_;

    /**
     * 创建时间
     */
    private Date created_time_;


}
