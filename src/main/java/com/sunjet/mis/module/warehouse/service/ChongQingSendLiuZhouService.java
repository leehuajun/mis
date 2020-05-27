package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.warehouse.entity.AcceptanceDetailEntity;
import com.sunjet.mis.module.warehouse.entity.ChongQingSendLiuZhouEntity;
import com.sunjet.mis.module.warehouse.entity.RailwaySendLIUZHOUEntity;
import com.sunjet.mis.module.warehouse.repository.AcceptanceDetailRepository;
import com.sunjet.mis.module.warehouse.repository.ChongQingSendLiuZhouRepository;
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
@Service("chongQingSendLiuZhouService")
public class ChongQingSendLiuZhouService {

    @Autowired
    private ChongQingSendLiuZhouRepository chongQingSendLiuZhouRepository;
    @Autowired
    private AcceptanceDetailRepository acceptanceDetailRepository;

    public List<ChongQingSendLiuZhouEntity> findAll() {
        try {
            return this.chongQingSendLiuZhouRepository.findAll();
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
    public PageResult<ChongQingSendLiuZhouEntity> getPageList(PageParam<ChongQingSendLiuZhouEntity> pageParam) {
        //1.查询条件
        ChongQingSendLiuZhouEntity chongQingSendLiuZhouEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ChongQingSendLiuZhouEntity> specification1 = null;
        Specification<ChongQingSendLiuZhouEntity> specification2 = null;
        Specification<ChongQingSendLiuZhouEntity> specification3 = null;

        //页面条件
        if (chongQingSendLiuZhouEntity != null && (StringUtils.isNotBlank(chongQingSendLiuZhouEntity.getVin()) || StringUtils.isNotBlank(chongQingSendLiuZhouEntity.getVsn()) || StringUtils.isNotBlank(chongQingSendLiuZhouEntity.getVehicleModel()))) {
            specification2 = Specifications.<ChongQingSendLiuZhouEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(chongQingSendLiuZhouEntity.getVin()), "vin", "%"+chongQingSendLiuZhouEntity.getVin()+"%")
                    .like(StringUtils.isNotEmpty(chongQingSendLiuZhouEntity.getVsn()), "vsn", "%"+chongQingSendLiuZhouEntity.getVsn()+"%")
                    .like(StringUtils.isNotEmpty(chongQingSendLiuZhouEntity.getVehicleModel()), "vehicleModel", "%"+chongQingSendLiuZhouEntity.getVehicleModel()+"%")
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
        specification1 = Specifications.<ChongQingSendLiuZhouEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<ChongQingSendLiuZhouEntity> pages = chongQingSendLiuZhouRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

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

    public ChongQingSendLiuZhouEntity findById(String id) {
        Optional<ChongQingSendLiuZhouEntity> entityOptional = chongQingSendLiuZhouRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public ChongQingSendLiuZhouEntity save(ChongQingSendLiuZhouEntity entity) {
        return chongQingSendLiuZhouRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            chongQingSendLiuZhouRepository.deleteById(id);
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
    public List<ChongQingSendLiuZhouEntity> saveAll(List<ChongQingSendLiuZhouEntity> importEntities) {
        //List<ChongQingSendLiuZhouEntity> resultEntities = new ArrayList<>();
        List<ChongQingSendLiuZhouEntity> addEntities = new ArrayList<>();
        HashSet<ChongQingSendLiuZhouEntity> resultEntities =new HashSet<>();

        List<ChongQingSendLiuZhouEntity> chassisWarehouseQingDaoEntities = chongQingSendLiuZhouRepository.findAll();
        for(ChongQingSendLiuZhouEntity entity:chassisWarehouseQingDaoEntities){
            for(ChongQingSendLiuZhouEntity importEntity:importEntities){
                if(StringUtils.isNotBlank(importEntity.getVsn()) && StringUtils.isNotBlank(entity.getVin()) && StringUtils.isNotBlank(importEntity.getVin())){
                    if(entity.getVin().equals(importEntity.getVin())){
                        importEntity.setVariety(importEntity.getVsn().substring(0,4));
                        addEntities.add(entity);
                    }
                }else{
                    resultEntities.add(importEntity);
                }
            }
        }
        if(resultEntities.size()>0){
            ZkUtils.showError("缺少VSN或VIN,请检查原因重新导入", "系统提示");
        }else{
            List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
            chongQingSendLiuZhouRepository.deleteAllByVinIn(list);
            chongQingSendLiuZhouRepository.saveAll(importEntities);
        }
        List conversionList = new ArrayList(resultEntities);
        return conversionList;

    }

}
