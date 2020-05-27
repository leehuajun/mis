package com.sunjet.mis.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lhj
 * @create 2015-12-09 下午1:14
 * 主键实体
 */
@Getter
@Setter
@MappedSuperclass
public class IdSystemEntity extends IdEntity {

    @Column(name = "enabled_")
    private Boolean enabled = true;

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
