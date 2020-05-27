package com.sunjet.mis.module.system.repository;


import com.sunjet.mis.module.system.entity.DictionaryEntity;
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
public interface DictionaryRepository extends JpaRepository<DictionaryEntity, String>, JpaSpecificationExecutor<DictionaryEntity> {


}
