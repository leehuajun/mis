package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.report.view.PlanExecSummaryViewBak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlanExecSummaryRepository extends JpaRepository<PlanExecSummaryView, String>, JpaSpecificationExecutor<PlanExecSummaryView> {
}
