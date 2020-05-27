package com.sunjet.mis.module.basic.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.basic.entity.PassengerCompartmenProvenancCarSeriesEntity;
import com.sunjet.mis.module.basic.entity.TruckSvarietyProvenancEntity;
import com.sunjet.mis.module.basic.repository.PassengerCompartmenProvenancCarSeriesRepository;
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
 * @Title: PassengerCompartmenProvenancCarSeriesService
 * @ProjectName mis
 * @Description: 客厢车 车系 产地 vsn 的匹配表
 * @date 2019/4/317:10
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("passengerCompartmenProvenancCarSeriesService")
public class PassengerCompartmenProvenancCarSeriesService {

    @Autowired
    private PassengerCompartmenProvenancCarSeriesRepository passengerCompartmenProvenancCarSeriesRepository;

//    /**
//     * 查询所有
//     **/
//
//    public List<PassengerCompartmenProvenancCarSeriesEntity> findAll() {
//        try {
//            return this.passengerCompartmenProvenancCarSeriesRepository.findAll();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 车型管理查询分页方法
     *
     * @param pageParam
     * @return
     */
    public PageResult<PassengerCompartmenProvenancCarSeriesEntity> getPageList(PageParam<PassengerCompartmenProvenancCarSeriesEntity> pageParam) {
        //1.查询条件
        PassengerCompartmenProvenancCarSeriesEntity passengerCompartmenProvenancCarSeriesEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<PassengerCompartmenProvenancCarSeriesEntity> specification = null;
        //页面查询条件
        if (passengerCompartmenProvenancCarSeriesEntity != null) {
            specification = Specifications.<PassengerCompartmenProvenancCarSeriesEntity>and()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .eq(StringUtils.isNotEmpty(passengerCompartmenProvenancCarSeriesEntity.getAbsvsn()), "absvsn",  passengerCompartmenProvenancCarSeriesEntity.getAbsvsn() )
                    .eq(StringUtils.isNotEmpty(passengerCompartmenProvenancCarSeriesEntity.getProduction()), "production",  passengerCompartmenProvenancCarSeriesEntity.getProduction() )
                    .build();
        }
        //3.执行查询
        Page<PassengerCompartmenProvenancCarSeriesEntity> pages = passengerCompartmenProvenancCarSeriesRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);

    }

    /**
     * 每次根据vin覆盖或新增数据
     * @param importEntities
     * @return
     */

    public List<PassengerCompartmenProvenancCarSeriesEntity> saveAll(List<PassengerCompartmenProvenancCarSeriesEntity> importEntities) {
        // List<SolidPinEntity> resultEntities = new ArrayList<>();
        List<PassengerCompartmenProvenancCarSeriesEntity> addEntities = new ArrayList<>();

        List<PassengerCompartmenProvenancCarSeriesEntity> passengerCompartmenProvenancCarSeriesEntities = passengerCompartmenProvenancCarSeriesRepository.findAll();
        for(PassengerCompartmenProvenancCarSeriesEntity entity:importEntities){
            for(PassengerCompartmenProvenancCarSeriesEntity importEntity:passengerCompartmenProvenancCarSeriesEntities){
                if(entity.getAbsvsn().equals(importEntity.getAbsvsn())){
                    addEntities.add(entity);
                }
            }
        }
        List<String> list = addEntities.stream().map(e -> e.getAbsvsn()).collect(Collectors.toList());

        passengerCompartmenProvenancCarSeriesRepository.deleteAllByAbsvsnIn(list);


        return passengerCompartmenProvenancCarSeriesRepository.saveAll(importEntities);

    }
    /**
     * 保存
     *
     * @param salesTargetEntity
     * @return
     */

    public PassengerCompartmenProvenancCarSeriesEntity save(PassengerCompartmenProvenancCarSeriesEntity entity) {
        return passengerCompartmenProvenancCarSeriesRepository.save(entity);
    }

    /**
     * 查询一个实体
     *
     * @param id
     * @return
     */
    public PassengerCompartmenProvenancCarSeriesEntity findById(String id) {
        return passengerCompartmenProvenancCarSeriesRepository.findById(id).orElse(null);
    }

    /**
     * 删除对象
     *
     * @param id
     */
    public boolean deleteById(String id) {
        try {
            passengerCompartmenProvenancCarSeriesRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
