package com.sunjet.mis.module.basic.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.basic.entity.SalesTargetEntity;
import com.sunjet.mis.module.basic.repository.SalesTargetRepository;
import com.sunjet.mis.utils.dto.PageParam;
import com.sunjet.mis.utils.dto.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.List;

/**
 * @author SUNJET-YFS
 * @Title: SalesTargetService
 * @ProjectName mis
 * @Description: 经销商销售目标
 * @date 2019/1/1414:09
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("salesTargetService")
public class SalesTargetService {
    @Autowired
    private SalesTargetRepository salesTargetRepository;

//    private List<SalesTargetEntity> findAll() {
//
//        return salesTargetRepository.findAll();
//    }

    /**
     * 车型管理查询分页方法
     *
     * @param pageParam
     * @return
     */
    public PageResult<SalesTargetEntity> getPageList(PageParam<SalesTargetEntity> pageParam) {
        //1.查询条件
        SalesTargetEntity salesTargetEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<SalesTargetEntity> specification = null;
        //页面查询条件
        if (salesTargetEntity != null && StringUtils.isNotBlank(salesTargetEntity.getName())) {
            specification = Specifications.<SalesTargetEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotBlank(salesTargetEntity.getProvince().getName()), "name", salesTargetEntity.getProvince().getName())
                    .like(StringUtils.isNotEmpty(salesTargetEntity.getCode()), "code", "%" + salesTargetEntity.getCode() + "%")
                    .like(StringUtils.isNotEmpty(salesTargetEntity.getName()), "name", "%" + salesTargetEntity.getName() + "%")
                    .build();
        }
        //3.执行查询
        Page<SalesTargetEntity> pages = salesTargetRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);

    }


    /**
     * 保存车型管理信息
     *
     * @param salesTargetEntity
     * @return
     */

    public SalesTargetEntity save(SalesTargetEntity salesTargetEntity) {
        return salesTargetRepository.save(salesTargetEntity);
    }

    /**
     * 查询一个实体
     *
     * @param id
     * @return
     */
    public SalesTargetEntity findById(String id) {
        return salesTargetRepository.findById(id).orElse(null);
    }

    /**
     * 删除对象
     *
     * @param id
     */
    public boolean deleteById(String id) {
        try {
            salesTargetRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
