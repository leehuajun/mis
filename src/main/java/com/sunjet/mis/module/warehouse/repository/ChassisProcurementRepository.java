package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.ChassisProcurementEntity;
import com.sunjet.mis.module.warehouse.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChassisProcurementRepository extends JpaRepository<ChassisProcurementEntity, String>, JpaSpecificationExecutor<ChassisProcurementEntity> {
    @Query("select DISTINCT ch.vehicleModel,ch.variety,ch.allocation from ChassisProcurementEntity ch ")
    List<Object[]> findDistinct();
}
