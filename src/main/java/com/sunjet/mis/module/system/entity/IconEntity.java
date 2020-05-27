package com.sunjet.mis.module.system.entity;

import com.sunjet.mis.base.entity.DocEntity;
import com.sunjet.mis.base.entity.IdEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * Created by lhj on 2015/9/6.
 * 图标实体
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_icon")
public class IconEntity extends IdEntity {
    private static final long serialVersionUID = -6056297580918896377L;
    /**
     * 图标名称
     */
    @Column(name = "name_", length = 50)
    private String name;
    /**
     * 图标类型
     */
    @Column(name = "category_", length = 50)
    private String category;

}
