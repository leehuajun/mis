package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductionPlanTrackingReportRepository extends JpaRepository<ProductionPlanTrackingReportView, String>, JpaSpecificationExecutor<ProductionPlanTrackingReportView> {
}
