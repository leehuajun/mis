package com.sunjet.mis.module.system.service;


import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.module.system.entity.IconEntity;
import com.sunjet.mis.module.system.repository.IconRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lhj on 16/6/17.
 * 图标
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("iconService")
public class IconService {

    @Autowired
    private IconRepository iconRepository;

    public List<IconEntity> findAll() {
        try {
            return this.iconRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}