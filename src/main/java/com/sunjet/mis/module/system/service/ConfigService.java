package com.sunjet.mis.module.system.service;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.system.entity.ConfigEntity;
import com.sunjet.mis.module.system.entity.IconEntity;
import com.sunjet.mis.module.system.repository.ConfigRepository;
import com.sunjet.mis.module.system.repository.IconRepository;
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
import java.util.Optional;

/**
 * Created by lhj on 16/6/17.
 * 图标
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("configService")
public class ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    public List<ConfigEntity> findAll() {
        try {
            return this.configRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数
     * @return result 包含 List<Entity> 和分页数据
     */
    public PageResult<ConfigEntity> getPageList(PageParam<ConfigEntity> pageParam) {
        //1.查询条件
        ConfigEntity configEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ConfigEntity> specification1 = null;
        Specification<ConfigEntity> specification2 = null;
        Specification<ConfigEntity> specification3 = null;

        //页面条件
        if (configEntity != null && StringUtils.isNotBlank(configEntity.getKey())) {
            specification2 = Specifications.<ConfigEntity>or()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(configEntity.getKey()), "key", "%"+configEntity.getKey()+"%")
                    .like(StringUtils.isNotEmpty(configEntity.getKey()), "value", "%"+configEntity.getKey()+"%")
                    .like(StringUtils.isNotEmpty(configEntity.getComment()),"comment","%"+configEntity.getComment()+"%")
                    .build();
        }

        //组织
//        if(authObject.getOrg().getOrgHierarchy().intValue()!=1) {
//            specification3 = Specifications.<ConfigEntity>or()
//                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
//                    .eq("orgId", authObject.getOrg().getObjId())
//                    .eq("orgType", "1")
//                    .build();
//        }

        //组合查询条件
        specification1 = Specifications.<ConfigEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<ConfigEntity> pages = configRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

        //4.数据转换
//        List<ConfigEntity> rows = new ArrayList<>();
//        for (ConfigEntity entity : pages.getContent()) {
//            rows.add(entity);
//        }

        //5.组装分页信息及集合信息
        //PageResult<ResourceInfo> result = new PageResult<>(rows, pages.getTotalElements(),pageParam.getPage(), pageParam.getPageSize());

        System.out.println(pages.getContent().size());
        //6.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public ConfigEntity findById(String id) {
        Optional<ConfigEntity> entityOptional = configRepository.findById(id);
        if(entityOptional.isPresent())
            return entityOptional.get();
        else
            return null;
    }

    public ConfigEntity save(ConfigEntity entity) {
        return configRepository.save(entity);
    }

    public boolean deleteById(String id){
        try {
            configRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}