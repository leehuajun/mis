package com.sunjet.mis.module.basic.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.basic.entity.RegionEntity;
import com.sunjet.mis.module.basic.repository.RegionRepository;
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

@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("regionService")
public class RegionService {

//    @Autowired
//    private RegionRepository regionRepository;
//
//    public List<RegionEntity> findAll(){
//        return regionRepository.findAll();
//    }
//
//
//    /**
//     * 地区查询分页方法
//     * @param pageParam
//     * @return
//     */
//    public PageResult<RegionEntity> getPageList(PageParam<RegionEntity> pageParam) {
//        //1.查询条件
//        RegionEntity regionEntity = pageParam.getInfoWhere();
//
//        //2.设置查询参数
//        Specification<RegionEntity> specification = null;
//        //页面查询条件
//            if (regionEntity != null && StringUtils.isNotBlank(regionEntity.getName())) {
//                specification = Specifications.<RegionEntity>and()
//                        //第一个参数为真假值，第二各为实体变量名，第三个为条件
//                        .like(StringUtils.isNotEmpty(regionEntity.getName()), "name",  "%" + regionEntity.getName() + "%")
//                        .build();
//            }
//
//        //3.执行查询
//        Page<RegionEntity> pages = regionRepository.findAll(specification, PageUtil.getPageRequest(pageParam));
//
//        //5.返回
//        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
//    }
//
//
//    /**
//     * 保存地区信息
//     * @param regionEntity
//     */
//    public RegionEntity save(RegionEntity regionEntity) {
//        return regionRepository.save(regionEntity);
//
//    }
//
//    /**
//     * 查询一个实体
//     * @param id
//     * @return
//     */
//    public RegionEntity findById(String id) {
//        return regionRepository.findById(id).orElse(null);
//    }
//
//    /**
//     * 删除对象
//     * @param id
//     */
//    public boolean deleteById(String id) {
//        try {
//            regionRepository.deleteById(id);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return  false;
//        }
//    }
}
