package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.AvailableInventoryEntity;
import com.sunjet.mis.module.warehouse.entity.ProductionWarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductionWarehouseRepository extends JpaRepository<ProductionWarehouseEntity, String>, JpaSpecificationExecutor<ProductionWarehouseEntity> {
    void deleteAllByVinIn(List<String> vins);

    @Query("select ch from ProductionWarehouseEntity ch WHERE ch.vin = ?1 AND ch.vsn = ?2 AND ch.warehouseArea like ?3 ")
    ProductionWarehouseEntity findByVinAndVsn(String vin,String vsn,String warehouseArea);

    @Query("select ch from ProductionWarehouseEntity ch WHERE DATE_FORMAT(ch.inboundDate, '%Y') = ?1 AND DATE_FORMAT(ch.inboundDate, '%c') = ?2 ")
    List<ProductionWarehouseEntity> findByInboundDate(String year,String month);

    @Query("select ch from ProductionWarehouseEntity ch WHERE ch.warehouseArea LIKE ?1")
    List<ProductionWarehouseEntity> findByWarehouseArea(String warehouseArea);

}
