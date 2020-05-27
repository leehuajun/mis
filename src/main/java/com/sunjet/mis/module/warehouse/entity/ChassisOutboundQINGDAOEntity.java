package com.sunjet.mis.module.warehouse.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/***
 *  青岛出库
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_chassis_outbound_qingdao")
public class ChassisOutboundQINGDAOEntity extends DocEntity {
    // VIN
    @ExcelColumn("VIN")
    @Column(name = "vin_",length = 50)
    private String vin;
    // 底盘道位
    @ExcelColumn("底盘道位")
    @Column(name = "chassis_bit_",length = 100)
    private String chassisBit;
    // vsn
    @ExcelColumn("底盘VSN")
    @Column(name = "vsn_",length = 100)
    private String vsn;
    // 青岛车型标识
    @ExcelColumn("青岛车型标识")
    @Column(name = "model_logo_",length = 100)
    private String modelLogo;
    // 股份生产批号
    @ExcelColumn("生产批号")
    @Column(name = "shares_produce_number_", length = 50)
    private String sharesProduceNumber;
    //收货批次号
    @ExcelColumn("司机码")
    @Column(name = "driver_code_", length = 100)
    private String driverCode;
    //产品
    @ExcelColumn("产品")
    @Column(name = "product_", length = 100)
    private String product;
    //过站时间
    @ExcelColumn("过站时间")
    @Column(name = "standing_time_", length = 100)
    private Date standingTime;
    //修改次数
    @ExcelColumn("修改次数")
    @Column(name = "modify_number_", length = 100)
    private String modifyNumber;
    // 最后操作
    @ExcelColumn("最后操作时间")
    @Column(name = "last_operation_",length = 100)
    private String lastOperation;
    // 扫描员
    @ExcelColumn("扫描员")
    @Column(name = "scan_member_",length = 100)
    private String scanMember;
    // 车型
    @ExcelColumn("车型代码")
    @Column(name = "vehicle_model_",length = 100)
    private String vehicleModel;
    @ExcelColumn("品种")
    @Column(name = "variety_", length = 20)
    private String variety;
}
