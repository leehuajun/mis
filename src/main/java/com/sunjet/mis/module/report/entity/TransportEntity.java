package com.sunjet.mis.module.report.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: wushi
 * @description: 在途报表
 * @Date: Created in 14:02 2019/3/14
 * @Modify by: wushi
 * @ModifyDate by: 14:02 2019/3/14
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_transport")
public class TransportEntity extends DocEntity {

    /**
     * 车辆名称
     */
    @ExcelColumn("车辆名称")
    @Column(name = "vehicle_name_", length = 100)
    private String vehicleName;

    /**
     * 经销商名称
     */
    @ExcelColumn("客户名称")
    @Column(name = "distributor_name_", length = 100)
    private String distributorName;

    /**
     * 经销商编码
     */
    @Column(name = "distributor_code_", length = 100)
    private String distributorCode;

    /**
     * vin
     */
    @ExcelColumn("VIN")
    @Column(name = "vin_", length = 100)
    private String vin;

}
