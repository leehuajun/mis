package com.sunjet.mis.module.basic.repository;

import com.sunjet.mis.module.basic.entity.ProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProvinceRepository extends JpaRepository<ProvinceEntity,String>, JpaSpecificationExecutor<ProvinceEntity> {
    ProvinceEntity findByName(String name);
}
