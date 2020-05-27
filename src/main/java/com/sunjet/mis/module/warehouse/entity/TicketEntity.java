package com.sunjet.mis.module.warehouse.entity;


import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 开票信息表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_ticket")
public class TicketEntity extends DocEntity {
    // VIN
    @ExcelColumn("VIN")
    @Column(name = "vin_",length = 50)
    private String vin;
    // 单据日期
    @ExcelColumn("单据日期")
    @Column(name = "invoice_time_",length = 100)
    private Date invoiceTime;
    //车辆型号
    @ExcelColumn("车辆型号")
    @Column(name = "vehicle_type_", length = 100)
    private String vehicleType;
    // vsn
    @ExcelColumn("VSN")
    @Column(name = "vsn_",length = 100)
    private String vsn;
    // 客户名称
    @ExcelColumn("客户名称")
    @Column(name = "customer_name_",length = 100)
    private String customerName;
    // 客户省份
    @ExcelColumn("客户省份")
    @Column(name = "customer_provinces_", length = 50)
    private String customerProvinces;
    //客户送货地址
    @ExcelColumn("客户送货地址")
    @Column(name = "customer_address_", length = 100)
    private String customerAddress;
    //客户联系人
    @ExcelColumn("客户联系人")
    @Column(name = "customer_contact_", length = 100)
    private String customerContact;
    //联系方式
    @ExcelColumn("联系方式")
    @Column(name = "customer_phone_", length = 100)
    private String customerPhone;
    //库位
    @ExcelColumn("库位")
    @Column(name = "warehouse_address_", length = 100)
    private String warehouseAddress;

}
