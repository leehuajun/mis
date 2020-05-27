package com.sunjet.mis.module.system.service;


import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.module.system.entity.MenuEntity;
import com.sunjet.mis.module.system.entity.PermissionEntity;
import com.sunjet.mis.module.system.entity.RoleEntity;
import com.sunjet.mis.module.system.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by lhj on 16/6/17.
 * 菜单实现类
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("menuService")
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    /**
     * save一个实体
     *
     * @param entity
     * @return
     */
    public MenuEntity save(MenuEntity entity) {
        try {
            return menuRepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过一个objId删除
     *
     * @param objId
     * @return
     */
    public boolean deleteById(String objId) {
        try {
            MenuEntity menuEntity = menuRepository.findOneById(objId);
            List<MenuEntity> menuEntityList = menuRepository.findAllByParent(objId);
            if(menuEntity != null && menuEntityList.size()>0){
                for(MenuEntity me:menuEntityList){
                    menuRepository.deleteById(me.getId());
                }
            }
            menuRepository.deleteById(objId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 通过objId查一个实体
     *
     * @param objId
     * @return
     */
    public MenuEntity findById(String objId) {
        try {
            Optional<MenuEntity> entityOptional = menuRepository.findById(objId);
            if (entityOptional.isPresent()) {
                return entityOptional.get();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有菜单
     *
     * @return
     */

    public List<MenuEntity> findAll() {
        // 获得数据源菜单
        List<MenuEntity> menuEntityList = menuRepository.findAll();

        return menuEntityList;
    }

    public List<MenuEntity> findParents() {
        return menuRepository.findAllByParentEquals(null);
    }
}