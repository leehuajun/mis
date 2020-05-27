package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.report.entity.SolidPinEntity;
import com.sunjet.mis.module.report.repository.SolidPinRepository;
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
import java.util.stream.Collectors;

/**
 * @author SUNJET-YFS
 * @Title: SolidPinService
 * @ProjectName mis
 * @Description: 客户信息卡实体导入实体Service
 * @date 2019/1/2316:44
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("solidPinService")
public class SolidPinService {


    @Autowired
    private SolidPinRepository solidPinRepository;
//    @Autowired
//    private DistributorRepository distributorRepository;

    public PageResult<SolidPinEntity> getPageList(PageParam<SolidPinEntity> pageParam) {
        //1.查询条件
        SolidPinEntity solidPinEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<SolidPinEntity> specification = null;

        if (solidPinEntity != null) {
            specification = Specifications.<SolidPinEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotBlank(solidPinEntity.getVsn()), "vsn", "%" + solidPinEntity.getVsn() + "%")
                    .like(StringUtils.isNotBlank(solidPinEntity.getVin()), "vin", "%" + solidPinEntity.getVin() + "%")
                    .like(StringUtils.isNotBlank(solidPinEntity.getDistributorCode()), "distributorCode", "%" + solidPinEntity.getDistributorCode() + "%")
                    .like(StringUtils.isNotBlank(solidPinEntity.getDistributorName()), "distributorName", "%" + solidPinEntity.getDistributorName() + "%")
                    .between((solidPinEntity.getStartDate() != null && solidPinEntity.getEndDate() != null), "invoiceDate", solidPinEntity.getStartDate(), solidPinEntity.getEndDate())
                    .build();
        }


        Page<SolidPinEntity> pages = solidPinRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }


    /**
     * 每次根据vin覆盖或新增数据
     *
     * @param importEntities
     * @return
     */

    public List<SolidPinEntity> saveAll(List<SolidPinEntity> importEntities) {
        List<SolidPinEntity> resultEntities = new ArrayList<>();
        List<SolidPinEntity> addEntities = new ArrayList<>();
        try {
            List<SolidPinEntity> solidPinEntities = solidPinRepository.findAll();

            for (SolidPinEntity entity : importEntities) {
                for (SolidPinEntity importEntity : solidPinEntities) {
                    if (entity.getVin().equals(importEntity.getVin())) {
                        addEntities.add(entity);
                    }
                    resultEntities.add(entity);
                }
            }

                List<String> list = addEntities.stream().map(e -> e.getVin()).collect(Collectors.toList());
                solidPinRepository.deleteAllByVinIn(list);
                solidPinRepository.saveAll(importEntities);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return resultEntities;
        }
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<SolidPinEntity> findAll() {
        return solidPinRepository.findAll();
    }

    /**
     * @Description: 保存配车单明细
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:20
     */

    public SolidPinEntity save(SolidPinEntity solidPinEntity) {
        try {
            return solidPinRepository.save(solidPinEntity);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

    }

    /**
     * @Description: 删除对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:24
     */
    public boolean delete(SolidPinEntity solidPinEntity) {
        try {
            solidPinRepository.delete(solidPinEntity);
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
            // AllotVehicleEntity allotVehicleEntity=allotVehicleRepository.findOne(id);
            solidPinRepository.deleteById(id);
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
    public SolidPinEntity findById(String id) {
        try {
            SolidPinEntity solidPinEntity = solidPinRepository.findById(id).orElse(null);
            return solidPinEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
