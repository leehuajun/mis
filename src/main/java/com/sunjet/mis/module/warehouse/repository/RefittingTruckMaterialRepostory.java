package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.RefittingTruckMaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author SUNJET-YFS
 * @Title: RefittingTruckMaterialRepostory
 * @ProjectName mis
 * @Description: 改装车月物料申报表
 * @date 2019/4/1110:11
 */
public interface RefittingTruckMaterialRepostory extends JpaRepository<RefittingTruckMaterialEntity, String>, JpaSpecificationExecutor<RefittingTruckMaterialEntity> {
}
