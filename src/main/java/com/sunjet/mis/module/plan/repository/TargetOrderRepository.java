package com.sunjet.mis.module.plan.repository;

import com.sunjet.mis.module.plan.entity.TargetOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Author: wushi
 * @description: 目标管理
 * @Date: Created in 11:56 2019/3/5
 * @Modify by: wushi
 * @ModifyDate by: 11:56 2019/3/5
 */
public interface TargetOrderRepository extends JpaRepository<TargetOrderEntity, String>, JpaSpecificationExecutor<TargetOrderEntity> {
}
