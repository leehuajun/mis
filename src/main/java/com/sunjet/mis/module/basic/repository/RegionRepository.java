package com.sunjet.mis.module.basic.repository;

import com.sunjet.mis.module.basic.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RegionRepository extends JpaRepository<RegionEntity,String>, JpaSpecificationExecutor<RegionEntity> {
}
