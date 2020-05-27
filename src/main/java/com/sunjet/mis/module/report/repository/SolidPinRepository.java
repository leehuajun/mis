package com.sunjet.mis.module.report.repository;

import com.sunjet.mis.module.report.entity.SolidPinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: SolidPinRepository
 * @ProjectName mis
 * @Description: 35实销导入实体Repository
 * @date 2019/1/2316:45
 */
public interface SolidPinRepository extends JpaRepository<SolidPinEntity, String>, JpaSpecificationExecutor<SolidPinEntity> {
    void deleteAllByVinIn(List<String> vins);

    List<SolidPinEntity> findBySaleDate(String saleDate);

//
//    @Query("select id  from rpt_solid_pin ")
//    List<SolidPinEntity> findByALLId(String id);
//
//    @Query("update rpt_solid_pin s set sale_Date_= invoice_Date_ where id_=?1")
//     void  updateInvoiceDate(String id, String saleDate);

//    @Query(insert into rpt_solid_pin (invoice_Date_)values(?1));
//    SolidPinEntity saveSaleDate (SolidPinEntity saleDate);
}
