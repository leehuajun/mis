package com.sunjet.mis.auth;

import com.sunjet.mis.module.system.entity.MenuEntity;
import com.sunjet.mis.module.system.entity.OrgEntity;
import com.sunjet.mis.module.system.entity.RoleEntity;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: lhj
 * @create: 2017-07-02 20:51
 * @description: 说明
 */
@Data
@Builder
public class ActiveUser implements Serializable {
    private static final long serialVersionUID = -5809095705388430051L;
    private String userId;//用户id
    private String logId;//登录id
    private String username;//用户姓名
    private OrgEntity org;   //组织id


    private List<MenuEntity> menus;     //菜单
    private List<RoleEntity> roles;     //角色
    private List<String> permissions;   //权限
    private Integer userType;            // 用户类别  internal / distributor
    private String phone;               // 用户联系电话


}
