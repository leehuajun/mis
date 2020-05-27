package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.ChassisWarehouseQingDaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChassisWarehouseQingDaoRepository extends JpaRepository<ChassisWarehouseQingDaoEntity, String>, JpaSpecificationExecutor<ChassisWarehouseQingDaoEntity> {
    void deleteAllByVinIn(List<String> vins);

    @Query("select ch from ChassisWarehouseQingDaoEntity ch WHERE ch.vin = ?1 AND ch.vsn = ?2 AND ch.scanMember like ?3 ")
    ChassisWarehouseQingDaoEntity findByVinAndVsn(String vin,String vsn,String scanMember);

    @Query("select ch from ChassisWarehouseQingDaoEntity ch WHERE  ch.scanMember like ?1 ")
    List<ChassisWarehouseQingDaoEntity> findByScanMember(String scanMember);


}
