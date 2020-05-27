package com.sunjet.mis.module.report.view;

import com.sunjet.mis.annotation.ExcelColumn;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Immutable
//@Subselect("SELECT DISTINCT\n" +
//        "\t\t\trsp.id_,\n" +
//        "\t\t\trsp.distributor_code_,\n" +
//        "\t\t\trsp.vin_,\n" +
//        "\t\t\trsp.customer_name_,\n" +
//        "\t\t\trsp.address_province_ AS customer_provinces_,\n" +
//        "\t\t\trsp.vehicle_model_ AS vehicle_type_,\n" +
//        "\t\t\trsp.engine_code_,\n" +
//        "\t\t\trsp.vsn_,\n" +
//        "\t\t\trsp.color_,\n" +
//        "\t\t\tbvm.vehicle_series_,\n" +
//        "\t\t\trsp.invoice_time_,\n" +
//        "\t\t\trsp.model_,\n" +
//        "\t\t\tbvm.name_ AS vehicle_model_,\n" +
//        "\t\t\tbr.name_ AS region_name_\n" +
//        "\t\tFROM\n" +
//        "\t\t\trpt_solid_pin rsp\n" +
//        "\t\tLEFT JOIN basic_distributor bd ON rsp.distributor_code_ = bd.sgmw_code_\n" +
//        "\t\tLEFT JOIN basic_vehicle_model bvm ON rsp.vehicle_model_ = bvm.code_\n" +
//        "\t\tINNER JOIN basic_provice bp ON bp.id_ = bd.province_id_\n" +
//        "\t\tINNER JOIN basic_region br ON br.id_ = bp.region_id_")

@Subselect("SELECT\n" +
        "\trsp.id_,\n" +
        "\tgz.distributor_sgmw_code_ AS distributor_code_,\n" +
        "\tkc.vin_,\n" +
        "\trt.customer_name_,\n" +
        "\trt.customer_provinces_,\n" +
        "\trt.vehicle_type_,\n" +
        "\tkc.engine_code_,\n" +
        "\trt.vsn_,\n" +
        "\trsp.color_,\n" +
        "\tkc.vehicle_series_,\n" +
        "\trt.invoice_time_,\n" +
        "\trsp.model_,\n" +
        "\tbvm.name_ AS vehicle_model_,\n" +
        "\tbr.name_ AS region_name_\n" +
        "FROM\n" +
        "\trpt_inventory kc\n" +
        "INNER JOIN rpt_solid_pin rsp ON rsp.vin_ = kc.vin_\n" +
        "INNER JOIN rpt_ticket rt ON rt.vin_ = kc.vin_\n" +
        "INNER JOIN rpt_vehicle_inv gz ON kc.vin_ = gz.vin_\n" +
        "INNER JOIN basic_vehicle_model bvm ON gz.vehicle_model_ = bvm.code_\n" +
        "INNER JOIN basic_distributor bd ON gz.distributor_sgmw_code_ = bd.sgmw_code_\n" +
        "INNER JOIN basic_province bp ON bp.name_ = rt.customer_provinces_\n" +
        "INNER JOIN basic_region br ON br.id_ = bp.region_id_\n" +
        "WHERE\n" +
        "\tgz.status_ = '已配车'")

 /**
 * 社会库存
 * */
public class SocialInventoryView {
    @Id
    @Column(name = "id_")
    private String id;
    //经销商号
    @Column(name = "distributor_code_")
    private String distributorCode;
    //vin
    @Column(name = "vin_")
    private String vin;
    //客户名称
    @Column(name = "customer_name_")
    private String customerName;
   //客户省份
    @Column(name = "customer_provinces_")
    private String customerProvinces;
    //车辆型号
    @Column(name = "vehicle_type_")
    private String vehicleType;
    //发动机号
    @Column(name = "engine_code_")
    private String engineCode;
    //vsn
    @Column(name = "vsn_")
    private String vsn;
    //颜色
    @Column(name = "color_")
    private String color;
    //车系
    @Column(name = "vehicle_series_")
    private String vehicleSeries;
    //单据日期
    @Column(name = "invoice_time_")
    private Date invoiceTime;
    //客查库存过程,型号
    @Column(name = "model_")
    private String model;
    //车型
    @Column(name = "vehicle_model_")
    private String vehicleModel;
    //所属区域
    @Column(name = "region_name_")
    private String regionName;


}
