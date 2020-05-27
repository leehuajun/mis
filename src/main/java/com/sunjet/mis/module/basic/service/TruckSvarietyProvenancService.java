package com.sunjet.mis.module.basic.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.basic.entity.TruckSvarietyProvenancEntity;
import com.sunjet.mis.module.basic.repository.TruckSvarietyProvenancRepository;
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
 * @Title: TruckSvarietyProvenancService
 * @ProjectName mis
 * @Description: 货改车 ABS-VSN 产品名称，产品类别，产地的匹配表
 * @date 2019/4/315:31
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("truckSvarietyProvenancService")
public class TruckSvarietyProvenancService {
    @Autowired
    private TruckSvarietyProvenancRepository truckSvarietyProvenancRepository;

    /**
     * 查询所有
     **/

    public List<TruckSvarietyProvenancEntity> findAll() {
        try {
            return this.truckSvarietyProvenancRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 车型管理查询分页方法
     *
     * @param pageParam
     * @return
     */
    public PageResult<TruckSvarietyProvenancEntity> getPageList(PageParam<TruckSvarietyProvenancEntity> pageParam) {
        //1.查询条件
        TruckSvarietyProvenancEntity truckSvarietyProvenancEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<TruckSvarietyProvenancEntity> specification = null;
        // 页面查询条件
        if (truckSvarietyProvenancEntity != null) {
            specification = Specifications.<TruckSvarietyProvenancEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(truckSvarietyProvenancEntity.getAbsvsn()), "absvsn", "%" + truckSvarietyProvenancEntity.getAbsvsn() + "%")
                    .eq(StringUtils.isNotEmpty(truckSvarietyProvenancEntity.getProduction()), "production",  truckSvarietyProvenancEntity.getProduction() )
                    .build();
        }
        //3.执行查询
        Page<TruckSvarietyProvenancEntity> pages = truckSvarietyProvenancRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);

    }

    /**
     * 每次根据vsn覆盖或新增数据
     *
     * @param importEntities
     * @return
     */

    public List<TruckSvarietyProvenancEntity> saveAll(List<TruckSvarietyProvenancEntity> importEntities) {
//        List<TruckSvarietyProvenancEntity> addEntities = new ArrayList<>();
//        List<TruckSvarietyProvenancEntity> truckSvarietyProvenancEntities = truckSvarietyProvenancRepository.findAll();
//
//        for (TruckSvarietyProvenancEntity entity : importEntities) {
//            for (TruckSvarietyProvenancEntity importEntity : truckSvarietyProvenancEntities) {
//                if (entity.getAbsvsn().equals(importEntity.getAbsvsn())) {
//                    addEntities.add(entity);
//                }
//            }
//        }
//        List<String> list = addEntities.stream().map(e -> e.getAbsvsn()).collect(Collectors.toList());
//
//        truckSvarietyProvenancRepository.deleteAllByAbsvsnIn(list);

        return truckSvarietyProvenancRepository.saveAll(importEntities);

    }

    /**
     * 保存
     *
     * @param salesTargetEntity
     * @return
     */

    public TruckSvarietyProvenancEntity save(TruckSvarietyProvenancEntity entity) {
        return truckSvarietyProvenancRepository.save(entity);
    }

    /**
     * 查询一个实体
     *
     * @param id
     * @return
     */
    public TruckSvarietyProvenancEntity findById(String id) {
        return truckSvarietyProvenancRepository.findById(id).orElse(null);
    }

    /**
     * 删除对象
     *
     * @param id
     */
    public boolean deleteById(String id) {
        try {
            truckSvarietyProvenancRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
