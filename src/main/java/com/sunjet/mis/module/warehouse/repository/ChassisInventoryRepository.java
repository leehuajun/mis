package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.ChassisInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChassisInventoryRepository extends JpaRepository<ChassisInventoryEntity, String>, JpaSpecificationExecutor<ChassisInventoryEntity> {
    void deleteAllByVinIn(List<String> vins);

    @Query("select ch from ChassisInventoryEntity ch WHERE ch.vehicleModel = ?1 AND ch.variety = ?2 AND ch.batchNumber =?3 AND DATE_FORMAT(ch.shipmentData, '%Y') =?4 AND DATE_FORMAT(ch.shipmentData, '%c') =?5 ")
    List<ChassisInventoryEntity> findByVehicleModel(String vehicleModel, String variety, String batchNumber, String year, String month);

    @Query("select ch from ChassisInventoryEntity ch WHERE ch.vehicleModel = ?1 AND ch.variety = ?2 AND  DATE_FORMAT(ch.shipmentData, '%Y') =?3 AND DATE_FORMAT(ch.shipmentData, '%c') =?4 ")
    List<ChassisInventoryEntity> findByVariety(String vehicleModel, String variety,  String year, String month);


    @Query("select ch from ChassisInventoryEntity ch WHERE ch.vehicleModel = ?1 AND ch.variety = ?2 AND ch.batchNumber =?3 AND DATE_FORMAT(ch.shipmentData, '%Y') =?4 AND DATE_FORMAT(ch.shipmentData, '%c') =?5 AND ch.dataSource like  ?6 ")
    List<ChassisInventoryEntity> findByDataSource(String vehicleModel, String variety, String batchNumber, String year, String month,String dataSource);

    @Query("select ch from ChassisInventoryEntity ch WHERE ch.vehicleModel = ?1 AND ch.variety = ?2  AND DATE_FORMAT(ch.shipmentData, '%Y') =?3 AND DATE_FORMAT(ch.shipmentData, '%c') =?4 AND ch.dataSource like  ?5 ")
    List<ChassisInventoryEntity> findByDataSourceAndVariety(String vehicleModel, String variety, String year, String month,String dataSource);

    @Query("select ch from ChassisInventoryEntity ch WHERE  ch.dataSource like ?1 ")
    List<ChassisInventoryEntity> findDataSource(String dataSource);

}
