package com.sunjet.mis.module.system.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wfb on 2018/8/4.
 * 角色视图
 */
@Data
@Entity
@Immutable
@Subselect("select obj_id,role_id,name,enabled,created_time from sys_roles")
public class RoleView implements Serializable {

    @Id
    private String objId;
    /**
     * 角色Id
     */
    private String roleId;
    /**
     * 角色名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    private Date createdTime;



    //Item中 vo
    @Transient
    private String users;   //角色关联的用户
    @Transient
    private String permissions;  //角色对应的权限
}

