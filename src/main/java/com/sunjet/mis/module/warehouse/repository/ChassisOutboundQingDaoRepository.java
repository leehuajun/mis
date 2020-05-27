package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.ChassisOutboundQINGDAOEntity;
import com.sunjet.mis.module.warehouse.entity.ChassisWarehouseQingDaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChassisOutboundQingDaoRepository extends JpaRepository<ChassisOutboundQINGDAOEntity, String>, JpaSpecificationExecutor<ChassisOutboundQINGDAOEntity> {
    void deleteAllByVinIn(List<String> vins);

    @Query("select ch from ChassisOutboundQINGDAOEntity ch WHERE ch.vin = ?1 AND ch.vsn = ?2 AND ch.scanMember like ?3 ")
    ChassisOutboundQINGDAOEntity findByVinAndVsn(String vin,String vsn,String scanMember);

    @Query("select ch from ChassisOutboundQINGDAOEntity ch WHERE ch.vehicleModel = ?1 AND ch.sharesProduceNumber = ?2 AND ch.variety = ?3 AND ch.scanMember like ?4 AND DATE_FORMAT(ch.lastOperation, '%Y') = ?5 AND DATE_FORMAT(ch.lastOperation, '%c') = ?6")
    List<ChassisOutboundQINGDAOEntity> findBVehicleModel(String vehicleModel,String sharesProduceNumber,String variety,String scanMember,String year,String month);

    @Query("select ch from ChassisOutboundQINGDAOEntity ch WHERE ch.scanMember like ?1 ")
    List<ChassisOutboundQINGDAOEntity> findByScanMember(String scanMember);

}
