package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.entity.TransportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author: wushi
 * @description: 在途
 * @Date: Created in 15:19 2019/3/14
 * @Modify by: wushi
 * @ModifyDate by: 15:19 2019/3/14
 */
public interface TransportRepository extends JpaRepository<TransportEntity, String>, JpaSpecificationExecutor<TransportEntity> {

}
