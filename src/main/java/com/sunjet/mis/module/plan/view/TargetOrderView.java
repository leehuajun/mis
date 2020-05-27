package com.sunjet.mis.module.plan.view;

import com.sunjet.mis.module.plan.entity.TargetOrderEntity;
import lombok.*;


import javax.persistence.Transient;
import java.util.Date;

/**
 * @Author: wushi
 * @description: 目标管理
 * @Date: Created in 13:43 2019/3/15
 * @Modify by: wushi
 * @ModifyDate by: 13:43 2019/3/15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TargetOrderView {


    private String id;


    /**
     * 地区
     */
    private String region;

    /**
     * 省份
     */
    private String provinceName;

    // 经销商名称
    private String distributorName;

    // 经销商编码(SGMW的编码)
    private String sgmwCode;
    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 一月
     */
    private double january;

    /**
     * 二月
     */
    private double february;

    /**
     * 三月
     */
    private double march;

    /**
     * 四月
     */
    private double april;

    /**
     * 五月
     */
    private double may;
    /**
     * 六月
     */
    private double june;
    /**
     * 七月
     */
    private double july;
    /**
     * 八月
     */
    private double august;

    /**
     * 九月
     */
    private double september;
    /**
     * 十月
     */
    private double october;

    /**
     * 十一月
     */
    private double november;
    /**
     * 十二月
     */
    private double december;

    /**
     * 年目标
     */
    private double yearTarget;

    @Transient
    private  int nian;

    private  Date year ;

    @Transient
    private TargetOrderEntity targetOrderEntity;


}
