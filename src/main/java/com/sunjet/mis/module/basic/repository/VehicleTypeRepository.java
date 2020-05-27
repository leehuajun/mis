package com.sunjet.mis.module.basic.repository;

import com.sunjet.mis.module.basic.entity.VehicleTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VehicleTypeRepository extends JpaRepository<VehicleTypeEntity, String>, JpaSpecificationExecutor<VehicleTypeEntity> {
}
