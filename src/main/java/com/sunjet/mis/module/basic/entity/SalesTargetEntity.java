package com.sunjet.mis.module.basic.entity;

import com.sunjet.mis.base.entity.IdEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * 经销商销售目标
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "basic_sales_target")
public class SalesTargetEntity extends IdEntity {
    /**
     * 名称
     */
    @Column(name = "name_", length = 50, nullable = false)
    private String name;
    /**
     * 编码
     */
    @Column(name = "code_", length = 50, nullable = false)
    private String code;
    /**
     * SGMW的经销商编码
     */
    @Column(name = "sgmw_code_", length = 50, nullable = false)
    private String sgmwCode;

    /**
     * 等级
     */
    @Column(name = "level_", length = 50)
    private String level;
    /**
     * 法人
     */
    @Column(name = "linkman_", length = 100)
    private String linkman;
    /**
     * 移动电话
     */
    @Column(name = "mobile_", length = 100)
    private String mobile;
    /**
     * 固定电话
     */
    @Column(name = "phone_", length = 100)
    private String phone;
    /**
     * 邮箱
     */
    @Column(name = "email_", length = 100)
    private String email;
    /**
     * 详细地址
     */
    @Column(name = "address_", length = 500)
    private String address;
    /**
     * 省份
     */
    @ManyToOne
    @JoinColumn(name = "province_id_",referencedColumnName = "id_")
    private ProvinceEntity province;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesTargetEntity that = (SalesTargetEntity) o;
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return name;
    }
}
