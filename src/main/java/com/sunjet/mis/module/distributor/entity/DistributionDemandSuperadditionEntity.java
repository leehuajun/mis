package com.sunjet.mis.module.distributor.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.DocEntity;
import com.sunjet.mis.helper.DateHelper;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


/**
 * @author SUNJET-YFS
 * @Title: DistributionDemandSuperadditionEntity
 * @ProjectName mis
 * @Description: 经销商需求追加表
 * @date 2019/3/1514:43
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="distribution_demand_superaddition")
public class DistributionDemandSuperadditionEntity extends DocEntity {

    @ExcelColumn("ID")
    @Column(name = "tid_", length = 100)
    private String tid;

    @ExcelColumn("年度")
    @Column(name = "year_", length = 100)
    private Integer year;

    @ExcelColumn("月份")
    @Column(name = "month_", length = 100)
    private Integer month;

    @ExcelColumn("周次")
    @Column(name = "cycle_", length = 100)
    private Integer cycle;

    @ExcelColumn("经销商代码")
    @Column(name = "distributor_code_", length = 100)
    private String distributorCode;

    @ExcelColumn("物料号")
    @Column(name = "vsn_", length = 100)
    private String vsn;

    @ExcelColumn("车型")
    @Column(name = "vehicle_model_", length = 100)
    private String vehicleModel;

    @ExcelColumn("颜色")
    @Column(name = "color_", length = 100)
    private String color;

    @ExcelColumn("类型")
    @Column(name = "type_", length = 100)
    private String type;

    @ExcelColumn("物料品种")
    @Column(name = "material_varieties_", length = 100)
    private String materialVarieties;

    @ExcelColumn("排放标准")
    @Column(name = "emission_standard_", length = 100)
    private String emissionStandard;

    @ExcelColumn("申报数")
    @Column(name = "declare_number_", length = 100)
    private int declareNumber;

    @ExcelColumn("满足数")
    @Column(name = "satisfy_number_", length = 100)
    private Integer satisfyNumber;

    @ExcelColumn("售达方")
    @Column(name = "sold_to_party_", length = 100)
    private String soldToParty;

    @ExcelColumn("送达方")
    @Column(name = "ship_to_party_", length = 100)
    private String shipToParty;

    @ExcelColumn("调整发起方")
    @Column(name = "adjustment_", length = 100)
    private String adjustment;

    @ExcelColumn("申报原因")
    @Column(name = "declare_reasons_", length = 100)
    private String declareReasons;

    @ExcelColumn("申报备注")
    @Column(name = "declare_note_", length = 200)
    private String declareNote;

    @ExcelColumn("平衡原因")
    @Column(name = "balance_reason_", length = 100)
    private String balanceReason;

    @ExcelColumn("平衡备注")
    @Column(name = "balance_note_", length = 100)
    private String balanceNote;

    @ExcelColumn("是否满足")
    @Column(name = "whether_satisfy_", length = 100)
    private String whetherSatisfy;

    @ExcelColumn("状态")
    @Column(name = "status_", length = 100)
    private String status;

    @ExcelColumn("平衡人")
    @Column(name = "balance_people_", length = 100)
    private String balancePeople;

    @ExcelColumn("平衡时间")
    @Column(name = "equilibrium_time_", length = 100)
    private String equilibriumTime;

    @ExcelColumn("拆分后调整")
    @Column(name = "split_adjustment_", length = 100)
    private String splitAdjustment;

    @ExcelColumn("申请时间")
    //@Temporal(TemporalType.DATE)
    @Column(name = "application_time_", length = 100)
    private Date applicationTime;

    @Transient
    private Date startDate= DateHelper.getFirstOfYear();

    @Transient
    private Date endDate = DateHelper.getEndDateTime();           // 结束日期

}
