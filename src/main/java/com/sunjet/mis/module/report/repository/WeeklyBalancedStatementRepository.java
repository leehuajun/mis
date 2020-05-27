package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.view.WeeklyBalancedStatementView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: WeeklyBalancedStatementRepository
 * @ProjectName mis
 * @Description: 周产平报表
 * @date 2019/3/711:09
 */
public interface WeeklyBalancedStatementRepository extends JpaRepository<WeeklyBalancedStatementView, String>, JpaSpecificationExecutor<WeeklyBalancedStatementView> {


}
