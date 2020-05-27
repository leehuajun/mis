//package com.sunjet.mis.module.warehouse.service;
//
//import com.github.wenhao.jpa.Specifications;
//import com.sunjet.mis.exception.MisException;
//import com.sunjet.mis.helper.DateHelper;
//import com.sunjet.mis.helper.PageUtil;
//import com.sunjet.mis.module.warehouse.entity.VehicleDisembarksWarehousingEntity;
//import com.sunjet.mis.module.warehouse.repository.VehicleDisembarksWarehousingRepository;
//import com.sunjet.mis.utils.dto.PageParam;
//import com.sunjet.mis.utils.dto.PageResult;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * @author SUNJET-YFS
// * @Title: VehicleDisembarksWarehousingService
// * @ProjectName mis
// * @Description: 车辆下线入库 （生产入库）
// * @date 2019/3/816:18
// */
//@Slf4j
//@Transactional(rollbackFor = MisException.class)
//@SuppressWarnings("ALL")
//@Service("vehicleDisembarksWarehousingService")
//public class VehicleDisembarksWarehousingService {
//    @Autowired
//    VehicleDisembarksWarehousingRepository vehicleDisembarksWarehousingRepository;
//
//
//    public PageResult<VehicleDisembarksWarehousingEntity> getPageList(PageParam<VehicleDisembarksWarehousingEntity> pageParam) {
//        //1.查询条件
//        VehicleDisembarksWarehousingEntity vehicleDisembarksWarehousingEntity = pageParam.getInfoWhere();
//
//        //2.设置查询参数
//        Specification<VehicleDisembarksWarehousingEntity> specification = null;
//        if (vehicleDisembarksWarehousingEntity != null) {
//            specification = Specifications.<VehicleDisembarksWarehousingEntity>and()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
//                    .like(StringUtils.isNotBlank(vehicleDisembarksWarehousingEntity.getVsn()), "vsn", "%" + vehicleDisembarksWarehousingEntity.getVsn() + "%")
//                    .like(StringUtils.isNotBlank(vehicleDisembarksWarehousingEntity.getVin()), "vin", "%" + vehicleDisembarksWarehousingEntity.getVin() + "%")
//                    .between((vehicleDisembarksWarehousingEntity.getStartDate() != null && vehicleDisembarksWarehousingEntity.getEndDate() != null), "inboundDate", vehicleDisembarksWarehousingEntity.getStartDate(), vehicleDisembarksWarehousingEntity.getEndDate())
//                   // .between((vehicleDisembarksWarehousingEntity.getStartDate() != null && vehicleDisembarksWarehousingEntity.getEndDate() != null), "inboundDate", new Range<Date>(vehicleDisembarksWarehousingEntity.getStartDate(), (vehicleDisembarksWarehousingEntity.getEndDate())))
//                    .eq(StringUtils.isNotBlank(vehicleDisembarksWarehousingEntity.getVehicleModel()), "vehicleModel", vehicleDisembarksWarehousingEntity.getVehicleModel())
//
//                    .build();
//        }
//
//        //3.执行查询
//        Page<VehicleDisembarksWarehousingEntity> pages = vehicleDisembarksWarehousingRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
//
//
//
//        //5.返回
//        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
//    }
//
//    /**
//     * 查询所有
//     *
//     * @return
//     */
//    public List<VehicleDisembarksWarehousingEntity> findAll() {
//        try {
//            vehicleDisembarksWarehousingRepository.findAll();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    /**
//     * @Description: 保存入库车辆
//     * @Author: YFS
//     * @CreateDate: 2019/1/11 14:20
//     */
//    public VehicleDisembarksWarehousingEntity save(VehicleDisembarksWarehousingEntity vehicleDisembarksWarehousingEntity) {
//        try {
//            vehicleDisembarksWarehousingRepository.save(vehicleDisembarksWarehousingEntity);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 每次根据vin覆盖或新增数据
//     *
//     * @param importEntities
//     * @return
//     */
//    public List<VehicleDisembarksWarehousingEntity> saveAll(List<VehicleDisembarksWarehousingEntity> importEntities) {
//
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
//
//
//        return vehicleDisembarksWarehousingRepository.saveAll(importEntities);
//
//    }
//
//
//}