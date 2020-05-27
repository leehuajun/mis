package com.sunjet.mis.module.basic.entity;

import com.sunjet.mis.annotation.ExcelColumn;
import com.sunjet.mis.base.entity.IdEntity;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

/**
 * 经销商实体对象
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "basic_distributor")
public class DistributorEntity extends IdEntity {
    /**
     * 名称
     */
    @ExcelColumn("经销商名称")
    @Column(name = "name_", length = 50, nullable = false)
    private String name;
    /**
     * 编码
     */
    @ExcelColumn("经销商编码")
    @Column(name = "code_", length = 50, nullable = false)
    private String code;
    /**
     * SGMW的经销商编码
     */
    @ExcelColumn("SGMW代码")
    @Column(name = "sgmw_code_", length = 50, nullable = false)
    private String sgmwCode;

    /**
     * 等级
     */
    @ExcelColumn("等级")
    @Column(name = "level_", length = 50)
    private String level;
    /**
     * 法人
     */
    @ExcelColumn("法人")
    @Column(name = "linkman_", length = 100)
    private String linkman;
    /**
     * 移动电话
     */
    @ExcelColumn("移动电话")
    @Column(name = "mobile_", length = 100)
    private String mobile;
    /**
     * 固定电话
     */
    @ExcelColumn("固定电话")
    @Column(name = "phone_", length = 100)
    private String phone;
    /**
     * 邮箱
     */
    @ExcelColumn("邮箱")
    @Column(name = "email_", length = 100)
    private String email;
    /**
     * 详细地址
     */
    @ExcelColumn("详细地址")
    @Column(name = "address_", length = 500)
    private String address;
    /**
     *省份
     */
    @ManyToOne
    @JoinColumn(name = "province_id_",referencedColumnName = "id_")
    private ProvinceEntity province;
    /**
     * 是否重点商家
     */
    @ExcelColumn("是否重点商家")
    @Column(name = "is_focus_merchants_",length = 20)
    private String isFocusMerchants;
    @ExcelColumn("省份")
    private String provinceName;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        DistributorEntity that = (DistributorEntity) o;
//        return this.getId().equals(that.getId());
//    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return name;
    }
}
