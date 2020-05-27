package com.sunjet.mis.module.basic.repository;

import com.sunjet.mis.module.basic.entity.TruckSvarietyProvenancEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: TruckSvarietyProvenancRepository
 * @ProjectName mis
 * @Description: 货改车 ABS-VSN 产品名称，产品类别，产地的匹配表
 * @date 2019/4/315:26
 */
public interface TruckSvarietyProvenancRepository extends JpaRepository<TruckSvarietyProvenancEntity, String>, JpaSpecificationExecutor<TruckSvarietyProvenancEntity> {

    void deleteAllByAbsvsnIn(List<String> absvsns);

    //List<TruckSvarietyProvenancEntity> findBySaleDate(String saleDate);

}
