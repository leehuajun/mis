package com.sunjet.mis.module.system.service;

import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.module.system.entity.DictionaryEntity;
import com.sunjet.mis.module.system.entity.DictionaryEntryEntity;
import com.sunjet.mis.module.system.repository.DictionaryEntryRepository;
import com.sunjet.mis.module.system.repository.DictionaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by lhj on 16/6/17.
 * 数据字典的键值
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("dictionaryEntryService")
public class DictionaryEntryService {

    @Autowired
    private DictionaryEntryRepository dictionaryEntryRepository;

    @Autowired
    private DictionaryRepository dictionaryRepository;

    public List<DictionaryEntryEntity> findAll() {
        try {
            return this.dictionaryEntryRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 保存车据字典的键值
     *
     * @param dictionaryEntryEntity
     * @return
     */
    public DictionaryEntryEntity save(DictionaryEntryEntity dictionaryEntryEntity) {



        if (StringUtils.isBlank(dictionaryEntryEntity.getId())) {
                dictionaryEntryEntity=dictionaryEntryRepository.save(dictionaryEntryEntity);

        }

        return dictionaryEntryRepository.save(dictionaryEntryEntity);
    }


    /**
     * 查询一个实体
     *
     * @param id
     * @return
     */
//    public DictionaryEntryEntity findById(String id) {
//        return dictionaryEntryRepository.findById(id).orElse(null);
//    }

    public DictionaryEntryEntity findById(String id) {

        DictionaryEntryEntity entity = null;

        try {
            Optional<DictionaryEntryEntity> entityOptional = dictionaryEntryRepository.findById(id);
            if (entityOptional.isPresent())
                entity = entityOptional.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MisException(e.getMessage());
        } finally {
            return entity;
        }
    }



    /**
     * 删除对象
     *
     * @param id
     */
    public boolean deleteById(String id) {
        try {

            dictionaryEntryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}