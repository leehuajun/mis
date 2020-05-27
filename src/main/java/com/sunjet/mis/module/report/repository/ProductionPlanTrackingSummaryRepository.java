package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.entity.AllotVehicleEntity;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingReportView;
import com.sunjet.mis.module.report.view.ProductionPlanTrackingSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ProductionPlanTrackingSummaryRepository extends JpaRepository<ProductionPlanTrackingSummaryView, String>, JpaSpecificationExecutor<ProductionPlanTrackingSummaryView> {
//    @Query("SELECT DISTINCT form PlanEntity")
//    List<ProductionPlanTrackingSummaryView> findAllByInvoiceDateBetweenAndSgmwCodeIn(String vehicleSeries,String origin,String type);
//SELECT DISTINCT
//    ky.warehouse_area_,
//    vm.vehicle_series_,
//    vm.name_
//            FROM
//    rpt_plan pl
//    INNER JOIN rpt_available_inventory ky ON pl.model_ = ky.vehicle_name_
//    INNER JOIN basic_vehicle_model vm ON vm.code_ = ky.vehicle_model_

}
