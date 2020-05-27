package com.sunjet.mis.module.basic.repository;

import com.sunjet.mis.module.basic.entity.VehicleModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VehicleModelRepository extends JpaRepository<VehicleModelEntity,String>, JpaSpecificationExecutor<VehicleModelEntity> {
}
