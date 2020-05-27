package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.ChongQingSendLiuZhouEntity;
import com.sunjet.mis.module.warehouse.entity.RailwaySendLIUZHOUEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChongQingSendLiuZhouRepository extends JpaRepository<ChongQingSendLiuZhouEntity, String>, JpaSpecificationExecutor<ChongQingSendLiuZhouEntity> {
    void deleteAllByVinIn(List<String> vins);

    @Query("select ch from ChongQingSendLiuZhouEntity ch WHERE ch.vehicleModel = ?1 AND ch.variety = ?2 AND ch.sharesProduceNumber =?3 AND DATE_FORMAT(ch.shipmentData, '%Y') =?4 AND DATE_FORMAT(ch.shipmentData, '%c') =?5 ")
    List<ChongQingSendLiuZhouEntity> findByDataSource(String vehicleModel, String variety, String batchNumber, String year, String month);

}
