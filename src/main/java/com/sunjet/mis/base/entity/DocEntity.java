package com.sunjet.mis.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author lhj
 * @create 2015-12-09 下午1:14
 * 业务基础实体
 */
@Getter
@Setter
@MappedSuperclass
public class DocEntity extends IdEntity {

    @Column(name = "creater_id_", length = 32)
    private String createrId;

    @Column(name = "creater_name_", length = 50)
    private String createrName;

    @Column(name = "modifier_id_", length = 32)
    private String modifierId;

    @Column(name = "modifier_name_", length = 50)
    private String modifierName;
    /**
     * 日期属性设置
     * DATE: 日期，2001-04-08
     * TIME: 时间：11:54:23
     * TIMESTAMP: 时间戳：2001-04-08 11:54:23
     *
     * @return
     */
    @Column(name = "created_time_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime = new Date();

    @Column(name = "modified_time_")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime = new Date();

    @PrePersist
    public void prePersist() {
        this.setCreatedTime(new Date());
        this.setModifiedTime(new Date());
    }

    @PreUpdate
    public void preUpdate() {
        this.setModifiedTime(new Date());
    }
}
