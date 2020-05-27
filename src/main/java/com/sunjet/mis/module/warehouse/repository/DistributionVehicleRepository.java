package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.ChassisProcurementEntity;
import com.sunjet.mis.module.warehouse.entity.DistributionVehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistributionVehicleRepository extends JpaRepository<DistributionVehicleEntity, String>, JpaSpecificationExecutor<DistributionVehicleEntity> {
    @Query("select dv from DistributionVehicleEntity dv WHERE dv.vehicleModel = ?1 AND dv.variety = ?2 AND dv.batchNumber =?3 AND dv.pointsYear =?4 AND dv.pointsMonth =?5 ")
    List<DistributionVehicleEntity> findByVehicleModel(String vehicleModel, String variety, String batchNumber, Integer year, Integer month);

    @Query("select dv from DistributionVehicleEntity dv WHERE dv.vehicleModel = ?1 AND dv.variety = ?2  AND dv.pointsYear =?3 AND dv.pointsMonth =?4 ")
    List<DistributionVehicleEntity> findByVariety(String vehicleModel, String variety,  Integer year, Integer month);

}
