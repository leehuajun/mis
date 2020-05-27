package com.sunjet.mis.module.sales.repository;

import com.sunjet.mis.module.sales.entity.SalesOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: SalesOrderRepository
 * @ProjectName mis
 * @Description: TODO
 * @date 2019/1/1115:49
 */
public interface SalesOrderRepository extends JpaRepository<SalesOrderEntity,String>, JpaSpecificationExecutor<SalesOrderEntity> {
}
