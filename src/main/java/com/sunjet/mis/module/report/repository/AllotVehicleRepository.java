package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface AllotVehicleRepository extends JpaRepository<AllotVehicleEntity, String>, JpaSpecificationExecutor<AllotVehicleEntity> {

    void deleteAllByVinIn(List<String> vins);

    List<AllotVehicleEntity> findAllByInvoiceDateBetweenAndSgmwCodeIn(Date startDate, Date endDate,List<String> list);
}
