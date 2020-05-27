package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.entity.ModifiedBusTransitSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: ModifiedBusTransitSummaryRepository
 * @ProjectName mis
 * @Description: 改装车在途汇总表
 * @date 2019/1/2817:24
 */
public interface ModifiedBusTransitSummaryRepository extends JpaRepository<ModifiedBusTransitSummaryEntity, String>, JpaSpecificationExecutor<ModifiedBusTransitSummaryEntity> {



}
