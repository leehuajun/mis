package com.sunjet.mis.module.warehouse.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 底盘采购计划
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_chassis_procurement")
public class ChassisProcurementEntity extends DocEntity {
    // 批次
    @ExcelColumn("批次号")
    @Column(name = "batch_number_", length = 100)
    private String batchNumber;
    // 车型
    @ExcelColumn("车型")
    @Column(name = "vehicle_model_", length = 100)
    private String vehicleModel;
    //整车物料号
    @ExcelColumn("VSN")
    @Column(name = "vsn_",length = 100)
    private String vsn;
    // 数量
    @ExcelColumn("数量")
    @Column(name = "amount_",length = 100)
    private Integer amount;
    // 特殊要求
    @ExcelColumn("特殊要求")
    @Column(name = "special_requirements_",length = 200)
    private String specialRequirements;
    // 品种
    @Column(name = "variety_", length = 20)
    private String variety;
    //销售需求
    @ExcelColumn("销售需求")
    @Column(name = "sales_demand_",length = 100)
    private String salesDemand;
    //配置
    @ExcelColumn("配置")
    @Column(name = "allocation_",length = 100)
    private String allocation;
    //year
    @Column(name = "year_")
    private Integer year;
    //month
    @Column(name = "month_")
    private Integer month;
}
