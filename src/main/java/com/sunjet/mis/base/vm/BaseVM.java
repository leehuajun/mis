package com.sunjet.mis.base.vm;


import com.sunjet.mis.auth.ActiveUser;
import com.sunjet.mis.helper.UserType;
import com.sunjet.mis.module.system.entity.UserEntity;
import com.sunjet.mis.vm.helper.PermissionStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wfb on 17-7-28.
 * 2018-10-27 lhj VM的基类只保留一些只读属性，不能包含业务逻辑
 */
public class BaseVM {
    @Getter
    @Setter
    private String title = "";

    @Getter
    private String emptyMessage = "没有找到合适的内容";

    //用户在每个VM中需要对该对象进行设置权限许可
    @Getter
    @Setter
    private PermissionStatus permissionStatus = new PermissionStatus();
    @Getter
    private ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();

//    private static final List<String> operations = Arrays.asList(
//            "read","create","modify","delete","search","audit","disaudit","import","export","print"
//    );

    /**
     * 判断当前用户是否具有 permission 权限
     * <p>
     * permission 字符串 :   资源:操作        user:save
     *
     * @param permission
     * @return
     */
    public Boolean hasPermission(String permission) {
        //去掉admin特殊处理 wfb 2018年11月23日
        if (getActiveUser().getLogId().equals("admin")) {
            return true;
        } else {
            return SecurityUtils.getSubject().isPermitted(permission);
        }
    }

    public void initPermissionStatus(String entityName){
        this.getPermissionStatus().setCanRead(hasPermission(entityName+":read"));
        this.getPermissionStatus().setCanCreate(hasPermission(entityName+":create"));
        this.getPermissionStatus().setCanModify(hasPermission(entityName+":modify"));
        this.getPermissionStatus().setCanDelete(hasPermission(entityName+":delete"));
        this.getPermissionStatus().setCanSearch(hasPermission(entityName+":search"));
        this.getPermissionStatus().setCanAudit(hasPermission(entityName+":audit"));
        this.getPermissionStatus().setCanDisaudit(hasPermission(entityName+":disaudit"));
        this.getPermissionStatus().setCanImport(hasPermission(entityName+":import"));
        this.getPermissionStatus().setCanExport(hasPermission(entityName+":export"));
        this.getPermissionStatus().setCanPrint(hasPermission(entityName+":print"));
    }

    public String removeBrackets(Object message) {
        return message.toString().replace("[", "").replace("]", "");
    }

    public boolean isDistributor(){
        if(this.activeUser.getUserType()== UserType.DISTRIBUTOR.getIndex())
            return true;
        return false;
    }
}
