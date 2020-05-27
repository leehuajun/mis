package com.sunjet.mis.module.basic.service;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.basic.entity.DistributorEntity;
import com.sunjet.mis.module.basic.entity.VehicleModelEntity;
import com.sunjet.mis.module.basic.repository.VehicleModelRepository;
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

/**
 * 车型管理 Service
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("vehicleModelService")
public class VehicleModelService {

    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    private List<VehicleModelEntity> findAll(){
        return vehicleModelRepository.findAll();
    }

    /**
     * 车型管理查询分页方法
     * @param pageParam
     * @return
     */
    public PageResult<VehicleModelEntity> getPageList(PageParam<VehicleModelEntity> pageParam){
        //1.查询条件
        VehicleModelEntity vehicleModelEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<VehicleModelEntity> specification = null;
        //页面查询条件
        if (vehicleModelEntity != null && (StringUtils.isNotBlank(vehicleModelEntity.getCode()) || StringUtils.isNotBlank(vehicleModelEntity.getVehicleSeries()))) {
            specification = Specifications.<VehicleModelEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(vehicleModelEntity.getCode()), "code",  "%" + vehicleModelEntity.getCode() + "%")
                    .like(StringUtils.isNotEmpty(vehicleModelEntity.getVehicleSeries()), "vehicleSeries",  "%" + vehicleModelEntity.getVehicleSeries() + "%")
                    .build();
        }

        //3.执行查询
        Page<VehicleModelEntity> pages = vehicleModelRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 保存车型管理信息
     * @param vehicleModelEntity
     * @return
     */

    public VehicleModelEntity save(VehicleModelEntity vehicleModelEntity){
        return vehicleModelRepository.save(vehicleModelEntity);
    }

    public List<VehicleModelEntity> saveAll(List<VehicleModelEntity> importEntities) {
        List<VehicleModelEntity> resultEntities = new ArrayList<>();
        List<VehicleModelEntity> addEntities = new ArrayList<>();
        //3. 保存数据
        vehicleModelRepository.saveAll(importEntities);
        return resultEntities;
    }

    /**
     * 查询一个实体
     * @param id
     * @return
     */
    public VehicleModelEntity findById(String id) {
        return vehicleModelRepository.findById(id).orElse(null);
    }


    /**
     * 删除对象
     * @param id
     */
    public boolean deleteById(String id) {
        try {
            vehicleModelRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
    }
}
