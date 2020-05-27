package com.sunjet.mis.module.warehouse.repository;

import com.sunjet.mis.module.warehouse.entity.ChassisOutboundQINGDAOEntity;
import com.sunjet.mis.module.warehouse.entity.RailwaySendKunMingEntity;
import com.sunjet.mis.module.warehouse.entity.RailwaySendLIUZHOUEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RailwaySendLIUZHOURepository extends JpaRepository<RailwaySendLIUZHOUEntity, String>, JpaSpecificationExecutor<RailwaySendLIUZHOUEntity> {
    void deleteAllByVinIn(List<String> vins);

    @Query("select ch from RailwaySendLIUZHOUEntity ch WHERE ch.vehicleModel = ?1 AND ch.variety = ?2 AND ch.sharesProduceNumber =?3 AND DATE_FORMAT(ch.outboundDate, '%Y') =?4 AND DATE_FORMAT(ch.outboundDate, '%c') =?5 ")
    List<RailwaySendLIUZHOUEntity> findByDataSource(String vehicleModel, String variety, String sharesProduceNumber, String year, String month);

}
