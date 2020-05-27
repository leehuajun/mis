package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.AcceptanceDetailEntity;
import com.sunjet.mis.module.warehouse.entity.ChassisProcurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AcceptanceDetailRepository extends JpaRepository<AcceptanceDetailEntity, String>, JpaSpecificationExecutor<AcceptanceDetailEntity> {
    void deleteAllByVinIn(List<String> vins);

    @Query("select ch from AcceptanceDetailEntity ch WHERE ch.vin = ?1 ")
    AcceptanceDetailEntity findByVin(String vin);

    @Query("select ch from AcceptanceDetailEntity ch WHERE ch.vehicleModel = ?1 AND ch.variety = ?2 AND ch.batchNumber =?3 AND DATE_FORMAT(ch.shipmentData, '%Y') = ?4 AND DATE_FORMAT(ch.shipmentData, '%c') = ?5 ")
    List<AcceptanceDetailEntity> findByVehicleModel(String vehicleModel, String variety, String batchNumber, String year, String month);

    @Query("select ch from AcceptanceDetailEntity ch WHERE ch.vehicleModel = ?1 AND ch.variety = ?2 AND DATE_FORMAT(ch.shipmentData, '%Y') = ?3 AND DATE_FORMAT(ch.shipmentData, '%c') = ?4 ")
    List<AcceptanceDetailEntity> findByVariety(String vehicleModel, String variety , String year, String month);

}
