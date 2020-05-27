package com.sunjet.mis.module.plan.entity;

import com.sunjet.mis.base.entity.DocEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 经销商销售目标实体对象
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan_sales_plan")
public class SalesPlanEntity extends DocEntity {
    @Column(name = "no_",length = 50)
    private String no;
}
