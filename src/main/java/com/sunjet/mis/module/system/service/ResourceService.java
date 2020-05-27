package com.sunjet.mis.module.system.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.system.entity.MenuEntity;
import com.sunjet.mis.module.system.entity.OperationEntity;
import com.sunjet.mis.module.system.entity.PermissionEntity;
import com.sunjet.mis.module.system.entity.ResourceEntity;
import com.sunjet.mis.module.system.repository.*;
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
 * @author: lhj
 * @create: 2017-07-02 00:51
 * @description: 资源操作
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("resourceService")
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private OrgRepository orgRepository;
    @Autowired
    private MenuRepository menuRepository;




    /**
     * @param id
     * @return
     */
    public ResourceEntity findById(String id) throws MisException {
        ResourceEntity entity = null;
        try {
            Optional<ResourceEntity> entityOptional = resourceRepository.findById(id);
            if (entityOptional.isPresent())
                entity = entityOptional.get();
//            log.info(String.valueOf(entity.getOperations().size()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MisException(e.getMessage());
        } finally {
            return entity;
        }
    }

    /**
     * get resource list
     *
     * @return
     */
    public List<ResourceEntity> findAll() {
        List<ResourceEntity> entities = new ArrayList<>();
        try {
            entities = resourceRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MisException(e.getMessage());
        } finally {
            return entities;
        }
    }

    public PageResult<ResourceEntity> getPageList(PageParam<ResourceEntity> pageParam) {
        //1.查询条件
        ResourceEntity resourceEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<ResourceEntity> specification1 = null;
        Specification<ResourceEntity> specification2 = null;
        Specification<ResourceEntity> specification3 = null;

        //页面查询条件
        if (resourceEntity != null && StringUtils.isNotBlank(resourceEntity.getName())) {
            specification2 = Specifications.<ResourceEntity>or()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(resourceEntity.getName()), "name", "%" + resourceEntity.getName() + "%")
                    .like(StringUtils.isNotEmpty(resourceEntity.getName()), "code", "%" + resourceEntity.getCode() + "%")
                    .build();
        }


        //组合查询条件
        specification1 = Specifications.<ResourceEntity>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<ResourceEntity> pages = resourceRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));
//
//        //获取用户ids
//        ArrayList<String> list = this.getUserIds(pages.getContent());
//
//        //4.用户关联的角色信息信息
//        HashMap<String, String> map = getOperations(list);
//
//        //用户对应的组织信息
//        HashMap<String, String> mapOrg = getOrgs(list);
//
//        for (UserEntity entity : pages.getContent()) {
//            //绑定角色信息
//            entity.setRoleNames(map.get(entity.getObjId()));
//
//            //绑定组织名称
//            entity.setOrganizationNames(mapOrg.get(entity.getObjId()));
//        }
//        for (ResourceEntity entity : pages) {
//            log.info(String.valueOf(entity.getOperations().size()));
//        }

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public ResourceEntity save(ResourceEntity entity) {
//        entity = resourceRepository.save(entity);
        if (StringUtils.isBlank(entity.getId())) {
            entity = resourceRepository.save(entity);
            List<OperationEntity> operationEntities = operationRepository.findAll();
            for (OperationEntity operationEntity : operationEntities) {
                PermissionEntity permissionEntity = PermissionEntity.builder()
                        .operation(operationEntity)
                        .resource(entity)
                        .seq(operationEntity.getSeq())
                        .build();
                permissionRepository.save(permissionEntity);
            }
        } else {
            entity = resourceRepository.save(entity);
        }
//        permissionRepository.deletePermissionEntitiesByResourceId(entity.getId());
//        List<PermissionEntity> permissions = new ArrayList<>();
//        for (OperationEntity operation : entity.getOperations()) {
//            permissions.add(PermissionEntity.builder().resource(entity).operation(operation).seq(operation.getSeq()).build());
//        }
//        permissionRepository.saveAll(permissions);
        return entity;
    }

    /**
     * @Description: 根据id删除对象
     * @Author: YFS
     * @CreateDate: 2019/1/15 16:13
     */

    /*try {
            OperationEntity entity = null;
            if (StringUtils.isBlank(operationEntity.getId())) {
                entity = operationRepository.save(operationEntity);
                List<ResourceEntity> resourceEntities = resourceRepository.findAll();

                for (ResourceEntity resourceEntity : resourceEntities) {
                    PermissionEntity permissionEntity = PermissionEntity.builder()
                            .resource(resourceEntity)
                            .operation(entity)
                            .seq(entity.getSeq())
                            .build();
                    permissionRepository.save(permissionEntity);
                }
                return entity;
            } else {
                entity = operationEntity;
                operationRepository.save(entity);
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/
    public boolean deleteById(String id) {
        try {
            List<PermissionEntity> allByResource = permissionRepository.findAllByResource(id);
            for (PermissionEntity permissionEntity:allByResource) {
                List<MenuEntity> menuEntities = menuRepository.findAllByPermission(permissionEntity);
                menuEntities.forEach(menuEntity -> {
                    menuEntity.setPermission(null);
                });
                menuRepository.saveAll(menuEntities);
                permissionRepository.deleteById(permissionEntity.getId());
            }
            resourceRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
