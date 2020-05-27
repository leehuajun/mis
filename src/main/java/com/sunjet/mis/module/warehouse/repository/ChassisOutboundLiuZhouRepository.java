package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.ChassisOutboundLiuZhouEntity;
import com.sunjet.mis.module.warehouse.entity.ChassisOutboundQINGDAOEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChassisOutboundLiuZhouRepository extends JpaRepository<ChassisOutboundLiuZhouEntity, String>, JpaSpecificationExecutor<ChassisOutboundLiuZhouEntity> {
    void deleteAllByVinIn(List<String> vins);

    @Query("select ch from ChassisOutboundLiuZhouEntity ch WHERE DATE_FORMAT(ch.standingTime, '%Y') = ?1 AND DATE_FORMAT(ch.standingTime, '%c') = ?2 ")
    List<ChassisOutboundLiuZhouEntity> findByStandingTime(String year,String month);
}
