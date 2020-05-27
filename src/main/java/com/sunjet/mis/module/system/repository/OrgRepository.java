package com.sunjet.mis.module.system.repository;

import com.sunjet.mis.module.system.entity.OrgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrgRepository extends JpaRepository<OrgEntity,String>, JpaSpecificationExecutor<OrgEntity> {

    @Query("select org from OrgEntity org where org.parent is null")
    public List<OrgEntity> findParentOrgs();

    @Query("select org from OrgEntity org where org.id = ?1")
    OrgEntity findOneById(String objId);

}
