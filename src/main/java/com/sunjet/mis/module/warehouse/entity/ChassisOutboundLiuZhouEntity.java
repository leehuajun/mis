package com.sunjet.mis.module.warehouse.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/***
 *  柳州出库
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_chassis_outbound_liuzhou")
public class ChassisOutboundLiuZhouEntity extends DocEntity {
    // VIN
    @ExcelColumn("VIN")
    @Column(name = "vin_",length = 50)
    private String vin;
    // 班次
    @ExcelColumn("班次")
    @Column(name = "shift_",length = 100)
    private String shift;
    // 产品名称
    @ExcelColumn("产品名称")
    @Column(name = "product_name_",length = 100)
    private String productName;
    // 扫描点代码
    @ExcelColumn("扫描点代码")
    @Column(name = "scan_code_",length = 100)
    private String scanCode;
    /// 扫描点名称
    @ExcelColumn("扫描点名称")
    @Column(name = "scan_name_",length = 100)
    private String scanName;
    //过站时间
    @ExcelColumn("过站时间")
    @Column(name = "standing_time_", length = 100)
    private Date standingTime;
    // 扫描人员
    @ExcelColumn("扫描人员")
    @Column(name = "scan_member_",length = 100)
    private String scanMember;
    // 备注
    @ExcelColumn("备注")
    @Column(name = "note_",length = 100)
    private String note;

    @Column(name = "vehicle_model_",length = 100)
    private String vehicleModel;
    @Column(name = "variety_", length = 20)
    private String variety;
    @Column(name = "batch_number_", length = 100)
    private String batchNumber;

}
