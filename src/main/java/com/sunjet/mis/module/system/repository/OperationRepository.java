package com.sunjet.mis.module.system.repository;


import com.sunjet.mis.module.system.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * UserRepository
 *
 * @author lhj
 * @create 2015-12-15 下午5:06
 */
public interface OperationRepository extends JpaRepository<OperationEntity, String>, JpaSpecificationExecutor<OperationEntity> {

//    @Query(value = "select o from OperationEntity o where o.resources ")
//    public List<OperationEntity> findAllByResourceId(String resourceId);

    @Query("select opera from OperationEntity opera where opera.id = ?1")
    OperationEntity findOneById(String objId);
}
