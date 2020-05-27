package com.sunjet.mis.module.system.repository;

import com.sunjet.mis.module.system.entity.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResourceRepository extends JpaRepository<ResourceEntity,String>, JpaSpecificationExecutor<ResourceEntity> {
}
