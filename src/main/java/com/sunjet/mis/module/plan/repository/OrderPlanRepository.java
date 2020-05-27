package com.sunjet.mis.module.plan.repository;

import com.sunjet.mis.module.plan.entity.OrderPlanEntity;
import com.sunjet.mis.module.report.view.PlanExecSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderPlanRepository extends JpaRepository<OrderPlanEntity, String>, JpaSpecificationExecutor<OrderPlanEntity> {
}
