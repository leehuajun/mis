package com.sunjet.mis.base.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author lhj
 * @create 2015-12-09 下午1:14
 * 主键实体
 */
@Getter
@Setter
@MappedSuperclass
public class IdEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
    @Column(name = "id_", length = 32,nullable = false)
    private String id;
    /**
     * 添加jpa乐观锁控制
     */
    @Version
    @Column(name = "version_")
    private Integer version = 0;
}
