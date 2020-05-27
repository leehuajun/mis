package com.sunjet.mis.module.system.service;


import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.module.system.entity.IconEntity;
import com.sunjet.mis.module.system.entity.OrgEntity;
import com.sunjet.mis.module.system.entity.PermissionEntity;
import com.sunjet.mis.module.system.entity.RoleEntity;
import com.sunjet.mis.module.system.repository.IconRepository;
import com.sunjet.mis.module.system.repository.PermissionRepository;
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
@Service("permissionService")
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public List<PermissionEntity> findAll() {
        try {
            return this.permissionRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PermissionEntity> findPermissionsByOrgs(List<RoleEntity> roles){
        return permissionRepository.findPermissionEntitiesByRolesContains(roles);
    }
}