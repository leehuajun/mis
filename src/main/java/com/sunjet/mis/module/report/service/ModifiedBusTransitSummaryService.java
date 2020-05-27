package com.sunjet.mis.module.report.service;

import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.report.entity.ModifiedBusTransitSummaryEntity;
import com.sunjet.mis.module.report.repository.ModifiedBusTransitSummaryRepository;
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
 * @Title: ModifiedBusTransitSummaryService
 * @ProjectName mis
 * @Description: 改装车在途汇总表
 * @date 2019/1/2817:24
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("modifiedBusTransitSummaryService")
public class ModifiedBusTransitSummaryService {

  @Autowired
    private ModifiedBusTransitSummaryRepository modifiedBusTransitSummaryRepository;

    public PageResult<ModifiedBusTransitSummaryEntity> getPageList(PageParam<ModifiedBusTransitSummaryEntity> pageParam) {
        //1.查询条件
        ModifiedBusTransitSummaryEntity modifiedBusTransitSummaryEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ModifiedBusTransitSummaryEntity> specification = null;


        Page<ModifiedBusTransitSummaryEntity> pages = modifiedBusTransitSummaryRepository.findAll(specification, PageUtil.getPageRequest(pageParam));

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }
    /**
     * @param importEntities
     * @return
     */
    public List<ModifiedBusTransitSummaryEntity> saveAll(List<ModifiedBusTransitSummaryEntity> importEntities) {

        return modifiedBusTransitSummaryRepository.saveAll(importEntities);

    }


    /**
     * 查询所有
     * @return
     */
    public List<ModifiedBusTransitSummaryEntity> findAll() {
        return modifiedBusTransitSummaryRepository.findAll();
    }

    /**
     * @Description: 保存配车单明细
     * @Author: YFS
     * @CreateDate: 2019/1/11 14:20
     */

    public ModifiedBusTransitSummaryEntity save(ModifiedBusTransitSummaryEntity modifiedBusTransitSummaryEntity) {
        try {
            return modifiedBusTransitSummaryRepository.save(modifiedBusTransitSummaryEntity);

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
    public boolean delete(ModifiedBusTransitSummaryEntity modifiedBusTransitSummaryEntity) {
        try {
            modifiedBusTransitSummaryRepository.delete(modifiedBusTransitSummaryEntity);
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
            modifiedBusTransitSummaryRepository.deleteById(id);
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
    public ModifiedBusTransitSummaryEntity findById(String id) {
        try {
            ModifiedBusTransitSummaryEntity modifiedBusTransitSummaryEntity = modifiedBusTransitSummaryRepository.findById(id).orElse(null);
            return modifiedBusTransitSummaryEntity;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
