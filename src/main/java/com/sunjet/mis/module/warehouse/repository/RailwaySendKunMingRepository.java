package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.ChongQingSendLiuZhouEntity;
import com.sunjet.mis.module.warehouse.entity.RailwaySendKunMingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RailwaySendKunMingRepository extends JpaRepository<RailwaySendKunMingEntity, String>, JpaSpecificationExecutor<RailwaySendKunMingEntity> {
    void deleteAllByVinIn(List<String> vins);

//    @Query("select ch from RailwaySendKunMingEntity ch WHERE ch.vehicleModel = ?1 AND ch.variety = ?2 AND ch.batchNumber =?3 AND DATE_FORMAT(ch.shipmentData, '%Y') =?4 AND DATE_FORMAT(ch.shipmentData, '%c') =?5 ")
//    List<RailwaySendKunMingEntity> findByDataSource(String vehicleModel, String variety, String batchNumber, String year, String month,String dataSource);

    @Query("select ch from RailwaySendKunMingEntity ch WHERE ch.vehicleModel = ?1 AND ch.variety = ?2 AND DATE_FORMAT(ch.shipmentData, '%Y') =?3 AND DATE_FORMAT(ch.shipmentData, '%c') =?4 ")
    List<RailwaySendKunMingEntity> findByVariety(String vehicleModel, String variety, String year, String month);


}
