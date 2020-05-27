package com.sunjet.mis.module.system.entity;


import com.sunjet.mis.base.entity.DocEntity;
import com.sunjet.mis.base.entity.IdEntity;
import com.sunjet.mis.base.entity.IdSystemEntity;
import lombok.*;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.*;

/**
 * Created by lhj on 2015/9/6.
 * 操作实体
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_operation")
public class OperationEntity extends IdSystemEntity {
    private static final long serialVersionUID = 9150516330799632348L;
    /**
     * 操作编码
     */
    @Column(name = "code_", length = 20, unique = true)
    private String code; //  操作编码
    /**
     * 操作名称
     */
    @Column(name = "name_", length = 20)
    private String name; //  操作名称
    /**
     * 序号
     */
    @Column(name = "seq_")
    private Integer seq; // 序号

    /**
     * 使用该操作的资源列表
     */
//    @Builder.Default
//    @ManyToMany(mappedBy = "operations")
//    private Set<ResourceEntity> resources = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationEntity that = (OperationEntity) o;
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
