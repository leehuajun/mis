package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.warehouse.entity.*;
import com.sunjet.mis.module.warehouse.repository.AcceptanceDetailRepository;
import com.sunjet.mis.module.warehouse.repository.ChassisInventoryRepository;
import com.sunjet.mis.module.warehouse.repository.ChassisOutboundQingDaoRepository;
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
@Service("railwaySendLIUZHOUService")
public class RailwaySendLIUZHOUService {

    @Autowired
    private RailwaySendLIUZHOURepository railwaySendLIUZHOURepository;
    @Autowired
    private AcceptanceDetailRepository acceptanceDetailRepository;
    @Autowired
    private ChassisInventoryRepository chassisInventoryRepository;

    public List<RailwaySendLIUZHOUEntity> findAll() {
        try {
            return this.railwaySendLIUZHOURepository.findAll();
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
    public PageResult<RailwaySendLIUZHOUEntity> getPageList(PageParam<RailwaySendLIUZHOUEntity> pageParam) {
        //1.查询条件
        RailwaySendLIUZHOUEntity railwaySendLIUZHOUEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<RailwaySendLIUZHOUEntity> specification1 = null;
        Specification<RailwaySendLIUZHOUEntity> specification2 = null;
        Specification<RailwaySendLIUZHOUEntity> specification3 = null;

        //页面条件
        if (railwaySendLIUZHOUEntity != null && (StringUtils.isNotBlank(railwaySendLIUZHOUEntity.getVin()) || StringUtils.isNotBlank(railwaySendLIUZHOUEntity.getVariety()) || StringUtils.isNotBlank(railwaySendLIUZHOUEntity.getSharesProduceNumber()))) {
            specification2 = Specifications.<RailwaySendLIUZHOUEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(railwaySendLIUZHOUEntity.getVin()), "vin", "%"+railwaySendLIUZHOUEntity.getVin()+"%")
                    .like(StringUtils.isNotEmpty(railwaySendLIUZHOUEntity.getVariety()), "variety", "%"+railwaySendLIUZHOUEntity.getVariety()+"%")
                    .like(StringUtils.isNotEmpty(railwaySendLIUZHOUEntity.getSharesProduceNumber()), "sharesProduceNumber", "%"+railwaySendLIUZHOUEntity.getSharesProduceNumber()+"%")
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
        specification1 = Specifications.<RailwaySendLIUZHOUEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<RailwaySendLIUZHOUEntity> pages = railwaySendLIUZHOURepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

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

    public RailwaySendLIUZHOUEntity findById(String id) {
        Optional<RailwaySendLIUZHOUEntity> entityOptional = railwaySendLIUZHOURepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public RailwaySendLIUZHOUEntity save(RailwaySendLIUZHOUEntity entity) {
        return railwaySendLIUZHOURepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            railwaySendLIUZHOURepository.deleteById(id);
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
    public List<RailwaySendLIUZHOUEntity> saveAll(List<RailwaySendLIUZHOUEntity> importEntities) {
//        List<RailwaySendLIUZHOUEntity> resultEntities = new ArrayList<>();
        List<RailwaySendLIUZHOUEntity> addEntities = new ArrayList<>();
        List<AcceptanceDetailEntity> addChassis = new ArrayList<>();
        List<ChassisInventoryEntity> saveChassis = new ArrayList<>();
        List<ChassisInventoryEntity> removeChassis = new ArrayList<>();
        HashSet<RailwaySendLIUZHOUEntity> resultEntities =new HashSet<>();

        List<RailwaySendLIUZHOUEntity> chassisWarehouseQingDaoEntities = railwaySendLIUZHOURepository.findAll();
        for(RailwaySendLIUZHOUEntity entity:chassisWarehouseQingDaoEntities){
            for(RailwaySendLIUZHOUEntity importEntity:importEntities){
                if(StringUtils.isNotBlank(entity.getVin()) && StringUtils.isNotBlank(importEntity.getVin())){
                    if(entity.getVin().equals(importEntity.getVin())){
                        addEntities.add(entity);
                    }
                }else{
                    resultEntities.add(entity);
                }
            }
        }
        //根据收车状态的vin查出验收信息 添加到集合
        for(RailwaySendLIUZHOUEntity importEntity:importEntities){
            if(StringUtils.isNotBlank(importEntity.getState())){
                if(importEntity.getState().equals("收车")){
                    AcceptanceDetailEntity ad = acceptanceDetailRepository.findByVin(importEntity.getVin());
                    addChassis.add(ad);
                }
            }else{
                resultEntities.add(importEntity);
            }
        }
        if(addChassis.size()>0) {
            for (AcceptanceDetailEntity ad : addChassis) {
                if (ad != null) {
                    ChassisInventoryEntity ci = new ChassisInventoryEntity();
                    ci.setOrderId(ad.getOrderId());
                    ci.setVin(ad.getVin());
                    ci.setVsn(ad.getVsn());
                    ci.setEngineCode(ad.getEngineCode());
                    ci.setShipmentData(ad.getShipmentData());
                    ci.setBatchNumber(ad.getBatchNumber());
                    ci.setColor(ad.getColor());
                    ci.setQualifiedCertificate(ad.getQualifiedCertificate());
                    ci.setBaseCode(ad.getBaseCode());
                    ci.setMakeData(ad.getMakeData());
                    ci.setVehicleModel(ad.getVehicleModel());
                    ci.setDataSource("中铁发柳州");
                    ci.setVariety(ad.getVariety());
                    saveChassis.add(ci);
                }
            }
        }
        //查询库存中的数据
        List<ChassisInventoryEntity> chassisInventoryEntities = chassisInventoryRepository.findAll();
        //将库存中已有车辆添加到集合
        for(ChassisInventoryEntity ci:chassisInventoryEntities){
            for(AcceptanceDetailEntity ad:addChassis){
                if(StringUtils.isNotBlank(ci.getVin()) && StringUtils.isNotBlank(ad.getVin())){
                    if (ci.getVin().equals(ad.getVin())) {
                        removeChassis.add(ci);
                    }
                }
            }
        }
        if(resultEntities.size()>0){
            ZkUtils.showError("缺少VIN或缺少车辆状态,请检查原因重新导入", "系统提示");
        }else{
            //删除已有车辆
            List<String> chassisList = removeChassis.stream().map(e -> e.getVin()).collect(Collectors.toList());
            chassisInventoryRepository.deleteAllByVinIn(chassisList);
            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
            if(list.size()>0) {
                railwaySendLIUZHOURepository.deleteAllByVinIn(list);
            }
            //保存收车车辆到库存
            if(saveChassis.size()>0) {
                chassisInventoryRepository.saveAll(saveChassis);
            }
            railwaySendLIUZHOURepository.saveAll(importEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;


    }

}
