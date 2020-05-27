package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BalanceRepository extends JpaRepository<BalanceEntity, String>, JpaSpecificationExecutor<BalanceEntity> {

//    @Query("select cb from BalanceEntity cb where cb.subjectCode in ?1")
//    List<BalanceEntity> findAllBySubjectCodeList(List<String> list);

//    @Modifying
//    @Query("delete from BalanceEntity cb where cb.distributorCode in ?1")
//    void deleteAllByCustomerCodeList(List<String> list);

    void deleteAllByDistributorCodeIn(List<String> list);

    List<BalanceEntity> findAllBySgmwCodeIn(List<String> list);
}
