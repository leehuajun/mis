package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.entity.ProcessDataSheetsDailyEntity;
import com.sunjet.mis.module.report.view.SocialInventoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: ProcessDataSheetsDailyRepository
 * @ProjectName mis
 * @Description: 每日数据处理
 * @date 2019/2/2615:26
 */
public interface ProcessDataSheetsDailyRepository extends JpaRepository<ProcessDataSheetsDailyEntity, String>, JpaSpecificationExecutor<ProcessDataSheetsDailyEntity> {

}
