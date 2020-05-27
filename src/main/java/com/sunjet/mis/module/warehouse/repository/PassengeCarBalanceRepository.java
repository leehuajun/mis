package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.PassengeCarBalanceEntity;
import com.sunjet.mis.module.warehouse.entity.TransformingTrucksBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: PassengeCarBalanceRepository
 * @ProjectName mis
 * @Description: 客厢车月成品结余
 * @date 2019/4/1816:42
 */
public interface PassengeCarBalanceRepository extends JpaRepository<PassengeCarBalanceEntity, String>, JpaSpecificationExecutor<PassengeCarBalanceEntity> {
}
