package com.sunjet.mis.module.system.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.system.entity.OrgEntity;
import com.sunjet.mis.module.system.entity.RoleEntity;
import com.sunjet.mis.module.system.entity.UserEntity;
import com.sunjet.mis.module.system.repository.OrgRepository;
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

/**
 * @author: lhj
 * @create: 2017-07-02 00:51
 * @description: 说明
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("orgService")
public class OrgService {
    @Autowired
    private OrgRepository orgRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    /**
     * get userInfo by logId
     *
     * @param logId
     * @return
     */
    public OrgEntity findById(String id) throws MisException {
        OrgEntity entity = null;
        try {
            Optional<OrgEntity> entityOptional = orgRepository.findById(id);
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
     * get userInfo list
     *
     * @return
     */
    public List<OrgEntity> findAll() {
        List<OrgEntity> entities = new ArrayList<>();
        try {
            entities = orgRepository.findAll();
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

    public OrgEntity changePassword(OrgEntity orgEntity) {
        try {
            return orgRepository.save(orgEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PageResult<OrgEntity> getPageList(PageParam<OrgEntity> pageParam) {
        //1.查询条件
        OrgEntity orgEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<OrgEntity> specification1 = null;
        Specification<OrgEntity> specification2 = null;
        Specification<OrgEntity> specification3 = null;

        //页面查询条件
        if(orgEntity!=null&& StringUtils.isNotBlank(orgEntity.getName())) {
            specification2 = Specifications.<OrgEntity>or()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(orgEntity.getName()), "name", "%" + orgEntity.getName() + "%")
                    .like(StringUtils.isNotEmpty(orgEntity.getName()), "code", "%" + orgEntity.getCode() + "%")
                    .build();
        }


        //组合查询条件
        specification1 = Specifications.<OrgEntity>and().predicate(specification2!=null,specification2).predicate(specification3!=null,specification3).build();

        //3.执行查询
        Page<OrgEntity> pages = orgRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));
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

        //5.返回
        return PageUtil.getPageResult(pages.getContent(), pages, pageParam);
    }

    public List<OrgEntity> findParentOrgs(){
        return orgRepository.findParentOrgs();
    }

    public OrgEntity save(OrgEntity entity) {
        return orgRepository.save(entity);
    }

    public boolean deleteById(String objId){
        try {
            OrgEntity orgEntity = orgRepository.findOneById(objId);
            Set<UserEntity> users = orgEntity.getUsers();
            if(users != null && users.size()>0) {
                for (UserEntity user : users) {
                    user.getOrgs().remove(orgEntity);
                }
                userService.saveAll(users);
            }
            orgRepository.deleteById(orgEntity.getId());
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
