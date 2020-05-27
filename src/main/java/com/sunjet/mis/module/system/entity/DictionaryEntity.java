package com.sunjet.mis.module.system.entity;

import com.sunjet.mis.base.entity.IdEntity;
import com.sunjet.mis.base.entity.IdSystemEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by lhj on 2015/9/6.
 * 数据字典实体
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_dictionary")
public class DictionaryEntity extends IdSystemEntity {
    private static final long serialVersionUID = -5719719404924042503L;
    /**
     * 字典名称
     */
    @Column(name = "name_", length = 100)
    private String name;
    /**
     * 备注
     */
    @Column(name = "comment_", length = 500)
    private String comment;

    @Builder.Default
    @OneToMany(mappedBy = "dictionary")
    private Set<DictionaryEntryEntity> dictionaryEntries = new HashSet<>();

}
