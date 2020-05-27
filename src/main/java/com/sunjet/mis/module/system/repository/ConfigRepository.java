package com.sunjet.mis.module.system.repository;


import com.sunjet.mis.module.system.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * IconsRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface ConfigRepository extends JpaRepository<ConfigEntity, String>, JpaSpecificationExecutor<ConfigEntity> {


}
