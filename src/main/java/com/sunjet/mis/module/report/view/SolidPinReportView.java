package com.sunjet.mis.module.report.view;

import com.sunjet.mis.helper.DateHelper;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.util.Date;

/**
 * @author SUNJET-YFS
 * @Title: SolidPinView
 * @ProjectName mis
 * @Description: 35实销整合报表bp.name_
 * @date 2019/2/189:49
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
@Subselect("SELECT DISTINCT  \n" +
        "        	rsp.id_,\n" +
        "        	rsp.vin_,#客户信息卡的vin\n" +
        "        	rsp.sale_Date_,#客户信息卡的开票日期\n" +
        "			rsp.invoice_Date_,\n" +
        "        	rsp.distributor_name_,#客户信息卡的经销商\n" +
        "        	rt.invoice_time_,#开票信息的单据日期\n" +
        "        	rt.customer_name_,#开票信息的一级经销商（客户商）\n" +
        "        	rt.vsn_,#开票信息的vsn\n" +
        "        	rt.customer_provinces_ AS customer_province_,#开票信息客户省份（一级代理商省份）\n" +
        "        	rt.vehicle_type_,#车型型号\n" +
        "        	rsp.distributor_code_,#经销商编号\n" +
        "        	rsp.onecode,#一级代码\n" +
        "        	bvm.name_,#车型\n" +
        "        	br.name_ AS region_name_ \n" +
        "        FROM\n" +
        "        	rpt_solid_pin rsp #客户信息卡\n" +
        "        	LEFT JOIN basic_distributor bd ON rsp.distributor_code_ = bd.sgmw_code_ #经销商实体对象\n" +
        "        	INNER JOIN basic_province bp ON bp.id_ = bd.province_id_\n" +
        "        	INNER JOIN basic_region br ON br.id_ = bp.region_id_ #通过省份查询区域\n" +
        "        	INNER JOIN rpt_ticket rt ON rsp.vin_ = rt.vin_ #开票信息\n" +
        "        	LEFT JOIN basic_vehicle_model bvm ON bvm.code_ = rt.vehicle_type_ #开票信息表 \n" +
        "			WHERE rsp.invoice_Date_")

//开票信息表   rpt_ticket
//区域实体对象 basic_region
//省份实体对象 basic_province
//车型实体对象 basic_vehicle_model
//经销商实体对象 basic_distributor
public class SolidPinReportView {

    @Id
    @Column(name = "id_")
    private String id;

    //车架号  35天实销原始导入表
    @Column(name = "vin_", length = 50)
    private String vin;

    // 开票日期
    @Column(name = "sale_Date_")
    private String saleDate;

    //经销商名称  rpt_solid_pin rsp
    @Column(name = "distributor_name_", length = 100)
    private String distributorName;


    //单据日期 rpt_process_data_sheets_daily
    @Column(name = "invoice_time_")
    private Date billDate;

    //客户名称一级经销商  开票信息
    @Column(name = "customer_name_", length = 100)
    private String customerName;

    //VSN   开票信息
    @Column(name = "vsn_", length = 100)
    private String vsn;

    //客户省份
    @Column(name = "customer_province_", length = 100)
    private String customerProvinces;

    //车辆型号	model  开票信息
    @Column(name = "vehicle_type_", length = 100)
    private String model;

    //经销商编号
    @Column(name = "distributor_code_", length = 50)
    private String distributorCode;

    //一级代码
    @Column(name = "onecode", length = 50)
    private String onecode;

    //车型名称  类型清单表 （具体在哪个表）
    @Column(name = "name_", length = 100)
    private String name;


    //所属区域  B按代码区域经销商实销 缺少字段（具体在哪个表）
    @Column(name = "region_name_")
    private String regionName;


    //  @ExcelColumn("销售日期")
    // 客户信息卡的开票日期通过转化成时间格式
    @Temporal(TemporalType.DATE)
    @Column(name = "invoice_Date_", length = 100)
    private Date invoiceDate;

    @Transient
    private Date startDate = DateHelper.getFirstOfYear(); // 开始日期

    @Transient
    private Date endDate   = DateHelper.getEndDateTime();
}
