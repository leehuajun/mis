package com.sunjet.mis.module.system.entity;

import com.sunjet.mis.base.entity.IdSystemEntity;
import lombok.*;
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
@Table(name = "sys_dictionary_entry")
public class DictionaryEntryEntity extends IdSystemEntity {
    private static final long serialVersionUID = -8097900066776826746L;
    /**
     * 字典的键
     */
    @Column(name = "key_", length = 100)
    private String key;
    /**
     * 字典的值
     */
    @Column(name = "value_", length = 200)
    private String value;
    @ManyToOne
    @JoinColumn(name = "dictionary_id_",referencedColumnName = "id_")
    private DictionaryEntity dictionary;

}
