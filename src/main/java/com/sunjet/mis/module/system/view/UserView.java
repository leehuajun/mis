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
 * Created by wfb on 17-8-4.
 * 用户列表
 */
@Data
@Entity
@Immutable
@Subselect("select obj_id,log_id,name,phone,email,user_type,dealer_id,agency_id,enabled,created_time from sys_users")
public class UserView implements Serializable {
    @Id
    private String objId;             //主键
    private String logId;             // 登录名
    private String name;              // 姓名
    private String phone;             // 电话
    private String email;
    private String userType;         // 用户类型   company:五菱工业用户  agency: 合作商  dealer: 服务站
    private Boolean enabled;        //是否启用

    private String dealerId;        // 用户所属 服务站
    private String agencyId;        // 用户所属 合作商

    private Date createdTime;


    //Item中 vo
    @Transient
    private String dealerName;        // 用户所属 服务站
    @Transient
    private String agencyName;        // 用户所属 合作商
    @Transient
    private String roleNames;          //拥有角色
}
