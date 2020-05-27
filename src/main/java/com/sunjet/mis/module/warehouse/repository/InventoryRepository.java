package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryEntity, String>, JpaSpecificationExecutor<InventoryEntity> {
    void deleteAllByVinIn(List<String> vins);
}
