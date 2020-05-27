package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.warehouse.entity.PassengeCarBalanceEntity;
import com.sunjet.mis.module.warehouse.repository.PassengeCarBalanceRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: PassengeCarBalanceService
 * @ProjectName mis
 * @Description: 客厢车月成品结余
 * @date 2019/4/1816:43
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("passengeCarBalanceService")
public class PassengeCarBalanceService {
    @Autowired
    PassengeCarBalanceRepository passengeCarBalanceRepository;

    public PageResult<PassengeCarBalanceEntity> getPageList(PageParam<PassengeCarBalanceEntity> pageParam) {
        //1.查询条件
        PassengeCarBalanceEntity passengeCarBalanceEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<PassengeCarBalanceEntity> specification = null;
        if (passengeCarBalanceEntity != null) {
            specification = Specifications.<PassengeCarBalanceEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                   .like(StringUtils.isNotBlank(passengeCarBalanceEntity.getAbsvsn()), "absvsn", "%" + passengeCarBalanceEntity.getAbsvsn() + "%")
//                    .like(StringUtils.isNotBlank(vehicleDisembarksWarehousingEntity.getVin()), "vin", "%" + vehicleDisembarksWarehousingEntity.getVin() + "%")
                    .between((passengeCarBalanceEntity.getStartDate() != null && passengeCarBalanceEntity.getEndDate() != null), "dateYearMonth", passengeCarBalanceEntity.getStartDate(), passengeCarBalanceEntity.getEndDate())// .between((vehicleDisembarksWarehousingEntity.getStartDate() != null && vehicleDisembarksWarehousingEntity.getEndDate() != null), "inboundDate", new Range<Date>(vehicleDisembarksWarehousingEntity.getStartDate(), (vehicleDisembarksWarehousingEntity.getEndDate())))
//                    .eq(StringUtils.isNotBlank(vehicleDisembarksWarehousingEntity.getVehicleModel()), "vehicleModel", vehicleDisembarksWarehousingEntity.getVehicleModel())

                    .build();
        }

        //3.执行查询
        Page<PassengeCarBalanceEntity> pages = passengeCarBalanceRepository.findAll(specification, PageUtil.getPageRequest(pageParam));



        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<PassengeCarBalanceEntity> findAll() {
        try {
            passengeCarBalanceRepository.findAll();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Description: 保存入库车辆
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:20
     */
    public PassengeCarBalanceEntity save(PassengeCarBalanceEntity passengeCarBalanceEntity) {
        try {
            passengeCarBalanceRepository.save(passengeCarBalanceEntity);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 每次根据vin覆盖或新增数据
     *
     * @param importEntities
     * @return
     */
    public List<PassengeCarBalanceEntity> saveAll(List<PassengeCarBalanceEntity> importEntities) {

//        List<VehicleDisembarksWarehousingEntity> addEntities = new ArrayList<>();
//        List<VehicleDisembarksWarehousingEntity> vehicleDisembarksWarehousingEntities = vehicleDisembarksWarehousingRepository.findAll();
//
//        for (VehicleDisembarksWarehousingEntity entity : vehicleDisembarksWarehousingEntities) {
//
//            for (VehicleDisembarksWarehousingEntity importEntity : vehicleDisembarksWarehousingEntities) {
//
//                if (entity.getVin().equals(importEntity.getVin())) {
//
//                    addEntities.add(entity);
//                }
//            }
//        }
//        List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
//
//        vehicleDisembarksWarehousingRepository.deleteAllByVinIn(list);


        return passengeCarBalanceRepository.saveAll(importEntities);

    }

}
