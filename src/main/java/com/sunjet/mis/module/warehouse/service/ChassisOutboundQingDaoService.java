package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.warehouse.entity.AcceptanceDetailEntity;
import com.sunjet.mis.module.warehouse.entity.ChassisOutboundQINGDAOEntity;
import com.sunjet.mis.module.warehouse.entity.ChassisWarehouseQingDaoEntity;
import com.sunjet.mis.module.warehouse.repository.AcceptanceDetailRepository;
import com.sunjet.mis.module.warehouse.repository.ChassisOutboundQingDaoRepository;
import com.sunjet.mis.module.warehouse.repository.ChassisWarehouseQingDaoRepository;
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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("chassisOutboundQingDaoService")
public class ChassisOutboundQingDaoService {

    @Autowired
    private ChassisOutboundQingDaoRepository chassisOutboundQingDaoRepository;
    @Autowired
    private AcceptanceDetailRepository acceptanceDetailRepository;

    public List<ChassisOutboundQINGDAOEntity> findAll() {
        try {
            return this.chassisOutboundQingDaoRepository.findAll();
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
    public PageResult<ChassisOutboundQINGDAOEntity> getPageList(PageParam<ChassisOutboundQINGDAOEntity> pageParam) {
        //1.查询条件
        ChassisOutboundQINGDAOEntity chassisWarehouseQingDaoEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ChassisOutboundQINGDAOEntity> specification1 = null;
        Specification<ChassisOutboundQINGDAOEntity> specification2 = null;
        Specification<ChassisOutboundQINGDAOEntity> specification3 = null;

        //页面条件
        if (chassisWarehouseQingDaoEntity != null && (StringUtils.isNotBlank(chassisWarehouseQingDaoEntity.getVin()) || StringUtils.isNotBlank(chassisWarehouseQingDaoEntity.getVsn()))) {
            specification2 = Specifications.<ChassisOutboundQINGDAOEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(chassisWarehouseQingDaoEntity.getVin()), "vin", "%"+chassisWarehouseQingDaoEntity.getVin()+"%")
                    .like(StringUtils.isNotEmpty(chassisWarehouseQingDaoEntity.getVsn()), "vsn", "%"+chassisWarehouseQingDaoEntity.getVsn()+"%")
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
        specification1 = Specifications.<ChassisOutboundQINGDAOEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<ChassisOutboundQINGDAOEntity> pages = chassisOutboundQingDaoRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

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

    public ChassisOutboundQINGDAOEntity findById(String id) {
        Optional<ChassisOutboundQINGDAOEntity> entityOptional = chassisOutboundQingDaoRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public ChassisOutboundQINGDAOEntity save(ChassisOutboundQINGDAOEntity entity) {
        return chassisOutboundQingDaoRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            chassisOutboundQingDaoRepository.deleteById(id);
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
    public List<ChassisOutboundQINGDAOEntity> saveAll(List<ChassisOutboundQINGDAOEntity> importEntities) {
//        List<ChassisOutboundQINGDAOEntity> resultEntities = new ArrayList<>();
        List<ChassisOutboundQINGDAOEntity> addEntities = new ArrayList<>();
        HashSet<ChassisOutboundQINGDAOEntity> resultEntities=new HashSet();

        List<ChassisOutboundQINGDAOEntity> chassisWarehouseQingDaoEntities = chassisOutboundQingDaoRepository.findAll();
        for(ChassisOutboundQINGDAOEntity entity:chassisWarehouseQingDaoEntities){
            for(ChassisOutboundQINGDAOEntity importEntity:importEntities){
                if(StringUtils.isNotBlank(entity.getVin()) && StringUtils.isNotBlank(importEntity.getVin())){
                    if(entity.getVin().equals(importEntity.getVin())){
                        addEntities.add(entity);
                    }
                }
            }
        }
        List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
        chassisOutboundQingDaoRepository.deleteAllByVinIn(list);

        List<AcceptanceDetailEntity> vm = acceptanceDetailRepository.findAll();
        for(ChassisOutboundQINGDAOEntity coqd:importEntities){
            if(coqd.getVsn()!=null && StringUtils.isNotBlank(coqd.getVsn())) {
                coqd.setVariety(coqd.getVsn().substring(0, 4));
            }
            for(AcceptanceDetailEntity ad:vm){
                if(StringUtils.isNotBlank(coqd.getVin()) && StringUtils.isNotBlank(ad.getVin())) {
                    if (coqd.getVin().equals(ad.getVin())) {
                        coqd.setVariety(ad.getVariety());
                        coqd.setVehicleModel(ad.getVehicleModel());
                    }
                }else {
                    resultEntities.add(coqd);
                }
            }
        }
        if(resultEntities.size()>0){
            ZkUtils.showError("SGMW验收明细中未找到以下vin", "系统提示");
        }else {
            chassisOutboundQingDaoRepository.saveAll(importEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;

    }

}
