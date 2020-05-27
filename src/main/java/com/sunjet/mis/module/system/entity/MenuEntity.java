package com.sunjet.mis.module.system.entity;


import com.sunjet.mis.base.entity.DocEntity;
import com.sunjet.mis.base.entity.IdEntity;
import com.sunjet.mis.base.entity.IdSystemEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by lhj on 2015/9/6.
 * 菜单实体
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_menu")
public class MenuEntity extends IdSystemEntity {
    private static final long serialVersionUID = -929747504110218495L;
    /**
     * 菜单名称
     */
    @Column(name = "name_", length = 20)
    private String name;
    /**
     * 菜单URL
     */
    @Column(name = "url_", length = 200)
    private String url;   // menu 使用
    /**
     * 菜单图标
     */
    @Builder.Default
    @Column(name = "icon_", length = 50)
    private String icon = "z-icon-credit-card";
    /**
     * 菜单排序
     */
    @Column(name = "seq_")
    private Integer seq; // 排序

    /**
     * 菜单需要的权限对象
     */
    @ManyToOne
    @JoinColumn(name = "permission_id_",referencedColumnName = "id_")
    private PermissionEntity permission;
    /**
     * 父对象
     */
    @ManyToOne
    @JoinColumn(name = "parent_id_")
    private MenuEntity parent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuEntity that = (MenuEntity) o;
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
