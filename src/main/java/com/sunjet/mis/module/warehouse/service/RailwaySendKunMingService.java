package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.warehouse.entity.AcceptanceDetailEntity;
import com.sunjet.mis.module.warehouse.entity.ChongQingSendLiuZhouEntity;
import com.sunjet.mis.module.warehouse.entity.RailwaySendKunMingEntity;
import com.sunjet.mis.module.warehouse.entity.RailwaySendLIUZHOUEntity;
import com.sunjet.mis.module.warehouse.repository.AcceptanceDetailRepository;
import com.sunjet.mis.module.warehouse.repository.RailwaySendKunMingRepository;
import com.sunjet.mis.module.warehouse.repository.RailwaySendLIUZHOURepository;
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
@Service("railwaySendKunMingService")
public class RailwaySendKunMingService {

    @Autowired
    private RailwaySendKunMingRepository railwaySendKunMingRepository;
    @Autowired
    private AcceptanceDetailRepository acceptanceDetailRepository;

    public List<RailwaySendKunMingEntity> findAll() {
        try {
            return this.railwaySendKunMingRepository.findAll();
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
    public PageResult<RailwaySendKunMingEntity> getPageList(PageParam<RailwaySendKunMingEntity> pageParam) {
        //1.查询条件
        RailwaySendKunMingEntity railwaySendKunMingEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<RailwaySendKunMingEntity> specification1 = null;
        Specification<RailwaySendKunMingEntity> specification2 = null;
        Specification<RailwaySendKunMingEntity> specification3 = null;

        //页面条件
        if (railwaySendKunMingEntity != null && (StringUtils.isNotBlank(railwaySendKunMingEntity.getVin()) || StringUtils.isNotBlank(railwaySendKunMingEntity.getVariety()) || StringUtils.isNotBlank(railwaySendKunMingEntity.getBatchNumber()) || StringUtils.isNotBlank(railwaySendKunMingEntity.getVehicleModel()))) {
            specification2 = Specifications.<RailwaySendKunMingEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(railwaySendKunMingEntity.getVin()), "vin", "%"+railwaySendKunMingEntity.getVin()+"%")
                    .like(StringUtils.isNotEmpty(railwaySendKunMingEntity.getVariety()), "variety", "%"+railwaySendKunMingEntity.getVariety()+"%")
                    .like(StringUtils.isNotEmpty(railwaySendKunMingEntity.getBatchNumber()), "batchNumber", "%"+railwaySendKunMingEntity.getBatchNumber()+"%")
                    .like(StringUtils.isNotEmpty(railwaySendKunMingEntity.getVehicleModel()), "vehicleModel", "%"+railwaySendKunMingEntity.getVehicleModel()+"%")
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
        specification1 = Specifications.<RailwaySendKunMingEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<RailwaySendKunMingEntity> pages = railwaySendKunMingRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

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

    public RailwaySendKunMingEntity findById(String id) {
        Optional<RailwaySendKunMingEntity> entityOptional = railwaySendKunMingRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public RailwaySendKunMingEntity save(RailwaySendKunMingEntity entity) {
        return railwaySendKunMingRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            railwaySendKunMingRepository.deleteById(id);
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
    public List<RailwaySendKunMingEntity> saveAll(List<RailwaySendKunMingEntity> importEntities) {
        //List<RailwaySendKunMingEntity> resultEntities = new ArrayList<>();
        List<RailwaySendKunMingEntity> addEntities = new ArrayList<>();
        HashSet<RailwaySendKunMingEntity> resultEntities =new HashSet<>();

        List<RailwaySendKunMingEntity> chassisWarehouseQingDaoEntities = railwaySendKunMingRepository.findAll();
        List<AcceptanceDetailEntity> allList = acceptanceDetailRepository.findAll();
        for(RailwaySendKunMingEntity importEntity:importEntities){
            for(AcceptanceDetailEntity ap:allList){
                if(StringUtils.isNotBlank(ap.getVin()) && StringUtils.isNotBlank(importEntity.getVin())){
                    if(importEntity.getVin().equals(ap.getVin())){
                        importEntity.setVariety(ap.getVariety());
                        importEntity.setShipmentData(ap.getShipmentData());
                        if(StringUtils.isBlank(importEntity.getVehicleModel())){
                            importEntity.setVehicleModel(ap.getVehicleModel());
                        }
                        if(StringUtils.isBlank(importEntity.getBatchNumber())){
                            importEntity.setBatchNumber(ap.getBatchNumber());
                        }
                    }
                }else{
                    resultEntities.add(importEntity);
                }
            }
        }
        for(RailwaySendKunMingEntity entity:chassisWarehouseQingDaoEntities){
            for(RailwaySendKunMingEntity importEntity:importEntities){
                if(StringUtils.isNotBlank(entity.getVin()) && StringUtils.isNotBlank(importEntity.getVin())){
                    if(entity.getVin().equals(importEntity.getVin()) && StringUtils.isNotBlank(importEntity.getShipmentData())){
                        addEntities.add(entity);
                    }
                }else{
                    resultEntities.add(importEntity);
                }
            }
        }

        if(resultEntities.size()>0){
            ZkUtils.showError("SGWM验收明细缺少VIN或中铁发昆明缺少发运时间,请检查原因重新导入", "系统提示");
        }else{
            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
            railwaySendKunMingRepository.deleteAllByVinIn(list);
            railwaySendKunMingRepository.saveAll(importEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;




    }

}
