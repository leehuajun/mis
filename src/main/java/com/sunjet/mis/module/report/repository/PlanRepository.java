package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanRepository extends JpaRepository<PlanEntity, String>, JpaSpecificationExecutor<PlanEntity> {

    List<PlanEntity> findAllByYearAndMonthAndSgmwCodeIn(Integer year,Integer month,List<String> list);

    @Query("select DISTINCT pe.type from PlanEntity pe")
    List<String> findType();

    @Query("select SUM(pe.requiredAmount) from PlanEntity pe WHERE pe.model = ?1 AND pe.vsn = ?2 AND pe.year =?3 AND pe.month =?4 ")
    String findRequiredAmount(String model,String vsn,Integer year,Integer month);

    @Query("select SUM(pe.firstWeekAmount) from PlanEntity pe WHERE pe.model = ?1 AND pe.vsn = ?2 AND pe.year =?3 AND pe.month =?4 ")
    Integer findFirstWeekAmount(String model,String vsn,Integer year,Integer month);
    @Query("select SUM(pe.secondWeekAmount) from PlanEntity pe WHERE pe.model = ?1 AND pe.vsn = ?2 AND pe.year =?3 AND pe.month =?4 ")
    Integer findSecondWeekAmount(String model,String vsn,Integer year,Integer month);
    @Query("select SUM(pe.thirdWeekAmount) from PlanEntity pe WHERE pe.model = ?1 AND pe.vsn = ?2 AND pe.year =?3 AND pe.month =?4 ")
    Integer findThirdWeekAmount(String model,String vsn,Integer year,Integer month);
    @Query("select SUM(pe.fourthWeekAmount) from PlanEntity pe WHERE pe.model = ?1 AND pe.vsn = ?2 AND pe.year =?3 AND pe.month =?4 ")
    Integer findFourthWeekAmount(String model,String vsn,Integer year,Integer month);
    @Query("select SUM(pe.fifthWeekAmount) from PlanEntity pe WHERE pe.model = ?1 AND pe.vsn = ?2 AND pe.year =?3 AND pe.month =?4 ")
    Integer findFifthWeekAmount(String model,String vsn,Integer year,Integer month);

    @Query("select pe from PlanEntity pe WHERE pe.model LIKE ?1 AND pe.vsn = ?2 AND pe.year =?3 AND pe.month =?4 ")
    List<PlanEntity> findAllByModelAndVsn(String model,String vsn,Integer year,Integer month);

}
