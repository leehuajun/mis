package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.InventoryEntity;
import com.sunjet.mis.module.warehouse.entity.SpecialVehicleMonthlyPlanBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: SpecialVehicleMonthlyPlanBalanceRepository
 * @ProjectName mis
 * @Description: 专用车月计划平衡详细表
 * @date 2019/4/1014:09
 */
public interface SpecialVehicleMonthlyPlanBalanceRepository extends JpaRepository<SpecialVehicleMonthlyPlanBalanceEntity, String>, JpaSpecificationExecutor<SpecialVehicleMonthlyPlanBalanceEntity> {
    void deleteAllBySsidIn(List<String> Ssids);
}
