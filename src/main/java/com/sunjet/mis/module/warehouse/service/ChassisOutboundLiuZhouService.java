package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.warehouse.entity.AcceptanceDetailEntity;
import com.sunjet.mis.module.warehouse.entity.ChassisOutboundLiuZhouEntity;
import com.sunjet.mis.module.warehouse.entity.RailwaySendKunMingEntity;
import com.sunjet.mis.module.warehouse.repository.AcceptanceDetailRepository;
import com.sunjet.mis.module.warehouse.repository.ChassisOutboundLiuZhouRepository;
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
@Service("chassisOutboundLiuZhouService")
public class ChassisOutboundLiuZhouService {

    @Autowired
    private ChassisOutboundLiuZhouRepository chassisOutboundLiuZhouRepository;
    @Autowired
    private AcceptanceDetailRepository acceptanceDetailRepository;

    public List<ChassisOutboundLiuZhouEntity> findAll() {
        try {
            return this.chassisOutboundLiuZhouRepository.findAll();
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
    public PageResult<ChassisOutboundLiuZhouEntity> getPageList(PageParam<ChassisOutboundLiuZhouEntity> pageParam) {
        //1.查询条件
        ChassisOutboundLiuZhouEntity chassisOutboundLiuZhouEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ChassisOutboundLiuZhouEntity> specification1 = null;
        Specification<ChassisOutboundLiuZhouEntity> specification2 = null;
        Specification<ChassisOutboundLiuZhouEntity> specification3 = null;

        //页面条件
        if (chassisOutboundLiuZhouEntity != null && StringUtils.isNotBlank(chassisOutboundLiuZhouEntity.getVin())) {
            specification2 = Specifications.<ChassisOutboundLiuZhouEntity>or()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(chassisOutboundLiuZhouEntity.getVin()), "vin", "%"+chassisOutboundLiuZhouEntity.getVin()+"%")
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
        specification1 = Specifications.<ChassisOutboundLiuZhouEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<ChassisOutboundLiuZhouEntity> pages = chassisOutboundLiuZhouRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

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

    public ChassisOutboundLiuZhouEntity findById(String id) {
        Optional<ChassisOutboundLiuZhouEntity> entityOptional = chassisOutboundLiuZhouRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public ChassisOutboundLiuZhouEntity save(ChassisOutboundLiuZhouEntity entity) {
        return chassisOutboundLiuZhouRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            chassisOutboundLiuZhouRepository.deleteById(id);
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
    public List<ChassisOutboundLiuZhouEntity> saveAll(List<ChassisOutboundLiuZhouEntity> importEntities) {
       // List<ChassisOutboundLiuZhouEntity> resultEntities = new ArrayList<>();
        List<ChassisOutboundLiuZhouEntity> addEntities = new ArrayList<>();
        HashSet<ChassisOutboundLiuZhouEntity> resultEntities =new HashSet<>();

        List<ChassisOutboundLiuZhouEntity> chassisWarehouseQingDaoEntities = chassisOutboundLiuZhouRepository.findAll();

        //sgmw验收明细

        List<AcceptanceDetailEntity> acceptanceDetailEntityList = acceptanceDetailRepository.findAll();
        for(ChassisOutboundLiuZhouEntity im:importEntities){
            for(AcceptanceDetailEntity ad:acceptanceDetailEntityList) {
                if (StringUtils.isNotBlank(im.getVin()) && StringUtils.isNotBlank(ad.getVin())) {
                    if (im.getVin().equals(ad.getVin())) {
                        im.setVehicleModel(ad.getVehicleModel());
                        im.setVariety(ad.getVariety());
                        im.setBatchNumber(ad.getBatchNumber());
                    }
                }else{
                    resultEntities.add(im);
                }
            }
        }

        for(ChassisOutboundLiuZhouEntity entity:chassisWarehouseQingDaoEntities){
            for(ChassisOutboundLiuZhouEntity importEntity:importEntities){
                if(entity.getVin().equals(importEntity.getVin())){
                    addEntities.add(entity);
                }
            }
        }

        if(resultEntities.size()>0){
            ZkUtils.showError("SGWM验收明细缺少VIN,请检查原因重新导入", "系统提示");
        }else{
            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
            chassisOutboundLiuZhouRepository.deleteAllByVinIn(list);
            chassisOutboundLiuZhouRepository.saveAll(importEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;

    }

}
