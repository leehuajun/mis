package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.TransformingTrucksBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: TransformingTrucksBalanceReposiory
 * @ProjectName mis
 * @Description: 货改车月成品结余
 * @date 2019/4/1815:56
 */
public interface TransformingTrucksBalanceReposiory extends JpaRepository<TransformingTrucksBalanceEntity, String>, JpaSpecificationExecutor<TransformingTrucksBalanceEntity> {
}
