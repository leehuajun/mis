package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.report.entity.ProcessDataSheetsDailyEntity;
import com.sunjet.mis.module.report.repository.ProcessDataSheetsDailyRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: ProcessDataSheetsDailyService
 * @ProjectName mis
 * @Description: TODO
 * @date 2019/2/2616:04
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("processDataSheetsDailyService")
public class ProcessDataSheetsDailyService {

    @Autowired
    private ProcessDataSheetsDailyRepository processDataSheetsDailyRepository;

    public PageResult<ProcessDataSheetsDailyEntity> getPageList(PageParam<ProcessDataSheetsDailyEntity> pageParam) {
        //1.查询条件
        ProcessDataSheetsDailyEntity processDataSheetsDailyEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ProcessDataSheetsDailyEntity> specification = null;
//        Specification<SolidPinEntity> specification2 = null;
//        Specification<SolidPinEntity> specification3 = null;

        //页面查询条件
        //if (pageParam.getUserType() == UserType.INTERNAL.getIndex()) {
//            if (processDataSheetsDailyEntity != null ) {
//                specification = Specifications.<ProcessDataSheetsDailyEntity>and()
//                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
//                        // .eq(StringUtils.isNotEmpty(solidPinEntity.getSgmwCode()), "sgmwCode", solidPinEntity.getSgmwCode())
//                        .like(StringUtils.isNotEmpty(solidPinEntity.getDistributorCode()), "distributorCode", "%" + solidPinEntity.getDistributorCode() + "%")
//                        .like(StringUtils.isNotEmpty(solidPinEntity.getCustomerCode()), "customerCode", "%" + solidPinEntity.getCustomerCode() + "%")
//                        .like(StringUtils.isNotEmpty(solidPinEntity.getCustomerName()), "customerName", "%" + solidPinEntity.getCustomerName()+ "%")
//                        .like(StringUtils.isNotEmpty(solidPinEntity.getVin()), "vin", "%" + solidPinEntity.getVin() + "%")
//                        .like(StringUtils.isNotEmpty(solidPinEntity.getVsn()), "vsn", "%" + solidPinEntity.getVsn() + "%")
//                        .build();
//            }

       // }
        /*else {
            if (solidPinEntity != null && StringUtils.isNotBlank(solidPinEntity.getVin())) {
                specification2 = Specifications.<SolidPinEntity>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                      //  .like(StringUtils.isNotEmpty(solidPinEntity.getSgmwCode()), "sgmwCode", "%" + solidPinEntity.getSgmwCode() + "%")
                        .like(StringUtils.isNotEmpty(solidPinEntity.getAgencyCode()), "agencyCode", "%" + solidPinEntity.getAgencyCode() + "%")
                        .like(StringUtils.isNotEmpty(solidPinEntity.getDistributorCode()), "distributorCode", "%" + solidPinEntity.getDistributorCode() + "%")
                        .like(StringUtils.isNotEmpty(solidPinEntity.getDistributorCode()), "distributorCode", "%" + solidPinEntity.getDistributorCode() + "%")
                        .like(StringUtils.isNotEmpty(solidPinEntity.getDistributorName()), "distributorName", "%" + solidPinEntity.getDistributorName() + "%")
                        .like(StringUtils.isNotEmpty(solidPinEntity.getVin()), "vin", "%" + solidPinEntity.getVin() + "%")
                        .like(StringUtils.isNotEmpty(solidPinEntity.getVsn()), "vsn", "%" + solidPinEntity.getVsn() + "%")
                        .build();
            }*/
        //}
        //组合查询条件
        //  specification = Specifications.<SolidPinEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();

        //3.执行查询

        Page<ProcessDataSheetsDailyEntity> pages = processDataSheetsDailyRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }
    /**
     * @param importEntities
     * @return
     */
    public List<ProcessDataSheetsDailyEntity> saveAll(List<ProcessDataSheetsDailyEntity> importEntities) {

        return processDataSheetsDailyRepository.saveAll(importEntities);

    }

    /**
     * 查询所有
     * @return
     */
    public List<ProcessDataSheetsDailyEntity> findAll() {
        return processDataSheetsDailyRepository.findAll();
    }

    /**
     * @Description: 保存配车单明细
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:20
     */

    public ProcessDataSheetsDailyEntity save(ProcessDataSheetsDailyEntity solidPinEntity) {
        try {
            return processDataSheetsDailyRepository.save(solidPinEntity);
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
    public boolean delete(ProcessDataSheetsDailyEntity processDataSheetsDailyEntity) {
        try {
            processDataSheetsDailyRepository.delete(processDataSheetsDailyEntity);
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
            processDataSheetsDailyRepository.deleteById(id);
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
    public ProcessDataSheetsDailyEntity findById(String id) {
        try {
            ProcessDataSheetsDailyEntity solidPinEntity = processDataSheetsDailyRepository.findById(id).orElse(null);
            return solidPinEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
