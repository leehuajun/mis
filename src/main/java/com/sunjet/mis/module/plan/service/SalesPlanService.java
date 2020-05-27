package com.sunjet.mis.module.plan.service;

import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.module.basic.entity.ProvinceEntity;
import com.sunjet.mis.module.plan.entity.SalesPlanEntity;
import com.sunjet.mis.module.plan.repository.SalesPlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: SalesPlanService  经销商销售目标
 * @ProjectName mis
 * @Description: TODO
 * @date 2019/1/1115:29
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("SalesPlanService")
public class SalesPlanService {

    @Autowired
    SalesPlanRepository salesPlanRepository;


    /**
     * 查询所有
     *
     * @return
     */
    public List<SalesPlanEntity> findAll() {
        return salesPlanRepository.findAll();
    }


    /**
     * @Description: 经销商销售目标  保存
     * @Author: YFS
     * @CreateDate: 2019/1/11 15:31
     */
    public SalesPlanEntity save(SalesPlanEntity salesPlanEntity) {
        return salesPlanRepository.save(salesPlanEntity);

    }

    /**
     * @Description: 删除对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:24
     */
    public boolean delete(SalesPlanEntity salesPlanEntity) {
        try {
            salesPlanRepository.delete(salesPlanEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @Description: 通过ID删除实体
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:29
     */
    public boolean deleteById(String id) {
        try {
           //  SalesPlanEntity salesPlanEntity=salesPlanRepository.findById(id).orElse(null);
            salesPlanRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description: 通过ID查找实体
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:38
     */
    public SalesPlanEntity findById(String id) {
        try {
            return  salesPlanRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
