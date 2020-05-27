package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.entity.DistributorDemandStatementEntity;
import com.sunjet.mis.module.report.view.DistributorCompleteReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: DistributorDemandStatementRepositoty
 * @ProjectName mis
 * @Description: 经销商需求表格
 * @date 2019/3/1213:51
 */


public interface DistributorDemandStatementRepositoty extends JpaRepository<DistributorDemandStatementEntity, String>, JpaSpecificationExecutor<DistributorDemandStatementEntity> {



}
