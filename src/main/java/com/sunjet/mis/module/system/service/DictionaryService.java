package com.sunjet.mis.module.system.service;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.system.entity.DictionaryEntity;
import com.sunjet.mis.module.system.entity.DictionaryEntryEntity;
import com.sunjet.mis.module.system.repository.DictionaryEntryRepository;
import com.sunjet.mis.module.system.repository.DictionaryRepository;
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
import java.util.Optional;

/**
 * Created by lhj on 16/6/17.
 * 数据字典
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("dictionaryService")
public class DictionaryService {

    @Autowired
    private DictionaryRepository dictionaryRepository;
    @Autowired
    private DictionaryEntryRepository dictionaryEntryRepository;

    public List<DictionaryEntity> findAll() {
        try {
            return this.dictionaryRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param id
     * @return
     */
    public DictionaryEntity findById(String id) throws MisException {
        DictionaryEntity entity = null;
        try {
            Optional<DictionaryEntity> entityOptional = dictionaryRepository.findById(id);
            if (entityOptional.isPresent())
                entity = entityOptional.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MisException(e.getMessage());
        } finally {
            return entity;
        }
    }

    public PageResult<DictionaryEntity> getPageList(PageParam<DictionaryEntity> pageParam) {
        //1.查询条件
        DictionaryEntity dictionaryEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<DictionaryEntity> specification1 = null;
        Specification<DictionaryEntity> specification2 = null;
        Specification<DictionaryEntity> specification3 = null;

        //页面查询条件
        if (dictionaryEntity != null && StringUtils.isNotBlank(dictionaryEntity.getName())) {
            specification2 = Specifications.<DictionaryEntity>or()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(dictionaryEntity.getName()), "name", "%" + dictionaryEntity.getName() + "%")
                    .like(StringUtils.isNotEmpty(dictionaryEntity.getComment()), "comment", "%" + dictionaryEntity.getComment() + "%")
                    .build();
        }
        
        //组合查询条件
        specification1 = Specifications.<DictionaryEntity>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<DictionaryEntity> pages = dictionaryRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

        //4.加载字典条目
        for (DictionaryEntity entity : pages) {
            entity.getDictionaryEntries().size();
        }

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public DictionaryEntity save(DictionaryEntity entity) {
        return dictionaryRepository.save(entity);
    }


    /**
     * 删除对象
     *
     * @param id
     */
    public boolean deleteById(String id) {
        try {
            List<DictionaryEntryEntity> dictionaryEntryEntities = dictionaryEntryRepository.findAllByDictionaryEntry(id);
            for (DictionaryEntryEntity dic : dictionaryEntryEntities) {
                dictionaryEntryRepository.deleteById(dic.getId());
            }
            dictionaryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}