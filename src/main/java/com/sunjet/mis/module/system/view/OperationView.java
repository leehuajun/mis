package com.sunjet.mis.module.system.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by wfb on 17-8-3.
 * 操作列表
 */
@Data
@Entity
@Immutable
@Subselect("select s.obj_Id,s.opt_code,s.opt_name,s.seq from sys_operations s")
public class OperationView implements Serializable {

    @Id
    private String objId;    //主键

    private String optCode; //  操作编码

    private String optName; //  操作名称

    private Integer seq; // 序号

}
