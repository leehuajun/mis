package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.warehouse.entity.ChassisProcurementEntity;
import com.sunjet.mis.module.warehouse.entity.ChassisWarehouseQingDaoEntity;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("chassisWarehouseQingDaoService")
public class ChassisWarehouseQingDaoService {

    @Autowired
    private ChassisWarehouseQingDaoRepository chassisWarehouseQingDaoRepositoryng;


    public List<ChassisWarehouseQingDaoEntity> findAll() {
        try {
            return this.chassisWarehouseQingDaoRepositoryng.findAll();
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
    public PageResult<ChassisWarehouseQingDaoEntity> getPageList(PageParam<ChassisWarehouseQingDaoEntity> pageParam) {
        //1.查询条件
        ChassisWarehouseQingDaoEntity chassisWarehouseQingDaoEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ChassisWarehouseQingDaoEntity> specification1 = null;
        Specification<ChassisWarehouseQingDaoEntity> specification2 = null;
        Specification<ChassisWarehouseQingDaoEntity> specification3 = null;

        //页面条件
        if (chassisWarehouseQingDaoEntity != null && (StringUtils.isNotBlank(chassisWarehouseQingDaoEntity.getVin()) ||StringUtils.isNotBlank(chassisWarehouseQingDaoEntity.getVsn()) || StringUtils.isNotBlank(chassisWarehouseQingDaoEntity.getSharesProduceNumber()))) {
            specification2 = Specifications.<ChassisWarehouseQingDaoEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(chassisWarehouseQingDaoEntity.getVin()), "vin", "%"+chassisWarehouseQingDaoEntity.getVin()+"%")
                    .like(StringUtils.isNotEmpty(chassisWarehouseQingDaoEntity.getVsn()), "vsn", "%"+chassisWarehouseQingDaoEntity.getVsn()+"%")
                    .like(StringUtils.isNotEmpty(chassisWarehouseQingDaoEntity.getSharesProduceNumber()), "sharesProduceNumber", "%"+chassisWarehouseQingDaoEntity.getSharesProduceNumber()+"%")
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
        specification1 = Specifications.<ChassisWarehouseQingDaoEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<ChassisWarehouseQingDaoEntity> pages = chassisWarehouseQingDaoRepositoryng.findAll(specification1, PageUtil.getPageRequest(pageParam));

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

    public ChassisWarehouseQingDaoEntity findById(String id) {
        Optional<ChassisWarehouseQingDaoEntity> entityOptional = chassisWarehouseQingDaoRepositoryng.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public ChassisWarehouseQingDaoEntity save(ChassisWarehouseQingDaoEntity entity) {
        return chassisWarehouseQingDaoRepositoryng.save(entity);
    }

    public boolean deleteById(String id){
        try {
            chassisWarehouseQingDaoRepositoryng.deleteById(id);
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
    public List<ChassisWarehouseQingDaoEntity> saveAll(List<ChassisWarehouseQingDaoEntity> importEntities) {
//        List<ChassisWarehouseQingDaoEntity> resultEntities = new ArrayList<>();
        List<ChassisWarehouseQingDaoEntity> addEntities = new ArrayList<>();
        HashSet<ChassisWarehouseQingDaoEntity> resultEntities =new HashSet<>();

        List<ChassisWarehouseQingDaoEntity> chassisWarehouseQingDaoEntities = chassisWarehouseQingDaoRepositoryng.findAll();
        for(ChassisWarehouseQingDaoEntity entity:chassisWarehouseQingDaoEntities){
            for(ChassisWarehouseQingDaoEntity importEntity:importEntities){
                if(StringUtils.isNotBlank(entity.getVin()) && StringUtils.isNotBlank(importEntity.getVin())){
                    if(entity.getVin().equals(importEntity.getVin())){
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
            chassisWarehouseQingDaoRepositoryng.deleteAllByVinIn(list);
            chassisWarehouseQingDaoRepositoryng.saveAll(importEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;
    }

}
