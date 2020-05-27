package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.view.SolidPinReportView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: SolidPinReportRepository
 * @ProjectName mis
 * @Description: 35实销整合报表
 * @date 2019/2/1816:55
 */
public interface SolidPinReportRepository extends JpaRepository<SolidPinReportView, String>, JpaSpecificationExecutor<SolidPinReportView> {

}
