package com.sunjet.mis.module.system.repository;


import com.sunjet.mis.module.system.entity.DictionaryEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * IconsRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface DictionaryEntryRepository extends JpaRepository<DictionaryEntryEntity, String>, JpaSpecificationExecutor<DictionaryEntryEntity> {

    @Query("select dic  from DictionaryEntryEntity dic where dic.dictionary.id =?1")
    List<DictionaryEntryEntity> findAllByDictionaryEntry(String id);

}
