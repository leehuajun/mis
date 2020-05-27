package com.sunjet.mis.module.basic.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import com.sunjet.mis.base.entity.IdEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author SUNJET-YFS
 * @Title: PassengerCompartmenProvenancCarSeriesEntity
 * @ProjectName mis
 * @Description: 客厢车 车系 产地 vsn 的匹配表
 * @date 2019/4/317:02
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "basic_passenger_compartmen_provenanc_car_series")
public class PassengerCompartmenProvenancCarSeriesEntity extends DocEntity {
    // ABS-VSN
    @ExcelColumn("ABS-VSN")
    @Column(name = "abs_vsn_", length = 50)
    private String absvsn;

    // 车系
    @ExcelColumn("产品类别")
    @Column(name = "product_category_", length = 50)
    private String  productCategory;

    // 车系
    @ExcelColumn("产品名称")
    @Column(name = "product_Name_", length = 50)
    private String productName;

    //基地
    @ExcelColumn("基地")
    @Column(name = "production_", length = 50)
    private String production;


}
