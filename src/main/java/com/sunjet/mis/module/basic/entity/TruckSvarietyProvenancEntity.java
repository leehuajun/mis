package com.sunjet.mis.module.basic.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author SUNJET-YFS
 * @Title: TruckSvarietyProvenancEntity
 * @ProjectName mis
 * @Description: 货改车 ABS-VSN 产品名称，产品类别，产地的匹配表
 * @date 2019/4/315:16
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "basic_truck_svariety_provenanc")
public class TruckSvarietyProvenancEntity extends DocEntity {

    // ABS-VSN
    @ExcelColumn("ABS-VSN")
    @Column(name = "abs_vsn_", length = 50)
    private String absvsn;

    // 产地
    @ExcelColumn("产地")
    @Column(name = "production_", length = 50)
    private String production;

    //产品名称
    @ExcelColumn("产品名称")
    @Column(name = "product_Name_", length = 50)
    private String productName;

    // 产品类别
    @ExcelColumn("产品类别")
    @Column(name = "product_category_", length = 50)
    private String productCategory;

}































