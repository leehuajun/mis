package com.sunjet.mis.module.sales.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.sales.entity.SalesOrderEntity;
import com.sunjet.mis.module.sales.repository.SalesOrderRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: SalesOrderService
 * @ProjectName mis
 * @Description: 销售订单
 * @date 2019/1/1115:45
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("salesOrderService")
public class SalesOrderService {
    @Autowired
    private SalesOrderRepository salesOrderRepository;


    /**
     * 查询所有
     *
     * @return
     */
    public List<SalesOrderEntity> findAll() {
        return salesOrderRepository.findAll();
    }


    /**
     * @Description: 经销商销售目标  保存
     * @Author: YFS
     * @CreateDate: 2019/1/11 15:31
     */
    public SalesOrderEntity save(SalesOrderEntity salesOrderEntity) {
        return salesOrderRepository.save(salesOrderEntity);

    }

    /**
     * @Description: 删除对象
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:24
     */
    public boolean delete(SalesOrderEntity salesOrderEntity) {
        try {
            salesOrderRepository.delete(salesOrderEntity);
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
            SalesOrderEntity salesOrderEntity = salesOrderRepository.findById(id).orElse(null);
            salesOrderRepository.deleteById(id);
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
    public SalesOrderEntity findById(String id) {
        try {
            SalesOrderEntity salesOrderEntity = salesOrderRepository.findById(id).orElse(null);
            return salesOrderEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取分页数据
     *
     * @param pageParam
     * @return
     */
    public PageResult<SalesOrderEntity> getPageList(PageParam<SalesOrderEntity> pageParam) {
        //1.查询条件
        SalesOrderEntity salesOrderEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<SalesOrderEntity> specification = null;

        //页面查询条件
        if (salesOrderEntity != null) {
            specification = Specifications.<SalesOrderEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(salesOrderEntity.getDistributorName()), "distributorName", "%" + salesOrderEntity.getDistributorName() + "%")
                    .like(StringUtils.isNotEmpty(salesOrderEntity.getVsn()), "vsn", "%" + salesOrderEntity.getVsn() + "%")
                    .build();
        }

        //3.执行查询

        Page<SalesOrderEntity> pages = salesOrderRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    /**
     * 保存所有
     *
     * @param importEntities
     * @return
     */
    public List<SalesOrderEntity> saveAll(List<SalesOrderEntity> list) {
        return salesOrderRepository.saveAll(list);
    }

    /**
     * @param importEntities
     * @return
     */
    @Transactional(propagation = Propagation.NEVER)//不执行事务
    public List<SalesOrderEntity> importData(List<SalesOrderEntity> importEntities) {
        List<SalesOrderEntity> err = new ArrayList<>();
        importEntities.forEach(info -> {
            try {
                salesOrderRepository.save(info);
            } catch (Exception e) {
                err.add(info);
            }
        });
        return err;
    }
}
