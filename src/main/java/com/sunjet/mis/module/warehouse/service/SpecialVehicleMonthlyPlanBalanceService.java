package com.sunjet.mis.module.warehouse.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.warehouse.entity.SpecialVehicleMonthlyPlanBalanceEntity;
import com.sunjet.mis.module.warehouse.repository.SpecialVehicleMonthlyPlanBalanceRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author SUNJET-YFS
 * @Title: SpecialVehicleMonthlyPlanBalanceService
 * @ProjectName mis
 * @Description: 专用车月计划平衡详细表
 * @date 2019/4/1014:10
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("specialVehicleMonthlyPlanBalanceService")
public class SpecialVehicleMonthlyPlanBalanceService {

    @Autowired
    private SpecialVehicleMonthlyPlanBalanceRepository specialVehicleMonthlyPlanBalanceRepository;

    public List<SpecialVehicleMonthlyPlanBalanceEntity> findAll() {
        try {
            return this.specialVehicleMonthlyPlanBalanceRepository.findAll();
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
    public PageResult<SpecialVehicleMonthlyPlanBalanceEntity> getPageList(PageParam<SpecialVehicleMonthlyPlanBalanceEntity> pageParam) {
        //1.查询条件

            //1.查询条件
            SpecialVehicleMonthlyPlanBalanceEntity specialVehicleMonthlyPlanBalanceEntity = pageParam.getInfoWhere();
            //2.设置查询参数
            Specification<SpecialVehicleMonthlyPlanBalanceEntity> specification = null;
            if (specialVehicleMonthlyPlanBalanceEntity != null) {
                specification = Specifications.<SpecialVehicleMonthlyPlanBalanceEntity>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .like(StringUtils.isNotBlank(specialVehicleMonthlyPlanBalanceEntity.getVsn()), "vsn","%"+ specialVehicleMonthlyPlanBalanceEntity.getVsn()+"%")
                        .like(StringUtils.isNotBlank(specialVehicleMonthlyPlanBalanceEntity.getVehicleType1()), "vehicleType1","%"+ specialVehicleMonthlyPlanBalanceEntity.getVehicleType1()+"%")
                        .like(StringUtils.isNotBlank(specialVehicleMonthlyPlanBalanceEntity.getDistributorCode()), "distributorCode", "%"+specialVehicleMonthlyPlanBalanceEntity.getDistributorCode()+"%")
                        .between((specialVehicleMonthlyPlanBalanceEntity.getStartDate() != null && specialVehicleMonthlyPlanBalanceEntity.getEndDate() != null), "dateYearMonth", specialVehicleMonthlyPlanBalanceEntity.getStartDate(), specialVehicleMonthlyPlanBalanceEntity.getEndDate())
                        .build();
            }
            //3.执行查询
            Page<SpecialVehicleMonthlyPlanBalanceEntity> pages = specialVehicleMonthlyPlanBalanceRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

            //5.返回
            return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
        }

    public SpecialVehicleMonthlyPlanBalanceEntity findById(String id) {
        Optional<SpecialVehicleMonthlyPlanBalanceEntity> entityOptional = specialVehicleMonthlyPlanBalanceRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public SpecialVehicleMonthlyPlanBalanceEntity save(SpecialVehicleMonthlyPlanBalanceEntity entity) {
        return specialVehicleMonthlyPlanBalanceRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            specialVehicleMonthlyPlanBalanceRepository.deleteById(id);
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
    public List<SpecialVehicleMonthlyPlanBalanceEntity> saveAll(List<SpecialVehicleMonthlyPlanBalanceEntity> importEntities) {

       List<SpecialVehicleMonthlyPlanBalanceEntity> resultEntities = new ArrayList<>();
        List<SpecialVehicleMonthlyPlanBalanceEntity> addEntities = new ArrayList<>();
        try {
            List<SpecialVehicleMonthlyPlanBalanceEntity> specialVehicleMonthlyPlanBalanceEntities = specialVehicleMonthlyPlanBalanceRepository.findAll();
            //导入循环
            for (SpecialVehicleMonthlyPlanBalanceEntity entity : importEntities) {

                for (SpecialVehicleMonthlyPlanBalanceEntity importEntity : specialVehicleMonthlyPlanBalanceEntities) {
                    if (entity.getSsid().equals(importEntity.getSsid())) {
                        addEntities.add(entity);
                    }
                    resultEntities.add(entity);
                }
            }
            //2. 删除对应vin的数据
            List<String> list = addEntities.stream().map(e -> e.getSsid()).collect(Collectors.toList());
            specialVehicleMonthlyPlanBalanceRepository.deleteAllBySsidIn(list);
            //3. 保存数据
            specialVehicleMonthlyPlanBalanceRepository.saveAll(importEntities);

        } catch (Exception e) {
            e.printStackTrace();
            throw new MisException(e.getMessage());
        } finally {
            return resultEntities;
        }

    }

  }
