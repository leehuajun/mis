package com.sunjet.mis.module.system.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.system.entity.PermissionEntity;
import com.sunjet.mis.module.system.entity.RoleEntity;
import com.sunjet.mis.module.system.entity.UserEntity;
import com.sunjet.mis.module.system.repository.RoleRepository;
import com.sunjet.mis.module.system.repository.UserRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: lhj
 * @create: 2017-07-02 00:51
 * @description: 说明
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("roleService")
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;


    /**
     * get userInfo by logId
     *
     * @param logId
     * @return
     */
    public RoleEntity findById(String id) throws MisException {
        RoleEntity entity = null;
        try {
            Optional<RoleEntity> entityOptional = roleRepository.findById(id);
            if (entityOptional.isPresent()) {
                entity = entityOptional.get();
                entity.getUsers().size();
                entity.getPermissions().size();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MisException(e.getMessage());
        } finally {
            return entity;
        }
    }

    /**
     * get userInfo list
     *
     * @return
     */
    public List<RoleEntity> findAll() {
        List<RoleEntity> entities = new ArrayList<>();
        try {
            entities = roleRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MisException(e.getMessage());
        } finally {
            return entities;
        }
    }

    /**
     * 修改密码
     *
     * @param userEntity
     * @return
     */

    public RoleEntity changePassword(RoleEntity roleEntity) {
        try {
            return roleRepository.save(roleEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PageResult<RoleEntity> getPageList(PageParam<RoleEntity> pageParam) {
        //1.查询条件
        RoleEntity roleEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<RoleEntity> specification1 = null;
        Specification<RoleEntity> specification2 = null;
        Specification<RoleEntity> specification3 = null;

        //页面查询条件
        if (roleEntity != null && StringUtils.isNotBlank(roleEntity.getName())) {
            specification2 = Specifications.<RoleEntity>or()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(roleEntity.getName()), "name", "%" + roleEntity.getName() + "%")
                    .like(StringUtils.isNotEmpty(roleEntity.getCode()), "code", "%" + roleEntity.getCode() + "%")
                    .build();
        }


        //组合查询条件
        specification1 = Specifications.<RoleEntity>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<RoleEntity> pages = roleRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));

        for (RoleEntity entity : pages) {
            entity.getUsers().size();
            entity.getPermissions().size();
        }

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public RoleEntity save(RoleEntity entity) {
        if(StringUtils.isNotBlank(entity.getId())){
            Optional<RoleEntity> origin = roleRepository.findById(entity.getId());
            if (origin.isPresent()) {
                // 删除原来使用了该角色的用户关联信息
                RoleEntity roleEntity = origin.get();
                for (UserEntity user : roleEntity.getUsers()) {
                    user.getRoles().remove(roleEntity);
                    userRepository.save(user);
                }
            }
        }

        List<String> list = entity.getUsers().stream().map(u -> u.getId()).collect(Collectors.toList());
        List<UserEntity> users = userRepository.findAllById(list);
        entity = roleRepository.save(entity);
        for (UserEntity user : users) {
            user.getRoles().add(entity);
            userRepository.save(user);
        }

        return entity;
    }

    public boolean deleteById(String id){
        try {
            RoleEntity roleEntity = roleRepository.findOneById(id);
            Set<UserEntity> users = roleEntity.getUsers();
            for (UserEntity user : users) {
                user.getRoles().remove(roleEntity);
            }
            userRepository.saveAll(users);
            roleEntity.getPermissions().clear();
            roleRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
