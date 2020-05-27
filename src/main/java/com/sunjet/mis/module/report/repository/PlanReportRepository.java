package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.report.view.PlanReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlanReportRepository extends JpaRepository<PlanReportView, String>, JpaSpecificationExecutor<PlanReportView> {
}
