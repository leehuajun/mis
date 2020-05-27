package com.sunjet.mis.module.basic.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.basic.entity.VehicleTypeEntity;
import com.sunjet.mis.module.basic.repository.VehicleTypeRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: wushi
 * @description: 车辆类型
 * @Date: Created in 9:25 2019/3/8
 * @Modify by: wushi
 * @ModifyDate by: 9:25 2019/3/8
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("vehicleTypeService")
public class VehicleTypeService {

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;


    /**
     * @Author: wushi
     * @description: 删除
     * @Date: Created in 14:57 2019/3/8
     * @Modify by: wushi
     * @ModifyDate by: 14:57 2019/3/8
     */
    public boolean deleteById(String id) {
        try {
            vehicleTypeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Author: wushi
     * @description: 保存实体
     * @Date: Created in 14:57 2019/3/8
     * @Modify by: wushi
     * @ModifyDate by: 14:57 2019/3/8
     */
    public VehicleTypeEntity save(VehicleTypeEntity vehicleTypeEntity) {
        return vehicleTypeRepository.save(vehicleTypeEntity);

    }

    /**
     * 查询一个车辆类型
     *
     * @param id
     * @return
     */
    public VehicleTypeEntity findById(String id) {
        return vehicleTypeRepository.findById(id).orElse(null);
    }

    /**
     * 分页
     *
     * @param pageParam
     * @return
     */
    public PageResult<VehicleTypeEntity> getPageList(PageParam<VehicleTypeEntity> pageParam) {
        //1.查询条件
        VehicleTypeEntity vehicleTypeEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<VehicleTypeEntity> specification = null;
        //页面查询条件
        if (vehicleTypeEntity != null && StringUtils.isNotBlank(vehicleTypeEntity.getKey())) {
            specification = Specifications.<VehicleTypeEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(vehicleTypeEntity.getKey()), "key", "%" + vehicleTypeEntity.getKey() + "%")
                    .like(StringUtils.isNotEmpty(vehicleTypeEntity.getVehicleType()), "vehicleType", "%" + vehicleTypeEntity.getVehicleType() + "%")
                    .build();
        }

        //3.执行查询
        Page<VehicleTypeEntity> pages = vehicleTypeRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }
}
