package com.sunjet.mis.module.system.view;


import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by wfb on 2017/08/4.
 * 权限列表
 */
@Data
@Entity
@Immutable
@Subselect("select obj_id,access_name,resource_name,permission_code,seq,enabled from sys_permissions")
public class PermissionView implements Serializable {

    @Id
    private String objId;
    /**
     * 访问名称
     */
    private String accessName;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 权限编码
     */
    private String permissionCode;
    /**
     * 序号
     */
    private Integer seq;

    /**
     * 状态
     */
    private Boolean enabled;
}
