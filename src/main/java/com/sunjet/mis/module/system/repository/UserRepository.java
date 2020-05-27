package com.sunjet.mis.module.system.repository;

import com.sunjet.mis.module.system.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: lhj
 * @create: 2017-07-02 19:56
 * @description: 说明
 */
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
    UserEntity findUserEntityByLogIdEquals(String logId);

    UserEntity findOneById(String id);
}
