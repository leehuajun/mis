package com.sunjet.mis.module.system.entity;


import com.sunjet.mis.base.entity.IdSystemEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;


/**
 * Created by lhj on 2015/9/6.
 * 系统配置信息实体
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_config")
public class ConfigEntity extends IdSystemEntity {
    private static final long serialVersionUID = 7374987575666673359L;
    /**
     * 配置项Key
     */
    @Column(name = "key_", length = 50)
    private String key;
    /**
     * 配置项Value
     */
    @Column(name = "value_", length = 200)
    private String value;
    /**
     * 配置项默认Value
     */
    @Column(name = "initial_value_", length = 200)
    private String initialValue;
    /**
     * 配置项备注
     */
    @Column(name = "comment_", length = 200)
    private String comment;

    /**
     * 所属组织
     */
    @ManyToOne
    @JoinColumn(name = "org_id_",referencedColumnName = "id_")
    private OrgEntity org;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigEntity that = (ConfigEntity) o;
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return key;
    }
}
