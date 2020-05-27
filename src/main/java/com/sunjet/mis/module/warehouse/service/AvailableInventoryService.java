package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.report.repository.AllotVehicleRepository;
import com.sunjet.mis.module.report.repository.BalanceRepository;
import com.sunjet.mis.module.report.repository.PlanRepository;
import com.sunjet.mis.module.report.repository.VehicleInvRepository;
import com.sunjet.mis.module.warehouse.entity.AcceptanceDetailEntity;
import com.sunjet.mis.module.warehouse.entity.AvailableInventoryEntity;
import com.sunjet.mis.module.warehouse.entity.InventoryEntity;
import com.sunjet.mis.module.warehouse.entity.TicketEntity;
import com.sunjet.mis.module.warehouse.repository.AvailableInventoryRepository;
import com.sunjet.mis.module.warehouse.repository.InventoryRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import com.sunjet.mis.utils.zk.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("availableInventoryService")
public class AvailableInventoryService {

    @Autowired
    private AvailableInventoryRepository availableInventoryRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private AllotVehicleRepository allotVehicleRepository;
    @Autowired
    private VehicleInvRepository vehicleInvRepository;

    public List<AvailableInventoryEntity> findAll() {
        try {
            return this.availableInventoryRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数
     * @return result 包含 List<Entity> 和分页数据
     */
    public PageResult<AvailableInventoryEntity> getPageList(PageParam<AvailableInventoryEntity> pageParam) {
        //1.查询条件
        AvailableInventoryEntity availableInventoryEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AvailableInventoryEntity> specification1 = null;
        Specification<AvailableInventoryEntity> specification2 = null;
        Specification<AvailableInventoryEntity> specification3 = null;

        //页面条件
        if (availableInventoryEntity != null && (StringUtils.isNotBlank(availableInventoryEntity.getVin()) || StringUtils.isNotBlank(availableInventoryEntity.getVsn()) || StringUtils.isNotBlank(availableInventoryEntity.getVehicleModel()))) {
            specification2 = Specifications.<AvailableInventoryEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(availableInventoryEntity.getVin()), "vin", "%"+availableInventoryEntity.getVin()+"%")
                    .like(StringUtils.isNotEmpty(availableInventoryEntity.getVsn()), "vsn", "%"+availableInventoryEntity.getVsn()+"%")
                    .like(StringUtils.isNotEmpty(availableInventoryEntity.getVehicleModel()), "vehicleModel", "%"+availableInventoryEntity.getVehicleModel()+"%")
                    .build();
        }

        //组织
//        if(authObject.getOrg().getOrgHierarchy().intValue()!=1) {
//            specification3 = Specifications.<ConfigEntity>or()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
//                    .eq("orgId", authObject.getOrg().getObjId())
//                    .eq("orgType", "1")
//                    .build();
//        }

        //组合查询条件
        specification1 = Specifications.<AvailableInventoryEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<AvailableInventoryEntity> pages = availableInventoryRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

        //4.数据转换
//        List<ConfigEntity> rows = new ArrayList<>();
//        for (ConfigEntity entity : pages.getContent()) {
//            rows.add(entity);
//        }

        //5.组装分页信息及集合信息
        //PageResult<ResourceInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        System.out.println(pages.getContent().size());
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public AvailableInventoryEntity findById(String id) {
        Optional<AvailableInventoryEntity> entityOptional = availableInventoryRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public AvailableInventoryEntity save(AvailableInventoryEntity entity) {
        return availableInventoryRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            availableInventoryRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 每次根据vin覆盖或新增数据
     *
     * @param importEntities
     * @return
     */
    public List<AvailableInventoryEntity> saveAll(List<AvailableInventoryEntity> importEntities) {
        //List<AvailableInventoryEntity> resultEntities = new ArrayList<>();
        List<AvailableInventoryEntity> addEntities = new ArrayList<>();
        HashSet<AvailableInventoryEntity> resultEntities =new HashSet<>();
        List<AvailableInventoryEntity> availableInventoryEntityList = availableInventoryRepository.findAll();
        for(AvailableInventoryEntity entity:availableInventoryEntityList){
            for(AvailableInventoryEntity importEntity:importEntities){
                if(StringUtils.isNotBlank(entity.getVin()) && StringUtils.isNotBlank(importEntity.getVin())) {
                    if (entity.getVin().equals(importEntity.getVin())) {
                        addEntities.add(entity);
                    }
                }else {
                    resultEntities.add(entity);
                }
            }
        }

        if(resultEntities.size()>0){
            ZkUtils.showError("缺少VIN,请检查原因重新导入", "系统提示");
        }else{
            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
            availableInventoryRepository.deleteAllByVinIn(list);
            availableInventoryRepository.saveAll(importEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;
//        try {
//            //1. 用SGMW的客户编号替换五菱工业的客户编号
//            List<AvailableInventoryEntity> listDistributor = availableInventoryRepository.findAll();
////            for (AvailableInventoryEntity entity : importEntities) {
////                List<AvailableInventoryEntity> list = listDistributor.stream().filter(e -> e.getCode().equalsIgnoreCase(entity.getDistributorCode())).collect(Collectors.toList());
////                if (list.size() == 1) {
//////                    entity.setCustomerCode(list.get(0).getSgmwCode());
////                    entity.setSgmwCode(list.get(0).getSgmwCode());
////                    addEntities.add(entity);
////                } else {
////                    resultEntities.add(entity);
////                }
////            }
//            //2. 删除对应vin的数据
//            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
//            allotVehicleRepository.deleteAllByVinIn(list);
//
//            //3. 保存数据
//            availableInventoryRepository.saveAll(addEntities);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new MisException(e.getMessage());
//        } finally {
//            return resultEntities;
//        }

    }

}
