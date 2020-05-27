package com.sunjet.mis.module.plan.repository;

import com.sunjet.mis.module.plan.entity.SalesPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: SalesPlanRepository
 * @ProjectName mis
 * @Description: TODO
 * @date 2019/1/1115:34
 */
public interface SalesPlanRepository extends JpaRepository<SalesPlanEntity,String>, JpaSpecificationExecutor<SalesPlanEntity> {

}
