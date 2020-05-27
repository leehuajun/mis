package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.warehouse.entity.RefittingTruckMaterialEntity;
import com.sunjet.mis.module.warehouse.repository.RefittingTruckMaterialRepostory;
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
import java.util.Optional;

/**
 * @author SUNJET-YFS
 * @Title: RefittingTruckMaterialService
 * @ProjectName mis
 * @Description: 改装车某月物料申报表
 * @date 2019/4/1110:13
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("refittingTruckMaterialService")
public class RefittingTruckMaterialService {

    @Autowired
    private RefittingTruckMaterialRepostory  refittingTruckMaterialRepostory;

    public List<RefittingTruckMaterialEntity> findAll() {
        try {
            return this.refittingTruckMaterialRepostory.findAll();
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
    public PageResult<RefittingTruckMaterialEntity> getPageList(PageParam<RefittingTruckMaterialEntity> pageParam) {
        //1.查询条件

        //1.查询条件
        RefittingTruckMaterialEntity refittingTruckMaterialEntity = pageParam.getInfoWhere();
        //2.设置查询参数
        Specification<RefittingTruckMaterialEntity> specification = null;
        if (refittingTruckMaterialEntity != null) {
                specification = Specifications.<RefittingTruckMaterialEntity>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .like(StringUtils.isNotBlank(refittingTruckMaterialEntity.getVsn()), "vsn","%"+ refittingTruckMaterialEntity.getVsn()+"%")
                        .like(StringUtils.isNotBlank(refittingTruckMaterialEntity.getBrandCode()), "brandCode", "%"+refittingTruckMaterialEntity.getBrandCode()+"%")
                        .like(StringUtils.isNotBlank(refittingTruckMaterialEntity.getTerrace()), "terrace","%"+ refittingTruckMaterialEntity.getTerrace()+"%")
                        .between((refittingTruckMaterialEntity.getStartDate() != null && refittingTruckMaterialEntity.getEndDate() != null), "dateYearMonth", refittingTruckMaterialEntity.getStartDate(), refittingTruckMaterialEntity.getEndDate())
                        .build();
        }
        //3.执行查询
        Page<RefittingTruckMaterialEntity> pages = refittingTruckMaterialRepostory.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    public RefittingTruckMaterialEntity findById(String id) {
        Optional<RefittingTruckMaterialEntity> entityOptional = refittingTruckMaterialRepostory.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public RefittingTruckMaterialEntity save(RefittingTruckMaterialEntity entity) {
        return refittingTruckMaterialRepostory.save(entity);
    }

    public boolean deleteById(String id){
        try {
            refittingTruckMaterialRepostory.deleteById(id);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     新增数据
     * @param importEntities
     * @return
     */
    public List<RefittingTruckMaterialEntity> saveAll(List<RefittingTruckMaterialEntity> importEntities) {


        return refittingTruckMaterialRepostory.saveAll(importEntities);



    }
//
//    List<SpecialVehicleMonthlyPlanBalanceEntity> findByVehicleModel(String vehicleModel, String variety, String batchNumber, Integer year, Integer month){
//        return specialVehicleMonthlyPlanBalanceRepository.findByVehicleModel(vehicleModel,variety,batchNumber,year,month);
//    }




}
