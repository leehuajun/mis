package com.sunjet.mis.module.plan.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: wushi
 * @description: 目标管理
 * @Date: Created in 10:54 2019/3/5
 * @Modify by: wushi
 * @ModifyDate by: 10:54 2019/3/5
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_target_order")
public class TargetOrderEntity extends DocEntity {

    // 经销商名称
    @ExcelColumn("经销商名称")
    @Column(name = "distributor_name_", length = 100)
    private String distributorName;

    // 经销商编码(SGMW的编码)
    @ExcelColumn("经销商代码")
    @Column(name = "distributor_sgmw_code_", length = 50)
    private String sgmwCode;

    /**
     * 省份
     */
    @Column(name = "province_name_", length = 50)
    private String provinceName;

    /**
     * 地区
     */
    @Column(name = "region_", length = 50)
    private String region;

    @ExcelColumn("车型")
    @Column(name = "vehicle_Type_", length = 60)
    private String vehicleType;


    @ExcelColumn("指标")
    @Column(name = "target_")
    private String target;

    //@ExcelColumn("1月")
    //@Column(name = "january_")
    //private double january;
    //
    //@ExcelColumn("2月")
    //@Column(name = "february_")
    //private double february;
    //
    //@ExcelColumn("3月")
    //@Column(name = "march_")
    //private double march;
    //
    //@ExcelColumn("4月")
    //@Column(name = "april_")
    //private double april;
    //
    //@ExcelColumn("5月")
    //@Column(name = "may_")
    //private double may;
    //
    //@ExcelColumn("6月")
    //@Column(name = "june_")
    //private double june;
    //
    //@ExcelColumn("7月")
    //@Column(name = "july_")
    //private double july;
    //
    //@ExcelColumn("8月")
    //@Column(name = "august_")
    //private double august;
    //
    //@ExcelColumn("9月")
    //@Column(name = "september_")
    //private double september;
    //
    //@ExcelColumn("10月")
    //@Column(name = "october_")
    //private double october;
    //
    //@ExcelColumn("11月")
    //@Column(name = "november_")
    //private double november;
    //
    //@ExcelColumn("12月")
    //@Column(name = "december_")
    //private double december;

    /**
     * 年目标
     */
    //@Column(name = "target_")
    //private double yearTarget;


}
