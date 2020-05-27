package com.sunjet.mis.module.basic.repository;

import com.sunjet.mis.module.basic.entity.SalesTargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: SalesTargetRepository
 * @ProjectName mis
 * @Description: TODO
 * @date 2019/1/1414:10
 */
public interface SalesTargetRepository extends JpaRepository<SalesTargetEntity, String>, JpaSpecificationExecutor<SalesTargetEntity> {
}
