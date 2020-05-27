package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.view.ChassisTrackingView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChassisTrackingRepository extends JpaRepository<ChassisTrackingView, String>, JpaSpecificationExecutor<ChassisTrackingView> {
}
