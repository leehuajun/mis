package com.sunjet.mis.module.basic.repository;

import com.sunjet.mis.module.basic.entity.DistributorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DistributorRepository extends JpaRepository<DistributorEntity,String>, JpaSpecificationExecutor<DistributorEntity> {
    void deleteAllByCode(List<String> code);

}
