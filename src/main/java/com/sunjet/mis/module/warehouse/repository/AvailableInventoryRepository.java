package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.AvailableInventoryEntity;
import com.sunjet.mis.module.warehouse.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvailableInventoryRepository extends JpaRepository<AvailableInventoryEntity, String>, JpaSpecificationExecutor<AvailableInventoryEntity> {

    @Query("select DISTINCT ai.vehicleModel,ai.vsn from AvailableInventoryEntity ai")
    List<Object[]> findVehicleModelAndVsn();

    @Query("SELECT DATE_FORMAT(sc.inboundDate, '%d') AS day_" +
            ",DATE_FORMAT(sc.inboundDate, '%c') AS month_" +
            ",DATE_FORMAT(sc.inboundDate, '%Y') AS year_,COUNT(sc) AS day_amount_ " +
            "FROM AvailableInventoryEntity sc " +
            "WHERE sc.vehicleModel =?1 AND sc.vsn = ?2 AND DATE_FORMAT(sc.inboundDate, '%Y') = ?3 AND DATE_FORMAT(sc.inboundDate, '%c') = ?4" +
            " GROUP BY DATE_FORMAT(sc.inboundDate, '%d')" +
            ",DATE_FORMAT(sc.inboundDate, '%c')" +
            ",DATE_FORMAT(sc.inboundDate, '%Y')")
    List<Object[]> findDayAndDayAmount(String vehicleModel,String vsn,String year,String month);

    void deleteAllByVinIn(List<String> vins);

}
