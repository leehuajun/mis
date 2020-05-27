package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.entity.DistributorDemandStatementEntity;
import com.sunjet.mis.module.report.view.DistributorDemandStatementView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: DistributorDemandStatementViewRepositoty
 * @ProjectName mis
 * @Description: TODO
 * @date 2019/3/1216:05
 */
public interface DistributorDemandStatementViewRepositoty extends JpaRepository<DistributorDemandStatementView, String>, JpaSpecificationExecutor<DistributorDemandStatementView> {
}
