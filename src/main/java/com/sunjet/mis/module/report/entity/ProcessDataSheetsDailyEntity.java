package com.sunjet.mis.module.report.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;
import javax.persistence.*;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author SUNJET-YFS
 * @Title: ProcessDataSheetsDaily
 * @ProjectName mis
 * @Description: 每日数据处理
 * @date 2019/2/2611:52
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rpt_process_data_sheets_daily")
public class ProcessDataSheetsDailyEntity extends DocEntity {

    // VIN
    @ExcelColumn("VIN")
    @Column(name = "vin_", length = 200)
    private String vin;

    //单据日期
    @ExcelColumn("单据日期")
    @Temporal(TemporalType.DATE)
    @Column(name = "bill_date_")
    private Date billDate;

    // 车辆型号	model
    @ExcelColumn("车辆型号")
    @Column(name = "model_", length = 200)
    private String model;

    // VSN
    @ExcelColumn("VSN")
    @Column(name = "vsn_", length = 200)
    private String vsn;

    // 这里客户名称就是经销商名称
    @ExcelColumn("客户名称")
    @Column(name = "customer_name_", length = 200)
    private String customerName;

    //这里客户省份就是经销商省份
    @ExcelColumn("客户省份")
    @Column(name = "customer_province_", length = 100)
    private String customerProvince;

    //客户送货地址 Customer delivery address
    @ExcelColumn("客户送货地址")
    @Column(name = "customer_delivery_address_", length = 200)
    private String customerDeliveryAddress;

    // 客户联系人
    @ExcelColumn("客户联系人")
    @Column(name = "contacts_", length = 50)
    private String contacts;

    // 联系方式
    @ExcelColumn("联系方式")
    @Column(name = "customer_way_", length = 100)
    private String customerWay;

    // 库位
    @ExcelColumn("库位")
    @Column(name = "storage_location_", length = 100)
    private String storageLocation;
}
