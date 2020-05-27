package com.sunjet.mis.module.system.service;

import com.github.wenhao.jpa.Specifications;
import com.sunjet.mis.exception.MisException;
import com.sunjet.mis.helper.PageUtil;
import com.sunjet.mis.module.system.entity.*;
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

import java.util.*;

/**
 * @author: lhj
 * @create: 2017-07-02 00:51
 * @description: 说明
 */
@Slf4j
@Transactional(rollbackFor = MisException.class)
@SuppressWarnings("ALL")
@Service("userService")
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private OrgRepository orgRepository;

    /**
     * get userInfo by logId
     *
     * @param logId
     * @return
     */
    public UserEntity findById(String id) {
        UserEntity entity = null;
        try {
            Optional<UserEntity> entityOptional = userRepository.findById(id);
            if (entityOptional.isPresent()) {
                entity = entityOptional.get();
                entity.getRoles().size();
                entity.getOrgs().size();
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
    public List<UserEntity> findAll() {
        List<UserEntity> entities = new ArrayList<>();
        try {
            entities = userRepository.findAll();
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

    public UserEntity changePassword(UserEntity userEntity) {
        UserEntity result = null;
        try {
            result = userRepository.save(userEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MisException(e.getMessage());
        } finally {
            return result;
        }
    }

    public PageResult<UserEntity> getPageList(PageParam<UserEntity> pageParam) {
        //1.查询条件
        UserEntity userEntity = pageParam.getInfoWhere();

        //2.设置查询参数
        Specification<UserEntity> specification1 = null;
        Specification<UserEntity> specification2 = null;
        Specification<UserEntity> specification3 = null;

        //页面查询条件
        if (userEntity != null && StringUtils.isNotBlank(userEntity.getName())) {
            specification2 = Specifications.<UserEntity>or()
                    //第一个参数为真假值，第二各为实体变量名，第三个为条件
                    .like(StringUtils.isNotEmpty(userEntity.getName()), "name", "%" + userEntity.getName() + "%")
                    .like(StringUtils.isNotEmpty(userEntity.getLogId()), "logId", "%" + userEntity.getLogId() + "%")
                    .build();
        }


        //组合查询条件
        specification1 = Specifications.<UserEntity>and().predicate(specification2 != null, specification2).predicate(specification3 != null, specification3).build();

        //3.执行查询
        Page<UserEntity> pages = userRepository.findAll(specification1, PageUtil.getPageRequest(pageParam));
        int roleSize = 0;
        int orgSize = 0;
        for (UserEntity entity : pages) {
            roleSize = entity.getRoles().size();
            orgSize = entity.getOrgs().size();
        }

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

    public UserEntity save(UserEntity entity) {
        return userRepository.save(entity);
    }

    public UserEntity findByLogId(String logId) {
        UserEntity result = null;
        try {
            List<MenuEntity> menus = menuRepository.findAll();
            result = userRepository.findUserEntityByLogIdEquals(logId);

            System.out.println(result.getRoles().size());
            System.out.println(result.getOrgs().size());
            if (result.getLogId().equalsIgnoreCase("admin")) {
                result.setMenus(menus);
                result.setPermissions(permissionRepository.findAll());

            } else {

                Set<PermissionEntity> permissions = new HashSet<>();
                for (RoleEntity role : result.getRoles()) {
//                role.getPermissions().size();
                    for (PermissionEntity permission : role.getPermissions()) {
                        permissions.add(permission);
                    }
                }
                result.setPermissions(new ArrayList<>(permissions));
                result.getOrgs().size();

//                System.out.println("权限项:" + result.getPermissions());
//                System.out.println("菜单项:" +menus);
//                System.out.println("菜单项数量:" +menus.size());
                Set<MenuEntity> smenus = new HashSet<>();
                for (MenuEntity menu : menus) {
                    for (PermissionEntity permissionEntity : result.getPermissions()) {
                        if (menu.getPermission()!=null && menu.getPermission().getId().equalsIgnoreCase(permissionEntity.getId())) {
                            smenus.add(menu);
                            smenus.add(menu.getParent());
                            break;
                        }
                    }
                }
//                System.out.println("菜单项:" +smenus);
                result.setMenus(new ArrayList<>(smenus));
//                System.out.println("菜单项:" +result.getMenus());
                Collections.sort(result.getMenus(), new Comparator<MenuEntity>(){
                    /*
                     * int compare(Person p1, Person p2) 返回一个基本类型的整型，
                     * 返回负数表示：p1 小于p2，
                     * 返回0 表示：p1和p2相等，
                     * 返回正数表示：p1大于p2
                     */
                    public int compare(MenuEntity p1, MenuEntity p2) {
                        //按照Person的年龄进行升序排列
                        if(p1.getSeq() > p2.getSeq()){
                            return 1;
                        }
                        if(p1.getSeq() == p2.getSeq()){
                            return 0;
                        }
                        return -1;
                    }
                });
//                System.out.println("菜单项:" +result.getMenus());
//                System.out.println("菜单项数量:" +result.getMenus().size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new MisException(e.getMessage());
        } finally {
            return result;
        }
    }

    public List<UserEntity> saveAll(Set<UserEntity> users) {
        return userRepository.saveAll(users);
    }

    public boolean deleteById(String id){
        try {
            UserEntity userEntity = userRepository.findOneById(id);
            userEntity.getRoles().clear();
            userEntity.getOrgs().clear();
            userRepository.save(userEntity);
            userRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
