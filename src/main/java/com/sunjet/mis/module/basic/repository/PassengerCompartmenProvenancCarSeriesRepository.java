package com.sunjet.mis.module.basic.repository;

import com.sunjet.mis.module.basic.entity.PassengerCompartmenProvenancCarSeriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: PassengerCompartmenProvenancCarSeriesRepository
 * @ProjectName mis
 * @Description: 客厢车 车系 产地 vsn 的匹配表
 * @date 2019/4/317:08
 */
public interface PassengerCompartmenProvenancCarSeriesRepository extends JpaRepository<PassengerCompartmenProvenancCarSeriesEntity, String>, JpaSpecificationExecutor<PassengerCompartmenProvenancCarSeriesEntity> {
 void deleteAllByAbsvsnIn (List<String> absvsns);

}
