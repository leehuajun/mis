package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.warehouse.entity.AcceptanceDetailEntity;
import com.sunjet.mis.module.warehouse.entity.ChassisProcurementEntity;
import com.sunjet.mis.module.warehouse.repository.AcceptanceDetailRepository;
import com.sunjet.mis.module.warehouse.repository.ChassisProcurementRepository;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("acceptanceDetailService")
public class AcceptanceDetailService {

    @Autowired
    private AcceptanceDetailRepository acceptanceDetailRepository;

    public List<AcceptanceDetailEntity> findAll() {
        try {
            return this.acceptanceDetailRepository.findAll();
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
    public PageResult<AcceptanceDetailEntity> getPageList(PageParam<AcceptanceDetailEntity> pageParam) {
        //1.查询条件
        AcceptanceDetailEntity acceptanceDetailEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<AcceptanceDetailEntity> specification1 = null;
        Specification<AcceptanceDetailEntity> specification2 = null;
        Specification<AcceptanceDetailEntity> specification3 = null;

        //页面条件
        if (acceptanceDetailEntity != null && StringUtils.isNotBlank(acceptanceDetailEntity.getBatchNumber()) || StringUtils.isNotBlank(acceptanceDetailEntity.getVehicleModel()) ||StringUtils.isNotBlank(acceptanceDetailEntity.getVariety()) || StringUtils.isNotBlank(acceptanceDetailEntity.getVin())) {
            specification2 = Specifications.<AcceptanceDetailEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(acceptanceDetailEntity.getBatchNumber()), "batchNumber", "%"+acceptanceDetailEntity.getBatchNumber()+"%")
                    .like(StringUtils.isNotEmpty(acceptanceDetailEntity.getVehicleModel()), "vehicleModel", "%"+acceptanceDetailEntity.getVehicleModel()+"%")
                    .like(StringUtils.isNotEmpty(acceptanceDetailEntity.getVariety()), "variety", "%"+acceptanceDetailEntity.getVariety()+"%")
                    .like(StringUtils.isNotEmpty(acceptanceDetailEntity.getVin()), "vin", "%"+acceptanceDetailEntity.getVin()+"%")
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
        specification1 = Specifications.<AcceptanceDetailEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<AcceptanceDetailEntity> pages = acceptanceDetailRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

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

    public AcceptanceDetailEntity findById(String id) {
        Optional<AcceptanceDetailEntity> entityOptional = acceptanceDetailRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public AcceptanceDetailEntity save(AcceptanceDetailEntity entity) {
        return acceptanceDetailRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            acceptanceDetailRepository.deleteById(id);
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
    public List<AcceptanceDetailEntity> saveAll(List<AcceptanceDetailEntity> importEntities) {
//        List<AcceptanceDetailEntity> resultEntities = new ArrayList<>();
        List<AcceptanceDetailEntity> addEntities = new ArrayList<>();
        HashSet<AcceptanceDetailEntity> resultEntities =new HashSet<>();

        List<AcceptanceDetailEntity> availableInventoryEntityList = acceptanceDetailRepository.findAll();
        for(AcceptanceDetailEntity db:importEntities){
            if(StringUtils.isNotBlank(db.getVsn())){
                db.setVariety(db.getVsn().substring(0,4));
            }else{
                resultEntities.add(db);
            }
            for(AcceptanceDetailEntity ad:availableInventoryEntityList){
                if(StringUtils.isNotBlank(ad.getVin())){
                    if(ad.getVin().equals(db.getVin())){
                        addEntities.add(ad);
                    }
                }else {
                    resultEntities.add(db);
                }
            }
        }
        if(resultEntities.size()>0){
            ZkUtils.showError("缺少VSN或VIN,请检查原因重新导入", "系统提示");
        }else{
            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
            acceptanceDetailRepository.deleteAllByVinIn(list);
            acceptanceDetailRepository.saveAll(importEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;



    }

}
