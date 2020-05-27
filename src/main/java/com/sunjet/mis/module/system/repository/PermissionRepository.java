package com.sunjet.mis.module.system.repository;


import com.sunjet.mis.module.system.entity.PermissionEntity;
import com.sunjet.mis.module.system.entity.ResourceEntity;
import com.sunjet.mis.module.system.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * UserRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface PermissionRepository extends JpaRepository<PermissionEntity, String>, JpaSpecificationExecutor<PermissionEntity> {
    void deletePermissionEntitiesByResourceId(String id);

    void deletePermissionEntitiesByOperationId(String id);

    List<PermissionEntity> findPermissionEntitiesByRolesContains(List<RoleEntity> roles);

    List<PermissionEntity> findPermissionEntitiesByResource(ResourceEntity resource );

  //List<PermissionEntity> findAllbyResourceEquals(ResourceEntity resource);

    void deleteAllByResourceEquals(ResourceEntity resource);

    @Query("select pe  from PermissionEntity pe where pe.resource.id =?1")
    List<PermissionEntity> findAllByResource(String id);


//    @Query("select pe.id from PermissionEntity pe where permission.id like ?1")
//    public List<String> findIdsByResource(String code);

}
