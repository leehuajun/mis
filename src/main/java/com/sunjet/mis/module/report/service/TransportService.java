package com.sunjet.mis.module.report.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.basic.repository.DistributorRepository;
import com.sunjet.mis.module.report.entity.SolidPinEntity;
import com.sunjet.mis.module.report.entity.TransportEntity;
import com.sunjet.mis.module.report.repository.TransportRepository;
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
/*在途报表*/
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("transportService")
public class TransportService {

    @Autowired
    private TransportRepository transportRepository;
//    @Autowired
//    private DistributorRepository distributorRepository;


        //1.查询条件
        public PageResult<TransportEntity> getPageList(PageParam<TransportEntity> pageParam) {
            //1.查询条件
            TransportEntity transportEntity = pageParam.getInfoWhere();

            //2.设置查询参数
            Specification<TransportEntity> specification = null;

            if (transportEntity != null) {
                specification = Specifications.<TransportEntity>and()
                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
                        .like(StringUtils.isNotBlank(transportEntity.getVin()), "vin", "%" + transportEntity.getVin() + "%")
                        .like(StringUtils.isNotBlank(transportEntity.getDistributorName()), "distributorName", "%" + transportEntity.getDistributorName() + "%")
                       // .like(StringUtils.isNotBlank(solidPinEntity.getDistributorCode()), "distributorCode", "%" + solidPinEntity.getDistributorCode() + "%")
                       // .like(StringUtils.isNotBlank(solidPinEntity.getDistributorName()), "distributorName", "%" + solidPinEntity.getDistributorName() + "%")
                       // .between((solidPinEntity.getStartDate() != null && solidPinEntity.getEndDate() != null), "invoiceDate", solidPinEntity.getStartDate(), solidPinEntity.getEndDate())
                        .build();
            }


            Page<TransportEntity> pages = transportRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

            //5.返回
            return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
        }


    /**
     * 每次根据vin覆盖或新增数据
     *
     * @param importEntities
     * @return
     */
    public List<TransportEntity> saveAll(List<TransportEntity> importEntities) {
        return transportRepository.saveAll(importEntities);
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<TransportEntity> findAll() {
        return transportRepository.findAll();
    }

    /**
     * @Description: 库存车辆状态跟踪   保存
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:56
     */
    public TransportEntity save(TransportEntity transportEntity) {
        try {
            return transportRepository.save(transportEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @Description: 通过id删除对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 15:02
     */
    public boolean deleteById(String id) {
        try {
            transportRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @Description: 通过id查询对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 15:04
     */

    public TransportEntity findById(String id) {
        try {
            return transportRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<TransportEntity> importData(List<TransportEntity> importEntities) {
        return transportRepository.saveAll(importEntities);
    }
}
