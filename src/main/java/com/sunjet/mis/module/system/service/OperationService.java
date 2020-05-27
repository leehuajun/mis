package com.sunjet.mis.module.system.service;


import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.system.entity.OperationEntity;
import com.sunjet.mis.module.system.entity.OrgEntity;
import com.sunjet.mis.module.system.entity.PermissionEntity;
import com.sunjet.mis.module.system.entity.ResourceEntity;
import com.sunjet.mis.module.system.repository.MenuRepository;
import com.sunjet.mis.module.system.repository.OperationRepository;
import com.sunjet.mis.module.system.repository.PermissionRepository;
import com.sunjet.mis.module.system.repository.ResourceRepository;
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
 * 操作列表
 * Created by lhj on 16/6/17.
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("operationService")
public class OperationService {

    @Autowired
    private OperationRepository operationRepository;//操作
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private PermissionRepository permissionRepository;

//    @Autowired
//    private ResourceWithOperationRepository resourceWithOperationRepository;//资源与操作的关联关系
//
//    @Autowired
//    private PermissionRepository permissionRepository;//权限
//
//    @Autowired
//    private RoleWithPermissionRepository roleWithPermissionRepository;

    @Autowired
    private MenuRepository menuRepository;//菜单

    /**
     * 获取操作列表集合
     *
     * @return
     */
    public List<OperationEntity> findAll() {
        try {
            return this.operationRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 参数（包含实体参数和分页参数）
     * @return result 包含 List<Entity> 和分页数据
     */
    public PageResult<OperationEntity> getPageList(PageParam<OperationEntity> pageParam) {

        //1.查询条件
        OperationEntity operationEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<OperationEntity> specification1 = null;
        Specification<OperationEntity> specification2 = null;
        Specification<OperationEntity> specification3 = null;


        //页面查询条件
        if(operationEntity!=null&& StringUtils.isNotBlank(operationEntity.getName())) {
            specification2 = Specifications.<OperationEntity>or()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(operationEntity.getName()), "name", "%" + operationEntity.getName() + "%")
                    .like(StringUtils.isNotEmpty(operationEntity.getName()), "code", "%" + operationEntity.getCode() + "%")
                    .build();
        }


        //组合查询条件
        specification1 = Specifications.<OperationEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();



        //3.执行查询
        Page<OperationEntity> pages = operationRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));
//        for (OperationEntity entity : pages) {
//            log.info(String.valueOf(entity.getResources().size()));
//        }

        //4.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public OperationEntity findOneById(String id) {
        try {
            Optional<OperationEntity> optional = operationRepository.findById(id);
            if(optional.isPresent()){
                return optional.get();
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存一个实体
     *
     * @param operationEntity
     * @return返回一个info
     */
    public OperationEntity save(OperationEntity operationEntity) {
        try {
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
        }
    }

    /**
     * 通过id删除实体
     *
     * @param id
     * @return返回实体
     */
//    public boolean delete(String id) {
//        try {
//
//            Optional<OperationEntity> entityOptional = operationRepository.findById(id);
//            // 如果不存在
//            if(!entityOptional.isPresent()){
//                return false;
//            }
//
//
//            //删除关联关系 sys_resource_operation
//            resourceWithOperationRepository.deleteByOperation(objId);
//
//            //删除关联权限 sys_permissions
//            List<String> permissionIds = permissionRepository.findIdsByCode("%:" + entity.getOptCode());//根据操作code获取对应的权限id
//            permissionRepository.deleteAllByCode("%:" + entity.getOptCode());
//
//            //删除角色与权限的关联关系 sys_role_permission
//            if(permissionIds!=null&&permissionIds.size()>0) {
//                roleWithPermissionRepository.deleteRoleWithPermission(permissionIds);
//            }
//            //更新菜单绑定权限 sys_menus --把permissionCode、permissionName的值设置为空
//            menuRepository.updateMenuPermissionByCode("%:" + entity.getOptCode());
//
//            //删除实体 sys_operations
//            operationRepository.delete(objId);
//
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean deleteById(String objId){
        try {
            OperationEntity operationEntity = operationRepository.findOneById(objId);
            permissionRepository.deletePermissionEntitiesByOperationId(objId);
            operationRepository.deleteById(operationEntity.getId());
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}