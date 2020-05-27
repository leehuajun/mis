package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import com.sunjet.mis.module.report.view.SocialInventoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SocialInventoryRepository extends JpaRepository<SocialInventoryView, String>, JpaSpecificationExecutor<SocialInventoryView> {
}
