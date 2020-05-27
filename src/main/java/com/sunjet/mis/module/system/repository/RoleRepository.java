package com.sunjet.mis.module.system.repository;

import com.sunjet.mis.module.system.entity.RoleEntity;
import com.sunjet.mis.module.system.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity,String>, JpaSpecificationExecutor<RoleEntity> {
    List<RoleEntity> findAllByOrgId(String orgId);

    RoleEntity findOneById(String id);
}
