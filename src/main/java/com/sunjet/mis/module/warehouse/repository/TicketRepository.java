package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.InventoryEntity;
import com.sunjet.mis.module.warehouse.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, String>, JpaSpecificationExecutor<TicketEntity> {
    void deleteAllByVinIn(List<String> vins);
}
