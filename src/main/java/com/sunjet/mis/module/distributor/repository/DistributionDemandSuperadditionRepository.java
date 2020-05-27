package com.sunjet.mis.module.distributor.repository;

import com.sunjet.mis.module.distributor.entity.DistributionDemandSuperadditionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: DistributionDemandSuperadditionRepository
 * @ProjectName mis
 * @Description: 经销商需求追加表
 * @date 2019/3/1515:28
 */
public interface DistributionDemandSuperadditionRepository extends JpaRepository<DistributionDemandSuperadditionEntity, String>, JpaSpecificationExecutor<DistributionDemandSuperadditionEntity> {

}
