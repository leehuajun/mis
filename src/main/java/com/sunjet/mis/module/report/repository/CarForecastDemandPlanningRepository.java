package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.view.CarForecastDemandPlanningView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: CarForecastDemandPlanningRepository
 * @ProjectName mis
 * @Description: 客厢车月预测客厢车销售需求计划 - 客厢车
 * @date 2019/4/2810:28
 */
public interface CarForecastDemandPlanningRepository extends JpaRepository<CarForecastDemandPlanningView, String>, JpaSpecificationExecutor<CarForecastDemandPlanningView> {
}
