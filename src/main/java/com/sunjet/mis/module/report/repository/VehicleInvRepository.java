package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.entity.VehicleInvEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface VehicleInvRepository extends JpaRepository<VehicleInvEntity, String>, JpaSpecificationExecutor<VehicleInvEntity> {
    void deleteAllByVinIn(List<String> vins);

    List<VehicleInvEntity> findAllByInvoiceTimeBetweenAndStatusEqualsAndSgmwCodeIn(Date startDate, Date endDate, String status,List<String> list);
}
